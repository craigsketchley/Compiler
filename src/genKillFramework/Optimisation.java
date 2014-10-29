package genKillFramework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import CFG.*;

public abstract class Optimisation<T>
{
	public boolean isForward;
	public ControlFlowInformation cfInfoType;
	
	public abstract Set<T> gen(Node n);
	
	public abstract Set<T> kill(Node n);

	public abstract Set<T> merge(Set<T> s1, Set<T> s2);
	
	public void optimise(Node n, Node next)
	{
		//The Out of node n should be the result
		n.getOutCfInfo();
		Set<T> result = new HashSet<T>();
		List<Node> nextNodes = new ArrayList<Node>();
 		
		Set<T> genNodes = opt.gen(next);
		Set<T> killNodes = opt.kill(next);
		
		n.add(next.getCfInfoIn());
		result.
		
		optimise(next);
		result.removeAll(killNodes);
		result.addAll(genNodes);
		
		opt.merge(result, nextNodeResult);
		
		
		return result;
	}
}
