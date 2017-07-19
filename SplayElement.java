 
import bridges.base.BSTElement;

public class SplayElement<K, E> extends BSTElement<K, E> {
	
	private SplayElement<K, E> parent;

    public SplayElement() {}

    public SplayElement(E e, SplayElement<K, E> left, SplayElement<K, E> right) {
        super(e, left, right);
    }

    public SplayElement(K key, E e, SplayElement<K, E> left, SplayElement<K, E> right) { super(key, e, left, right); }

    public SplayElement(K key,E e) {
        super(key, e);
    }

    public String getDataStructType() {
        return "SplayTree";
    }

    public SplayElement(String label, K key, E e) { super(label, key, e); }

    public SplayElement(SplayElement<K, E> left, SplayElement<K, E> right) { super(left, right); }

    public SplayElement<K, E> getLeft() { return (SplayElement<K, E>)super.getLeft(); }

    public SplayElement<K, E> getRight() { return (SplayElement<K, E>)super.getRight(); }
    
    public SplayElement<K, E> getParent() { return (SplayElement<K, E>)parent; }
    
    public void setParent(SplayElement<K, E> x) { parent = x;}
    

}
