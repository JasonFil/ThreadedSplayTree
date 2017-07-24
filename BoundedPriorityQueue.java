
import java.util.TreeMap;
import java.util.LinkedList;


/**
 * <p><tt>BoundedPriorityQueue</tt> is a Priority Queue whose number of elements
 * is bounded. Insertions are such that if the queue's provided capacity is surpassed,
 * its length is not expanded, but rather the maximum priority element is ejected
 * (which could be the element just attempted to be enqueued). Note that <tt>BoundedPriorityQueue</tt>s
 * are {@link Iterable}s.</p>
 * 
 * @author ------ YOUR NAME HERE! --------
 *
 * @param <T> An {@link Object} that will be held by <tt>this</tt>. Note that <tt>T</tt> is not declared as
 *           {@link Comparable}: its priority is supplied on the fly by the enqueueing method.
 */



/**
 * <p><tt>BoundedPriorityQueue</tt> is a Priority Queue whose number of elements
 * is bounded. Insertions are such that if the queue's provided capacity is surpassed,
 * its length is not expanded, but rather the maximum priority element is ejected
 * (which could be the element just attempted to be enqueued). Note that <tt>BoundedPriorityQueue</tt>s
 * are {@link Iterable}s.</p>
 * 
 * @author ------ YOUR NAME HERE! --------
 *
 * @param <T> An {@link Object} that will be held by <tt>this</tt>. Note that <tt>T</tt> is not declared as
 *           {@link Comparable}: its priority is supplied on the fly by the enqueueing method.
 */
public class BoundedPriorityQueue<T> {
	
	private TreeMap<Double, LinkedList<T>> x;
	private int bound;
	private int size;
	public int ops;
	

	/**
	 * Constructor that specifies the size of our queue.
	 * @param size The static size of the <tt>BoundedPriorityQueue</tt>. Has to be a positive integer.
	 * @throws RuntimeException if <tt>size</tt> is not positive.
	 */
	public BoundedPriorityQueue(int size){
		x = new TreeMap<Double,LinkedList<T>>();
		this.bound = size;
		this.ops = 0;
		this.size = 0;
	}

	/**
	 * Enqueueing elements for<tt> BoundedPriorityQueue</tt>s works a little bit differently from general case
	 * PriorityQueue objects. If the queue is not at capacity, the <tt>element</tt> is inserted at its
	 * appropriate location in the sequence. On the other hand, if the object is at capacity, the element is
	 * inserted in its appropriate spot in the sequence (if such a spot exists, based on its <tt>priority</tt>) and
	 * the maximum priority element is ejected from the structure.
	 * 
	 * @param element The element to insert in the queue.
	 * @param priority The priority of the element to insert in the queue.
	 * @see #dequeue()
	 */
 	public void enqueue(T element, double priority) {
 		LinkedList<T> r; this.ops++; 
 		
 		if ((r = this.x.get(priority)) == null) {
 			r = new LinkedList<T>();
 			r.addLast(element);
 			this.x.put(priority, r);
 		} else {r.addLast(element); this.x.put(priority, r);}
 		
 		if (this.bound < (this.size + 1)) { 
			r = this.x.get(this.x.lastKey());
			if (r.isEmpty()) this.x.remove(this.x.firstKey());
			r.removeLast();
			if (r.isEmpty()) this.x.remove(this.x.lastKey());
		} else this.size++;
	}

	/**
	 * Remove and return the minimum priority (top) element from the queue.
	 * @return The minimum priority element from the queue.
	 * @see #first()
	 * @see #enqueue(Object, double)
	 */
 	public T dequeue() {
 		if (isEmpty()) return null;
 		LinkedList<T> r = this.x.get(this.x.firstKey());
 		if (r.isEmpty()) this.x.remove(this.x.firstKey());
 		T w = r.removeFirst();
 		if (r.isEmpty()) this.x.remove(this.x.firstKey());
 		this.size--; this.ops++;
 		return w;
	}

	/**
	 * Return, <b>without removing it from the queue</b>, the minimum priority (top) element of the queue.
	 * @return The minimum priority element of the queue.
	 * @see #last()
	 * @see #dequeue()
	 */
 	public T first() {
 		return (isEmpty()) ? null : this.x.get(this.x.firstKey()).getFirst();
	}
	
	/**
	 * Returns the last element in the queue. Useful for cases where we want to 
	 * compare the priorities of a given quantity with the maximum priority of 
	 * our stored quantities. In a linked minheap-based implementation of any PriorityQueue,
	 * this operation would scan <em>O(n)</em> nodes and <em>O(nlogn)</em> links. In an array-based minheap,
	 * it takes constant time.
	 * @return The maximum priority element in our queue, or <tt>null</tt> if the queue is empty.
	 */
	public T last() {
		return (isEmpty()) ? null : this.x.get(this.x.lastKey()).getLast();
	}

	/**
	 * Return the queue's size, as measured by the amount of elements stored in it.
	 * @return The number of elements in the queue.
	 */
 	public int size() {
		return this.size;
	}

	/**
	 * Query the queue for emptiness.
	 * @return <tt>true</tt> if and only if there are <b>no</b> elements in the queue, <tt>false</tt> otherwise.
	 */
 	public boolean isEmpty() {
		return this.x.isEmpty();
	}

}