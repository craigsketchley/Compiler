package comp3109a3;

import java.util.Arrays;

import IntermediateLanguage.*;

public class HelloWorld {

	public static void main(String[] args) {
		
		System.out.println("Hello, World!");
		
		Program program = new Program();
		Function function = new Function("factorial", Arrays.asList("n"));
		Block block = new Block(0);
		block.instructions.add(new LdInstruction(1, "n"));
		block.instructions.add(new LcInstruction(2, 0));
		block.instructions.add(new BinOpInstruction("eq", 3, 1, 2));
		block.instructions.add(new StInstruction("cond", 3));
		block.instructions.add(new LdInstruction(4, "cond"));
		block.instructions.add(new BrInstruction(4, 1,  2));
		function.blocks.add(block);
		block = new Block(1);
		block.instructions.add(new LcInstruction(5, 1));
		block.instructions.add(new StInstruction("tmp", 5));
		block.instructions.add(new RetInstruction(6));
		function.blocks.add(block);
		block = new Block(2);
		block.instructions.add(new LdInstruction(7, "n"));
		block.instructions.add(new LcInstruction(8, 1));
		block.instructions.add(new BinOpInstruction("sub", 9, 7, 8));
		block.instructions.add(new StInstruction("tmp", 9));
		block.instructions.add(new LdInstruction(10, "tmp"));
		block.instructions.add(new CallInstruction(11, "factorial", Arrays.asList(10)));
		block.instructions.add(new LdInstruction(12, "n"));
		block.instructions.add(new BinOpInstruction("mul", 13, 11, 12));
		block.instructions.add(new StInstruction("tmp", 13));
		block.instructions.add(new LdInstruction(14, "tmp"));
		block.instructions.add(new RetInstruction(14));
		function.blocks.add(block);
		program.functions.add(function);
		function = new Function("main", Arrays.asList("n"));
		block = new Block(0);
		block.instructions.add(new LdInstruction(1, "n"));
		block.instructions.add(new CallInstruction(2, "factorial", Arrays.asList(1)));
		block.instructions.add(new StInstruction("tmp", 2));
		block.instructions.add(new LdInstruction(3, "tmp"));
		block.instructions.add(new RetInstruction(3));
		function.blocks.add(block);
		program.functions.add(function);
		
		//program now is the AST for factorial program
		System.out.println(program);

	}

}
