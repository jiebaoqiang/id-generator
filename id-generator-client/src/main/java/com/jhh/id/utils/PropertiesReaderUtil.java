package com.jhh.id.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 资源文件读取工具类
 * @author siguiyang
 */
public class PropertiesReaderUtil {

    /**
     * 读取配置
     *
     * @param file 文件名,不带 .properties的后缀
     * @param name key名
     */
    public static String read(String file, String name) {
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = PropertiesReaderUtil.class.getResourceAsStream("/" + file + ".properties");
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return props.get(name).toString();

    }
}