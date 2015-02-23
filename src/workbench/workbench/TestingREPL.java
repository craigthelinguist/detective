 package workbench;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import wblang.primitives.Int;
import wblang.primitives.Kore;
import wblang.rules.Expression;
import wblang.rules.Primitive;


public class TestingREPL {

	private static Scanner scan;
	
	private static Map<String, Expression> bindings = new HashMap<>();

	public static void assign(String name, Expression expr) {
		bindings.put(name, expr);
	}
	
	public static Expression getBinding(String name) {
		if (bindings.containsKey(name)) return bindings.get(name);
		else return null;
	}
	
	public static void shutdown () {
		scan.close();
		System.exit(0);
	}
	
	public static void error (String msg) {
		System.err.println("*** " + msg + " ***");
	}
	
	public static void run() {
		while (scan.hasNextLine()) {
			String input = scan.nextLine().trim();
			try {
				Expression expr = new Parser(input).parseExpression();
				Primitive p = expr.exec();
			}
			catch (Exception e) {
				error(e.getMessage());
				System.exit(1);
			}
		}
	}
	
	public static void REPL () {
		while (true) {
			System.out.print(">");
			String input = scan.nextLine().trim();	
			try {
				Expression expr = new Parser(input).parseExpression();
				Primitive p = expr.exec();
				if (p != Kore.kore) System.out.println(p.eval()+"\n");
			} catch (Exception e) {
				error(e.getMessage());
			}
		}
	}

	public static void main (String[] args) {
		if (args.length == 1) {
			String fpath = args[0];
			File file = new File(fpath);
			try {
				scan = new Scanner(file);
				run();
			}
			catch (FileNotFoundException fnfe) {
				System.err.println("Could not find file: " + fpath);
			}
		}
		
		scan = new Scanner(System.in);
		REPL();
	}
		
}
