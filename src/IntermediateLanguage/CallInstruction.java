package IntermediateLanguage;

import java.util.List;

public class CallInstruction implements Instruction
{
	
	public Register register; //register to store the return value
	public String functionId; //function to call
	public List<Register> args; //registers containing the argument values
	
	public CallInstruction(Register register, String functionId, List<Register> args)
	{
		this.register = register;
		this.functionId = functionId;
		this.args = args;
	}

	public String toString()
	{
		String argString = args.toString().replace(", ", " ");
		argString = argString.substring(1, argString.length() - 1);
		return String.format("(call %s %s %s)", register, functionId, argString);
	}

	@Override
	public List<Register> getReferencedRegisters()
	{
		return args; //TODO: deep copy?
	}

	@Override
	public Register getAssignedRegister()
	{
		return register;
	}
}
