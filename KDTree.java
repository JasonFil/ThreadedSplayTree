import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import bridges.connect.Bridges;
import bridges.data_src_dependent.EarthquakeUSGS;
import bridges.data_src_dependent.USGSaccount;



 
/**
 * <tt>KDTree</tt> imlements <em>K</em>-D Trees. <em>K</em> is a positive integer.
 *  By default, <em>k=2</em>. <tt>KDTree</tt> supports standard insertion, deletion and
 *  search routines, and also allows for range searching and nearest neighbor queries.
 *
 * @author ---- Brandon Newman ---------
 */
public class KDTree<E> {

	
	private int k;
	private KDTreeElement<E> root;

	/* To pass our tests, you will need to complete the class' public interface, declared below.
	 * YOU WILL NEED TO COMMENT OUT THE EXCEPTION THROWINGS, OTHERWISE YOU WILL FAIL ALL TESTS THAT
	 * INCLUDE CALLS TO THE RELEVANT METHODS! */

	/**
	 * Default constructor constructs <tt>this</tt> with <em>k=2</em>.
	 */
	public KDTree(){
		this(2);
	}

	/**
	 * This constructor requires that the user provide the value for <em>k</em>.
	 * @param k The dimensionality of <tt>this</tt>.
	 * @throws RuntimeException if <tt>k&lt;=0</tt>.
	 */
	public KDTree(int k){
		this.k = k;
	}

	/**
	 * Inserts <tt>p</tt> into the <tt>KDTree</tt>.
	 * @param p The {@link KDPoint} to insert into the tree.
	 */
	public void insert(KDPoint x) {
		this.root = ins(this.root,new KDTreeElement<E>(x,null,null,null),0);
	}
	
	public void insert(KDPoint x, E val) {
		this.root = ins(this.root,new KDTreeElement<E>(x,val,null,null),0);
	}
	
	public void insert(KDTreeElement<E> x) {
		this.root = ins(this.root,x,0);
	}
	
	private KDTreeElement<E> ins (KDTreeElement<E> i, KDTreeElement<E> x, int ddim) {
		if (i == null) return x;
		else if (i.getKey().coords[ddim] <= x.getKey().coords[ddim]) i.setRight(ins(i.getRight(),x,(ddim+1)%this.k));
		else  i.setLeft(ins(i.getLeft(),x,(ddim+1)%this.k));
		return i;
	}
	
	/**
	 * Deletes <tt>p</tt> from the <tt>KDTree</tt>. If <tt>p</tt> is not in the
	 * tree, this method performs no changes to the tree.
	 * @param p The {@link KDPoint} to delete from the tree.
	 */
	public void delete(KDPoint p) {
		if (p == null) return;
		this.root = del(this.root,p);
	}
	
	private KDTreeElement<E> del(KDTreeElement<E> i, KDPoint x) {
		if (i == null) return null;
		if (i.getKey().equals(x)) {
			if (i.getRight() != null){
				i.setKey((min(i.getRight(),i.getDim())));
				i.setRight(del(i.getRight(),i.getKey()));
			} else if (i.getLeft() != null) {
				i.setKey((min(i.getLeft(),i.getDim())));
				i.setRight(del(i.getLeft(),i.getKey()));
				i.setLeft(null);
			} else return null;
		} 
			if (i.getKey().coords[i.getDim()] <= x.coords[i.getDim()]) i.setRight(del(i.getRight(),x)) ;
			else i.setLeft(del(i.getLeft(),x)); 
		return i;
		
		}
	

	private KDPoint min(KDTreeElement<E> r, int ddim) {
		if (r == null) return null;
		else if (r.getDim() == ddim) return (r.getLeft() == null) ? r.getKey() : min(r.getLeft(),ddim);
		else return minofthree(min(r.getLeft(),ddim),min(r.getRight(),ddim),r.getKey(),ddim);
	}
	
	private KDPoint minofthree(KDPoint one, KDPoint two, KDPoint three, int ddim) {
		return m(one,m(two,three,ddim),ddim);
	}
	
