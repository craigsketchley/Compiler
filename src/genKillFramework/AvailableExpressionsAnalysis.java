package genKillFramework;

import intermediateLanguage.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cfg.*;

/**
 * Runs a simple Available Expressions analysis. Not currently used for any optimisation.
 * 
 * TODO: Remove?
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class AvailableExpressionsAnalysis extends DataFlowAnalysis<Set<String>>
{	
	/**
	 * Construct an Available Expressions Analysis.
	 * 
	 * @param cfg
	 */
	public AvailableExpressionsAnalysis(ControlFlowGraph cfg)
	{
		super(cfg);
		
		for(Node n : cfg.getAllNodes())
		{
			in.put(n, new HashSet<String>());
			out.put(n, new HashSet<String>());
		}
	}

	@Override
	public Set<String> gen(Node n)
	{
		// TODO: Using only LdInsructions
		Set<String> result = new HashSet<String>();
		
		if(!n.isSentinel())
		{
			Instruction instruction = n.getInstruction();
			if (instruction instanceof LdInstruction) {
				// Get variable (expression) from ld instruction.
				String expression = ((LdInstruction)instruction).variable;
				// TODO: If using more complicated expressions, we would need
				// to check if any variables being assigned.
				result.add(expression);
			}
		}
		
		return result;
	}

	@Override
	public Set<String> kill(Node n)
	{
		// TODO: Looking for St instructions only.
		Set<String> result = new HashSet<String>();
		
		if(!n.isSentinel())
		{
			Instruction instruction = n.getInstruction();
			if (instruction instanceof StInstruction) {
				// Get variable (expression) from ld instruction.
				String expression = ((StInstruction)instruction).id;
				result.add(expression);
			}
		}
		
		return result;
	}

	@Override
	public Set<String> meet(Node n)
	{
		Set<String> result = new HashSet<String>();
		Set<Node> p = n.getAllPredecessors();
		
		if (p.size() > 0) {
			Node[] predecessors = p.toArray(new Node[p.size()]);
			
			result = out.get(predecessors[0]); // Must have at least 1 elem.
				
			// Sets the IN to the intersection of all the preceding OUTs.
			for(int i = 1; i < p.size(); i++)
			{
				result.retainAll(out.get(predecessors[i]));
			}
		
		}
		return result;
	}
	
	public Set<String> transfer(Node n)
	{
		Set<String> result = new HashSet<String>();
		
		result.addAll(in.get(n));
		result.removeAll(kill(n));
		result.addAll(gen(n));
		
		return result;
	}

	@Override
	public Map<Node, Set<String>> analyse()
	{
		return analyse(Direction.FORWARDS);
	}

	@Override
	public boolean updateMeet(Map<Node, Set<String>> map, Node n) {
		int size = map.get(n).size();
		//Merges with the current out set of the node to maintain MONOTONICITY
		map.get(n).addAll(meet(n));
		//The set changed iff the size changed
		return size != map.get(n).size();
	}
	
}
