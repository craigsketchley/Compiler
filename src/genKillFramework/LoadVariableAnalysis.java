package genKillFramework;

import intermediateLanguage.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import lattice.Lattice;
import cfg.ControlFlowGraph;
import cfg.Node;

/**
 * See comments in the abstract DataFlowAnalysis class for general information.
 * Comments in this file will detail only details unique to this analysis.
 * 
 * LoadVariableAnalysis is used to analyse information about Variables loaded
 * into registers.
 * 
 * Maps are stored for each node. If a register is in the map, then it is known
 * to hold a specific variable.
 */
public class LoadVariableAnalysis extends DataFlowAnalysis<HashMap<Register, Lattice<String>>>
{
	public LoadVariableAnalysis(ControlFlowGraph cfg)
	{
		super(cfg);
		for(Node n : cfg.getAllNodes())
		{
			in.put(n, new HashMap<Register, Lattice<String>>());
			out.put(n, new HashMap<Register, Lattice<String>>());
		}
	}

	/**
	 * See the overridden method documentation for general details.
	 * 
	 * In this case, only Load Variable instructions generate values. The
	 * generated value is a mapping of the assigned register, and a Lattice
	 * class object holding the information that this register is known to hold
	 * the variable
	 */
	@Override
	public HashMap<Register, Lattice<String>> gen(Node n)
	{
		HashMap<Register, Lattice<String> > generatedInfo = new HashMap<Register, Lattice<String> >();
		
		//if this is a sentinel, gen nothing
		if(n.isSentinel())
		{
			return generatedInfo;
		}
		
		Instruction instruction = n.getInstruction();
		
		Register assigned = instruction.getAssignedRegister();
		
		//if there's no assignment, gen nothing
		if(assigned == null)
		{
			return generatedInfo;
		}
		//if this is a variable load, gen (register, variable)
		else if(instruction instanceof LdInstruction)
		{
			generatedInfo.put(
					((LdInstruction) instruction).register,
					new Lattice<String>(((LdInstruction) instruction).variable));
		}
		/*
		 * a logical extension to this code, would be to track other types of
		 * assignment, such as constants, or expressions.
		 */
		
		return generatedInfo;
	}

	/**
	 * See the overridden method documentation for general details.
	 * 
	 * Store instructions should kill all register/value pairs matching the
	 * variable. This is indicated in the output of this function by a
	 * (null, value) pair.
	 * 
	 * Any assignment to a register should kill the register/value pair (if
	 * it exists) matching that particular register. This is represented in the
	 * output of this function by a (register, TOP) pair.
	 */
	@Override
	public HashMap<Register, Lattice<String>> kill(Node n)
	{

		HashMap<Register, Lattice<String> > generatedInfo = new HashMap<Register, Lattice<String> >();
		
		//if this is a sentinel, do nothing
		if(n.isSentinel())
		{
			return generatedInfo;
		}

		Instruction instruction = n.getInstruction();
		
		//if this is a store, kill any (register, variable)
		if(instruction instanceof StInstruction)
		{
			generatedInfo.put(
					null,
					new Lattice<String>(((StInstruction) instruction).id));
		}
		//other register assignment, gen (register, TOP)
		else if(instruction.getAssignedRegister() != null)
		{
			generatedInfo.put(
					instruction.getAssignedRegister(),
					new Lattice<String>(Lattice.State.TOP));
		}
		return generatedInfo;
	}

	/**
	 * See the overridden method documentation for general details.
	 * 
	 * First we get the intersection of all the registers with information
	 * available from the predecessor nodes. We use intersection because if
	 * information is missing from any execution path, then we do not know enough
	 * about that register to optimise it (BOTTOM.)
	 * 
	 * We then merge the lattices for each register that we have information for.
	 * Merging KNOWN and KNOWN results in KNOWN if and only if the value is identical.
	 * Otherwise, the lattice is promoted to TOP (superposition.) Any registers which
	 * are still KNOWN (fixed) values after the merge are useful information, so we
	 * store them.
	 */
	@Override
	public HashMap<Register, Lattice<String>> meet(Node n)
	{
		HashMap<Register, Lattice<String>> result = new HashMap<Register, Lattice<String>>();
		
		Set<Register> registerIntersection = new HashSet<Register>();
		
		//get the intersection of keys
		Iterator<Node> it = n.getAllPredecessors().iterator();
		if(!it.hasNext())
		{
			//no predecessors
			return result;
		}
		//start with everything from first node
		registerIntersection.addAll(out.get(it.next()).keySet());
		//then intersect the rest
		while(it.hasNext())
		{
			registerIntersection.retainAll(out.get(it.next()).keySet());
		}
		
		//now we need to see if we know the values
		for(Register key : registerIntersection)
		{
			it = n.getAllPredecessors().iterator();
			Lattice<String> value = out.get(it.next()).get(key);
			while(it.hasNext())
			{
				value.merge(out.get(it.next()).get(key));
			}
			if(value.getState() == Lattice.State.KNOWN)
			{
				result.put(key, value);
			}
			/* note that TOP values are no more useful to us than BOTTOM,
			 * in this particular analysis, so we simply omit them.
			 */
		}
		
		return result;
	}

	@Override
	public boolean updateDataFlowInfo(Map<Node, HashMap<Register, Lattice<String>>> map, Node n)
	{
		int size = map.get(n).size();
		map.get(n).putAll(meet(n));
		return size != map.get(n).size();
	}
	
	/**
	 * See the overridden method documentation for general details.
	 * 
	 * The transfer function is quite simple, just removing the pairs
	 * indicated by the output of the kill function.
	 * 
	 * We start with the 'in' set, then work through the 'kill' set.
	 * Any wildcards (null, value) require us to kill all pairs with that
	 * value. Other pairs (register, value) just require us to kill that
	 * register.
	 * 
	 * Finally, we add the 'gen' set (at most one pair, in this case.)
	 */
	@Override
	public HashMap<Register, Lattice<String>> transfer(Node n)
	{
		HashMap<Register, Lattice<String>> result =
				new HashMap<Register, Lattice<String>>();

		//initialise the result with the 'in' set
		result.putAll(in.get(n));
		
		//subtract the 'kill' set from result
		HashMap<Register, Lattice<String>> kill = kill(n);
		for(Register killkey : kill.keySet())
		{
			//a null key represents a variable with wildcard register
			if(killkey == null)
			{
				//remove all pairs matching the value
				String killvalue = kill.get(killkey).getValue();
				for(Register inkey : result.keySet())
				{
					Lattice<String> invalue = result.get(inkey);
					//Lattice.equals is true iff both values are KNOWN and equal
					if(invalue.equals(killvalue))
					{
						result.remove(inkey);
					}
				}
			}
			//non-null keys represent registers to remove
			else
			{
				result.remove(killkey);
			}
		}

		//union the 'gen' set into result
		result.putAll(gen(n));
		
		return result;		
	}

	@Override
	public Map<Node, HashMap<Register, Lattice<String>>> analyse()
	{
		//Analyse from start->end
		return analyse(Direction.FORWARDS);
	}
}