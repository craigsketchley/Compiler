package intermediateLanguage;

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
		return args;
	}

	@Override
	public Register getAssignedRegister()
	{
		return register;
	}

	@Override
	public void rewriteReferencedRegisters(Register from, Register to)
	{
		for(int i = 0; i < args.size(); ++i)
		{
			if(args.get(i).equals(from))
			{
				args.set(i, new Register(to));
			}
		}
	}
	
	@Override
	public void rewriteAssignedRegister(Register register)
	{
		this.register = new Register(register);
	}

}
