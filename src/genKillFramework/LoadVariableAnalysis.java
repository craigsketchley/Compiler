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
		//other variable assignment, gen (register, TOP)
		/*else
		{
			generatedInfo.put(
					((StInstruction) instruction).register,
					new Lattice<String>(Lattice.State.TOP)); //TODO: placeholder for KNOWN expression?
		}*/
		return generatedInfo;
	}

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
		//other variable assignment, gen (register, TOP)
		else if(instruction.getAssignedRegister() != null)
		{
			generatedInfo.put(
					instruction.getAssignedRegister(),
					new Lattice<String>(Lattice.State.TOP));
		}
		return generatedInfo;
	}

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
				result.put(key, value); //TODO: should we always put this in? e.g. when TOP?
			}
		}
		
		return result;
	}

	public boolean updateMeet(Map<Node, HashMap<Register, Lattice<String>>> map, Node n)
	{
		int size = map.get(n).size();
		map.get(n).putAll(meet(n));
		return size != map.get(n).size();
	}
	
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
		return analyse(Direction.FORWARDS);
	}
}