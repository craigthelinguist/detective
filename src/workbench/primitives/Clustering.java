package primitives;

import java.util.Iterator;
import java.util.List;

import clustering.Cluster;
import rules.Primitive;

public class Clustering extends Primitive implements Iterable<Seq<Cluster>> {
	
	private List<Seq<Cluster>> clusters;

	public Clustering (List<Seq<Cluster>> cls) {
		clusters = cls;
	}
	
	@Override
	public Iterator<Seq<Cluster>> iterator() {
		return clusters.iterator();
	}
	
}
