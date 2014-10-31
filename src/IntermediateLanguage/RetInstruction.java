package IntermediateLanguage;

import java.util.Arrays;
import java.util.List;

public class RetInstruction implements Instruction {
	
	public Register register; //register containing the return value
	
	public RetInstruction(Register register)
	{
		this.register = register;
	}

	@Override
	public String toString()
	{
		return String.format("(ret %s)", register);
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
