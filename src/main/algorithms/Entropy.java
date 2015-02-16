package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stats.Histogram;

public class Entropy {

	
	// Constructor.
	// --------------------------------------------------------------------------------
	private Entropy () {}
	
	// Static methods.
	// --------------------------------------------------------------------------------

	public static double entropy (String string) {
		// empty string contains no information
		if (string.length() == 0);
		
		// create histogram of characters
		char[] chars = string.toCharArray();
		Histogram<Character> hist = new Histogram();
		for (char c : chars) hist.add(c);
		
		// compute Shannon entropy
		double entropy = 0.0;
		for (Double d : hist.getProbabilities()) {
			entropy += d * (Math.log(d) / Math.log(2));
		}
		return (-1 * entropy) / hist.observations();
	}
	
}