	private KDPoint m( KDPoint left, KDPoint right, int ddim) {
		if (left == null) return (right == null) ? null : right;
		if (right == null) return left;
		return (left != null && right != null && left.coords[ddim] < right.coords[ddim]) ? left : right;
	}
	
	/**
	 * Searches the tree for <tt>p</tt> and reports if it found it.
	 * @param p The {@link KDPoint} to look for in the tree.
	 * @return <tt>true</tt> iff <tt>p</tt> is in the tree.
	 */
	public boolean search(KDPoint p) {
		KDTreeElement<E> i = this.root;
		while(i != null) {
			if (i.getKey().equals(p)) return true;
			i = (i.getKey().coords[i.getDim()] <= p.coords[i.getDim()]) ? i.getRight() : i.getLeft();
		}
		return false;
	}

	/**
	 * Returns the root node of the <tt>KDTree</tt>.
	 * @return The {@link KDPoint} located at the root of the tree, or <tt>null</tt>
	 * if the tree is empty.
	 */
	public KDTreeElement<E> getRoot(){
		return (this.isEmpty()) ? null : this.root;
	}

	/**
	 * Performs a range query on the KD-Tree. Returns all the {@link KDPoint}s whose
	 * {@link KDPoint#distance(KDPoint) distance} from  <tt>p</tt> is <b>at most</b> <tt>range</tt>. This means that
	 * range queries on the KD-Tree are <b>inclusive</b> of the range proper! The query point itself should <b>NOT</b>
	 * be reported. The <tt>KDPoint</tt>s are <b>NOT</b> required to be sorted by distance from <tt>p</tt>.
	 * @param p The query {@link KDPoint}.
	 * @param range The maximum {@link KDPoint#distance(KDPoint, KDPoint)} from <tt>p</tt>
	 * that we allow a {@link KDPoint} to have if it should be part of the solution.
	 * @return A {@link Collection} over all {@link KDPoint}s which satisfy our query. The
	 * {@linkplain Collection} will be empty if there are no points which satisfy the query.
	 */
	public Collection<KDPoint> range(KDPoint p, double range){
		ArrayList<KDPoint> r = new ArrayList<KDPoint>();
		return ran(this.root,p,range,r);
		
		
	}
	
	private ArrayList<KDPoint> ran(KDTreeElement<E> b, KDPoint p, double range, ArrayList<KDPoint> best) {
		if (b == null) return best;
		if (b.getKey().distance(p) <= range && !b.getKey().equals(p)) best.add(b.getKey());
		// check subtree of p first
		// Check other subtree last
        if ((b.getKey().coords[b.getDim()] <= p.coords[b.getDim()])) {  
        	 best = ran(b.getRight(), p, range, best);
             best = ran(b.getLeft(), p, range, best);
        } else {
        	best = ran(b.getLeft(), p, range, best);
            best = ran(b.getRight(), p, range, best);
		}
		return best;
	}

	/** Performs a nearest neighbor query on the <tt>KDTree</tt>. Returns the {@link KDPoint}
	 * which is closest to <tt>p</tt>, as dictated by {@link KDPoint#distance(KDPoint) distance(KDPoint p)}.
	 * This point will be <b>DISTINCT</b> from <tt>p</tt> (otherwise this would make this operation trivial to implement).
	 * @param p The query {@link KDPoint}.
	 * @return The solution to the nearest neighbor query. This method will return <tt>null</tt> if
	 * there are no points other than <tt>p</tt> in the tree.
	 */
	public KDPoint nearestNeighbor(KDPoint p){
		BoundedPriorityQueue<KDPoint> i = new BoundedPriorityQueue<KDPoint>(1);
		return best(this.root,p,i).first();
	}
	
