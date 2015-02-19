package vectors;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import algorithms.Entropy;
import dns.Domains;
import dns.Host;
import stats.Histogram;
import stats.Stats;

public class Vectors {

	// Constructors
	// --------------------------------------------------------------------------------

	private Vectors () {}
	
	
	// Static methods.
	// --------------------------------------------------------------------------------
	
	public static double distance (Vector v1, Vector v2) {
		double sum = 0.0;
		for (int i = 0; i < Math.max(v1.dimensions(), v2.dimensions()); i++){
			sum += Math.pow(v1.get(i) - v2.get(i), 2);
		}
		return Math.sqrt(sum);
	}
	
	public static Vector mean (List<Vector> vectors) {
		
		if (vectors.isEmpty()) throw new ArrayIndexOutOfBoundsException("No vectors to take the mean of!");
		
		// get highest dimensions
		int highestDimension = vectors.get(0).dimensions();
		for (Vector v : vectors) {
			if (v.dimensions() > highestDimension) highestDimension = v.dimensions();
		}
		
		// compute mean for each component
		double[] values = new double[highestDimension];
		for (int i = 0; i < highestDimension ;i++) {
			double[] dubs = new double[vectors.size()];
			int j = 0;
			for (Vector vector : vectors) {
				double value = vector.get(i);
				dubs[j++] += value;
			}
			values[i] = Stats.mean(dubs);
		}
		
		return new Vector(values);
	}
	
	
	
	// Entropy Vector stuff.
	// --------------------------------------------------------------------------------
	public static Vector EntropyVector (String str) {
		return new Vector(new double[]{
			Entropy.entropy(str),
			Entropy.entropy(Domains.extract2LD(str)),
			Entropy.entropy(Domains.extract3LD(str))
		});
	}
	
	public static Vector EntropyVector (Collection<String> strings) {
		Vector[] vectors = new Vector[strings.size()];
		int i = 0;
		for (String str : strings) {
			vectors[i] = EntropyVector(str);
		}	
		
		double[] supremeArray = new double[3];
		for (int j = 0; j < 3; j++) {
			double sum = 0.0;
			for (Vector v : vectors) {
				sum += v.get(j);
			}
			supremeArray[j] = sum / vectors.length;
		}
		
		return new Vector(supremeArray);
	}
	

	
	
	// Feature Vector stuff.
	// --------------------------------------------------------------------------------

	/**
	 * Make a feature vector from the given host and domains queried.
	 * @param host: that sent the queries.
	 * @param domains: list of the domains queried by the host.
	 * @return a vector whose components represent structural features about the domains.
	 */
	public static Vector featureVector (Host host, List<String> domains) {
		double[] entropyFeatures = Vectors.entropyFeatures(domains);
		double[] ngramFeatures = Vectors.ngramFeatures(domains);
		double[] structuralFeatures = Vectors.structuralFeatures(domains);
		double[] features = new double[entropyFeatures.length + ngramFeatures.length + structuralFeatures.length];
		int index = 0;
		for (double feature : entropyFeatures) features[index++] = feature;
		for (double feature : ngramFeatures) features[index++] = feature;
		for (double feature : structuralFeatures) features[index++] = feature;
		return new Vector(features, host, domains);
	}

	/**
	 * Compute entropy features about the domains.
	 * @param domains: list of domains
	 * @return double[] whose entries represent entropy features about the domains.
	 */
	private static double[] entropyFeatures (List<String> domains) {
		
		// will store entropy scores for each domain queried
		double[] entropyFull = new double[domains.size()];
		double[] entropy2LDs = new double[domains.size()];
		double[] entropy3LDs = new double[domains.size()];
		
		// construct array of features
		int j = 0;
		for (String domain : domains) {
			entropyFull[j] = Entropy.entropy(domain);
			entropy2LDs[j] = Entropy.entropy(Domains.extract2LD(domain));
			entropy3LDs[j] = Entropy.entropy(Domains.extract3LD(domain));
		}
		
		// return array of features
		return new double[] {
				Stats.mean(entropyFull),
				Stats.median(entropyFull),
				Stats.stdev(entropyFull),
				Stats.mean(entropy2LDs),
				Stats.median(entropy2LDs),
				Stats.stdev(entropy2LDs),
				Stats.mean(entropy3LDs),
				Stats.median(entropy3LDs),
				Stats.stdev(entropy3LDs) };
	}

	/**
	 * Compute ngram features about the domains.
	 * @param domains: list of domains
	 * @return double[] whose entries represent ngram features about the domains.
	 */
	private static double[] ngramFeatures (List<String> domains) {
		double[] ngrams = new double[12];
		int j = 0;
		for (int degree = 1; degree <= 4; degree++) {
			Histogram<String> hist = Histogram.ngrams(domains, degree);
			int[] counts = new int[hist.observations()];
			int p = 0;
			for (int count : hist.getCounts()) counts[p++] = count;
			ngrams[j++] = Stats.mean(counts);
			ngrams[j++] = Stats.median(counts);
			ngrams[j++] = Stats.stdev(counts);
		}
		return ngrams;
	}
		
	
	/**
	 * Compute structural features about the domains.
	 * @param domains: list of domains
	 * @return double[] whose entries represent entropy features about the domains.
	 */
	private static double[] structuralFeatures (List<String> domains) {
		
		// remember relevant info
		Set<String> tlds = new HashSet<>();
		Set<Character> chars = new HashSet<>();
		int[] lengths = new int[domains.size()];
		int[] domainLevels = new int[domains.size()];
		int numDotComs = 0;
		
		// one pass through all the domains
		for (int i = 0; i < domains.size(); i++) {
			String domain = domains.get(i);
			for (char c : domain.toCharArray()) chars.add(c);
			String[] split = domain.split("\\.");
			String tld = split[split.length-1];
			tlds.add(tld);
			if (tld.equals(".com")) numDotComs++;
			lengths[i] = domain.length();
			domainLevels[i] = split.length;
		}
		
		// return features
		return new double[] {
			Stats.mean(lengths),
			Stats.variance(lengths),
			Stats.median(lengths),
			Stats.stdev(lengths),
			Stats.mean(domainLevels),
			Stats.variance(domainLevels),
			Stats.median(domainLevels),
			Stats.stdev(domainLevels),
			1.0 * tlds.size(),
			1.0 * chars.size(),
			1.0 * numDotComs / domains.size() };
		
	}
	
}
