package genKillFramework;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import CFG.*;
import IntermediateLanguage.Register;

public abstract class DataFlowAnalysis<T>
{
	protected Map<Node, Set<T>> in;
	protected Map<Node, Set<T>> out;
	ControlFlowGraph cfg;
	
	public DataFlowAnalysis(ControlFlowGraph cfg)
	{
		this.cfg = cfg;
		in = new HashMap<Node, Set<T>>();
		out = new HashMap<Node, Set<T>>();
		
		for(Node n : cfg.getAllNodes())
		{
			in.put(n, new HashSet<T>());
			out.put(n, new HashSet<T>());
		}
		
	}
	
	public abstract Set<T> gen(Node n);
	
	public abstract Set<T> kill(Node n);

	/* Handles the OUT[B] = meet(IN[B]) 
	 * or IN[B] = meet(OUT[B]) internally for each node
	 */
	public abstract Set<T> meet(Node n);

	/* Handles the transfer IN[B] = transfer(OUT[B]) 
	 * or OUT[B] = transfer(IN[B]) internally for each node
	 */
	public abstract Set<T> transfer(Node n);
	
	public abstract Map<Node, Set<T>> analyse();
	
	
}