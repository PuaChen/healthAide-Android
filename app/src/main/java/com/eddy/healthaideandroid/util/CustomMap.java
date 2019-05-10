package com.eddy.healthaideandroid.util;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Map工具类
 *
 * @author Chen
 */
public class CustomMap extends HashMap<String, Object> {

    public static CustomMap create(String key, Object val) {
        return new CustomMap().put(key, val);
    }

    public static CustomMap create() {
        return new CustomMap();
    }

    @Override
    public CustomMap put(String key, Object value) {
        super.put(key.trim(), value);
        return this;
    }

    public CustomMap remove(String key) {
        super.remove(key);
        return this;
    }

    public String toJson() {
        return JSONObject.toJSONString(this);
    }

    public String getToString(String key) {
        return MapUtils.getString(this, key);
    }

    /**
     * 将普通的Map 转换为 CustomMap
     *
     * @param map
     * @return
     */
    public static CustomMap converToCustomMap(Map<String, Object> map) {
        CustomMap customMap = new CustomMap();
        customMap.putAll(map);
        return customMap;
    }

}
