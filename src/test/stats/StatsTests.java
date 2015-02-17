package stats;

import static org.junit.Assert.*;

import org.junit.Test;

public class StatsTests {

	private final double DELTA = 0.000001;
	
	@Test
	public void testMean001 () {
		double[] dubs = new double[]{ 1.0 };
		double mean = Stats.mean(dubs);
		assertEquals(mean, 1.0, DELTA);
	}
	
	@Test
	public void testMean002 () {
		double[] dubs = new double[] { 1.0, 2.0 };
		double mean = Stats.mean(dubs);
		assertEquals(mean, 1.5, DELTA);
	}
	
	@Test
	public void testMean004 () {
		double[] dubs = new double[] { 1.0, 3.0, 5.0 };
		double mean = Stats.mean(dubs);
		assertEquals(mean, 3.0, DELTA);
	}

	@Test
	public void testMean005 () {
		double[] dubs = new double[] { 3.0, 5.0, 1.0 };
		double mean = Stats.mean(dubs);
		assertEquals(mean, 3.0, DELTA);
	}

	@Test
	public void testMean006 () {
		double[] dubs = new double[] { 1.0, 5.0, 10.0, 16.0, 14.0, 3.0, 4.0 };
		double mean = Stats.mean(dubs);
		assertEquals(mean, (1.0+5.0+10.0+16.0+14.0+3.0+4.0)/dubs.length , DELTA);
	}
	
	@Test
	public void testMedian001 () {
		double[] dubs = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 };
		double median = Stats.median(dubs);
		assertEquals(median, 3.0, DELTA);
	}
	
	@Test
	public void testMedian002 () {
		double[] dubs = new double[] { 1.0, 5.0, 4.0, 2.0, 3.0 };
		double median = Stats.median(dubs);
		assertEquals(median, 3.0, DELTA);		
	}
	
	@Test
	public void testMedian003 () {
		double[] dubs = new double[] { 1.0, 3.0, 16.0, 20.0, 24.0 };
		double median = Stats.median(dubs);
		assertEquals(median, 16.0, DELTA);
	}
	
	@Test
	public void testMedian004 () {
		double[] dubs = new double[] { 1.0, 3.0, 16.0, 24.0 };
		double median = Stats.median(dubs);
		assertEquals(median, 19.0/2.0, DELTA);		
	}
	
	@Test
	public void testMedian005 () {
		double[] dubs = new double[] { 1.0, 1.0, 1.0, 20.0 };
		double median = Stats.median(dubs);
		assertEquals(median, 1.0, DELTA);
	}
	
	
	
}
