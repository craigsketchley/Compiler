package intermediateLanguage;

import java.util.Arrays;
import java.util.List;

/* This is likely to change later into an interface (implemented by different operations */

public class BinOpInstruction implements Instruction
{
	
	public String op; //the operation
	public Register dest; //register to write to
	public Register lhs; //left argument
	public Register rhs; //right argument
	
	public BinOpInstruction(String op, Register dest, Register lhs, Register rhs)
	{
		this.op = op;
		this.dest = dest;
		this.lhs = lhs;
		this.rhs = rhs;
	}

	@Override
	public String toString()
	{
		return String.format("(%s %s %s %s)", op, dest, lhs, rhs);
	}

	@Override
	public List<Register> getReferencedRegisters()
	{
		return Arrays.asList(lhs, rhs);
	}
	
	@Override
	public Register getAssignedRegister()
	{
		return dest;
	}
	
	@Override
	public void rewriteReferencedRegisters(Register from, Register to)
	{
		if(lhs.equals(from))
		{
			lhs = new Register(to);
		}
		if(rhs.equals(to))
		{
			rhs = new Register(to);
		}
	}
	
	@Override
	public void rewriteAssignedRegister(Register register)
	{
		dest = new Register(register);
	}
	
	/** 
	 * Calculate the value of the result of the binary operation given fixed register values
	 * @param leftVal 		left register value
	 * @param rightVal      right register value
	 * @return
	 */
	public int calculateValue(int leftValue, int rightValue) throws Exception
	{
		switch(op)
		{
		case "add":
			return leftValue + rightValue;
		case "mul":
			return leftValue * rightValue;
		case "div":
			return leftValue / rightValue;
		case "sub":
			return leftValue - rightValue;
		case "lt":
			return (leftValue < rightValue) ? 1:0;
		case "gt":
			return (leftValue > rightValue) ? 1:0;
		case "eq":
			return (leftValue == rightValue) ? 1:0;
		default:
			throw new Exception("Binary Operation not defined");
		}
	}
}
