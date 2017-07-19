import bridges.connect.Bridges;
import bridges.data_src_dependent.EarthquakeUSGS;
import bridges.data_src_dependent.USGSaccount;
import java.util.List;

public class SplayTree <K extends Comparable<K>, E> {
	
	public SplayElement<K, E> root; // pointer to the root of the tree
	
	SplayTree() {}
	
	private SplayElement<K, E> search(K key, SplayElement<K, E> node) {
        if (key.compareTo(node.getKey()) < 0) 
            return (node.getLeft() == null) ? node : search(key, node.getLeft());
        if (key.compareTo(node.getKey()) > 0) 
        	return (node.getRight() == null) ? node : search(key, node.getRight());
        
        return node;
    }
	
	 private void rotateLeft( SplayElement<K, E> x) {
		 	SplayElement<K, E> y = x.getRight();
	        x.setRight(y.getLeft());
	        if (y.getLeft() != null) y.getLeft().setParent(x);
	        y.setParent(x.getParent());
	        if (x.getParent() == null) root = y;
	        else 
	            if (x == x.getParent().getLeft()) x.getParent().setLeft(y);
	            else x.getParent().setRight(y);
	        y.setLeft(x);
	        x.setParent(y);
	    }
	
	 private void rotateRight(SplayElement<K, E> x) {
		    SplayElement<K, E> y = x.getLeft();
	        x.setLeft(y.getRight());
	        if (y.getRight() != null) y.getRight().setParent(x);
	        y.setParent(x.getParent());
	        if (x.getParent() == null) root = y;
	        else 
	            if (x == x.getParent().getLeft()) x.getParent().setLeft(y);
	            else x.getParent().setRight(y);
	        y.setRight(x);
	        x.setParent(y);
	    }
	 /**
	  * Splay: This function uses AVL style rotations to bring the desired node up to the root of the tree.
	  * By Doing this, we have optimized the node for temporal and spatial locality with respect to that node. 
	  * @param node
	  */
	 
	 private void splay(SplayElement<K, E> node) {
		 SplayElement<K, E> p, g;
	     while (node.getParent() != null) {
	    	 if ((g = (p = node.getParent()).getParent()) == null) 
	    		 if (p.getLeft() == node) rotateRight(p);
	             else rotateLeft(p);
	         else 
	        	 if (p.getLeft() == node && g.getLeft() == p) {
	        		 rotateRight(g);
	                 rotateRight(node.getParent());
	             } else if (p.getLeft() == node && g.getRight() == p) {
	                 rotateRight(p);
	                 rotateLeft(node.getParent());
	             } else if (p.getRight() == node && g.getRight() == p) {
	                 rotateLeft(g);
	                 rotateLeft(node.getParent());
	             } else {
	                 rotateLeft(p);
	                 rotateRight(node.getParent());
	             }
	      }
	  }
	 
	 	/**
	 	 * Splay Tree Search: this function optimizes 
	 	 * the tree for the spatial locality of the element searched for
	 	 * while determining whether the element is in the tree
	 	 * @param key
	 	 * @return true if in the tree else false
	 	 */
		 public boolean search(K key) {
		      if (root == null) return false;
		      SplayElement<K, E> node = search(key, root);
		      splay(node);
		      return node.getKey().compareTo(key) == 0;
		 }
	 
	 
	 	
	 	/**Splay Tree Insert: inserts the key into the tree and sets the value to null 
	 	 * while also optimizing the tree for spatial locality around that element
	 	 * 
	 	 * @param key
	 	 */
	 
	 	public void insert(K key) {
	 		this.insert(new SplayElement<K,E>(key, null, null, null));
	 	}
	 	
	 	/**Splay Tree Insert: inserts the key into the tree and sets the value to val 
	 	 * while also optimizing the tree for spatial locality around that element
	 	 * 
	 	 * @param key
	 	 */
	 	
	 	public void insert(K key, E val) {
	 		this.insert(new SplayElement<K,E>(key, val, null, null));
	 	}
	 	
	 	/**Splay Tree Insert: inserts the element into the tree
	 	 * while also optimizing the tree for spatial locality around that element
	 	 * 
	 	 * @param key
	 	 */
	 	public void insert(SplayElement<K, E> n) {
	        if (root == null) {
	            root = n;
	            return;
	        }
	        SplayElement<K, E> node = search(n.getKey(), this.root);
	        if (n.getKey().compareTo(node.getKey()) < 0) {
	        	n.setParent(node);
                node.setLeft(n);
	        } else if (n.getKey().compareTo(node.getKey()) > 0) {
	        	n.setParent(node);
                node.setRight(n); 
	        } splay(n);
	    }
	 	/**
	 	 * Splay Tree Delete: deletes the key and its assocatiated value from the tree 
	 	 * while also optimizing the tree for spatial locality around that element
	 	 * 
	 	 * @param key
	 	 */
	 	
	 	 public void delete(K key) {
	         if (root == null) return;
	         search(key);
	         if (this.root.getKey().compareTo(key) == 0) {
	        	 if (this.root.getLeft() == null) {
	        		 this.root.getRight().setParent(null);
	        		 this.root = this.root.getRight(); 
	        		 return;
	        	 }
	        	 this.root.getLeft().setParent(null);
	        	 SplayElement<K, E> node = search(key, this.root.getLeft());
	       	     splay(node);
	       	     node.setRight(this.root.getRight());
	       	     this.root = this.root.getRight();
	         }
	     }
	 	 /**
	 	  * returns whether the tree is empty
	 	  * @return true if there are no elements in the tree else false
	 	  */
	 	 
	 	
		 	public boolean isEmpty() {
		 		return this.root == null;
		 	}
	 	
	 	/**
		 * returns the current root of the splay tree
		 * @return the root of the splay tree
		 */
		public SplayElement<K, E> getRoot() {
			return this.root;
		}
	
		public static void main(String[] args) throws Exception {
	    	
			int amount = 10;
	   					//create the Bridges object
			Bridges<Double, EarthquakeUSGS> bridges = new Bridges<Double, EarthquakeUSGS>(9,
									"1016042408643", "bnewman9");

			bridges.setTitle("A Binary Search Tree Example with Earthquake Data");

						// Retrieve a list of 10 earthquake  records  from USGS using the BRIDGES API
			USGSaccount name = new USGSaccount( "earthquake" );
			List<EarthquakeUSGS> eqlist = Bridges.getEarthquakeUSGSData(name, amount);

						// create BST nodes and insert into a tree
			SplayTree<Double,EarthquakeUSGS> tree = new SplayTree<Double, EarthquakeUSGS>(); 
			
			for (int k = 0; k < amount; k++) {
				SplayElement<Double, EarthquakeUSGS> splay_node = 
					new SplayElement<Double, EarthquakeUSGS>(eqlist.get(k).getMagnitude(), eqlist.get(k)); 
						// set label of the node
				splay_node.setLabel(eqlist.get(k).getTitle() + "\n\nLat/Long: ( " + 
					eqlist.get(k).getLatit() + "," + eqlist.get(k).getLongit() + " )\n\n" + 
					eqlist.get(k).getTime());
				tree.insert(splay_node);
			}
			tree.delete(eqlist.get(9).getMagnitude());

									// set some visual attributes
			tree.getRoot().getVisualizer().setColor("red");

	             					//set visualizer type
			bridges.setDataStructure(tree.getRoot());
	        						// visualize the tree
			bridges.visualize();
	    }
	

		
}

