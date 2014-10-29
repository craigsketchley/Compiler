module Main where

import IntermediateParser
import System.Exit 
import System.Environment (getArgs)

{- 
 - |Receives an input file with intermediate code
 - outputs equivalent, optimised, code
 -}
main = do

    -- Check the correct number of arguments were provided
    args <- getArgs
    if not ((length args) == 2)
        then do
            putStrLn "Error: Incorrect number of arguments."
            putStrLn "Usage: $ ./Optimiser sourcefile outputfile"
            exitFailure
        else
            return ()

    -- Read the arguments into intput/output file variables
    [input_file, output_file] <- getArgs

    -- Apply the lexer and parser to the contents of the input file
    parse_result <- fmap (intermediateLanguageParser . lexer) (readFile input_file)

    -- Check if it parsed successfully
    if (not $ parseSuccess parse_result)
        then do
            -- Parse failure. Print error and exit.
            --print parse_result
            putStrLn "Syntax Error."
            exitFailure
        else do
            return () -- Do nothing

    -- Get the AST (just the list of functions) from the parser
    -- TODO: note this is a hacky way to strip the Program constructor out...
    let input_ast = parseResult parse_result

    putStrLn "Optimising..."
    writeFile output_file (show $ input_ast)
    putStrLn (show $ input_ast) --TODO: remove
    -- writeFile output_file (show $ optimise input_ast)
    putStrLn "Done!"
