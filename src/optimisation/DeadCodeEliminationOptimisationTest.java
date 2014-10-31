package optimisation;

import static org.junit.Assert.*;
import intermediateLanguage.Parser;
import intermediateLanguage.Program;

import org.junit.Test;

public class DeadCodeEliminationOptimisationTest
{

	@Test
	public void testAssignmentSpec()
	{
		Program input = Parser.parse("input/deadCodeAssignmentSpec");
		Program expected = Parser.parse("expected/deadCodeAssignmentSpec");
		
		Program output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), output.toString());
	}

	@Test
	public void testUselessLoads()
	{
		Program input = Parser.parse("input/deadCodeUselessLoads");
		Program expected = Parser.parse("expected/deadCodeUselessLoads");
		
		Program output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), output.toString());
	}

	@Test
	public void testUselessBinOps()
	{
		Program input = Parser.parse("input/deadCodeUselessBinOp");
		Program expected = Parser.parse("expected/deadCodeUselessBinOp");
		
		Program output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), output.toString());
	}
	
	@Test
	public void testUnreachable()
	{
		Program input = Parser.parse("input/deadCodeUnreachable");
		Program expected = Parser.parse("expected/deadCodeUnreachable");
		
		Program output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		assertEquals(expected.toString(), output.toString());
	}

	@Test
	public void testDoubleReturn()
	{
		Program input = Parser.parse("input/deadCodeDoubleReturn");
		Program expected = Parser.parse("expected/deadCodeDoubleReturn");
		
		Program output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		
		System.out.println(expected);
		System.out.println(output);
		
		assertEquals(expected.toString(), output.toString());
	}

}
