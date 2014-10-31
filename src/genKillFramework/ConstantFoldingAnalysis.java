package genKillFramework;

import genKillFramework.DataFlowAnalysis.Direction;
import intermediateLanguage.BinOpInstruction;
import intermediateLanguage.Instruction;
import intermediateLanguage.LcInstruction;
import intermediateLanguage.Register;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import lattice.Lattice;
import cfg.ControlFlowGraph;
import cfg.Node;

public class ConstantFoldingAnalysis extends DataFlowAnalysis<Map<Register, Lattice<Integer>>>
{

	public ConstantFoldingAnalysis(ControlFlowGraph cfg)
	{
		super(cfg);
	}

	@Override
	public Map<Register, Lattice<Integer>> gen(Node n)
	{
		/* Constant folding does not use Gen-Kill framework */ 
		return null;
	}

	@Override
	public Map<Register, Lattice<Integer>> kill(Node n)
	{
		/* Constant folding does not use Gen-Kill framework */
		return null;
	}

	@Override
	public Map<Register, Lattice<Integer>> meet(Node n)
	{
		/* For all incoming Data Flow information we need to merge 
		 * the values mapped to the register
		 */
		Map<Register, Lattice<Integer>> result = new HashMap<Register, Lattice<Integer>>();
		
		//Set<Register> registerIntersection = new HashSet<Register>();
		
		//Merge the Lattice Maps from predecessors\
		for(Node pred : n.getAllPredecessors())
		{
			Map<Register, Lattice<Integer>> predMap = out.get(pred);
			//For all Registers in the predecessor merge the Lattice into result
			for(Register k : predMap.keySet())
			{
				if(!result.containsKey(k) || result.get(k).isStateBottom())
				{
					//Implies Register is undefined in the result
					Lattice<Integer> newVal = new Lattice<Integer>(Lattice.State.BOTTOM);
					result.put(k, newVal.merge(predMap.get(k)));
				}
				else
				{
					//Register exists in the result map, we should monotonically merge
					result.get(k).merge(predMap.get(k));
				}
			}	
		}		
		return result;
	}

	@Override
	public boolean updateMeet(Map<Node, Map<Register, Lattice<Integer>>> map,
			Node n)
	{
		//Perform the meet operation and track if any variables change from old IN[]
		boolean isUpdated = false;
		Map<Register, Lattice<Integer>> newIn = meet(n);
		for(Node pred : n.getAllPredecessors())
		{
			Map<Register, Lattice<Integer>> predMap = out.get(pred);
			//For all Registers in the predecessor merge the Lattice into result
			for(Register k : predMap.keySet())
			{
				if(!result.containsKey(k) || result.get(k).isStateBottom())
				{
					//Implies Register is undefined in the result
					Lattice<Integer> newVal = new Lattice<Integer>(Lattice.State.BOTTOM);
					result.put(k, newVal.merge(predMap.get(k)));
				}
				else
				{
					//Register exists in the result map, we should monotonically merge
					result.get(k).merge(predMap.get(k));
				}
			}	
		}		
		
		
		
		Map<Register, Lattice<Integer>> 
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<Register, Lattice<Integer>> transfer(Node n)
	{
		Map<Register, Lattice<Integer>> result = new HashMap<Register, Lattice<Integer>>();
		Map<Register, Lattice<Integer>> inMap = in.get(n);
		
		
		/*Dependant on the Instruction type at the Node*/
		
		Instruction instruction = n.getInstruction();
		Register assignedReg = instruction.getAssignedRegister();
	
		Lattice<Integer> assignedRegValue = new Lattice<Integer>(Lattice.State.BOTTOM);
		
		//Start analysis with the existing Data Flow Information
		result.putAll(inMap);
		if(assignedReg == null)
		{
			//No changes in the Data flow information
			return result;
		}
		else
		{
			//Instruction assigned a register
			
			//If the instruction is a load constant then define the assigned Register
			//Regardless of Lattice State
			if(instruction instanceof LcInstruction)
			{
				Lattice<Integer> constant = new Lattice<Integer>(
													((LcInstruction) instruction).getConstant());
				result.put(assignedReg, constant);
				return result;
			}
			else if(instruction instanceof BinOpInstruction)
			{
				//We have to merge the results of the referenced registers
				Register refRegLeft = instruction.getReferencedRegisters().get(0);
				Register refRegRight = instruction.getReferencedRegisters().get(1);
				
				//Determine the State of the referenced Registers
				//If they don't appear in the Data Flow Info then it is implied to be Undefined/BOTTOM
				Lattice<Integer> refRegLeftLattice = new Lattice<Integer>(Lattice.State.BOTTOM); 
				Lattice<Integer> refRegRightLattice = new Lattice<Integer>(Lattice.State.BOTTOM);
				if(inMap.containsKey(refRegLeft))
				{
					refRegLeftLattice = inMap.get(refRegLeft);
				}
				if(inMap.containsKey(refRegRight))
				{
					refRegRightLattice = inMap.get(refRegRight);
				}
				
				
				if(refRegLeftLattice.isStateBottom() 
						|| refRegRightLattice.isStateBottom())
				{
					//Monotonicity : If any of the references are unknown then result is BOTTOM
					assignedRegValue.setStateBottom();
				}
				else if(refRegLeftLattice.isStateTop()
						|| refRegRightLattice.isStateTop())
				{
					//If any of the references are TOP states then result is TOP
					assignedRegValue.setStateTop();
				}
				else
				{
					//Both reference registers are KNOWN
					try
					{
						int val = ((BinOpInstruction) instruction).calculateValue(refRegLeftLattice.getValue(), 
																refRegRightLattice.getValue());
						assignedRegValue.setValue(val);
					} 
					catch (Exception e)
					{
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}
			else
			{
				/*Instruction relies on information outside of the function e.g. call or ld*/
				//Monotonicty : assigned Value should be TOP
				assignedRegValue.setStateTop();
			}
		}
		
		result.put(assignedReg, assignedRegValue);
		return result;
	}

	@Override
	public Map<Node, Map<Register, Lattice<Integer>>> analyse()
	{
		return analyse(Direction.FORWARDS);
	}





}
