package clustering;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class VectorTests {

	@Test
	public void testIndexing001 () {
		double[] ds = new double[]{1.0, 36.4, 23.7};
		Vector v = new Vector(ds);
		assertTrue(v.get(0) == 1.0);
		assertTrue(v.get(1) == 36.4);
		assertTrue(v.get(2) == 23.7);
		assertTrue(v.get(3) == 0.0);
		assertTrue(v.get(4) == 0.0);
		try{ v.get(-1); fail("Should have thrown error from bad index."); }
		catch (Exception e){}
	}
	
	@Test
	public void testDistance001 () {
		for (int trial = 1; trial < 100; trial++) {
			 Vector v1 = randomVector(100);
			 Vector v2 = v1;
			 assertTrue("Every vector should be distance of 0 from itself.", Vectors.distance(v1, v2) == 0);
		}
	}
	
	@Test
	public void testDistance002 () {
		Vector v1 = new Vector(new double[]{ 2.0 });
		Vector v2 = new Vector(new double[]{ 4.0 });
		assertTrue("1d vector distance was wrong.", Vectors.distance(v1, v2) == 2.0);
	}
	
	@Test
	public void testDistance003 () {
		Vector v1 = new Vector(new double[]{ 1.0, 0.0 });
		Vector v2 = new Vector(new double[]{ 0.0, 1.0 });
		assertEquals("2d unit triangle has hypotenuse of sqrt(2)", Vectors.distance(v1, v2), Math.sqrt(2), 0.001);
	}
	
	@Test
	public void testDistance004 () {
		for (int trial = 1; trial < 100; trial++) {
			 Vector v1 = randomVector(100);
			 Vector v2 = randomVector(100);
			 assertEquals("Vector distance is commutative.", Vectors.distance(v1,v2), Vectors.distance(v2,v1), 0.0);
		}
	}

	private Random rand = new Random();
	
	private Vector randomVector (int length) {
		double[] dubs = new double[length];
		for (int i = 0; i < length; i++) {
			dubs[i] = rand.nextDouble()*1000;
		}
		return new Vector(dubs);
	}
	
}
