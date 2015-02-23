package functions.inherent;

import static org.junit.Assert.*;

import org.junit.Test;

import wblang.functions.indigenous.FuncVectorise;
import wblang.primitives.Str;
import wblang.primitives.Vec;
import wblang.rules.Primitive;

public class TestFuncVectorise {

	@Test
	public void TestVectorise001 () {
		try {
			Str str = new Str("johnny");
			Primitive[] prims = new Primitive[]{ str };
			FuncVectorise fv = new FuncVectorise(prims);
			Primitive p = fv.exec();
			assert(p instanceof Vec);
		} catch (Exception e) {
			fail();
		}
	}
	
}
