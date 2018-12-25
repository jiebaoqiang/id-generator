package com.jhh.id.resp;

import java.io.Serializable;

/**
 * 数据响应对象
 *
 * @param <T>
 */
public class Response<T> implements Serializable {

    private int code;

    private String msg;

    private T data;

    private long time;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", time=" + time +
                '}';
    }
}
