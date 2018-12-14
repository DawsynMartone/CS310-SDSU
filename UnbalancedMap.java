package edu.sdsu.cs.datastructures;

/*
Dawsyn Martone
820272018
cssc0777

Jorden Cook
820092202
cssc0776
 */

import edu.sdsu.cs.datastructures.IMap;

import java.util.LinkedList;

public class UnbalancedMap<K extends Comparable<K>, V extends Comparable<V>> implements IMap<K, V> {

    private class Node<K extends Comparable<K>, V extends Comparable<V>> {
        K key;
        V value;
        Node left;
        Node right;
        Node adult;

        Node(K key, V val) {
            this.key = key;
            this.value = val;
        }
    }

    private Node root;
    private int currSize;

    public UnbalancedMap() {
    }

    UnbalancedMap(IMap<K, V> m) {
        for (Object key : m.keyset()) {
            add((K) key, getValue((K) key));
        }
    }

    @Override
    public int size() {
        return currSize;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public void clear() {
        root = null;
        currSize = 0;
    }

    @Override
    public boolean contains(K key) {
        Node nodeCurr = root;
        while (nodeCurr != null) {
            if (key.compareTo((K) nodeCurr.key) < 0) {
                if (nodeCurr.left != null) {
                    nodeCurr = nodeCurr.left;
                } else {
                    break;
                }
            } else if (key.compareTo((K) nodeCurr.key) > 0) {
                if (nodeCurr.right != null) {
                    nodeCurr = nodeCurr.right;
                } else {
                    break;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean add(K key, V value) {
        if (root != null) {
            Node nodeCurr = root;
            while (nodeCurr != null) {
                if (key.compareTo((K) nodeCurr.key) < 0) {
                    if (nodeCurr.left != null) {
                        nodeCurr = nodeCurr.left;
                    } else {
                        nodeCurr.left = new Node(key, value);
                        nodeCurr.left.adult = nodeCurr;
                        ++currSize;
                        break;
                    }
                } else if (key.compareTo((K) nodeCurr.key) > 0) {
                    if (nodeCurr.right != null) {
                        nodeCurr = nodeCurr.right;
                    } else {
                        nodeCurr.right = new Node(key, value);
                        nodeCurr.right.adult = nodeCurr;
                        ++currSize;
                        break;
                    }
                } else {
                    return false;
                }
            }
        } else {
            root = new Node(key, value);
            ++currSize;
        }
        return true;
    }

    @Override
    public V delete(K key) {
        V data = getValue(key);
        remove(root, key);
        currSize--;
        return data;

    }

    private Node remove(Node curr, K key) {
        if (curr == null) return null;
        if (key.compareTo((K) curr.key) < 0) curr.left = remove(curr.left, key);
        else if (key.compareTo((K) curr.key) > 0) curr.right = remove(curr.right, key);
        else if (curr.left != null && curr.right != null) {
            curr.key = findMin(curr.right).key;
            curr.right = removeMin(curr.right);
        } else {
            if (curr.right != null) {
                if (curr == root) root = curr.right;
                else curr = curr.right;
            } else if (curr.left != null) {
                if (curr == root) root = curr.left;
                else curr = curr.left;
            } else if (curr == root) root = null;
            else curr = null;
        }
        return curr;
    }

    private Node findMin(Node node) {
        if (node.left == null)
            return node;
        else
            return findMin(node.left);
    }

    private Node removeMin(Node node) {
        if (node == null)
            return null;
        else if (node.left != null) {
            node.left = removeMin(node.left);
            return node;
        } else
            return node.right;
    }

    @Override
    public V getValue(K key) {
        if (contains(key)) {
            return (V) findNode(key).value;
        }
        return null;
    }

    private Node findNode(K key) {
        if (root != null) {
            Node currNode = root;
            while (currNode != null) {
                if (key.compareTo((K) currNode.key) > 0)
                    currNode = currNode.right;
                else if (key.compareTo((K) currNode.key) < 0)
                    currNode = currNode.left;
                else
                    return currNode;
            }
        }
        return null;
    }

    @Override
    public K getKey(V value) {
        if (root == null) return null;
        return findKey(value);
    }

    private K findKey(V value) {
        Node currNode = root;
        while (currNode != null) {
            if (value.compareTo((V) currNode.value) > 0)
                currNode = currNode.right;
            else if (value.compareTo((V) currNode.value) < 0)
                currNode = currNode.left;
            else
                return (K) currNode.key;
        }
        return null;
    }

    @Override
    public Iterable<K> getKeys(V value) {
        LinkedList<K> list = new LinkedList<>();
        if (root == null) return null;
        return getKeysHelper(root, list, value);
    }

    private Iterable<K> getKeysHelper(Node n, LinkedList<K> keys, V value) {

        if (n != null){
            getKeysHelper(n.left, keys, value);

            if (value.compareTo((V) n.value) == 0)
                keys.add((K) n.key);
            getKeysHelper(n.right, keys, value);

        }
        return keys;
    }

    @Override
    public Iterable<K> keyset() {
        LinkedList<K> keys = new LinkedList<>();
        if (root != null)
            return keyHelper(root, keys);
        return keys;
    }

    private Iterable<K> keyHelper(Node n, LinkedList<K> keys) {
        if (n != null) {
            keyHelper(n.left, keys);
            keys.add((K) n.key);
            keyHelper(n.right, keys);
        }
        return keys;
    }

    @Override
    public Iterable<V> values() {
        LinkedList<V> valueLL = new LinkedList<>();
        if (root != null)
            return valuesHelper(root, valueLL);
        return valueLL;
    }

    private Iterable<V> valuesHelper(Node n, LinkedList<V> val) {
        if (n != null) {
            valuesHelper(n.left, val);
            val.add((V) n.value);
            valuesHelper(n.right, val);
        }
        return val;
    }
}


