package genKillFramework;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import CFG.*;
import IntermediateLanguage.*;

public class AvailableExpressionsAnalysis extends DataFlowAnalysis<Set<String>>
{
	boolean computed;
	
	/**
	 * Construct an Available Expressions Analysis. It will be able to perform
	 * @param cfg
	 */
	public AvailableExpressionsAnalysis(ControlFlowGraph cfg)
	{
		super(cfg);
		
		// Go through adding all expressions to each set.
		Set<String> allExpressions = new HashSet<String>();
		for (Node n : cfg.getAllNodes())
		{
			Instruction instruction = n.getInstruction();
			if (instruction instanceof LdInstruction) {
				// Get variable (expression) from ld instruction.
				String expression = ((LdInstruction)instruction).variable;
				// TODO: If using more complicated expressions, we would need
				// to check if any variables being assigned.
				allExpressions.add(expression);
			}
		}		
		
		for(Node n : cfg.getAllNodes())
		{
			if (n.isSentinel())
			{ // Empty set for the start/end sentinals
				in.put(n, new HashSet<String>());
				out.put(n, new HashSet<String>());
			}
			else
			{ // Universal Set for all others.
				in.put(n, new HashSet<String>(allExpressions));
				out.put(n, new HashSet<String>(allExpressions));				
			}
		}
		
		this.computed = false;
	}

	@Override
	public Set<String> gen(Node n)
	{
		// TODO: Using only LdInsructions
		Set<String> generatedInfo = new HashSet<String>();
		
		if(!n.isSentinel())
		{
			Instruction instruction = n.getInstruction();
			if (instruction instanceof LdInstruction) {
				// Get variable (expression) from ld instruction.
				String expression = ((LdInstruction)instruction).variable;
				// TODO: If using more complicated expressions, we would need
				// to check if any variables being assigned.
				generatedInfo.add(expression);
			}
		}
		
		return generatedInfo;
	}

	@Override
	public Set<String> kill(Node n)
	{
		// TODO: Looking for St instructions only.
		Set<String> infoToKill = new HashSet<String>();
		
		if(!n.isSentinel())
		{
			Instruction instruction = n.getInstruction();
			if (instruction instanceof StInstruction) {
				// Get variable (expression) from ld instruction.
				String expression = ((StInstruction)instruction).id;
				infoToKill.add(expression);
			}
		}
		
		return infoToKill;
	}

	@Override
	public Set<String> meet(Node n)
	{
		Set<String> in = new HashSet<String>();
		
		// Sets the IN to the intersection of all the preceding OUTs.
		for(Node m : n.getAllSuccessors())
		{
			in.retainAll(out.get(m));
		}
		
		return in;
	}
	
	public Set<String> transfer(Node n)
	{
		Set<String> out = new HashSet<String>();
		
		out.addAll(in.get(n));
		out.removeAll(kill(n));
		out.addAll(gen(n));
		
		return out;
	}

	@Override
	public Map<Node, Set<String>> analyse()
	{
		if (computed) {
			// Saves recomputation.
			return out;
		}
		
		boolean isChanged = true;
		
		/*From the end of the CFG*/
		List<Node> bfsOrdering = cfg.bfs(true);
		
		Set<String> temp = null;
		
		while(isChanged)
		{
			isChanged = false;
			
			for (Node n : bfsOrdering)
			{
				System.out.println(n);
				// Calculate the IN of the node.
				temp = meet(n);
				in.put(n, temp);
				
				// Save the size of the old OUT for comparison.
				int oldCount = out.get(n).size();
				
				// Calculate the OUT of the current node.
				temp = transfer(n);
				out.put(n, temp);
				
				// If OUT has changed, flag it so we repeat.
				isChanged = oldCount != out.get(n).size();
			}
		}
		
		computed = true;
		
		return out;
	}
	
}
