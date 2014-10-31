package intermediateLanguage;

import java.util.Arrays;
import java.util.List;

public class StInstruction implements Instruction
{

	public String id; //The named variable to store too
	public Register register; //The register to save from

	public StInstruction(String id, Register register)
	{
		this.id = id;
		this.register = register;
	}

	@Override
	public String toString()
	{
		return String.format("(st %s %s)", id, register);
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
		if(register.equals(from))
		{
			register = new Register(to);
		}
	}
	
	@Override
	public void rewriteAssignedRegister(Register register)
	{
		//nothing to do
		return;
	}

}
