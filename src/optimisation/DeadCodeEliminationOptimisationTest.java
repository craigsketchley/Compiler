package optimisation;

import static org.junit.Assert.*;
import intermediateLanguage.Parser;
import intermediateLanguage.*;

import org.junit.Test;

/**
 * Tests the Dead Code Elimination optimisation.
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class DeadCodeEliminationOptimisationTest
{

	/**
	 * Tests our implementation works for the example provided in the spec.
	 */
	@Test
	public void testAssignmentSpec()
	{
		Program input = Parser.parse("input/deadCodeAssignmentSpec");
		Program expected = Parser.parse("expected/deadCodeAssignmentSpec");
		
		Program output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), output.toString());
	}
	
	/**
	 * Tests against the other example given in the spec.
	 */
	@Test
	public void testAssignmentSpec2()
	{
		Program input = Parser.parse("input/deadCodeAssignmentSpec2");
		Program expected = Parser.parse("expected/deadCodeAssignmentSpec2");
		
		Program output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), output.toString());
	}

	/**
	 * Tests removal of lots of repeated uesless loads.
	 */
	@Test
	public void testUselessLoads()
	{
		Program input = Parser.parse("input/deadCodeUselessLoads");
		Program expected = Parser.parse("expected/deadCodeUselessLoads");
		
		Program output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), output.toString());
	}

	/**
	 * Tests the removal of repeated useless binary operations.
	 */
	@Test
	public void testUselessBinOps()
	{
		Program input = Parser.parse("input/deadCodeUselessBinOp");
		Program expected = Parser.parse("expected/deadCodeUselessBinOp");
		
		Program output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), output.toString());
	}
	
	/**
	 * 
	 */
	@Test
	public void testUnreachable()
	{
		Program input = Parser.parse("input/deadCodeUnreachable");
		Program expected = Parser.parse("expected/deadCodeUnreachable");
		
		Program output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), output.toString());
	}

	/**
	 * Tests optimising a function with 2 returns, one after another.
	 */
	@Test
	public void testDoubleReturn()
	{
		Program input = Parser.parse("input/deadCodeDoubleReturn");
		Program expected = Parser.parse("expected/deadCodeDoubleReturn");
		
		Program output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), output.toString());
	}

}
