package datastructs;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaTest {

	public static void main(String[] args) {
		
		String[] array = {"Rafael Nadal", "John Stewart", "Bill O'Reilly"};
		List<String> players = Arrays.asList(array);
		
		List<String> names = players.stream()
									.map((name) -> name.split(" ")[0])
									.collect(Collectors.toList());
			
		names.forEach(name -> System.out.println(name));
		
	}
	
}
