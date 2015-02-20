package workbench;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import errors.ParsingException;
import errors.TypeException;
import functions.Function;
import functions.FunctionFactory;
import primitives.Int;
import primitives.Str;
import rules.Assignment;
import rules.Block;
import rules.Expression;
import rules.Primitive;
import rules.Variable;

public class Parser {

	private int index;
	private String input;
	private int length;
	
	public Parser (String input) {
		this.index = 0;
		this.input = input;
		this.length = input.length();
	}
	
	private static List<Character> delimiters;
	static {
		delimiters.add('(');
		delimiters.add(')');
		delimiters.add(';');
		delimiters.add('\n');
		delimiters.add(',');
	}
	
	private static List<Character> whitespace;
	static {
		whitespace.add(' ');
	}
	
	public Expression parseExpression()
	throws ParsingException, InstantiationException, IllegalAccessException,
	IllegalArgumentException, InvocationTargetException, TypeException {
		
		// parse int literal
		if (num()) return Parser.parseInt(input);
		
		// parse string literal
		String token = peek();
		if (token.startsWith("\"") && token.endsWith("\"")) return Parser.parseInt(input);
		
		// is it a variable binding?
		if (!delimiter()) throw new ParsingException("Unknown token while parsing: " + peek());
		
		// is it a function call?
		if (peek("(")) {
			Expression[] exprs = parseFunctionArgs();
			Primitive[] prims = new Primitive[exprs.length];
			for (int i = 0; i < exprs.length; i++) {
				prims[i] = exprs[i].exec();
			}
			return FunctionFactory.make(token, prims);
		}
		
		// variable binding?
		else if (peek("=")) {
			gobble("=");
			Expression expr = parseExpression();
			return new Assignment(token, expr);
		}
	
		// unknown
		else {
			throw new ParsingException("Unknown token found while parsing expression: " + token);
		}
		
	}
	
	private Expression[] parseFunctionArgs()
	throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParsingException, TypeException {
		
		if (!gobble("(")) throw new ParsingException("No opening bracket for function arguments.");
		if (gobble(")")) return new Expression[] {}; // no args;
		
		List<Expression> exprs = new ArrayList<>();
		do {
			Expression expr = parseExpression();
			if (!gobble(",")) throw new ParsingException("No separator while parsing function arguments.");
			exprs.add(expr);
		} while (!gobble("("));
		Expression[] exprArray = new Expression[exprs.size()];
		for (int i = 0; i < exprs.size(); i++) {
			exprArray[i] = exprs.get(i);
		}
		return exprArray;
	}

	public static Int parseInt (String input) throws ParsingException {
		Parser p = new Parser(input);
		p.skipWhiteSpace();
		String str = p.peek();
		try {
			return new Int(Integer.parseInt(str));
		}
		catch (NumberFormatException nfe) {
			throw new ParsingException("Error parsing int literal.");
		}
	}
	
	public boolean isInt (String token) {
		try{
			Integer.parseInt(token);
			return true;
		}
		catch(NumberFormatException nfe) {
			return false;
		}
	}
	
	public void skipWhiteSpace () {
		while (whitespace() && index < length) index++;
		while (whitespace() && index < length) index++;
	}	
	
	public boolean num () {
		skipWhiteSpace();
		return Character.isDigit(input.charAt(index));
	}		
	
	public boolean delimiter () {
		skipWhiteSpace();
		return delimiters.contains(input.charAt(index));
	}
	
	public boolean whitespace () {
		skipWhiteSpace();
		return whitespace.contains(input.charAt(index));
	}
	
	public boolean peek (String expected) {
		String p = peek();
		return p.equals(expected);
	}
	
	public String peek () {
		skipWhiteSpace();
		if (delimiter()) return ""+input.charAt(index);
		StringBuilder sb = new StringBuilder();
		for (int i = index; i < length && !delimiter() && !whitespace(); i++) {
			sb.append(input.charAt(i));
		}
		return sb.toString();
	}
	
	public boolean gobble (String expected) {
		String str = peek();
		if (str.equals(expected)){
			index += str.length();
			return false;
		}
		else return false;
	}
	
}
