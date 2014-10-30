package CFG;

import static org.junit.Assert.*;

import org.junit.Test;

import IntermediateLanguage.*;

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
		Program program = Parser.parse("input/unreachableBlockExample");
		for (Function function : program.functions) {
			ControlFlowGraph cfg = new ControlFlowGraph(function);
			Function outputFunction = cfg.convertToFunction();
			assertEquals(function.toString(), outputFunction.toString());
		}
	}
	
	@Test
	public void testNoOptimisation3()
	{
		Program program = Parser.parse("input/assignmentSpecExample");
		for (Function function : program.functions) {
			ControlFlowGraph cfg = new ControlFlowGraph(function);
			Function outputFunction = cfg.convertToFunction();
			assertEquals(function.toString(), outputFunction.toString());
		}
	}
	
	/**
	 * Removing an unreachable block test.
	 */
	@Test
	public void testRemoveUnreachableBlock1()
	{
		Program programInput = Parser.parse("input/unreachableBlockExample");
		Program programOutput = Parser.parse("expected/unreachableBlockExample");
		for (int i = 0; i < programInput.functions.size(); i++) {
			Function functionIn = programInput.functions.get(i);
			Function functionOut = programOutput.functions.get(i);
			
			ControlFlowGraph cfg = new ControlFlowGraph(functionIn);
			cfg.removeUnreachableCode();
			
			assertEquals(cfg.convertToFunction().toString(), functionOut.toString());
		}
	}

}
