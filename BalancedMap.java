/*
Dawsyn Martone
Jorden Cook
cssc0776
 */
package edu.sdsu.cs.datastructures;

import edu.sdsu.cs.datastructures.IMap;

import java.util.LinkedList;
import java.util.TreeMap;

public class BalancedMap<K extends Comparable<K>, V> implements IMap<K,V> {


    private TreeMap<K, V> contents;
    K key;
    V value;

    public BalancedMap() {
        contents = new TreeMap<K,V>();
        key = null;
        value = null;
    }

    public BalancedMap(IMap<K, V> source) {
        contents = new TreeMap<>();
        for (K key : source.keyset()) {
            add(key, source.getValue(key));
        }

    }

    public boolean contains(K key) {
        return contents.containsKey(key);
    }

    public boolean add(K key, V value) {
        if(contains(key)){
            return false;
        }
        return contents.put(key, value) == null;
    }

    public V delete(K key) {
       V rand = getValue(key);
           contents.remove(key);
           return rand;
       }


    public V getValue(K key) {
        if (isEmpty() || contents.get(key) == null) {
            return null;
        } else
            return contents.get(key);
    }

    public K getKey(V value) {
        for(K key: keyset()){
            if(getValue(key).equals(value))
                return key;
        }
        return null;
    }

    public Iterable<K> getKeys(V value) {
        LinkedList<K> outcome = new LinkedList<>();
        for(K key: keyset()){
            if(getValue(key).equals(value)){
                outcome.add(key);
            }
        }
        return outcome;
    }

    public int size() {
        return contents.size();
    }

    public boolean isEmpty() {
        return contents.size() == 0;
    }

    public void clear() {
        contents.clear();
    }

    public Iterable<K> keyset() {
        return contents.keySet();
    }

    public Iterable<V> values() {
        return contents.values();
    }
}
