package lattice;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test the Lattice implementation.
 * 
 * @author Joe Godbehere
 * @author Ricky Ratnayake
 * @author Craig Sketchley
 *
 */
public class LatticeTest {

	/**
	 * Test the string representation of Lattices
	 */
	@Test
	public void testToString() {
		assertEquals("TOP", new Lattice<Integer>(Lattice.State.TOP).toString());
		assertEquals("BOTTOM", new Lattice<Integer>(Lattice.State.BOTTOM).toString());
		assertEquals("1", new Lattice<Integer>(1).toString());
		assertEquals("-2", new Lattice<Integer>(-2).toString());
	}
	
	/**
	 * Test the comparison of Lattice objects. Lattices should only be
	 * considered equal if they are both in state KNOWN and have the same value
	 */
	@Test
	public void testEquals() {
		
		Lattice<Integer> top = new Lattice<Integer>(Lattice.State.TOP);
		Lattice<Integer> top2 = new Lattice<Integer>(Lattice.State.TOP);
		Lattice<Integer> bottom = new Lattice<Integer>(Lattice.State.BOTTOM);
		Lattice<Integer> bottom2 = new Lattice<Integer>(Lattice.State.BOTTOM);
		Lattice<Integer> a = new Lattice<Integer>(1);
		Lattice<Integer> b = new Lattice<Integer>(1);
		Lattice<Integer> c = new Lattice<Integer>(2);
		
		assertFalse(top.equals(top2));
		assertFalse(top.equals(bottom));
		assertFalse(top.equals(a));

		assertFalse(bottom.equals(top));
		assertFalse(bottom.equals(bottom2));
		assertFalse(bottom.equals(a));

		assertFalse(a.equals(c));
		
		assertTrue(a.equals(b));
	}

	/**
	 * Test that Lattice objects merge monotonically. 
	 */
	@Test
	public void testMonotonic() {
		
		Lattice<Integer> top = new Lattice<Integer>(Lattice.State.TOP);
		Lattice<Integer> top2 = new Lattice<Integer>(Lattice.State.TOP);
		Lattice<Integer> bottom = new Lattice<Integer>(Lattice.State.BOTTOM);
		Lattice<Integer> bottom2 = new Lattice<Integer>(Lattice.State.BOTTOM);
		Lattice<Integer> a = new Lattice<Integer>(1);
		Lattice<Integer> b = new Lattice<Integer>(1);
		Lattice<Integer> c = new Lattice<Integer>(2);

		assertTrue(top.merge(top2).isStateTop());
		assertTrue(top.merge(bottom).isStateTop());
		assertTrue(top.merge(a).isStateTop());

		assertTrue(bottom.merge(bottom2).isStateBottom());
		assertTrue(bottom.merge(top).isStateTop());

		bottom2.merge(a);
		assertTrue(bottom2.getState() == Lattice.State.KNOWN);
		assertTrue(1 == bottom2.getValue());

		a.merge(b);
		assertTrue(1 == a.getValue());
		assertTrue(bottom2.getState() == Lattice.State.KNOWN);

		assertTrue(a.merge(c).isStateTop());
	}

}
