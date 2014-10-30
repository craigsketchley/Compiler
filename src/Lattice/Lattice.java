package Lattice;

/**
 * Lattice
 * Generic class. It carries the lattice value of TOP, BOTTOM or
 * KNOWN (which corresponds to a particular value of the generic type)
 */
public class Lattice<T>
{

	/**
	 * Lattice.State
	 * An enum to store the state of a lattice:
	 * TOP (super-position)
	 * KNOWN (known value)
	 * BOTTOM (no information)
	 */
	public enum State
	{
		TOP, //super-position
		KNOWN, //known value
		BOTTOM //no information
	};

	/** An exception class for error states in the value */
	public static class UnknownLatticeState extends RuntimeException
	{
		private static final long serialVersionUID = -2635534618819921257L;
		public UnknownLatticeState(String string)
		{
			super(string);
		}
	}
	
	// member variables
	private T value; //The actual value (if known)
	private State state; //The state (TOP, BOTTOM, KNOWN)
	
	/** Constructor if the state is TOP or BOTTOM */
	public Lattice(State state)
	{
		this.value = null;
		this.state = state;		
	}
	
	/** Constructor if the value is known (KNOWN) */
	public Lattice(T value)
	{
		this.value = value;
		this.state = State.KNOWN;
	}
	
	/** Retrieve the state */
	public State getState()
	{
		return state;
	}
	
	/** Retrieve the value. Throws exception if state is not MIDDLE */
	public T getValue()
	{
		if(state == State.KNOWN)
		{
			return value;
		}
		else
		{
			throw new UnknownLatticeState("Tried to get a middle value, but state was TOP or BOTTOM");
		}
	}
	
	/** Return the result of merging another Value into this one.
	 * Does not change the other value */
	public Lattice<T> merge(Lattice<T> other)
	{
		switch(this.getState())
		{
		//State TOP, stays TOP
		case TOP:
			return this;
		
		//State KNOWN, changes to TOP if other is TOP or if different known values.
		case KNOWN:
			switch(other.getState())
			{
			case TOP: //Become TOP
				state = State.TOP;
				break;
			case KNOWN: //Both are known. Check if they are different
				if(other.getValue() != this.getValue())
					state = State.TOP; //different values
				break;
			case BOTTOM: //No change
				break;
			}
			return this;
			
		//State BOTTOM, change to whatever the other has
		case BOTTOM:
			switch(other.getState())
			{
			case TOP: state = State.TOP; break;
			case KNOWN: state = State.KNOWN; value = other.getValue(); break;
			case BOTTOM: break;
			}
			return this;
			
		//this can never happen. Required to suppress Java warning
		default:
			throw new UnknownLatticeState("Value not TOP, KNOWN or BOTTOM");
		}
	}
}
