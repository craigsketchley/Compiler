package Lattice;

public class Value {
	
	/** An exception class for error states in the value */
	public class UnknownLatticeState extends RuntimeException {
		private static final long serialVersionUID = -2635534618819921257L;
		public UnknownLatticeState(String string) {
			super(string);
		}
	}
	
	/** enum to store whether the value is:
	 * TOP (superposition)
	 * MIDDLE (known value)
	 * BOTTOM (unknown)
	 */
	public enum State {TOP, MIDDLE, BOTTOM};

	private int value;
	private State state;
	
	/** Constructor if the state is TOP or BOTTOM */
	public Value(State state) {
		this.value = 0;
		this.state = state;		
	}
	
	/** Constructor if the value is known (MIDDLE) */
	public Value(int value) {
		this.value = value;
		this.state = State.MIDDLE;
	}
	
	/** Retrieve the state */
	public State getState() {
		return state;
	}
	
	/** Retrieve the value. Throws exception if state is not MIDDLE */
	public int getValue() {
		if(state == State.MIDDLE) {
			return value;
		} else {
			throw new UnknownLatticeState("Tried to get a middle value, but state was TOP or BOTTOM");
		}
	}
	
	/** Return the result of merging another Value into this one.
	 * Does not change the other value */
	public Value merge(Value other) {
		
		switch(this.getState()) {

		//State TOP, stays TOP
		case TOP:
			return this;
		
		//State MIDDLE, changes to TOP if other is TOP or if different known values.
		case MIDDLE:
			switch(other.getState()) {
			case TOP: //Become TOP
				state = State.TOP;
				break;
			case MIDDLE: //Both are known. Check if they are different
				if(other.getValue() != this.getValue())
					state = State.TOP; //different values
				break;
			case BOTTOM: //No change
				break;
			}
			return this;
			
		//State BOTTOM, change to whatever the other has
		case BOTTOM:
			switch(other.getState()) {
			case TOP: state = State.TOP; break;
			case MIDDLE: state = State.MIDDLE; value = other.getValue(); break;
			case BOTTOM: break;
			}
			return this;
			
		//this can never happen. Required to suppress Java warning
		default:
			throw new UnknownLatticeState("Value not TOP, MIDDLE or BOTTOM");
		}
	}

}
