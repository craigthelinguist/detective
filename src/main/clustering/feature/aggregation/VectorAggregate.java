package clustering.feature.aggregation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vectors.Vector;
import vectors.Vectors;
import dns.Host;

public class VectorAggregate implements AggregationStrategy {

	@Override
	public List<Vector> aggregate(List<Host> hosts, int SUBSET_SIZE) {
		List<Vector> vectors = new ArrayList<>();
		for (Host host : hosts) {
			List<List<String>> partition = partition(host, SUBSET_SIZE);
			for (List<String> block : partition) {
				Vector vector = Vectors.featureVector(host, block);
				vectors.add(vector);
			}
		}
		return vectors;
	}

	private List<List<String>> partition (Host host, int SUBSET_SIZE) {
		
		List<String> queries = host.getQueries();
		
		// initialise UnionFindCollect trees
		Set<UnionFindCollect> blocks = new HashSet<>();
		for (String str : queries) {
			UnionFindCollect ufc = new UnionFindCollect(str);
			blocks.add(ufc);
		}
		
		// keep track of the best pair so far
		UnionFindCollect[] bestPair;
		double closestDist;
		
		do {
			
			bestPair = null;
			closestDist = -1.0;
			
			for (UnionFindCollect ufc1 : blocks) {
				for (UnionFindCollect ufc2 : blocks) {
					
					
					// don't compare with yourself
					if (ufc1 == ufc2) continue;
					
					// find the heads
					UnionFindCollect head1 = ufc1.find();
					UnionFindCollect head2 = ufc2.find();
					
					// already in the same tree
					if (head1 == head2) continue;
					
					// get list of strings corresponding to this set
					List<String> collect1 = head1.collect();
					List<String> collect2 = head2.collect();
					
					// too big to merge
					if (collect1.size() + collect2.size() > SUBSET_SIZE) continue;
					
					// turn the set into corresponding entropy vector
					Vector entropy1 = Vectors.featureVector(host, collect1);
					Vector entropy2 = Vectors.featureVector(host, collect2);
					
					
					double dist = Vectors.distance(entropy1, entropy2);
					if (closestDist == -1 || dist < closestDist) {
						bestPair = new UnionFindCollect[]{ head1, head2 };
						closestDist = dist;
					}
					
					
				}
			}
			
			// union the best pair that you found
			if (bestPair != null) {
			
				UnionFindCollect head1 = bestPair[0];
				UnionFindCollect head2 = bestPair[1];

				/* Debugging
				List<String> collect1 = head1.collect();
				List<String> collect2 = head2.collect();
				System.out.println("Merging: \n" + collect1 + "\n" + collect2);
				*/
				
				head1.union(head2);
				blocks.remove(head1);
			}
			
		} while (bestPair != null);

		// map each node to its corresponding list of domains, return the list of list of domains
		List<List<String>> partition = new ArrayList<>();
		for (UnionFindCollect block : blocks) {
			List<String> strings = block.collect();
			partition.add(strings);
		}
		
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
	
}
