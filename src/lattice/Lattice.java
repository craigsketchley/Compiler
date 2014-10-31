package lattice;

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
	
	private T value; //The actual value (if known)
	private State state; //The state (TOP, BOTTOM, KNOWN)
	
	/**
	 * Constructor for states TOP or BOTTOM (value is set to null)
	 * @param state The initial state of the lattice
	 */
	public Lattice(State state)
	{
		this.value = null;
		this.state = state;		
	}
	
	/**
	 * Constructor for known values (state is set to KNOWN)
	 * @param value The initial value of the lattice
	 */
	public Lattice(T value)
	{
		this.value = value;
		this.state = State.KNOWN;
	}
	
	/**
	 * Retrieve the state.
	 * @return the current state of the lattice
	 */
	public State getState()
	{
		return state;
	}
	
	/**
	 * Retrieve the value. Throws an exception if state is not MIDDLE
	 * @return the value of the state, if known
	 */
	public T getValue()
	{
		if(state == State.KNOWN)
		{
			return value;
		}
		else
		{
			throw new UnknownLatticeState(
					"Tried to get a middle value, but state was TOP or BOTTOM");
		}
	}
	
	/**
	 * Merges another lattice into this one (then returns itself)
	 * Changes the called object, but not the argument
	 * @param other Another lattice
	 * @return itself, after merging in the other lattice
	 */
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
	
	/**
	 * Returns the string representation of the lattice:
	 *  TOP, BOTTOM, or the known value itself
	 */
	public String toString()
	{
		switch(state)
		{
		case TOP: return "TOP";
		case BOTTOM: return "BOTTOM";
		case KNOWN: return value.toString();
		}
		return "Invalid state";
	}
	
	/**
	 * Check equality of two lattice objects
	 * @param other Another lattice object
	 * @return true iff both are known and carry the same value
	 */
	public boolean equals(Lattice<T> other) {
		if(other.getState() != this.getState())
		{
			return false;
		}
		if(state == State.KNOWN)
		{
			return value.equals(other.value);
		}
		return false; //not enough information
	}
	
	/**
	 * Sets the value (and updates the state to KNOWN)
	 * @param val
	 */
	public void setValue(T val)
	{
		state = State.KNOWN;
		value = val;
	}

	/**
	 * Sets the state to TOP (and nulls the value)
	 */
	public void setStateTop()
	{
		value = null;
		state = State.TOP;
	}
	
	/**
	 * Sets the state to BOTTOM (and nulls the value)
	 */
	public void setStateBottom()
	{
		value = null;
		state = State.BOTTOM;
	}
	
	public boolean isStateTop()
	{
		return (state == State.TOP);
	}
	
	public boolean isStateBottom()
	{
		return (state == State.BOTTOM);
	}
}
