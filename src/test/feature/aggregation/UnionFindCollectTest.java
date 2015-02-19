package feature.aggregation;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import clustering.feature.aggregation.EntropyAggregate;
import clustering.feature.aggregation.EntropyAggregate.UnionFindCollect;

public class UnionFindCollectTest {

	private static final EntropyAggregate agg = new EntropyAggregate();
	
	@Test
	public void testFind001() {
		
		UnionFindCollect ufc1 = agg.new UnionFindCollect("hello");
		UnionFindCollect ufc2 = agg.new UnionFindCollect("johnny");
		
		assertTrue(ufc1 == ufc1.find());
		assertTrue(ufc2 == ufc2.find());
	}

	@Test
	public void testFind002() {
		
		UnionFindCollect ufc1 = agg.new UnionFindCollect("hello");
		UnionFindCollect ufc2 = agg.new UnionFindCollect("johnny");
		
		ufc1.union(ufc2);
		assertTrue(ufc2 == ufc1.find());
		assertTrue(ufc2 == ufc1.find());
		assertTrue(ufc2.kids().contains(ufc1));
		assertTrue(ufc2.kids().size() == 1);
	}
	
	@Test
	public void testFind003 () {
	
		UnionFindCollect ufc1 = agg.new UnionFindCollect("hello");
		UnionFindCollect ufc2 = agg.new UnionFindCollect("johnny");
		ufc1.union(ufc1);
		assertTrue(ufc1 == ufc1.find());
		assertTrue(ufc2 == ufc2.find());
		
	}
	
	
	@Test
	public void testFind004 () {
		
		UnionFindCollect ufc1 = agg.new UnionFindCollect("a");
		UnionFindCollect ufc2 = agg.new UnionFindCollect("b");
		UnionFindCollect ufc3 = agg.new UnionFindCollect("c");
		UnionFindCollect ufc4 = agg.new UnionFindCollect("d");
		
		ufc1.union(ufc2);
		ufc4.union(ufc3);
		
		assertTrue(ufc2 == ufc1.find());
		assertTrue(ufc2 == ufc2.find());
		assertTrue(ufc3 == ufc4.find());
		assertTrue(ufc3 == ufc3.find());
		
		ufc4.union(ufc1);
		
		assertTrue(ufc2 == ufc1.find());
		assertTrue(ufc2 == ufc2.find());
		assertTrue(ufc2 == ufc3.find());
		assertTrue(ufc2 == ufc4.find());
		
	}
	
	@Test
	public void testCollect001 () {
		
		UnionFindCollect ufc1 = agg.new UnionFindCollect("billy");
		List<String> collect = ufc1.collect();
		assertTrue(collect.contains("billy"));
		assertTrue(collect.size() == 1);
		
	}
	
	@Test
	public void testCollect002 () {
		UnionFindCollect ufc1 = agg.new UnionFindCollect("billy");
		UnionFindCollect ufc2 = agg.new UnionFindCollect("johnny");
		UnionFindCollect ufc3 = agg.new UnionFindCollect("hemi");
		UnionFindCollect ufc4 = agg.new UnionFindCollect("rawinia");
		
		ufc1.union(ufc2);
		ufc3.union(ufc1);
		ufc2.union(ufc4);
		
		assertTrue(ufc1.find() == ufc4);
		assertTrue(ufc2.find() == ufc4);
		assertTrue(ufc3.find() == ufc4);
		assertTrue(ufc4.find() == ufc4);
		assertTrue(ufc4.kids().size() == 3);
		assertTrue(ufc4.kids().contains(ufc1));
		assertTrue(ufc4.kids().contains(ufc2));
		assertTrue(ufc4.kids().contains(ufc3));
		
		List<String> collect = ufc4.collect();
		assertTrue(collect.contains("billy"));
		assertTrue(collect.contains("johnny"));
		assertTrue(collect.contains("hemi"));
		assertTrue(collect.contains("rawinia"));
		assertTrue(collect.size() == 4);
		
	}
	
}
