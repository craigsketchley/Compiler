package genKillFramework;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import IntermediateLanguage.*;

import CFG.Node;
import IntermediateLanguage.Instruction;

public class LiveVariableOptimisation extends Optimisation<Register>
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
			List<Register> killedRegisters = instruction.getAssignedRegisters();	
			result.addAll(killedRegisters);
		}
		
		return result;
	}

	@Override
	public Set<Register> merge(Set<Register> s1, Set<Register> s2)
	{
		Set<Register> result = new HashSet<Register>();
		result.addAll(s1);
		result.addAll(s2);
		
		return result;
		
	}
	
	public Set<Register> transfer(Node n)
	{
		Set<Register> result = new HashSet<Register>();
		result.addAll(n.getOut());
		result.removeAll(kill(n));
		result.addAll(gen(n));
		return result;
		
	}

}