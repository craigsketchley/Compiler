package CFG;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

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
	
	/**
	 * Testing removing certain nodes.
	 */
	@Test
	public void testRemovingCFGNodes()
	{
		Program program = Parser.parse("input/factorialExample");
		for (Function function : program.functions) {
			ControlFlowGraph cfg = new ControlFlowGraph(function);
			
			System.out.println(cfg);

			List<Node> removeNodes = new LinkedList<Node>();
			
			for (Node n : cfg.allNodes)
			{
				if (!(n.getInstruction() instanceof BrInstruction) &&
					!(n.getInstruction() instanceof RetInstruction) &&
					!n.isSentinel())
				{
					removeNodes.add(n);
				}
			}
			
			for (Node n : removeNodes)
			{
				try {
					cfg.removeNode(n);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println(cfg);
		}
	}

}
