import Data.Graph as G
import Data.Map as M
import Data.List as L

{- for AST data structures -}
import IntermediateParser

{- a cfg is a graph and a map -}
type ControlFlowGraph = G.Graph M.Map

data Node 
  = Node Statement
  deriving Show

type NodeMap = M.Map G.Vertex Node

{- Raf and Dylan say Ben said we can optimise each function seperately
  therefore, the cfg only represents a list of statements, not functions
-}

buildCfg :: Function -> ControlFlowGraph
buildCfg (Function id args blocks) = 
  where statements = L.map Node (map concat blocks)
        edges = ???
        {- need to use the interpreter? -}











[s1,s2,s3,s4,s5,s6] = ["y = 5", "x = 3", "z = x + y", "x = x + y", "x = x + 1", "goto L7"] 
ss = L.map Main.Node [s1,s2,s3,s4,s5,s6] 

[n1,n2,n3,n4,n5,n6] = [1..6]::[Int]
ns = [n1,n2,n3,n4,n5,n6] 

e = [(n1,n2), (n2,n3), (n2, n4), (n4, n5), (n3, n6), (n5, n6)]
g = buildG (1,6) e

nmap = M.fromList (zip ns ss)

