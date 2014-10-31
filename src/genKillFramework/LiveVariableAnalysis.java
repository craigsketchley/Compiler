package genKillFramework;

import intermediateLanguage.Instruction;
import intermediateLanguage.Register;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cfg.ControlFlowGraph;
import cfg.Node;

/**
 * Anaylses a ControlFlowGraph for Live Variables. 
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class LiveVariableAnalysis extends DataFlowAnalysis<Set<Register>>
{
	/**
	 * Constructs a LiveVariableAnalysis given a ControlFlowGraph (CFG).
	 * 
	 * Once constructed, call the {@link #analyse()} method to initiate analysis and return a mapping from a Node in the CFG to Set<Register>.
	 * 
	 * @param cfg the control flow graph.
	 */
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
		Set<Register> out = new HashSet<Register>();
		
		for(Node j : n.getAllSuccessors())
		{
			out.addAll(in.get(j));
		}
		
		return out;
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
		Set<Register> in = new HashSet<Register>();
		in.addAll(out.get(n));
		in.removeAll(kill(n));
		in.addAll(gen(n));
		return in;		
	}

	@Override
	public Map<Node, Set<Register>> analyse()
	{
		return analyse(Direction.BACKWARDS);
	}
	
}
