 package workbench;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import wblang.primitives.Kore;
import wblang.rules.Expression;
import wblang.rules.Primitive;


public class Interpreter {

	private static Scanner scan;

	private static String CURRENT_DIR = "src/input/".replace('/', File.separatorChar);
	private static Map<String, Expression> bindings = new HashMap<>();

	public static String currentDir () {
		return CURRENT_DIR;
	}
	
	public static void changeDir (String newDir) {
		CURRENT_DIR = "src/" + newDir + "/";
		CURRENT_DIR = CURRENT_DIR.replace('/', File.separatorChar);
	}
	
	public static void assign(String name, Expression expr) {
		bindings.put(name, expr);
	}
	
	public static Expression getBinding(String name) {
		if (bindings.containsKey(name)) return bindings.get(name);
		else return null;
	}
	
	public static List<String> getBindings () {
		List<String> bindings = new ArrayList<>();
		for (String s : Interpreter.bindings.keySet()) {
			bindings.add(s);
		}
		Collections.sort(bindings);
		return bindings;
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
		new Shell(scan).REPL();
	}
		
}
