package clustering.feature.aggregation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import vectors.Vector;
import vectors.Vectors;
import dns.Host;

/**
 * 
 * This implementation is O(3^d*n)
 * 	d = number of dimensions
 * 	n = number of vectors
 * 
 * @author aaroncraig
 *
 */
public class NeighbourAggregate implements AggregateStrategy {

	final int SUBSET_SIZE;
	
	public NeighbourAggregate(int SUBSET_SIZE) {
		this.SUBSET_SIZE = SUBSET_SIZE;
	}
	
	@Override
	public List<Vector> aggregate(List<Host> hosts) {
		List<Vector> vectors = new ArrayList<>();
		for (Host host : hosts) {
			List<List<String>> partition = partition(host);
			for (List<String> block : partition) {
				Vector vector = Vectors.featureVector(host, block);
				vectors.add(vector);
			}
		}
		return vectors;
	}

	static int count = 0;
	
	public static Vector[] closestPair (List<Vector> vectors) {
		
		if (vectors.size() < 2) return null;
		int dimensions = vectors.get(0).dimensions();
		
		
		Vector[] vSet = new Vector[vectors.size()];
		vSet[0] = vectors.get(0); vSet[1] = vectors.get(1);
		double deltaX = Vectors.distance(vSet[0], vSet[1]);
		Vector[] pair = { vSet[0], vSet[1] };
		Grid grid = new Grid(vSet, deltaX, dimensions);
		
		for (int i = 2; i < vectors.size(); i++) {
			
			// construct set i
			Vector current = vectors.get(i);
			vSet[i] = current;
			
			// get points in the neighbourhood of current
			List<Vector> neighbours = grid.neighbourhood(current);
			
			// get smallest distance in that neighbourhood
			double min = -1;
			Vector[] minPair = new Vector[2];
			for (Vector vector : neighbours) {
				double d = Vectors.distance(vector, current);
				if (min == -1 || d < min) {
					min = d;
					minPair = new Vector[]{ current, vector };
				}
			}
			
			// was that distance smaller than deltaX?
			if (min != -1 && min < deltaX) {
				deltaX = min;
				pair = minPair;
				grid = new Grid(vSet, deltaX, dimensions);
			}
			else grid.insert(current);
			
		}
		
		return pair;
	}
		
	
	private List<List<String>> partition (Host host) {
		
		List<String> queries = host.getQueries();
		
		// initialise UnionFindCollect trees
		Set<UnionFindCollect> blocks = new HashSet<>();
		for (String str : queries) {
			UnionFindCollect ufc = new UnionFindCollect(str);
			blocks.add(ufc);
		}
		
		// keep track of the best pair at each iteration
		List<Vector> vectors = new ArrayList<>();
		
		while (true) {

			System.out.println("Count " + (count++));
			// vectorise each head
			Map<Vector, UnionFindCollect> map = new HashMap<>();
			vectors = new ArrayList<>();
			Set<UnionFindCollect> seen = new HashSet<>();
			for (UnionFindCollect ufc : blocks) {
				ufc = ufc.find();
				if (seen.contains(ufc)) continue;
				seen.add(ufc);
				List<String> collect = ufc.collect();
				if (collect.size() >= SUBSET_SIZE) continue;
				Vector v = Vectors.featureVector(host, collect);
				vectors.add(v);
				map.put(v, ufc);
			}
			if (vectors.size() < 2) break;
			
			// get closest pair and merge
			Vector[] closest = closestPair(vectors);
			UnionFindCollect ufc1 = map.get(closest[0]);
			UnionFindCollect ufc2 = map.get(closest[1]);
			ufc1.union(ufc2);
			
		}
		
		// map the final ufc nodes to their corresponding domains
		List<List<String>> partition = blocks.stream()
											 .map(block -> block.find().collect())
											 .collect(Collectors.toList());
		
		// return the vectors
		return partition;
		
	}
	
	public class UnionFindCollect {
	
		private String value;
		private UnionFindCollect head;
		private List<UnionFindCollect> kids;
		
		public UnionFindCollect (String value) {
			this.value = value;
			this.head = null;
			this.kids = new ArrayList<>();
		}
		
		public UnionFindCollect find () {
			if (head == null) return this;
			else{
				UnionFindCollect newHead = this.head.find();
				this.head.kids.remove(this);
				this.head = newHead;
				newHead.kids.add(this);
				return newHead;
			}
		}
		
		public List<UnionFindCollect> kids() {
			return this.kids;
		}
		
		public void addKid (UnionFindCollect newKid) {
			this.kids.add(newKid);
		}
		
		public boolean union (UnionFindCollect other) {
			UnionFindCollect head1 = find();
			UnionFindCollect head2 = other.find();
			if (head1 == head2) return false;
			if (head1.head != null) head1.head.kids.remove(this);
			head1.head = head2;
			head2.kids.add(head1);
			return true;
		}
		
		public List<String> collect () {
			List<String> values = new ArrayList<>();
			values.add(value);
			for (UnionFindCollect kid : kids) {
				List<String> childValues = kid.collect();
				values.addAll(childValues);
			}
			return values;
		}
		
		@Override
		public int hashCode() {
			return value.hashCode();
		}
		
		public String toString() {
			return value;
		}
		
	}
	
	public String toString () {
		return "VectorAggregate";
	}
	
}
