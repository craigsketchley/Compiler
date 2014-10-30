package IntermediateLanguage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Parser
 * Provides static methods to support parsing of files into Programs
 */
public class Parser
{
	// A pattern matching a legal variable or function name
	public final static Pattern ID = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");

	//public final static Pattern NUM = Pattern.compile("-?[0-9]+"); just use nextInt
	
	// A pattern matching a legal register id
	public final static Pattern REG = Pattern.compile("r[1-9][0-9]*");
	
	// A partial pattern (string) of the instruction labels
	public final static String OP_STR =
			"(lc)|(ld)|(st)|(add)|(sub)|(mul)|(div)|(lt)|(gt)|(eq)|(br)|(ret)|(call)";
	
	// A pattern matching an instruction, *excluding* the trailing parenthesis!
	public final static Pattern INSTRUCTION = Pattern.compile(
			String.format("\\s*\\( (%s) [^)]+", OP_STR));

	/**
	 * ParseException
	 * Nested class representing a parse error (corresponding to a syntax error)
	 */
	public static class ParseException extends RuntimeException
	{
		public ParseException(String string)
		{
			super(string);
		}
		private static final long serialVersionUID = -8438735493626198339L;
	}
	
	/**
	 * Parser.parse
	 * Static method that accepts a file path and either returns a Program object,
	 * or null, or throws an exception.
	 */
	public static Program parse(String path)
	{
		//Read the file at path into a string
		String code = null;
		try
		{
			code = new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
		} catch (IOException e)
		{
			e.printStackTrace();
			return null; //Error reading file
		}
		
		//Pre-processing:
		//Add spaces between word breaks (i.e. between ( and labels etc.)
		code = code.replaceAll("\\b", " ");
		//Replace all blocks of whitespace with single spaces
		code = code.replaceAll("\\s+", " ");
		
		//Create a scanner, returning tokens separated by spaces
		Scanner scan = new Scanner(code);
		scan.useDelimiter(" ");
		
		//The program must start with a (
		scan.next("\\(");
		Program program = new Program();

		//For each (, parse in a function
		while(scan.hasNext("\\("))
		{
			program.functions.add(parseFunction(scan));
		}
		
		//The program must end with a )
		scan.next("\\)");
		if(scan.hasNext())
		{
			throw new ParseException("Data in file after end of Program");
		}
		
		//All done!
		return program;
	}
	
	/**
	 * Parser.parseFunction
	 * Given a scanner, parse in the next function
	 */
	public static Function parseFunction(Scanner scan)
	{
		
		//The function must start with a (
		scan.next("\\(");
		
		//There must be a function ID
		String id = scan.next(ID);
		
		//There must be a (arglist)
		scan.next("\\(");
		ArrayList<String> args = new ArrayList<String>();
		while(scan.hasNext(ID))
		{
			args.add(scan.next(ID));
		}
		scan.next("\\)");

		//Create an empty function with the header discovered so far
		Function function = new Function(id, args);
		
		//Parse all blocks, which must start with (
		while(scan.hasNext("\\("))
		{
			function.blocks.add(parseBlock(scan));
		}
		
		//The function must end with )
		scan.next("\\)");

		//All done!
		return function;
	}
	
	/**
	 * Parser.parseBlock
	 * Given a scanner, parse in the next block
	 */
	public static Block parseBlock(Scanner scan)
	{
		//The blocks must start with a (, then a number		
		scan.next("\\(");
		Block block = new Block(scan.nextInt());
		
		//Change the delimiter to ), so we can easily grab whole instructions
		scan.useDelimiter("\\)");
		
		//For each instruction
		while(scan.hasNext(INSTRUCTION))
		{
			//Scan the instruction using space separator
			scan.useDelimiter(" ");
			
			//The instruction must start with a (, then the type
			scan.next("\\(");
			String op = scan.next();
			
			//Build the appropriate instruction type
			Instruction instr;
			switch(op)
			{
			case "ret": instr = new RetInstruction(parseReg(scan)); break;
			case "ld": instr = new LdInstruction(parseReg(scan), parseId(scan)); break;
			case "lc": instr = new LcInstruction(parseReg(scan), scan.nextInt()); break;
			case "st": instr = new StInstruction(parseId(scan), parseReg(scan)); break;
			case "br": instr = new BrInstruction(parseReg(scan), scan.nextInt(), scan.nextInt()); break;
			case "call":
				Register dest = parseReg(scan);
				String id = scan.next(ID);
				//keep reading arguments until we reach the end of the statement )
				ArrayList<Register> args = new ArrayList<Register>();
				while(scan.hasNext(REG))
				{
					args.add(parseReg(scan));
				}
				instr = new CallInstruction(dest, id, args); break;
			default: instr = new BinOpInstruction(op, parseReg(scan), parseReg(scan), parseReg(scan));
			//default: throw new ParseException("Instruction type not recognised: " + op);
			}
			
			//Add the instruction to the list, then read the trailing )
			block.instructions.add(instr);
			scan.next("\\)");
			
			//Set the delimiter back to ) again, so we can check for another instruction
			scan.useDelimiter("\\)");
		}
		
		//Done reading instructions, set the delimiter back to space
		scan.useDelimiter(" ");
		
		//The block must end with a )
		scan.next("\\)");

		return block;
		
	}
	
	/**
	 * Parser.parseId
	 * Given a scanner, return an ID using the next token
	 */
	public static String parseId(Scanner scan)
	{
		if(scan.hasNext(ID))
		{
			return scan.next(ID);
		}
		throw new ParseException("Syntax error scanning ID");
	}

	/**
	 * Parser.parseId
	 * Given a scanner, return a Register using the next token
	 */
	public static Register parseReg(Scanner scan)
	{
		if(scan.hasNext(REG))
		{
			//Get the register string, then discard the leading r to get the int
			return new Register(Integer.parseInt(scan.next(REG).substring(1)));
		}
		throw new ParseException("Syntax error scanning REG");
	}

}
