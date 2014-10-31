package genKillFramework;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import CFG.ControlFlowGraph;
import CFG.Node;
import IntermediateLanguage.Instruction;
import IntermediateLanguage.Register;
import Lattice.Lattice;

public class LiveVariableAnalysis extends DataFlowAnalysis<Set<Register>>
{
	public LiveVariableAnalysis(ControlFlowGraph cfg)
	{
		super(cfg);
		for(Node n : cfg.getAllNodes())
		{
			in.put(n, new HashSet<Register>());
			out.put(n, new HashSet<Register>());
		}

	}

	@Override
	public Set<Register> gen(Node n)
	{
		Set<Register> generatedInfo = new HashSet<Register>();
		
		if(!n.isSentinel())
		{
			Instruction instruction = n.getInstruction();
			List<Register> usedRegisters = instruction.getReferencedRegisters();	
			generatedInfo.addAll(usedRegisters);
		}
		
		return generatedInfo;
	}

	@Override
	public Set<Register> kill(Node n)
	{
		Set<Register> infoToKill = new HashSet<Register>();
		
		if(!n.isSentinel())
		{
			Instruction instruction = n.getInstruction();
			Register killedReg = instruction.getAssignedRegister();	
			if(killedReg != null)
			{
				infoToKill.add(killedReg);
			}
		}
		
		return infoToKill;
	}

	@Override
	public Set<Register> meet(Node n)
	{
		Set<Register> result = new HashSet<Register>();
		
		for(Node j : n.getAllSuccessors())
		{
			result.addAll(in.get(j));
		}
		
		return result;
	}

	@Override
	public boolean updateMeet(Map<Node, Set<Register>> map, Node n)
	{
		int size = map.get(n).size();
		//Merges with the current out set of the node to maintain MONOTONICITY
		map.get(n).addAll(meet(n));
		//The set changed iff the size changed
		return size != map.get(n).size();
	}

	@Override
	public Set<Register> transfer(Node n)
	{
		Set<Register> result = new HashSet<Register>();
		result.addAll(out.get(n));
		result.removeAll(kill(n));
		result.addAll(gen(n));
		return result;		
	}

	@Override
	public Map<Node, Set<Register>> analyse()
	{
		return analyse(Direction.BACKWARDS);
	}
	
}
