package com.travis.library.basic.model;

import java.io.Serializable;

/**
 * 实体类(实现clone方法)
 * Created by chenkai.gu on 2017/2/13.
 */
public class Entity<T extends Serializable> implements Serializable, Cloneable {

    /**
     * 实体类clone
     *
     * @return clone得到的实体类
     */
    public T clone() {
        T t = null;
        try {
            t = (T) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return t;
    }
}
