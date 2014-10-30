package optimisation;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import IntermediateLanguage.Parser;
import IntermediateLanguage.Program;

public class DeadCodeEliminationOptimisationTest
{

	@Test
	public void test()
	{
		Program input = Parser.parse("input/assignmentSpecExample");

		Program expected = Parser.parse("expected/dCETest1");
		System.out.println(expected);
		Program output = Optimiser.optimise(input, new DeadCodeEliminationOptimisation());
		System.out.println(output);
		assertEquals(expected.toString(), output.toString());
	}
}
