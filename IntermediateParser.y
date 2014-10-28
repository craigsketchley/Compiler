-------------------------------------------------------------------------------
-- Happy parser generator template
-------------------------------------------------------------------------------

{

module IntermediateParser where

{-|
  Imports required by the lexer
-}
import Data.Char (isAlpha, isAlphaNum, isDigit, isSpace)

}

-- Some header information
%name intermediateLanguageParser
%tokentype { Token }
%error { parseError }

{-|
  Define the mapping of Happy token labels to Haskell data types
-}
%token
    lc          { TokenLc }
    ld          { TokenLd }
    st          { TokenSt }
    add         { TokenAdd }
    sub         { TokenSub }
    mul         { TokenMul }
    div         { TokenDiv }
    lt          { TokenLt }
    gt          { TokenGt }
    eq          { TokenEq }
    br          { TokenBr }
    ret         { TokenRet }
    call        { TokenCall }
    int         { TokenInt $$ }
    var         { TokenVar $$ }
    reg         { TokenReg $$ }
    '('         { TokenOB }
    ')'         { TokenCB }


{-
  this directive helps with error handling, see 2.5.1:
    http://www.haskell.org/happy/doc/html/sec-monads.html
-}
%monad { E } { thenE } { returnE }

-- Required symbol to instruct Happy that the rules follow
%%

Program     : '(' Functions ')'             { $2 }

Functions   : {- empty -}                   { [] }
            | Function Functions            { $1 : $2 }

Function    : '(' Id Arguments Blocks ')'   { Function $2 $3 $4 }

Arguments   : '(' IdList ')'                { $2 }

IdList      : {- empty -}                   { [] }
            | Id IdList                     { $1 : $2 }

Blocks      : '(' Num Instructions ')'        { [Block $2 $3] }
            | '(' Num Instructions ')' Blocks { (Block $2 $3) : $5 }

Instructions : Instruction                  { [$1] }
             | Instruction Instructions     { $1 : $2 }

Instruction : '(' lc   Reg Num ')'          { MemOp (LoadConst $3 $4) }
            | '(' ld   Reg Id  ')'          { MemOp (Load $3 $4) }
            | '(' st   Id  Reg ')'          { MemOp (Store $3 $4) }
            | '(' add  Reg Reg Reg ')'      { BinOp (Add $3 $4 $5) }
            | '(' sub  Reg Reg Reg ')'      { BinOp (Sub $3 $4 $5) }
            | '(' mul  Reg Reg Reg ')'      { BinOp (Mul $3 $4 $5) }
            | '(' div  Reg Reg Reg ')'      { BinOp (Div $3 $4 $5) }
            | '(' lt   Reg Reg Reg ')'      { BinOp (Lt $3 $4 $5) }
            | '(' gt   Reg Reg Reg ')'      { BinOp (Gt $3 $4 $5) }
            | '(' eq   Reg Reg Reg ')'      { BinOp (Eq $3 $4 $5) }
            | '(' br   Reg Num Num ')'      { Control (Br $3 $4 $5) }
            | '(' ret  Reg ')'              { Control (Ret $3) }
            | '(' call Reg Id RegList ')'   { Control (Call $3 $4 $5) }

RegList     : {- empty -}                   { [] }
            | Reg RegList                   { $1 : $2 }

Num         : int                           { $1 }
Reg         : reg                           { R $1 }
Id          : var                           { I $1 }
            {-an id might match a register label, so accept that too -}
            | reg                           { I $1 }
            {-an id might match an instruction label,
              so we accept those tokens here and sub
              back in the label itself -}
            | lc                            { I "lc" }
            | ld                            { I "ld" }
            | st                            { I "st" }
            | add                           { I "add" }
            | sub                           { I "sub" }
            | mul                           { I "mul" }
            | div                           { I "div" }
            | lt                            { I "lt" }
            | gt                            { I "gt" }
            | eq                            { I "eq" }
            | br                            { I "br" }
            | ret                           { I "ret" }
            | call                          { I "call" }


