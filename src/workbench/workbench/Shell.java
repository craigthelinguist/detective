package workbench;

import java.util.Scanner;

import wblang.primitives.Kore;
import wblang.rules.Expression;
import wblang.rules.Primitive;

public class Shell{

	private Scanner scan;
	
	public Shell (Scanner sc) {
		scan = sc;
	}
	
	public void REPL () {
		while (true) {
			System.out.print(">");
			String input = scan.nextLine().trim();	
			try {
				Expression expr = new Parser(input).parseExpression();
				Primitive p = expr.exec();
				if (p != Kore.kore) System.out.println(p.eval()+"\n");
			} catch (Exception e) {
				Interpreter.error(e.getMessage());
			}
		}
	}
}
