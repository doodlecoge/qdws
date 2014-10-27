package com.cisex.ds;

/**
 * User: huaiwang
 * Date: 13-9-22
 * Time: 上午10:14
 */
public class KeyValuePair<K, V> {
    private K key;
    private V value;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
