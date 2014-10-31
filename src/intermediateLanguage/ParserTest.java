package intermediateLanguage;

import static org.junit.Assert.*;
import intermediateLanguage.Parser.ParseException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import optimisation.Optimiser;
import optimisation.RedundantLoadOptimisation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ParserTest
{
	@Test
	public void testParseFile() {
		Program input = Parser.parse("input/redundantLoadsAssignmentSpec");
		String expected = Parser.path2string("input/redundantLoadsAssignmentSpec");
		expected = expected.replace("\t", "    "); //spaces not tabs
		assertEquals(expected, input.toString());
	}
	
	@Test
	public void testParseReg()
	{
		try {
			Parser.parseReg("r0");
			fail("r0 should not parse");
		} catch(ParseException e) {;}
		try {
			Parser.parseReg("r-1");
			fail("r-1 should not parse");
		} catch(ParseException e) {;}
		try {
			Parser.parseReg("R1");
			fail("R1 should not parse");
		} catch(ParseException e) {;}
		try {
			Parser.parseReg("1");
			fail("1 should not parse (as a register)");
		} catch(ParseException e) {;}
		
		for(int i = 1; i <101; ++i) {
			assertEquals(i, Parser.parseReg("r" + i).register);
		}
	}

	@Test
	public void testParseID()
	{
		try {
			Parser.parseId("0abc");
			fail("0abc should not parse");
		} catch(ParseException e) {;}
		try {
			Parser.parseId("_a");
			fail("_a should not parse");
		} catch(ParseException e) {;}
		assertEquals("abc", Parser.parseId("abc"));
	}

	@Test
	public void testParseInstruction()
	{
		try {
			Parser.parseInstruction("0abc");
			fail("0abc should not parse");
		} catch(ParseException e) {;}
		try {
			Parser.parseInstruction("_a");
			fail("_a should not parse");
		} catch(ParseException e) {;}
		Instruction i = Parser.parseInstruction("(ld r1 n)");
		assertEquals("(ld r1 n)", i.toString());
	}

	@Test
	public void testPatterns() {
		assertTrue(Parser.INSTRUCTION.matcher("( ld r1 n ").find());
	}

}
