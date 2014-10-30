package genKillFramework;

import java.util.Set;

import CFG.*;
import IntermediateLanguage.Register;

public abstract class DataFlowAnalysis<T>
{
	public boolean isForward;
	
	public abstract Set<T> gen(Node n);
	
	public abstract Set<T> kill(Node n);

	public abstract Set<T> meet(Set<T> s1, Set<T> s2);
	
	public abstract Set<Register> transfer(Node n);
	
	/*public void optimise(Node n, Node next)
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
	}*/
}
