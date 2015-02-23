package workbench;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wblang.errors.ParsingException;
import wblang.errors.ReflectionException;
import wblang.errors.TypeException;
import wblang.functions.Function;
import wblang.functions.FunctionFactory;
import wblang.primitives.Hash;
import wblang.primitives.Int;
import wblang.primitives.Seq;
import wblang.primitives.Str;
import wblang.rules.Assignment;
import wblang.rules.Block;
import wblang.rules.Expression;
import wblang.rules.Primitive;
import wblang.rules.Variable;

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
		'(', ')', ';', '\n', ',', '=', ';', '[', ']', ':'
	});
	
	private static List<Character> whitespace = Arrays.asList(new Character[]{
		' '	
	});
	
	public Expression parseExpression()
	throws ParsingException, ReflectionException, TypeException {
		
		// parse int literal
		if (num()) {
			Int intt = parseInt(input);
			index += intt.toString().length();
			return intt;
		}
		
		String token = peek();
		index += token.length();

		// parse string literal
		if (token.startsWith("\"") && token.endsWith("\"")) return new Parser(token).parseStr();
		
		// is token reference to an existing variable?
		if (TestingREPL.getBinding(token) != null) {
			
			// either reassigning variable, or dereferencing.
			if (!done() && peek("=")) {
				gobble("=");
				Expression expr = parseExpression();
				return new Assignment(token, expr);
			}
			// array subscripting
			else if (!done() && peek("[")) {

				Primitive prim = TestingREPL.getBinding(token).exec();
				if (prim instanceof Seq) return parseSlice((Seq)prim);	
				else if (prim instanceof Hash) return parseHashIndex((Hash)prim);
				else throw new ParsingException("Can only subscript into Seq, Host, or Hash.");
				
			}
			else return TestingREPL.getBinding(token);
		}
		
		// need delimiter
		if (!delimiter()) throw new ParsingException("Unknown token: " + peek());
		
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
			Primitive prim = expr.exec();
			return new Assignment(token, prim);
		}
	
		// unknown
		else {
			throw new ParsingException("Unknown token found while parsing expression: " + token);
		}
		
	}
	
	private Expression parseHashIndex (Hash hash) throws ParsingException, TypeException, ReflectionException {
		if (!gobble("[")) throw new ParsingException("Hash indexing must start with '['");
		Primitive key = parseExpression().exec();
		if (!gobble("]")) throw new ParsingException("Hash indexing must end with ']'");
		if (done()) return hash.get(key);
		if (!gobble("=")) throw new ParsingException("Expected '='");
		Primitive value = parseExpression().exec();
		Primitive[] args = new Primitive[]{ hash, key, value };
		return FunctionFactory.make("hashput", args);
	}
	
	private Function parseSlice (Seq seq) throws ParsingException, TypeException, ReflectionException {
		if (!gobble("[")) throw new ParsingException("Array subscript must start with '['");
		Int startIndx;
		Int endIndx;
		Primitive prim;
		
		if (gobble(":")) {
			
			// slice of the form array[:]
			if (gobble("]")) {
				startIndx = new Int(0);
				endIndx = new Int(seq.size());
				Primitive[] prims = new Primitive[]{ seq, startIndx, endIndx };
				return FunctionFactory.make("slice", prims);	
			}
			
			// slice of the form array[:end]
			else {
				startIndx = new Int(0);
				String str = peek();
				prim = new Parser(str).parseExpression().exec();
				if (!(prim instanceof Int)) throw new ParsingException("Array subscripts must be an Int.");
				endIndx = (Int)prim;
				gobble(str);
				if (!gobble("]")) throw new ParsingException("Slice did not end with ']'");
				Primitive[] prims = new Primitive[]{ seq, startIndx, endIndx };
				return FunctionFactory.make("slice", prims);
			}
			
		}
		
		// slice of the form array[start:
		String str = peek();
		prim = new Parser(str).parseExpression().exec();
		if (!(prim instanceof Int)) throw new ParsingException("Array subscripts must be an Int.");
		startIndx = (Int)prim;
		gobble(str);
			
		// array subscript array[indx]
		if (gobble("]")) {
			Primitive[] prims = new Primitive[]{ seq, startIndx };
			return FunctionFactory.make("get", prims);
		}
		else if (!gobble(":")) throw new ParsingException("Unknown token while parsing slice: " + peek());
		
		// unbounded slice, e.g.: array[start:]
		if (gobble("]")) {
			endIndx = new Int(seq.size());
			Primitive[] prims = new Primitive[]{ seq, startIndx, endIndx };
			return FunctionFactory.make("slice", prims);
		}
		
		// slice of the form array[start:end]
		str = peek();
		prim = new Parser(str).parseExpression().exec();
		if (!(prim instanceof Int)) throw new ParsingException("Array subscripts must be an Int.");
		endIndx = (Int)prim;
		gobble(str);
		if (!gobble("]")) throw new ParsingException("Array slice must end with ']'");
		Primitive[] prims = new Primitive[]{ seq, startIndx, endIndx };
		return FunctionFactory.make("slice", prims);
		
	}
	
	private boolean done () {
		if (this.index == input.length()) return true;
		skipWhiteSpace();
		if (this.index == input.length()) return true;
		return false;
	}
	
	private Str parseStr () throws ParsingException {
		if (!gobbleDelimiter('"')) throw new ParsingException("String literal must start with \"");
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

	public Int parseInt (String input) throws ParsingException {
		skipWhiteSpace();
		String str = peek();
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
	
	public boolean num () {
		skipWhiteSpace();
		return Character.isDigit(input.charAt(index));
	}
	
	public boolean num (int i) {
		return Character.isDigit(input.charAt(i));
	}		
	
	public boolean delimiter () {
		skipWhiteSpace();
		return delimiters.contains(input.charAt(index));
	}
	
	public boolean delimiter (int i) {
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
	
	public boolean gobbleDelimiter (char delim) {
		if (input.charAt(index) == delim) {
			index++;
			return true;
		}
		return false;
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
