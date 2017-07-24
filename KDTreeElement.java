import bridges.base.BSTElement;

public class KDTreeElement<E> extends BSTElement<KDPoint,E>{
		private int dim;
	
	 public KDTreeElement() {}

	    public KDTreeElement(E e, KDTreeElement<E> left, KDTreeElement<E> right) {
	        super(e, left, right);
	    }

	    public KDTreeElement(KDPoint key, E e, KDTreeElement< E> left, KDTreeElement< E> right) { super(key, e, left, right); }

	    public KDTreeElement(KDPoint key,E e) {
	        super(key, e);
	    }
	    
	    public KDTreeElement<E> setDim(int d) {
	    	dim = d;
	    	return this;
	    }
	    
	    public int getDim() {
	    	return dim;
	    }
	    
	    public KDPoint getKey() {
	    	return (KDPoint) super.getKey();
	    }

	    public String getDataStructType() {
	        return "K-Dimensional Tree";
	    }

	    public KDTreeElement(String label, KDPoint key, E e) { super(label, key, e); }

	    public KDTreeElement(KDTreeElement<E> left, KDTreeElement<E> right) { super(left, right); }

	    public KDTreeElement<E> getLeft() { return (KDTreeElement<E>)super.getLeft(); }

	    public KDTreeElement<E> getRight() { return (KDTreeElement<E>)super.getRight(); }
	    
	    

}
