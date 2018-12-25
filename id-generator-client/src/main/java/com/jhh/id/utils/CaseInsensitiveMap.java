package com.jhh.id.utils;

import java.util.Map;

/**
 * 忽略大小写的Map<br>
 * 对KEY忽略大小写，get("Value")和get("value")获得的值相同，put进入的值也会被覆盖
 *
 * @author siguiyang
 *
 * @param <K> 键类型
 * @param <V> 值类型
 */
public class CaseInsensitiveMap<K, V> extends CustomKeyMap<K, V> {


    private static final long serialVersionUID = 2686137668518044718L;

    /**
     * 构造
     */
    public CaseInsensitiveMap() {
        super();
    }

    /**
     * 构造
     *
     * @param initialCapacity 初始大小
     * @param loadFactor 加载因子
     */
    public CaseInsensitiveMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * 构造
     *
     * @param initialCapacity 初始大小
     */
    public CaseInsensitiveMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * 构造
     *
     * @param m Map
     */
    public CaseInsensitiveMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    /**
     * 构造
     *
     * @param loadFactor 加载因子
     * @param m Map
     * @since 3.1.2
     */
    public CaseInsensitiveMap(float loadFactor, Map<? extends K, ? extends V> m) {
        super(loadFactor, m);
    }

    /**
     * 将Key转为小写
     *
     * @param key KEY
     * @return 小写KEY
     */
    @Override
    protected Object customKey(Object key) {
        if (key instanceof CharSequence) {
            key = key.toString().toLowerCase();
        }
        return key;
    }
}
