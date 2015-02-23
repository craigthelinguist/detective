package wblang.functions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import wblang.errors.ParsingException;
import wblang.errors.ReflectionException;
import wblang.errors.TypeException;
import wblang.functions.indigenous.*;
import wblang.rules.Primitive;

public class FunctionFactory {

	private FunctionFactory () {}
	
	public static final Map<String, Class> funcs = new HashMap<>();
	static {
		funcs.put("ls", FuncLs.class);
		funcs.put("usage", FuncUsage.class);
		funcs.put("help", FuncHelp.class);
		funcs.put("aliases", FuncAliases.class);
		funcs.put("alias", FuncAlias.class);
		funcs.put("quit", FuncQuit.class);
		funcs.put("clusterer", FuncClusterer.class);
		funcs.put("cluster", FuncCluster.class);
		funcs.put("load", FuncLoad.class);
		funcs.put("slice", FuncSlice.class);
		funcs.put("vectorise", FuncVectorise.class);
		funcs.put("loadhosts", FuncLoadHosts.class);
		funcs.put("get", FuncGet.class);
		funcs.put("type", FuncType.class);
	}
	
	public static final Map<String, String> aliases = new HashMap<>();
	static {
		aliases.put("list", "ls");
	}
	
	public static void addAlias (String a1, String a2) {
		aliases.put(a1, a2);
	}
	
	public static String getAlias (String s1) {
		return aliases.get(s1);
	}
	
	public static Function make (String name, Primitive[] args)
	throws ParsingException, TypeException, ReflectionException {
		
		if (aliases.containsKey(name)) name = aliases.get(name);
		if (!funcs.containsKey(name)) throw new ParsingException("Unknown function name " + name);
		Class cl = funcs.get(name);
		
		
		Constructor[] constructors = cl.getConstructors();
		Constructor constructor = null;
		for (int i = 0; i < constructors.length; i++) {
			Constructor cons = constructors[i];
			if (cons.getParameterTypes().length == 1){
				constructor = cons;
				break;
			}
		}
		
		

		if (constructor == null) throw new ReflectionException("Could not find constructor for function.");
		try {
			Object obj = constructor.newInstance(new Object[]{ args });
			Function func = (Function)obj;
			return func;
		} catch (InstantiationException e) {
			throw new ReflectionException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e.getMessage());
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof TypeException) throw (TypeException)e.getTargetException();
			else throw new ReflectionException(e.getMessage());
		}

	}
	
	public static Function make (String name) {
		try{
			Class cl = FunctionFactory.funcs.get(name);
			Constructor[] constructors = cl.getConstructors();
			Constructor cons = null;
			
			for (Constructor con : constructors) {
				if (con.getParameterTypes().length == 0){
					cons = con;
					break;
				}
			}
			
			Function f = (Function)(cons.newInstance());
			return f;
		}
		catch (Exception e) {
			return null;
		}
	}
	
}
