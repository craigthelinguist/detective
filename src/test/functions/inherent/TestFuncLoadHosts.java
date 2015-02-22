package functions.inherent;

import static org.junit.Assert.*;

import org.junit.Test;

import primitives.Seq;
import primitives.Str;
import rules.Primitive;

public class TestFuncLoadHosts {

	@Test
	public void testLoadHosts001 () {
		
		Str str = new Str("test-hosts.txt");
		Primitive[] prims = new Primitive[]{ str };
		try{
			FuncLoadHosts flh = new FuncLoadHosts(prims);
			Primitive p = flh.exec();
			assertTrue(p instanceof Seq);
		}
		catch (Exception e){
			fail();
		}
		
	}
	
}
