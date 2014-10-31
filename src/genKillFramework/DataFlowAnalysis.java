package genKillFramework;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	/**
	 * Enum to represent the direction of the analysis
	 * FORWARDS : From entry point to exit point
	 * BACKWARDS : From exit point to entry point
	 */
	public enum Direction {FORWARDS, BACKWARDS}
	
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
	 * Update the map with meet data of node n
	 * @param map
	 * @param n
	 * @return true if the map size changed
	 */
	public abstract boolean updateMeet(Map<Node, T> map, Node n);

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

	/**
	 * Given a control flow graph and a direction to analyse it, it will analyse
	 * it and produce an analysis result as a mapping from nodes to information
	 * @param direction
	 * @return
	 */
	public Map<Node, T> analyse(Direction direction)
	{
		boolean changed = true;
		
		//Get an ordering of the nodes by BFS, traversing in the given direction
		List<Node> orderedNodes =
				(direction == Direction.FORWARDS) ? cfg.bfs(true) : cfg.bfs(false);
		
		//Run fixed point function
		while(changed)
		{
			//Indicate whether a change has been seen
			changed = false;
			
			//Run transfer function on all nodes
			for(Node n : orderedNodes)
			{
				//Note that if the Meet data didn't change, this won't, so there's
				//no requirement to check for changes at this point
				Map<Node, T> map = (direction == Direction.FORWARDS) ? out : in;
				map.put(n, transfer(n));
			}

			//Run meet function on all nodes, checking for a change
			for(Node n : orderedNodes)
			{
				//Merge with all previous inputs to maintain MONOTONICITY
				Map<Node, T> map = (direction == Direction.FORWARDS) ? in : out;
				if(updateMeet(map, n))
				{
					changed = true;
				}
			}
		}
		
		return (direction == Direction.FORWARDS) ? in : out;
	}

}