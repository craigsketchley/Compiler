package intermediateLanguage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Provides static methods to support parsing of files into Programs
 * 
 * This is a simple implementation of a recursive descent parser.
 * Note that this parser was written for Assignment 3.
 * It is assumed that the input will be well formed (since testing
 * was in the scope of Assignment 2.)
 */
public class Parser
{
	/**
	 * A pattern matching a legal variable or function name
	 */
	public final static Pattern ID = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");

	/**
	 * A pattern matching a legal register id
	 */
	public final static Pattern REG = Pattern.compile("r[1-9][0-9]*");
	
	/**
	 * A partial pattern (string) of the instruction labels
	 */
	public final static String OP_STR =
			"(lc)|(ld)|(st)|(add)|(sub)|(mul)|(div)|(lt)|(gt)|(eq)|(br)|(ret)|(call)";
	
	/**
	 * A pattern matching an instruction, *excluding* the trailing parenthesis!
	 */
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
	 * Preprocess a string of code, then produce a space delimited scanner
	 * @param code a string of code
	 * @return Scanner (space delimited)
	 */
	public static Scanner buildScanner(String code)
	{
		//Pre-processing:
		//Strip comments
		code = code.replaceAll("//[^\n]*", "");
		//Add whitespace around brackets (i.e. between ( and labels etc.)
		code = code.replaceAll("\\(", " ( ");
		code = code.replaceAll("\\)", " ) ");
		//Replace all blocks of whitespace with single spaces
		code = code.replaceAll("\\s+", " ");

		//Create a scanner, returning tokens separated by spaces
		Scanner scan = new Scanner(code);
		scan.useDelimiter(" ");
		return scan;
	}
	
	/**
	 * Read the file at path into a string
	 * @param path to the file to read
	 * @return contents of the file
	 */
	public static String path2string(String path)
	{
		try
		{
			return new String(
					Files.readAllBytes(Paths.get(path)),
					Charset.defaultCharset());
		} catch (IOException e)
		{
			throw new ParseException("File could not be read: " + path);
		}
	}
	
	/**
	 * Static method that accepts a file path and either returns a Program object,
	 * or null, or throws an exception.
	 * @param path the path to a file
	 * @return a Program object
	 */
	public static Program parse(String path)
	{
		return parseString(path2string(path));
	}
	
	/**
	 * Static method that accepts a string of code and returns a Program object,
	 * or null, or throws an exception.
	 * @param code intermediate code, as a string
	 * @return a Program object
	 */
	public static Program parseString(String code)
	{
		try
		{
			Scanner scan = buildScanner(code);
			
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

		} catch(InputMismatchException e) { ;
		} catch(NoSuchElementException e) { ;
		}
		throw new ParseException("Syntax error scanning Program");
	}
	
	/**
	 * Alternative entry point into parseFunction(Scanner)
	 * It just generates a scanner based on the given string of code
	 * @param code a String of code starting with a function 
	 * @return a Function object
	 */
	public static Function parseFunction(String code)
	{
		return parseFunction(buildScanner(code));
	}
	
	/**
	 * Given a scanner, parse in the next function
	 * @param scan a Scanner containing code
	 * @return a Function object
	 */
	public static Function parseFunction(Scanner scan)
	{
		try
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
			
		} catch(InputMismatchException e) { ;
		} catch(NoSuchElementException e) { ;
		}
		throw new ParseException("Syntax error scanning Function");
	}
	
	/**
	 * Alternative entry point into parseBlock(Scanner)
	 * It just generates a scanner based on the given string of code
	 * @param code a String of code starting with a block
	 * @return a Block object
	 */
	public static Block parseBlock(String code)
	{
		return parseBlock(buildScanner(code));
	}
	
	/**
	 * Given a scanner, parse in the next block
	 * @param scan a Scanner containing code
	 * @return a Block object
	 */
	public static Block parseBlock(Scanner scan)
	{
		try
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
				block.instructions.add(parseInstruction(scan));
				
				//Set the delimiter back to ) again, so we can check for another instruction
				scan.useDelimiter("\\)");
			}
			
			//Done reading instructions, set the delimiter back to space
			scan.useDelimiter(" ");
			
			//The block must end with a )
			scan.next("\\)");
	
			return block;

		} catch(InputMismatchException e) { ;
		} catch(NoSuchElementException e) { ;
		}
		throw new ParseException("Syntax error scanning Block");
	}
	
	/**
	 * Alternative entry point into parseInstruction(Scanner)
	 * It just generates a scanner based on the given string of code
	 * @param code a String of code starting with an instruction 
	 * @return an Instruction object
	 */
	public static Instruction parseInstruction(String code)
	{
		return parseInstruction(buildScanner(code));
	}
	
	/**
	 * Given a scanner, parse in the next Instruction
	 * @param scan a Scanner containing code
	 * @return an Instruction object
	 */
	public static Instruction parseInstruction(Scanner scan)
	{
		try
		{
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
			
			//then read the trailing ), then return the instruction
			scan.next("\\)");
			return instr;

		} catch(InputMismatchException e) { ;
		} catch(NoSuchElementException e) { ;
		}
		throw new ParseException("Syntax error scanning Instruction");
	}

	/**
	 * Alternative entry point into parseId(Scanner)
	 * It just generates a scanner based on the given string of code
	 * @param code a String of code starting with an ID
	 * @return an ID object (String)
	 */
	public static String parseId(String code)
	{
		return parseId(buildScanner(code));
	}

	/**
	 * Given a scanner, return an ID using the next token
	 * @param scan
	 * @return an ID from the scanner
	 */
	public static String parseId(Scanner scan)
	{
		try
		{
			if(scan.hasNext(ID))
			{
				return scan.next(ID);
			}
		} catch(InputMismatchException e) { ;
		} catch(NoSuchElementException e) { ;
		}
		throw new ParseException("Syntax error scanning ID");
	}

	/**
	 * Alternative entry point into parseReg(Scanner)
	 * It just generates a scanner based on the given string of code
	 * @param code a String of code starting with a Register
	 * @return a Register object
	 */
	public static Register parseReg(String code)
	{
		return parseReg(buildScanner(code));
	}

	/**
	 * Given a scanner, return a register using the next token
	 * @param scan
	 * @return a register from the scanner
	 */
	public static Register parseReg(Scanner scan)
	{
		try
		{
			if(scan.hasNext(REG))
			{
				//Get the register string, then discard the leading r to get the int
				return new Register(Integer.parseInt(scan.next(REG).substring(1)));
			}
		} catch(InputMismatchException e) { ;
		} catch(NoSuchElementException e) { ;
		}
		throw new ParseException("Syntax error scanning REG");
	}
}
