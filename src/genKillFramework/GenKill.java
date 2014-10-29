package genKillFramework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import CFG.ControlFlowGraph;
import CFG.Node;

public class GenKill<T>
{
	private ControlFlowGraph cfg;
	private Optimisation<T> opt;
	
	
	public GenKill(ControlFlowGraph cfg, Optimisation<T> opt)
	{
		this.cfg = cfg; 
		this.opt = opt;
	}
	
	public Set<T> optimiseBackward(Node n)
	{
		Set<T> result = new HashSet<T>();
		List<Node> nextNodes = new ArrayList<Node>();
 		
		if(opt.isForward)
			nextNodes = n.getAllPredecessors();
 		else
 			nextNodes = n.getAllSuccessors();
 		
		for(Node next : nextNodes)
		{
			Set<T> genNodes = opt.gen(next);
			Set<T> killNodes = opt.kill(next);
			Set<T> nextNodeResult = optimise(next);
			nextNodeResult.removeAll(killNodes);
			nextNodeResult.addAll(genNodes);
			
			opt.merge(result, nextNodeResult);
		}
		return result;
	}
	
	public Set<T> optimiseForward(Node n, Set)
	{
		Set<T> result = new HashSet<T>();
		List<Node> nextNodes = new ArrayList<Node>();
 		
		nextNodes = n.getAllPredecessors();
 		
		for(Node next : nextNodes)
		{
			Set<T> genNodes = opt.gen(next);
			Set<T> killNodes = opt.kill(next);
			Set<T> nextNodeResult = optimise(next);
			
			nextNodeResult.removeAll(killNodes);
			nextNodeResult.addAll(genNodes);
			
			opt.merge(result, nextNodeResult);
		}
		
		
		return result;
	}
}
