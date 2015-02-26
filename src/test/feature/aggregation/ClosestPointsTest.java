package feature.aggregation;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import clustering.feature.aggregation.ClosestPair;
import vectors.Vector;

public class ClosestPointsTest {

	@Test
	public void test001() {
	
		Vector[] vects = {
			new Vector(new double[]{4.0,3.0,2.0} ),
			new Vector(new double[]{230,423,150} ),
			new Vector(new double[]{123,500,400} ),
			new Vector(new double[]{6,5,0} ),
			new Vector(new double[]{123,432,432} )
		};
	
		List<Vector> vectors = Arrays.asList(vects);
		Vector[] pair = ClosestPair.closestPair(vectors);
		
		if (!verify(pair, vects[0], vects[3])) fail("Wrong answer.");
	}
	
	private boolean verify (Vector[] answer, Vector v1, Vector v2){		
		if (answer[0].equals(v1) && answer[1].equals(v2)) return true;
		if (answer[0].equals(v2) && answer[1].equals(v1)) return true;
		return false;
	}
	
}
