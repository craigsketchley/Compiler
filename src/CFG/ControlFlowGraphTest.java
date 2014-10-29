package CFG;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import IntermediateLanguage.*;

public class ControlFlowGraphTest
{

	@Test
	public void test()
	{
		Function function = new Function("test1", Arrays.asList("x"));
		Block block = new Block(0);
		block.instructions.add(new LdInstruction(new Register(1), "x"));
		block.instructions.add(new BrInstruction(new Register(1), 1, 1));
		function.blocks.add(block);
		
		block = new Block(1);
		block.instructions.add(new LcInstruction(new Register(2), 1));
		block.instructions.add(new BinOpInstruction("add", new Register(1), new Register(1), new Register(2)));
		block.instructions.add(new BrInstruction(new Register(1), 3,  3));
		function.blocks.add(block);
		block = new Block(2);
		block.instructions.add(new LdInstruction(new Register(2), "x"));
		block.instructions.add(new LcInstruction(new Register(3), 2));
		block.instructions.add(new BinOpInstruction("add", new Register(1), new Register(2), new Register(3)));
		block.instructions.add(new BrInstruction(new Register(1), 3,  3));
		function.blocks.add(block);
		block = new Block(3);
		block.instructions.add(new LdInstruction(new Register(5), "x"));
		block.instructions.add(new RetInstruction(new Register(5)));
		function.blocks.add(block);
	
		
		System.out.println(function);
		System.out.println("OPTIMIZING");
		ControlFlowGraph cfg = new ControlFlowGraph(function);
		//System.out.println(cfg);
		
		cfg.removeUnreachableCode();
		Function f2 = cfg.convertToFunction();
		System.out.println(f2);
		
	}
	
	@Test
	public void testCFGPrint()
	{
		Program program = Parser.parse("input/example");
		for (Function f : program.functions) {
			ControlFlowGraph cfg = new ControlFlowGraph(f);
			System.out.println(cfg);			
		}
		
	}

}
