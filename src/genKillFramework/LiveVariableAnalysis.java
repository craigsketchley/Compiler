package genKillFramework;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import IntermediateLanguage.*;

import CFG.Node;
import IntermediateLanguage.Instruction;

public class LiveVariableAnalysis extends DataFlowAnalysis<Register>
{

	@Override
	public Set<Register> gen(Node n)
	{
		Set<Register> result = new HashSet<Register>();
		
		if(!n.isSentinel())
		{
			Instruction instruction = n.getInstruction();
			List<Register> usedRegisters = instruction.getReferencedRegisters();	
			result.addAll(usedRegisters);
		}
		
		return result;
	}

	@Override
	public Set<Register> kill(Node n)
	{
		Set<Register> result = new HashSet<Register>();
		
		if(!n.isSentinel())
		{
			Instruction instruction = n.getInstruction();
			Register killedReg = instruction.getAssignedRegister();	
			if(killedReg != null)
				result.add(killedReg);
		}
		
		return result;
	}

	@Override
	public void meet(Node n)
	{
		for(Node j : n.getAllSuccessors())
		{
			n.getIn().addAll(j.getIn());
		}
	}
	
	public void transfer(Node n)
	{
		Set<Register> result = new HashSet<Register>();
		result.addAll(n.getOut());
		result.removeAll(kill(n));
		result.addAll(gen(n));
		n.getIn().addAll(result);		
	}

}
