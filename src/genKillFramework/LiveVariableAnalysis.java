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
 * @see DataFlowAnalysis comments for general information. Comments in this
 * file will detail only details unique to this analysis.
 * 
 * Anaylses a ControlFlowGraph for Live Variables at all program points.
 * 
 * Once constructed, call the {@link #analyse()} method to initiate analysis
 * and return a mapping from a Node in the CFG to a Set of Registers. If a register
 * is in the map at given instruction, then the value within that register
 * is 'live'.
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
	 * @param cfg the control flow graph.
	 */
	public LiveVariableAnalysis(ControlFlowGraph cfg)
	{
		super(cfg);
		
		// Initialise all nodes IN/OUT sets as the empty set.
		for(Node n : cfg.getAllNodes())
		{
			in.put(n, new HashSet<Register>());
			out.put(n, new HashSet<Register>());
		}

	}

	/**
	 * See the overridden method documentation for general details.
	 * 
	 * This is the GEN operation for Live Variable Analysis. A register is
	 * generated at a given node in the CFG if it is 'used' at that program
	 * point. A register is 'used' if it is referenced by a branch, return or
	 * on the right-hand side of an expression.
	 */
	@Override
	public Set<Register> gen(Node n)
	{
		Set<Register> result = new HashSet<Register>();
		
		// Return the empty set if it is a start/end sentinal node.
		if(!n.isSentinel())
		{
			// Get the registers used in the nodes instruction.
			Instruction instruction = n.getInstruction();
			List<Register> usedRegisters = instruction.getReferencedRegisters();	
			
			// Add the used registers to the output set.
			result.addAll(usedRegisters);
		}
		
		return result;
	}

	/**
	 * See the overridden method documentation for general details.
	 * 
	 * This is the KILL operation for Live Variable Analysis. A register is
	 * killed at a given node in the CFG if it is 'defined' at that program
	 * point. A register is 'defined' if anything is assigned into that
	 * register.
	 */
	@Override
	public Set<Register> kill(Node n)
	{
		Set<Register> result = new HashSet<Register>();
		
		// Return the empty set if it is a start/end sentinal node.
		if(!n.isSentinel())
		{
			// Get the registers assigned in the nodes instruction.
			Instruction instruction = n.getInstruction();
			Register killedReg = instruction.getAssignedRegister();	
			
			// Some instructions don't assign, so we check...
			if(killedReg != null)
			{
				// If so, we add the register to the kill set.
				result.add(killedReg);
			}
		}
		
		return result;
	}

	/**
	 * See the overridden method documentation for general details.
	 * 
	 * The Meet operator in Live Variable Analysis. The Meet operator for a
	 * given node is the union of all the successive nodes IN values, because
	 * this analysis is backwards through the CFG.
	 */
	@Override
	public Set<Register> meet(Node n)
	{
		Set<Register> result = new HashSet<Register>();
		
		// For all successors, union the IN sets.
		for(Node j : n.getAllSuccessors())
		{
			result.addAll(in.get(j));
		}
		
		return result;
	}

	@Override
	public boolean updateDataFlowInfo(Map<Node, Set<Register>> map, Node n)
	{
		int size = map.get(n).size();
		//Merges with the current out set of the node to maintain MONOTONICITY
		map.get(n).addAll(meet(n));
		//The set changed iff the size changed
		return size != map.get(n).size();
	}

	/**
	 * See the overridden method documentation for general details.
	 * 
	 * This is a simple transfer function, returning: GEN[n] U {OUT[n] - KILL[n]}
	 */
	@Override
	public Set<Register> transfer(Node n)
	{
		Set<Register> result = new HashSet<Register>();
		
		// Get the current OUT set of the given node.
		result.addAll(out.get(n));
		
		// Remove the KILL set.
		result.removeAll(kill(n));
		
		// Add the GEN set.
		result.addAll(gen(n));
		
		return result;		
	}

	@Override
	public Map<Node, Set<Register>> analyse()
	{
		// Analyse from end -> start.
		return analyse(Direction.BACKWARDS);
	}
	
}
