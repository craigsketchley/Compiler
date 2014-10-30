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

public class LiveVariableAnalysis extends DataFlowAnalysis<Register>
{
	public LiveVariableAnalysis(ControlFlowGraph cfg)
	{
		super(cfg);
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
		Set<Register> out = new HashSet<Register>();
		
		for(Node j : n.getAllSuccessors())
		{
			out.addAll(in.get(j));
		}
		
		return out;
	}
	
	public Set<Register> transfer(Node n)
	{
		Set<Register> in = new HashSet<Register>();
		in.addAll(out.get(n));
		in.removeAll(kill(n));
		in.addAll(gen(n));
		return in;		
	}

	@Override
	public Map<Node, Set<Register>> analyse()
	{
		boolean isChanged = true; 
		
		/*From the end of the CFG*/
		List<Node> bfsOrdering = cfg.bfs(false);
		
		while(isChanged)
		{
			isChanged = false;
			for(Node n : bfsOrdering)
			{
				int oldCount = out.get(n).size(); 
				for(Node j : n.getAllSuccessors())
				{
					in.put(j, transfer(j));
				}
				//Merges with the current out set of the node to maintain MONOTONICITY
				out.get(n).addAll(meet(n));
				if(oldCount != out.get(n).size())
				{
					isChanged = true; 
				}
			}
		}
		
		return out;
	}
	
}
