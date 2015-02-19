package algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import stats.Histogram;

public class Strings {

	private Strings () {}
	
	/**
	 * Return true if two strings share no common characters.
	 * @param s1: first string
	 * @param s2: second string
	 * @return true if they have no chars in common
	 */
	public boolean disjoint (String s1, String s2) {
		for (char c : s1.toCharArray()) {
			if (s1.contains(""+c)) return false;
		}
		return true;
	}

	public static double Bhattacharyya (Collection<String> s1, Collection<String> s2) {
		return Bhattacharyya(s1,s2,2);
	}
	
	/**
	 * Compute the Bhattacharyya distance between two strings. Assumes that the valid characters
	 * in the strings are [A-Za-z]
	 * @param s1: first string
	 * @param s2: second string
	 * @param degree: degree of ngrams to use
	 * @return Bhattacharyya distance
	 */
	public static double Bhattacharyya (Collection<String> s1, Collection<String> s2, int degree) {
		
		Histogram<String> hist1 = Histogram.ngrams(s1, degree, true);
		Histogram<String> hist2 = Histogram.ngrams(s2, degree, true);
		
		double dist = 0.0;
		Set<String> ngramsExamined = new HashSet<>();
		
		for (String st : hist1.getKeys()){
			double p1 = hist1.getProb(st);
			double p2 = hist2.getProb(st);
			double result = Math.sqrt(p1*p2);
			if (result == 0.0) throw new RuntimeException("Laplace smoothing has gone awry!");
			dist += result;
			ngramsExamined.add(st);
		}

		for (String st : hist2.getKeys()){
			if (ngramsExamined.contains(st)) continue;
			double p1 = hist1.getProb(st);
			double p2 = hist2.getProb(st);
			double result = Math.sqrt(p1*p2);
			if (result == 0.0) throw new RuntimeException("Laplace smoothing has gone awry!");
			dist += result;
			ngramsExamined.add(st);
		}
		
		return -1.0 * Math.log(dist);
		
	}
	
	public static void main (String[] args) {
		List<String> s1 = new ArrayList<>();
		List<String> s2 = new ArrayList<>();
		s1.add("helloworld");
		s2.add("johnwayne");
		double dist = Strings.Bhattacharyya(s1, s2);
		System.out.println(dist);
	}
	
}
