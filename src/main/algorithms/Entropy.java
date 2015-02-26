package algorithms;

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
		hist.normalise();
		
		// compute Shannon entropy
		double entropy = 0.0;
		for (Double d : hist.getProbabilities()) {
			entropy += d * (Math.log(d) / Math.log(2));
		}
		return (-1 * entropy) / hist.observations();
	}
	
}
