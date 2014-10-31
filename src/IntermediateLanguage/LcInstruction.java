package IntermediateLanguage;

import java.util.Collections;
import java.util.List;

public class LcInstruction implements Instruction
{
	
	public Register register; //the register loaded into
	public int value; //the constant loaded
	
	public LcInstruction(Register register, int value)
	{
		this.register = register;
		this.value = value;
	}
	
	@Override
	public String toString()
	{
		return String.format("(lc %s %d)", register, value);
	}

	@Override
	public List<Register> getReferencedRegisters()
	{
		return Collections.emptyList();
	}

	@Override
	public Register getAssignedRegister()
	{
		return register;
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
	
	public int getConstant()
	{
		return value;
	}

}
