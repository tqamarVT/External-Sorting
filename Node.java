/**
 * Key-value pair
 * 
 * @author Taimoor Qamar
 * @version 10-23-19
 *
 */
public class Node<K extends Comparable<? super K>, V>
    implements Comparable<Node<K, V>> {
    private K key;
    private V value;


    /**
     * Makes a new Node with the given key and value.
     * Key and Value should each have an equals method defined.
     * 
     * @param k
     *            the key of the new Node
     * @param v
     *            the value of the new Node
     */
    public Node(K k, V v) {
        key = k;
        value = v;
    }


    /**
     * Getter for Key
     * 
     * @return the key
     */
    public K getKey() {
        return key;
    }


    /**
     * Getter for value
     * 
     * @return the value
     */
    public V getValue() {
        return value;
    }


    /**
     * Setter for key
     * 
     * @param k
     *            the new key
     */
    public void setKey(K k) {
        key = k;
    }


    /**
     * Setter for value
     * 
     * @param v
     *            the new value
     */
    public void setValue(V v) {
        value = v;
    }


    /**
     * Two nodes are equal if and only if their keys are equal. Obviously, the
     * equals method of the class being used as the key should be defined for
     * this to work properly.
     * 
     * @param other
     *            the Object being compared to this for equality
     * @return whether other is equal to this.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (this.getClass() == other.getClass()) {
            @SuppressWarnings("unchecked") // looks checked to me ^
            Node<K, V> otherNode = (Node<K, V>)other;
            return this.compareTo(otherNode) == 0;
        }
        return false;
    }


    /**
     * Two Nodes are valueEqual if and only if their keys and values are the
     * same. Obviously, the equals methods of the classes being used as the key
     * and the value should be defined for this to work properly.
     * 
     * @param other
     *            the Node being compared to this for equality
     * @return
     *         whether other is equal to this
     */
    public boolean valueEquals(Node<K, V> other) {
        return this.equals(other) && this.value.equals(other.value);
    }


    /**
     * Compares the keys of this and other. If this key is greater than the
     * other key, return a positive int. If this key is less than the other key,
     * return a negative int. If the keys are equal, return 0.
     * 
     * @param other
     *            the Node being compared to this
     * @return If this key is greater than the other key, return a positive int.
     *         If this key is less than the other key, return a negative int. If
     *         the keys are equal, return 0.
     */
    @Override
    public int compareTo(Node<K, V> other) {
        return this.key.compareTo(other.key);
    }


    /**
     * Not sure if we'll need this, but I'll include it anyways. "Key: " +
     * key.toString() + " | Value: " + value.toString();
     * 
     * @return the String representation of this Node
     */
    public String toString() {
        return "Key: " + key.toString() + " | Value: " + value.toString();
    }
}
