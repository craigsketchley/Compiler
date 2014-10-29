package genKillFramework;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import CFG.Node;
import IntermediateLanguage.Instruction;

public class LiveVariableOptimisation extends Optimisation<Integer>
{

	@Override
	public Set<Integer> gen(Node n)
	{
		Set<Integer> result = new HashSet<Integer>();
		
		Instruction instruction = n.getInstruction();
		List<Integer> usedRegisters = instruction.getReferencedRegisters();	
		result.addAll(usedRegisters);
		
		return result;
	}

	@Override
	public Set<Integer> kill(Node n)
	{
		Set<Integer> result = new HashSet<Integer>();
		
		Instruction instruction = n.getInstruction();
		List<Integer> killedRegisters = instruction.getAssignedRegisters();	
		result.addAll(killedRegisters);
		
		return result;
	}

	@Override
	public Set<Integer> merge(Set<Integer> s1, Set<Integer> s2)
	{
		Set<Integer> result = new HashSet<Integer>();
		result.addAll(s1);
		result.addAll(s2);
		
		return result;
		
	}

}
