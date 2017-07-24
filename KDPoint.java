

/** <p><tt>KDPoint</tt> is a class that represents a k-dimensional point in Euclidean
 * space, where <em>k</em> is a positive integer. It provides methods for initialization,
 * copy construction, equality checks and distance calculations. The precision of <tt>KDPoint</tt>s
 * is <tt>double</tt>.</p>
 * 
 * <p>Students should feel free to add functionality to this class, but it is best
 * if the current functionality is left untouched, since our jUnit tests depend on the
 * current functionality!</p>
 *
 * @author Jason Filippou (jasonfil@cs.umd.edu)
 */
public class KDPoint {
	
	/** To make matters simple for client code, we will allow the <tt>KDPoint</tt>'s
	 * coordinates to be publicly accessible.
	 */
	public double[] coords;
	
	
	/**
	 * Default constructor initializes <tt>this</tt> as a 2D <tt>KDPoint</tt> describing
	 * the Cartesian origin.
	 */
	public KDPoint(){
		this(2);
	}
	/**
	 * Initialize a <em>k</em>-dimensional <tt>KDPoint</tt> at the origin of the axes.
	 * @param k The dimensionality of the <tt>KDPoint</tt>.
	 */
	public KDPoint(int k) {
		if(k <= 0)
			throw new RuntimeException("All Points need to have a positive dimensionality.");
		coords = new double[k];
	}
	
	/**
	 * Initialize a <tt>KDPoint</tt> with some values. Implicitly sets the <tt>KDPoint</tt>'s
	 * dimensionality.  
	 * @param vals The values with which to initialize the <tt>KDPoint</tt>.
	 */
	public KDPoint(double... vals){
		coords = new double[vals.length];
		for(int i = 0; i < coords.length; i++) // Preserve immutability.
			coords[i] = vals[i];
	}
	
	/**
	 * Initialize a <tt>KDPoint</tt> based on an already existing <tt>KDPoint</tt>.
	 * @param p The <tt>KDPoint</tt> on which we will base the creation of <tt>this</tt>.
	 */
	public KDPoint(KDPoint p){
		this(p.coords);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null)
			return false;
		if(o.getClass() != getClass())
			return false;
		KDPoint oCasted = (KDPoint)o; // No ClassCastExceptions here because of above check
		if(oCasted.coords.length != coords.length)
			return false;
		for(int i = 0; i < coords.length; i++)
			if(coords[i] != oCasted.coords[i])
				return false;
		return true;
	}
	
	/**
	 * Calculate the squared Euclidean distance between <tt>this</tt> and <tt>p</tt>.
	 * @param p The <tt>KDPoint</tt> to calculate the distance to.
	 * @return The squared Euclidean distance between the two <tt>KDPoint</tt>s.
	 * @throws RuntimeException if the dimensionalities of the two points are different.
	 */
	public double distance(KDPoint p) throws RuntimeException{
		if(coords.length != p.coords.length)
			throw new RuntimeException("Cannot calculate the Euclidean Distance between KDPoints of different dimensionalities.");
		double sum = 0.0;
		for(int i = 0; i < coords.length; i++)
			sum += Math.pow(coords[i] - p.coords[i], 2);
		return sum;
	}
	
	/**
	 * A static version of distance calculations. Since the Euclidean distance is symmetric,
	 * it's somewhat awkward to have to specify a start and end point, as {@link #distance(KDPoint) distance} does,
	 * so we provide this option as well.
	 * @param p1 One of the two <tt>KDPoint</tt>s to calculate the distance of.
	 * @param p2 One of the two <tt>KDPoint</tt>s to calculate the distance of.
	 * @return The Euclidean distance between <tt>p1</tt> and <tt>p2</tt>.
	 */
	public static double distance(KDPoint p1, KDPoint p2){
		return p1.distance(p2);
	}
	
	@Override
	public String toString(){
		String retVal = "A KDPoint with coordinates: (";
		for(int i = 0; i < coords.length; i++){
			retVal += coords[i];
			if(i < coords.length - 1) 
				retVal += ", ";
		}
		return retVal +").";
	}
}
