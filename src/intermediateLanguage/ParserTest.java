package intermediateLanguage;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

	@Test
	public void testPatterns() {
		assertTrue(Parser.INSTRUCTION.matcher("( ld r1 n ").find());
	}

}
