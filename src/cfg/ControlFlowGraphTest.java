package cfg;

import static org.junit.Assert.*;
import intermediateLanguage.*;

import org.junit.Test;

public class ControlFlowGraphTest
{

	/**
	 * Test the input function is the same as the output function
	 * if no optimisation has been performed.
	 * 
	 * TODO: Should we implement an equals method for functions?
	 */
	@Test
	public void testNoOptimisation1()
	{
		Program program = Parser.parse("input/factorialExample");
		for (Function function : program.functions) {
			ControlFlowGraph cfg = new ControlFlowGraph(function);
			Function outputFunction = cfg.convertToFunction();
			assertEquals(function.toString(), outputFunction.toString());
		}
	}
	
	@Test
	public void testNoOptimisation2()
	{
		Program program = Parser.parse("input/unreachableCodeBlock1");
		for (Function function : program.functions) {
			ControlFlowGraph cfg = new ControlFlowGraph(function);
			Function outputFunction = cfg.convertToFunction();
			assertEquals(function.toString(), outputFunction.toString());
		}
	}
	
	/**
	 * Removing an unreachable code tests.
	 */
	
	@Test
	public void testRemoveUnreachableCodeBlock1()
	{
		Program programInput = Parser.parse("input/unreachableCodeBlock1");
		Program programOutput = Parser.parse("expected/unreachableCodeBlock1");
		for (int i = 0; i < programInput.functions.size(); i++) {
			Function functionIn = programInput.functions.get(i);
			Function functionOut = programOutput.functions.get(i);
			
			ControlFlowGraph cfg = new ControlFlowGraph(functionIn);
			cfg.removeUnreachableCode();
			
			assertEquals(cfg.convertToFunction().toString(), functionOut.toString());
		}
	}

	@Test
	public void testRemoveUnreachableCodeBlock2()
	{
		Program programInput = Parser.parse("input/unreachableCodeBlock2");
		Program programOutput = Parser.parse("expected/unreachableCodeBlock2");
		for (int i = 0; i < programInput.functions.size(); i++) {
			Function functionIn = programInput.functions.get(i);
			Function functionOut = programOutput.functions.get(i);
			
			ControlFlowGraph cfg = new ControlFlowGraph(functionIn);
			cfg.removeUnreachableCode();
			
			assertEquals(cfg.convertToFunction().toString(), functionOut.toString());
		}
	}
	
	@Test
	public void testRemoveUnreachableCodeDoubleReturn()
	{
		Program programInput = Parser.parse("input/unreachableCodeDoubleReturn");
		Program programOutput = Parser.parse("expected/unreachableCodeDoubleReturn");
		for (int i = 0; i < programInput.functions.size(); i++) {
			Function functionIn = programInput.functions.get(i);
			Function functionOut = programOutput.functions.get(i);
			
			ControlFlowGraph cfg = new ControlFlowGraph(functionIn);
			cfg.removeUnreachableCode();
			
			assertEquals(cfg.convertToFunction().toString(), functionOut.toString());
		}
	}
	
	/**
	 * Testing removing certain nodes.
	 */
	@Test
	public void testRemovingCFGNodes()
	{
		Program program = Parser.parse("input/factorialExample");
		for (Function function : program.functions) {
			ControlFlowGraph cfg = new ControlFlowGraph(function);
			
			
		}
	}


}