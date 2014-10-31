package optimisation;

import static org.junit.Assert.*;
import intermediateLanguage.Parser;
import intermediateLanguage.Program;

import org.junit.Test;

public class ConstantPropagationOptimisationTest
{

	/**
	 * Tests our implementation works for the example provided in the spec.
	 */
	@Test
	public void testAssignmentSpec()
	{
		Program input = Parser.parse("input/constantPropagationBinOps");
		Program expected = Parser.parse("expected/constantPropagationBinOps");
		
		Program output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), output.toString());
	}

}
