package intermediateLanguage;

import java.util.Arrays;
import java.util.List;

public class BrInstruction implements Instruction
{

	public Register register; //register used for decision
	public int blockTrue; //block to move to if true
	public int blockFalse; //block to move to if false

	public BrInstruction(Register register, int blockTrue, int blockFalse)
	{
		this.register = register;
		this.blockTrue = blockTrue;
		this.blockFalse = blockFalse;
	}

	@Override
	public String toString()
	{
		return String.format("(br %s %d %d)", register, blockTrue, blockFalse);
	}

	@Override
	public List<Register> getReferencedRegisters()
	{
		return Arrays.asList(register);
	}

	@Override
	public Register getAssignedRegister()
	{
		return null;
	}
	
	@Override
	public void rewriteReferencedRegisters(Register from, Register to)
	{
		//nothing to do
		return;
	}
	
	@Override
	public void rewriteAssignedRegister(Register register)
	{
		this.register = new Register(register);
	}
	
}
