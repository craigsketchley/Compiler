package optimisation;

import static org.junit.Assert.*;
import intermediateLanguage.Parser;
import intermediateLanguage.Program;

import org.junit.Test;

/**
 * Test the remove redundant loads optimisation.
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class RedundantLoadOptimisationTest {

	@Test
	public void test() {

		Program input = Parser.parse("input/redundantLoad");
		//Program expected = Parser.parse("expected/redundantLoad");
		System.out.println(input);
		
		Program output = Optimiser.optimise(input, new RedundantLoadOptimisation());
		System.out.println(output);
		output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		System.out.println(output);
		//assertEquals(expected.toString(), output.toString());
	}

	@Test
	public void testAssignmentSpec() {

		Program input = Parser.parse("input/redundantLoadsAssignmentSpec");
		Program expected = Parser.parse("expected/redundantLoadsAssignmentSpec");
		
//		System.out.println(input);
		
		Program output = Optimiser.optimise(input, new RedundantLoadOptimisation());
//		System.out.println(output);
		
		assertEquals(expected.toString(), output.toString());

//		output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
//		System.out.println(output);

	}

}
