package optimisation;

import static org.junit.Assert.*;
import intermediateLanguage.Parser;
import intermediateLanguage.Program;

import org.junit.Test;

/**
 * Tests the unreachable code optimisation.
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class UnreachableCodeOptimisationTest {

	/**
	 * Test the input function is the same as the output function
	 * if no optimisation has been performed.
	 */
	@Test
	public void testNoOptimisation1()
	{
		Program inProgram = Parser.parse("input/factorialExample");
		Program origProgram = Parser.parse("input/factorialExample");
		
		inProgram = Optimiser.optimise(inProgram, new UnreachableCodeOptimisation());
		
		assertEquals(inProgram.toString(), origProgram.toString());
	}
	
	/**
	 * Test the input function is the same as the output function
	 * if no optimisation has been performed.
	 */
	@Test
	public void testNoOptimisation2()
	{
		Program inProgram = Parser.parse("input/unreachableCodeBlock1");
		Program origProgram = Parser.parse("input/unreachableCodeBlock1");

		inProgram = Optimiser.optimise(inProgram, new UnreachableCodeOptimisation());
		
		assertEquals(inProgram.toString(), origProgram.toString());
	}
	
	/**
	 * Remove a unreachable code test.
	 */
	@Test
	public void testRemoveUnreachableCodeBlock1()
	{
		Program programInput = Parser.parse("input/unreachableCodeBlock1");
		Program programOutput = Parser.parse("expected/unreachableCodeBlock1");

		programInput = Optimiser.optimise(programInput, new UnreachableCodeOptimisation());
		
		assertEquals(programInput.toString(), programOutput.toString());
	}

	/**
	 * Remove a unreachable code test.
	 */
	@Test
	public void testRemoveUnreachableCodeBlock2()
	{
		Program programInput = Parser.parse("input/unreachableCodeBlock2");
		Program programOutput = Parser.parse("expected/unreachableCodeBlock2");

		programInput = Optimiser.optimise(programInput, new UnreachableCodeOptimisation());
		
		assertEquals(programInput.toString(), programOutput.toString());
	}
	
	/**
	 * Remove a unreachable double return code test.
	 */
	@Test
	public void testRemoveUnreachableCodeDoubleReturn()
	{
		Program programInput = Parser.parse("input/unreachableCodeDoubleReturn");
		Program programOutput = Parser.parse("expected/unreachableCodeDoubleReturn");

		programInput = Optimiser.optimise(programInput, new UnreachableCodeOptimisation());
		
		assertEquals(programInput.toString(), programOutput.toString());
	}
	

}
