package genKillFramework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import CFG.ControlFlowGraph;
import CFG.ControlFlowInformation;
import CFG.Node;
import IntermediateLanguage.Register;

public class GenKill
{
	private ControlFlowGraph cfg;
	private DataFlowAnalysis<Register> opt;
	private Map<Node, ControlFlowInformation> outMap;
	private Map<Node, ControlFlowInformation> inMap; 
	
	
	public GenKill(ControlFlowGraph cfg, DataFlowAnalysis<Register> opt)
	{
		this.cfg = cfg; 
		this.opt = opt;
	}
	
	public void optimiseBackward()
	{
		boolean isChanged = true; 
		
		/*From the end of the CFG*/
		List<Node> bfsOrdering = cfg.bfs(false);
		
		while(isChanged)
		{
			isChanged = false;
			for(Node n : bfsOrdering)
			{
				Set<Register> result = new HashSet<Register>();
				for(Node j : n.getAllSuccessors())
				{
					result = opt.meet(result, opt.transfer(n));
				}
				//Union with old to keep monotonicity 
				int oldCount = n.getOut().size();
				n.getOut().addAll(result);
				if(oldCount != n.getOut().size())
					isChanged = true; 
			}
		}
		
	}
}
	
/*	public Set<T> optimiseForward()
	{
		Node next = cfg.start.getAllSuccessors()
		
		
		
		
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
	}*/

