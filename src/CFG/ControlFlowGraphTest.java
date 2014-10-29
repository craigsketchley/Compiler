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
		Program program = new Program();
		Function function = new Function("test1", Arrays.asList("x"));
		Block block = new Block(0);
		block.instructions.add(new LdInstruction(1, "x"));
		block.instructions.add(new BrInstruction(1, 1, 1));
		function.blocks.add(block);
		
		block = new Block(1);
		block.instructions.add(new LcInstruction(2, 1));
		block.instructions.add(new BinOpInstruction("add", 1, 1, 2));
		block.instructions.add(new BrInstruction(1, 3,  3));
		function.blocks.add(block);
		block = new Block(2);
		block.instructions.add(new LdInstruction(2, "x"));
		block.instructions.add(new LcInstruction(3, 2));
		block.instructions.add(new BinOpInstruction("add", 1, 2, 3));
		block.instructions.add(new BrInstruction(1, 3,  3));
		function.blocks.add(block);
		block = new Block(3);
		block.instructions.add(new LdInstruction(5, "x"));
		block.instructions.add(new RetInstruction(5));
		function.blocks.add(block);
	
		
		System.out.println(function);
		System.out.println("OPTIMIZING");
		ControlFlowGraph cfg = new ControlFlowGraph(function);
		cfg.removeUnreachableCode();
		Function f2 = cfg.convertToFunction();
		if(f2 == null)
			System.out.println("is null");
		System.out.println(f2);
		
	}

}
