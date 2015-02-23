package functions.inherent;

import static org.junit.Assert.*;

import org.junit.Test;

import wblang.functions.indigenous.FuncClusterer;
import wblang.primitives.Int;
import wblang.primitives.Str;
import wblang.rules.Primitive;

public class TestFuncClusterer {
	
	@Test
	public void testConstruction001 () {
		
		Int arg1 = new Int(3);
		Int arg2 = new Int(3);
		Str arg3 = new Str("basic");
		Str arg4 = new Str("random");
		
		Primitive[] prims = new Primitive[]{ arg1, arg2, arg3, arg4 };
		try{
			FuncClusterer fc = new FuncClusterer(prims);
		}
		catch(Exception e){
			fail();
		}
	}
	
}
