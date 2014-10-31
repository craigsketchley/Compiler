package genKillFramework;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import IntermediateLanguage.*;
import CFG.ControlFlowGraph;
import CFG.Node;
import IntermediateLanguage.Instruction;
import Lattice.Lattice;

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
		HashMap<Register, Lattice<String>> in = new HashMap<Register, Lattice<String>>();
		
		Set<Register> registerIntersection = new HashSet<Register>();
		
		//get the intersection of keys
		Iterator<Node> it = n.getAllPredecessors().iterator();
		if(!it.hasNext())
		{
			//no predecessors
			return in;
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
				in.put(key, value); //TODO: should we always put this in? e.g. when TOP?
			}
		}
		
		return in;
	}

	public boolean updateMeet(Map<Node, HashMap<Register, Lattice<String>>> map, Node n)
	{
		int size = map.get(n).size();
		map.get(n).putAll(meet(n));
		return size != map.get(n).size();
	}
	
	public HashMap<Register, Lattice<String>> transfer(Node n)
	{
		HashMap<Register, Lattice<String>> out = new HashMap<Register, Lattice<String>>();
		
		//start with the gen values
		out.putAll(gen(n));
		
		//subtract kill from in
		HashMap<Register, Lattice<String>> in = new HashMap<Register, Lattice<String>>();
		in.putAll(this.in.get(n));
		HashMap<Register, Lattice<String>> kill = kill(n);
		for(Register killkey : kill.keySet())
		{
			if(killkey == null)
			{
				String killvalue = kill.get(killkey).getValue();
				for(Register inkey : in.keySet())
				{
					Lattice<String> invalue = in.get(inkey);
					if((invalue.getState() == Lattice.State.KNOWN) &&
							(invalue.getValue().equals(killvalue)))
					{
						in.remove(inkey);
					}
				}
			}
			else
			{
				in.remove(killkey);
			}
		}
		
		//merge in the modified set
		out.putAll(in);
		
		return out;		
	}

	@Override
	public Map<Node, HashMap<Register, Lattice<String>>> analyse()
	{
		return analyse(Direction.FORWARDS);
	}
}