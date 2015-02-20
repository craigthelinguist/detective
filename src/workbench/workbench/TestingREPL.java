package workbench;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import primitives.Kore;
import rules.Expression;
import rules.Primitive;


public class TestingREPL {

	private static Scanner scan;
	
	private static Map<String, Expression> bindings = new HashMap<>();
	
	public static void assign(String name, Expression expr) {
		bindings.put(name, expr);
	}
	
	public static Expression getBinding(String name) {
		if (bindings.containsKey(name)) return bindings.get(name);
		else return Kore.kore;
	}
	
	private static void shutdown () {
		scan.close();
		System.exit(0);
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
				Expression expr = new Parser(input).parseExpression();
				Primitive p = expr.exec();
				if (p != Kore.kore) System.out.println(p.eval());
				System.out.println();
				
			} catch (Exception e) {
				error(e.getMessage());
			}
		}
	}
	
}
