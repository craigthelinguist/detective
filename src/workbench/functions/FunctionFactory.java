package functions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import errors.ParsingException;
import errors.TypeException;
import functions.inherent.FuncHelp;
import functions.inherent.FuncLs;
import functions.inherent.FuncUsage;
import primitives.Str;
import rules.Primitive;

public class FunctionFactory {

	private FunctionFactory () {}
	
	public static final Map<String, Class> funcs = new HashMap<>();
	static {
		funcs.put("ls", FuncLs.class);
		funcs.put("usage", FuncUsage.class);
		funcs.put("help", FuncHelp.class);
	}
	
	public static final Map<String, String> aliases = new HashMap<>();
	static {
		aliases.put("list", "ls");
	}
	
	public static Function make (String name, Primitive[] args)
	throws ParsingException, TypeException, InstantiationException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {		
		if (aliases.containsKey(name)) name = aliases.get(name);
		if (!funcs.containsKey(name)) throw new ParsingException("Unknown function name " + name);
		Class cl = funcs.get(name);
		Constructor constructor = cl.getConstructors()[0];
		return (Function)(constructor.newInstance(new Object[]{ args }));
	}
	
	public static Function make (String name) {
		try{
			Class cl = FunctionFactory.funcs.get(name);
			Constructor cons = cl.getConstructors()[1];
			Function f = (Function)(cons.newInstance());
			return f;
		}
		catch (Exception e) {
			return null;
		}
	}
	
}
