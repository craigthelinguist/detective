package stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Histogram <KEY> {
	
	// Fields.
	// --------------------------------------------------------------------------------

	private Map <KEY, Integer> hist;
	private Map <KEY, Double> histNormed;
	private int numObservations;
	
	
	// Constructors.
	// --------------------------------------------------------------------------------
	public Histogram () {
		hist = new HashMap<>();
		histNormed = new HashMap<>();
		numObservations = 0;
	}
	
	public Histogram (Iterable<KEY> observations) {
		hist = new HashMap<>();
		numObservations = 0;
		for (KEY k : observations) this.add(k, 1);
		this.normalise();
	}
	
	
	// Instance methods.
	// --------------------------------------------------------------------------------

	/**
	 * Add one observation for a given key.
	 * @param k: key that was observed.
	 */
	public void add (KEY k) {
		this.add(k, 1);
	}
	
	/**
	 * Add a number of observations for a given key to the histogram.
	 * @param k: key you'll be adding observations for.
	 * @param count: number of times key was observed.
	 */
	public void add (KEY k, int count) {
		if (hist.containsKey(k)) hist.put(k, hist.get(k) + count);
		else hist.put(k, count);
		numObservations += count;
		histNormed = null;
	}
	
	/**
	 * Get the number of times the given key appears in the histogram.
	 * @param k: key to check
	 * @return int
	 */
	public int getCount (KEY k) {
		if (hist.containsKey(k)) return hist.get(k);
		else return 0;
	}
	
	/**
	 * Get the probability that the given key appears in the histogram.
	 * @param k: key to check.
	 * @return double
	 */
	public double getProb (KEY k) {
		if (histNormed == null)
			throw new UnsupportedOperationException("Histogram must be normed before getting probability.");
		if (histNormed.containsKey(k)) return histNormed.get(k);
		else return 0.0;
	}
	
	/**
	 * Compute the normalisation of this histogram so you can check the
	 * probability of a given key. Normalisation must be re-computed everytime
	 * you add keys to the histogram.
	 */
	public void normalise () {
		if (this.histNormed != null) return; // already been normed
		this.histNormed = new HashMap<>();
		for (KEY k : hist.keySet()) {
			int count = hist.get(k);
			histNormed.put(k, 1.0 * count / numObservations);
		}
	}
	
	/**
	 * Return the number of observations in this histogram.
	 * @return int
	 */
	public int observations () {
		return this.numObservations;
	}
	
	
	// Iterators.
	// --------------------------------------------------------------------------------

	public Collection<Double> getProbabilities () {
		if (this.histNormed == null)
			throw new UnsupportedOperationException("Histogram must be normed before getting probabilities.");
		return this.histNormed.values();
	}
	
	public Collection<Integer> getCounts () {
		return this.hist.values();
	}

	
	// Static methods for automatically making ngram histograms.
	// --------------------------------------------------------------------------------
	
	public static Histogram<String> ngrams(List<String> strings, int degree) {
		Histogram<String> hist = new Histogram<>();
		for (String str : strings) {
			List<String> ngrams = Histogram.ngrams(str, degree);
			for (String ngram : ngrams) hist.add(ngram);
		}
		hist.normalise();
		return hist;
	}
	
	private static List<String> ngrams(String string, int degree) {
		List<String> ngrams = new ArrayList<>();
		char[] chars = string.toCharArray();
		for (int i = 0; i < string.length() - degree + 1; i++){
			StringBuilder sb = new StringBuilder();
			for (int j = i; j < i + degree; j++) sb.append(chars[j]);
			ngrams.add(sb.toString());
		}
		return ngrams;
	}
	
}
