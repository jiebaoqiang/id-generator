package com.jhh.id.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jhh.id.annotation.IdGenerator;
import com.jhh.id.http.HttpClient;
import com.jhh.id.resp.Response;
import com.jhh.id.utils.BeanUtil;
import com.jhh.id.utils.PropertiesReaderUtil;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Id 自动生成拦截器<br />
 * 当mybatis 触发insert 操作时，查看实体类中是否存在@IdGenerator 注解<br />
 * 如果存在此注解，则拦截调用生成的Id，重新赋值
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
}
)
public class IdGeneratorHandler implements Interceptor {

    /**
     * 访问资源的路径
     */
    private final static String requestURL = PropertiesReaderUtil.read("third", "httpIdGeneratorURL");

    /**
     * 重试次数，3次
     */
    private final static int retryTimes = 3;

    /**
     * Id Field 持久化缓存
     */
    private final static Map<Class, List<Field>> idMap = new ConcurrentHashMap<>();

    private String database;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 拦截 Executor 的 update 方法 生成sql前将 tenantId 设置到实体中
        if (invocation.getTarget() instanceof Executor) {
            Executor executor = (Executor) invocation.getTarget();
            // 获取第一个参数
            MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
            // 非 insert 语句 不处理
            if (ms.getSqlCommandType() != SqlCommandType.INSERT) {
                return invocation.proceed();
            }

            // mybatis的参数对象
            Object paramObj = invocation.getArgs()[1];
            if (paramObj == null) {
                return invocation.proceed();
            }

            // 插入语句只传一个基本类型参数, 不做处理, 只处理pojo对象
            if (ClassUtils.isPrimitiveOrWrapper(paramObj.getClass())
                    || String.class.isAssignableFrom(paramObj.getClass())
                    || Number.class.isAssignableFrom(paramObj.getClass())) {
                return invocation.proceed();
            }

            //缓存需要处理的对象
            Set<Object> idObjects = new LinkedHashSet<>();

            //收集需要处理的对象
            collectIdObjects(paramObj, idObjects);

            //填充对象ID
            handleIdFields(idObjects);

            //清空临时容器
            idObjects.clear();

            return executor.update(ms, paramObj);
        }
        return invocation.proceed();
    }

    //填充ID
    private void handleIdFields(Set<Object> idObjects) {

        //计算每个IDGenerator需要获取的个数
        Map<Field, AtomicInteger> idCounts = new HashMap<>();
        Iterator<Object> iter = idObjects.iterator();
        while (iter.hasNext()) {
            Object object = iter.next();
            List<Field> fields = idMap.get(object.getClass());
            if (fields != null) {
                fields.stream().forEach(t -> {
                    idCounts.putIfAbsent(t, new AtomicInteger(0));
                    idCounts.get(t).incrementAndGet();
                });
            }
        }

        //批量获取ID
        Map<Field, LinkedList<Integer>> availableIds = idCounts.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                e -> {
                    IdGenerator idGenerator = e.getKey().getAnnotation(IdGenerator.class);
                    // 如果注解指定了Id生成器，则使用注解的Id生成器，没有则使用全局的配置的自定义生成器
                    if (StringUtils.isNotBlank(idGenerator.value())) {
                        database = idGenerator.value();
                    }
                    LinkedList<Integer> ids = new LinkedList<>();
                    String url = requestURL + "list/" + database + "/" + e.getValue().get();
                    List<Integer> fetchIds = retryFetchIds(url, 0);
                    ids.addAll(fetchIds);
                    return ids;
                }));

        //填充ID
        idObjects.stream().forEach(t -> {
            List<Field> fields = idMap.get(t.getClass());
            if (fields != null) {
                fields.stream().forEach(t1 -> {
                    try {
                        PropertyDescriptor ps = BeanUtil.getPropertyDescriptor(t.getClass(), t1.getName());
                        if (ps != null && ps.getReadMethod() != null && ps.getWriteMethod() != null) {
                            Object value = ps.getReadMethod().invoke(t);
                            if (value == null) {
                                int anInt = availableIds.get(t1).pop();
                                ps.getWriteMethod().invoke(t, anInt);
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });

        //清空缓存
        idCounts.clear();
    }

    private List<Integer> retryFetchIds(String url, int time) {
        String result = HttpClient.doGet(url, true);
        Response response = JSON.parseObject(result, Response.class);
        if (null != response && 200 == response.getCode()) {
            return ((JSONArray) response.getData()).toJavaList(Integer.class);
        } else if (time < retryTimes) {
            return retryFetchIds(url, time + 1);
        } else {
            throw new RuntimeException(response.getMsg());
        }
    }

    /**
     * 设置参数
     */
    private void collectIdObjects(Object object, Set<Object> idObjects) throws Exception {
        // 如果是Map类型，遍历元素
        if (object instanceof Map) {
            Iterator iter = ((Map) object).values().iterator();
            while (iter.hasNext()) {
                collectIdObjects(iter.next(), idObjects);
            }
            return;
        }

        // 如果是Collection类型，遍历元素
        if (object instanceof Collection) {
            Iterator iter = ((Collection) object).iterator();
            while (iter.hasNext()) {
                collectIdObjects(iter.next(), idObjects);
            }
            return;
        }

        // 如果参数是bean，反射设置值
        Class<?> clazz = object.getClass();

        if (idMap.containsKey(clazz)) {
            idObjects.add(object);
        } else {
            List<Field> fieldTemp = new ArrayList<>();
            // 获取bean 的所有字段
            Field[] fields = clazz.getDeclaredFields();
            // 遍历bean 的字段，查看此字段是否存在@IdGenerator 注解
            for (Field field : fields) {
                if (field.isAnnotationPresent(IdGenerator.class)) {
                    idObjects.add(object);
                    fieldTemp.add(field);
                }
            }
            idMap.put(clazz, fieldTemp);
        }
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        database = properties.getProperty("database");
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
