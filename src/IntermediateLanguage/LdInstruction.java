package IntermediateLanguage;

import java.util.Collections;
import java.util.List;

public class LdInstruction implements Instruction {
	
	public Register register; //the register loaded into
	public String variable; //the variable loaded from
	
	public LdInstruction(Register register, String variable)
	{
		this.register = register;
		this.variable = variable;
	}

	@Override
	public String toString()
	{
		return String.format("(ld %s %s)", register, variable);
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
		//nothing to do
		return;
	}
	
	@Override
	public void rewriteAssignedRegister(Register register)
	{
		if(this.register.equals(register))
		{
			this.register = new Register(register);
		}
	}
	
}
