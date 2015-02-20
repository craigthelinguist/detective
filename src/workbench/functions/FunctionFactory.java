package functions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import errors.ParsingException;
import errors.TypeException;
import primitives.Primitive;
import primitives.Str;

public class FunctionFactory {

	private FunctionFactory () {}
	
	public static final Map<String, Class> funcs = new HashMap<>();
	static {
		funcs.put("ls", FuncLs.class);
		funcs.put("list", FuncLs.class);
		funcs.put("usage", FuncUsage.class);
	}
	
	public static Function make (String name, Primitive[] args)
	throws ParsingException, TypeException, InstantiationException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {		
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
