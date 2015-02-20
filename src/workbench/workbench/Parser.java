package workbench;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import errors.ParsingException;
import errors.ReflectionException;
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
	
	private static List<Character> delimiters = Arrays.asList(new Character[]{
		'(', ')', ';', '\n', ',', '"'
	});
	
	private static List<Character> whitespace = Arrays.asList(new Character[]{
		' '	
	});
	
	public Expression parseExpression()
	throws ParsingException, ReflectionException, TypeException {
		
		// parse int literal
		if (num(index)) return Parser.parseInt(input);
		
		// parse string literal
		String token = peek();
		if (token.startsWith("\"") && token.endsWith("\"")) return parseStr();
		index += token.length();
		
		// is it a variable binding?
		if (!delimiter(index)) throw new ParsingException("Unknown token while parsing: " + peek());
		
		// is it a function call?
		if (peek("(")) {
			List<Expression> exprs = parseFunctionArgs();
			Primitive[] prims = new Primitive[exprs.size()];
			for (int i = 0; i < exprs.size(); i++) {
				prims[i] = exprs.get(i).exec();
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
	
	private Str parseStr () throws ParsingException {
		if (!gobble("\"")) throw new ParsingException("String literal must start with \"");
		StringBuilder sb = new StringBuilder();
		while (input.charAt(index) != '"' ) {
			sb.append(input.charAt(index++));
		}
		index++;
		if (sb.length() == 0) return Str.EmptyString;
		else return new Str(sb.toString());
	}
	
	private List<Expression> parseFunctionArgs()
	throws ReflectionException, TypeException, ParsingException {
		
		if (!gobble("(")) throw new ParsingException("No opening bracket for function arguments.");
		if (gobble(")")) return new ArrayList<>(); // no args;
		
		List<Expression> exprs = new ArrayList<>();
		while (true) {
			Expression expr = parseExpression();
			exprs.add(expr);
			if (gobble(",")) continue;
			else if (gobble(")")) break;
			else throw new ParsingException("No separator while parsing function arguments.");
		}
		
		return exprs;
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
		while (whitespace(index) && index < length) index++;
	}	
	
	public boolean num (int i) {
		skipWhiteSpace();
		return Character.isDigit(input.charAt(i));
	}		
	
	public boolean delimiter (int i) {
		skipWhiteSpace();
		return delimiters.contains(input.charAt(i));
	}
	
	public boolean whitespace (int i) {
		return whitespace.contains(input.charAt(i));
	}
	
	public boolean peek (String expected) {
		String p = peek();
		return p.equals(expected);
	}
	
	public String peek () {
		skipWhiteSpace();
		if (delimiter(index)) return ""+input.charAt(index);
		StringBuilder sb = new StringBuilder();
		for (int i = index; i < length && !delimiter(i) && !whitespace(i); i++) {
			sb.append(input.charAt(i));
		}
		return sb.toString();
	}
	
	public boolean gobble (String expected) {
		String str = peek();
		if (str.equals(expected)){
			index += str.length();
			return true;
		}
		else return false;
	}
	
}
