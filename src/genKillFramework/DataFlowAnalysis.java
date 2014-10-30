package genKillFramework;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import CFG.*;

/**
 * Encapsulates the Gen/Kill framework for a data flow analysis.
 * 
 * Implement this interface to generate a specific data flow analysis.
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 * @param <T> The type of information stored for the data flow analysis.
 */
public abstract class DataFlowAnalysis<T>
{
	protected Map<Node, T> in;
	protected Map<Node, T> out;
	ControlFlowGraph cfg;
	
	/**
	 * Creates the in and out information mappings for the given control flow graph
	 * @param cfg
	 */
	public DataFlowAnalysis(ControlFlowGraph cfg)
	{
		this.cfg = cfg;
		in = new HashMap<Node, T>();
		out = new HashMap<Node, T>();
	}
	
	/**
	 * The Gen operation of the Gen/Kill framework.
	 * 
	 * Returns the set of information that is generated for the given node.
	 * 
	 * @param n
	 * @return
	 */
	public abstract T gen(Node n);
	
	/**
	 * The Kill operation of the Gen/Kill framework.
	 * 
	 * Returns the set of information to be killed for the given node.
	 * 
	 * @param n
	 * @return
	 */
	public abstract T kill(Node n);

	/**
	 * The meet operator of the Gen/Kill framework.
	 * 
	 * Given a node, combines all the IN/OUT of it's successors/predecessors,
	 * depending on the type of analysis implemented.
	 * 
	 * Handles the OUT[B] = meet(IN[B]) 
	 * or IN[B] = meet(OUT[B]) internally for each node
	 * 
	 * @param n
	 */
	public abstract T meet(Node n);

	/**
	 * The transfer function of the Gen/Kill framework.
	 * 
	 * Handles the transfer IN[B] = transfer(OUT[B]) 
	 * or OUT[B] = transfer(IN[B]) internally for each node
	 * 
	 * @param n
	 */
	public abstract T transfer(Node n);
	
	/**
	 * Given a control flow graph, it will analyse it and produce an analysis
	 * result as a mapping from nodes to information.
	 * @return
	 */
	public abstract Map<Node, T> analyse();
}