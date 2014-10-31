package optimisation;

import static org.junit.Assert.assertEquals;
import intermediateLanguage.Parser;
import intermediateLanguage.Program;

import org.junit.Test;

/**
 * Tests the Optimiser class. Used to test multiple optimisations in various orders.
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class OptimiserTest {

	/**
	 * Tests removing unreachable code then dead code elimination.
	 */
	@Test
	public void testUnreachableThenDeadCode() {
		Program input = Parser.parse("input/optimiserUnreachableAndDeadCode");
		Program expected = Parser.parse("expected/optimiserUnreachableAndDeadCode");
		
		input = Optimiser.optimise(input, new UnreachableCodeOptimisation());
		input = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), input.toString());
	}

	/**
	 * Tests dead code elimination then removing unreachable code.
	 */
	@Test
	public void testDeadCodeThenUnreachable() {
		Program input = Parser.parse("input/optimiserUnreachableAndDeadCode");
		Program expected = Parser.parse("expected/optimiserUnreachableAndDeadCode");
		
		input = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		input = Optimiser.optimise(input, new UnreachableCodeOptimisation());
		
		assertEquals(expected.toString(), input.toString());
	}

	/**
	 * Tests removing unreachable code, then applying redundant loads
	 * optimisation, then dead code elimination.
	 */
	@Test
	public void testUnreachableRedundantLoadsDeadCode() {
		Program input = Parser.parse("input/optimiserAssignmentSpec");
		Program expected = Parser.parse("expected/optimiserAssignmentSpec");
		
		input = Optimiser.optimise(input, new UnreachableCodeOptimisation());
		input = Optimiser.optimise(input, new RedundantLoadOptimisation());
		input = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), input.toString());
	}

}