	private BoundedPriorityQueue<KDPoint> best(KDTreeElement<E> b, KDPoint p, BoundedPriorityQueue<KDPoint> nn) {
		if (b == null) return nn;
		 
		if (nn.isEmpty()) {
			if (b.getKey().equals(p) == false) nn.enqueue(b.getKey(), b.getKey().distance(p));
		} else {
			if (b.getKey().equals(p) == false) 
				nn.enqueue(b.getKey(),b.getKey().distance(p));
		}
		if ((b.getKey().coords[b.getDim()] <= p.coords[b.getDim()])) {  
       	    nn = best(b.getRight(), p, nn);
            nn = best(b.getLeft(), p, nn);
       } else {
       		nn = best(b.getLeft(), p, nn);
            nn = best(b.getRight(), p, nn);
		}
		return nn;
	}
	
	
	/**
	 * Performs a m-nearest neighbors query on the <tt>KDTree</tt>. Returns the <em>m</em>
	 * {@link KDPoint}s which are nearest to <tt>p</tt>, as dictated by {@link KDPoint#distance(KDPoint) distance(KDPoint p)}.
	 * The {@link KDPoint}s are sorted in ascending order of distance.
	 * @param m A positive integer denoting the amount of neighbors to return.
	 * @param p The query point.
	 * @return A {@link BoundedPriorityQueue} containing the m-nearest neighbors of <tt>p</tt>.
	 * This queue will be empty if the tree contains only <tt>p</tt>.
	 * @throws RuntimeException If <tt>m&lt;=0</tt>.
	 */
	public BoundedPriorityQueue<KDPoint> kNearestNeighbors(int m, KDPoint p){ 
		BoundedPriorityQueue<KDPoint> i = new BoundedPriorityQueue<KDPoint>(m);
		return best(this.root,p,i);
	}

	/**
	 * Returns the height of the tree. By convention, the height of an empty tree is -1.
	 * @return The height of <tt>this</tt>.
	 */
	public int height() {return hgt(this.root);}
	
	private int hgt(KDTreeElement<E> x) {
		return (x == null) ? -1 : 1 + max(hgt(x.getLeft()),hgt(x.getRight()));
	}
	
	private int max(int a, int b) {return (a > b) ? a : b;}

	/**
	 * Reports whether the <tt>KDTree</tt> is empty, that is, it contains zero {@link KDPoint}s.
	 * @return <tt>true</tt> iff there are no {@link KDPoint}s stored in <tt>this</tt>.
	 */
	public boolean isEmpty(){
		return this.root == null;
	}
	
	public static void main (String[] args) throws Exception {
		int amount = 10;
			//create the Bridges object
Bridges<Double, EarthquakeUSGS> bridges = new Bridges<Double, EarthquakeUSGS>(9,
					"1016042408643", "bnewman9");

bridges.setTitle("A Binary Search Tree Example with Earthquake Data");

		// Retrieve a list of 10 earthquake  records  from USGS using the BRIDGES API
USGSaccount name = new USGSaccount( "earthquake" );
List<EarthquakeUSGS> eqlist = Bridges.getEarthquakeUSGSData(name, amount);

		// create BST nodes and insert into a tree
KDTree<EarthquakeUSGS> tree = new KDTree<EarthquakeUSGS>(); 

for (int k = 0; k < amount; k++) {
	KDTreeElement<EarthquakeUSGS> kd_node = 
	new KDTreeElement< EarthquakeUSGS>(new KDPoint(eqlist.get(k).getLatit(), eqlist.get(k).getLongit()), eqlist.get(k)); 
		// set label of the node
kd_node.setLabel(eqlist.get(k).getTitle() + "\n\nLat/Long: ( " + 
	eqlist.get(k).getLatit() + "," + eqlist.get(k).getLongit() + " )\n\n" + 
	eqlist.get(k).getTime());
	tree.insert(kd_node);
}
tree.delete(new KDPoint(eqlist.get(9).getLatit(), eqlist.get(9).getLongit()));

					// set some visual attributes
tree.getRoot().getVisualizer().setColor("red");

 					//set visualizer type
bridges.setDataStructure(tree.getRoot());
					// visualize the tree
bridges.visualize();
	}

}