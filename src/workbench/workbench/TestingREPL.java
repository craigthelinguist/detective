package workbench;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import errors.ParsingException;
import errors.TypeException;
import functions.Function;
import functions.FunctionFactory;
import primitives.Int;
import primitives.Kore;
import primitives.Primitive;
import primitives.Str;


public class TestingREPL {

	private static Scanner scan;
	
	/**
	 * Return an invocation object which contains the command to execute and any arguments
	 * to pass into this command.
	 * @param input: what the user typed in.
	 * @return an invocation object containing the command name and any arguments
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static Function parseCommand (String input)
	throws TypeException, ParsingException, InstantiationException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		// sanity checks
		if (!input.contains("(") || !input.contains(")") || !(input.charAt(input.length()-1) == ')')) throw new ParsingException("Malformed function call.");
			
		// keep track of current char we're looking at
		int i = 0;

		// parse command name
		StringBuilder sb = new StringBuilder();
		for (; i < input.length() && input.charAt(i) != '('; i++) {
			sb.append(input.charAt(i));
		}
		String cmdName = sb.toString().toLowerCase();
		i++; // it is now looking one afetr the '('
		
		// calling with no arguments
		if (input.charAt(i) == ')') return FunctionFactory.make(cmdName, new Primitive[0]);
		
		// missing closing bracket
		if (i == input.length()-1) throw new ParsingException("Malformed function call. Did you mean: " + input + ")?");
		
		// parse args into primitives
		String substr = input.substring(i, input.length()-1);
		String[] args = substr.split(",");
		Primitive[] prims = new Primitive[args.length];
		for (int k = 0; k < args.length; k++) {
			prims[k] = parse(args[k].trim());
		}
		
		return FunctionFactory.make(cmdName, prims);
	}
	
	private static void shutdown () {
		scan.close();
		System.exit(0);
	}
	
	private static Primitive parse (String str)
	throws ParsingException {
		// Str
		if (str.startsWith("\"") && str.endsWith("\"")) {
			if (str.length() == 2) return new Str(""); // empty string
			else return new Str(str.substring(1, str.length()-1));
		}
		
		// Int
		else if (isInt(str)) return new Int(Integer.parseInt(str));
		
		// unknown
		else throw new ParsingException("Unknown argument: " + str);
	}
	
	private static boolean isInt (String str) {
		try{
			Integer.parseInt(str);
			return true;
		}
		catch(NumberFormatException nfe) { return false; }
	}
	
	public static void error (String msg) {
		System.err.println("*** " + msg + " ***");
	}
			
	public static void main(String[] args) {
		scan = new Scanner(System.in); 
		while (true) {
			System.out.print(">");
			String input = scan.nextLine().trim();			
			try {
				Function func = parseCommand(input);
				if (func.returnType() != Kore.class) System.out.println(func.eval() + "\n");
			} catch (Exception e) { error(e.getMessage()); }
			
		}
	}
	
}
