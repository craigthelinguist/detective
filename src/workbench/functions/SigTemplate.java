package functions;

public class SigTemplate {

	public static String make (String name, String[] inputTypes, String[] outputTypes) {
		
		String s1 = name + " :: (";
		for (int i = 0; i < inputTypes.length; i++) {
			s1 += inputTypes[i];
			if (i != inputTypes.length -1) s1 += ",";
		}
		s1 += ") -> (";
		for (int i = 0; i < outputTypes.length; i++) {
			s1 += outputTypes[i];
			if (i != outputTypes.length -1) s1 += ",";
		}
		return s1 + ")";
		
	}
	
}