{

-------------------------------------------------------------------------------
-- Error handling.
-------------------------------------------------------------------------------

{-|
  Simple error handling, based off section 2.5.1 of the Happy documentation:
  http://www.haskell.org/happy/doc/html/sec-monads.html

  If a parsing error occurs, the error state is represented by an E(rror)
  object containing a list of the remaining (unscanned) tokens.
-}
parseError :: [Token] -> E a
parseError tokens = failE (show tokens)

{-|
  Data type for error state. Either Ok with the parse result, or Failed 
with some error information
-}
data E a
    = Ok a
    | Failed String
    deriving Show

thenE :: E a -> (a -> E b) -> E b
m `thenE` k = 
    case m of 
        Ok a -> k a
        Failed e -> Failed e

returnE :: a -> E a
returnE a = Ok a

failE :: String -> E a
failE err = Failed err

catchE :: E a -> (String -> E a) -> E a
catchE m k = 
    case m of
        Ok a -> Ok a
        Failed e -> k e

{-|
  Return true iff the parsing succeeded
-}
parseSuccess :: (E a) -> Bool
parseSuccess (Ok _) = True
parseSuccess _ = False

{-|
  Return the result of the parsing.
-}
parseResult :: (E Program) -> [Function]
parseResult (Ok flist) = flist -- success case, return the list of functions
parseResult _ = [] -- fail case, return an empty list


-------------------------------------------------------------------------------
-- Definitions of the data types used in the AST
-------------------------------------------------------------------------------

{-|
  Basic data types for names and values
-}

{- TODO: do we really need Id, Reg, Var? can't we have
data Var
    = Id String
    | Reg String
    either way, data type I and R is bad naming.
-}
data Id
    = I String
    deriving(Eq, Ord)

instance Show Id where
    show (I id) = "{Identifier : " ++ id ++ "}"

data Reg
    = R String
    deriving(Eq, Ord)

instance Show Reg where
    show (R reg) = "{Register : " ++ reg ++ "}"

data Var
    = VR Reg
    | VI Id
    deriving(Eq, Ord)

instance Show Var where
    show (VR reg) = show reg
    show (VI id) = show id


type Program = [Function]

data Function = Function Id Args [Block]
      deriving (Show)

type BlockId = Int

data Block = Block BlockId [Instruction]
      deriving (Show)

type Args = [Id]


{-|
  Instructions fall into three main types
-}
data Instruction
    = Control ControlType
    | BinOp BinOpType
    | MemOp MemOpType
    deriving (Show)

{-|
  Control flow operations
-}
data ControlType
    = Br        Reg BlockId BlockId
    | Ret       Reg
    | Call      Reg Id [Reg]
    deriving (Show)

{-|
  Binary operations
-}
data BinOpType
    = Add       Reg Reg Reg
    | Sub       Reg Reg Reg
    | Mul       Reg Reg Reg
    | Div       Reg Reg Reg
    | Lt        Reg Reg Reg
    | Gt        Reg Reg Reg
    | Eq        Reg Reg Reg
    deriving (Show)

{-|
  Memory access operations
-}
data MemOpType
    = LoadConst Reg Int
    | Load      Reg Id
    | Store     Id Reg
    deriving (Show)


{-|
  Data type of the tokens passed from the lexer to the parser
-}
data Token
      = TokenLc --opcodes
      | TokenLd
      | TokenSt
      | TokenAdd
      | TokenSub
      | TokenMul
      | TokenDiv
      | TokenLt
      | TokenGt
      | TokenEq
      | TokenBr
      | TokenRet
      | TokenCall
      | TokenInt Int --an integer
      | TokenVar String --an id
      | TokenReg String --possibly a register label
      | TokenOB --open parenthesis
      | TokenCB --close parenthesis
      deriving Show


{-|
  Lexer. Based off the example in the Happy documentation.
  Breaks a string into a series of tokens, ready to be consumed by the parser
-}
lexer :: String -> [Token]
lexer [] = []
lexer (c:cs) 
      | isSpace c = lexer cs --ignore whitespace
      | isAlpha c = lexVar (c:cs) --read a string
      | isDigit c = lexNum (c:cs) --read a (non-negative) number
lexer ('-':cs) = lexNum ('-':cs)
lexer ('(':cs) = TokenOB : lexer cs
lexer (')':cs) = TokenCB : lexer cs
lexer ('/':'*':cs) = lexer (lexSkipComment cs) --start of a comment block

{-|
  Read a sequence of digits from a string into a single number, then
  continue lexing the remainder of the string.
-} 
lexNum :: String -> [Token]
lexNum ('-':cs) = TokenInt (negate (read num)) : lexer rest
      where (num,rest) = span isDigit cs
lexNum cs = TokenInt (read num) : lexer rest
      where (num,rest) = span isDigit cs

{-|
  Read a sequence of alphanumerics from a string into a single word, then
  continue lexing the remainder of the string. If the word matches a token,
  return that token (and lex remainder of the string). Otherwise, it must be
  an ID, call lexId to read the alphanumerics required
-}
lexVar ::  String -> [Token]
lexVar cs =
   case span isAlphaNum cs of
      ("lc",rest)   -> TokenLc    : lexer rest
      ("ld",rest)   -> TokenLd    : lexer rest
      ("st",rest)   -> TokenSt    : lexer rest
      ("add",rest)  -> TokenAdd   : lexer rest
      ("sub",rest)  -> TokenSub   : lexer rest
      ("mul",rest)  -> TokenMul   : lexer rest
      ("div",rest)  -> TokenDiv   : lexer rest
      ("lt",rest)   -> TokenLt    : lexer rest
      ("gt",rest)   -> TokenGt    : lexer rest
      ("eq",rest)   -> TokenEq    : lexer rest
      ("br",rest)   -> TokenBr    : lexer rest
      ("ret",rest)  -> TokenRet   : lexer rest
      ("call",rest) -> TokenCall  : lexer rest
      (id,rest)     -> (lexRegOrVar id) : lexer rest

{-|
  Read a sequence of alphanumerics as an id or register label, then continue
  lexing the remaining string. Note that if the sequence matches a register,
  then TokenReg is created. It is the responsibility of the parser to decide
  if the token is actually an ID.
-}
lexRegOrVar :: String -> Token
lexRegOrVar a@(x:y:rest)
  | (x == 'r') && (all isDigit (y:rest)) && (y/='0')
    = TokenReg a
  | otherwise = TokenVar a
lexRegOrVar a = TokenVar a

{-|
  Consume characters from the string until the next close block comment
  marker has been consumed.
-}
lexSkipComment ::  String -> String
lexSkipComment ('*':'/':cs) = cs --comment closed, return the remainder
lexSkipComment (_:cs) = lexSkipComment cs --comment not yet closed, recurse

}
