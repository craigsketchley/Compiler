import Data.Graph as G
import Data.Map as M
import Data.List as L

type NodeMap = M.Map G.Vertex Node
type Statement = String

data Node 
    = Node Statement
    deriving Show

[s1,s2,s3,s4,s5,s6] = ["y = 5", "x = 3", "z = x + y", "x = x + y", "x = x + 1", "goto L7"] 
ss = L.map Main.Node [s1,s2,s3,s4,s5,s6] 

[n1,n2,n3,n4,n5,n6] = [1..6]::[Int]
ns = [n1,n2,n3,n4,n5,n6] 

e = [(n1,n2), (n2,n3), (n2, n4), (n4, n5), (n3, n6), (n5, n6)]
g = buildG (1,6) e

nmap = M.fromList (zip ns ss)



-- type Label = String

-- data Program 
--     = Empty
--     | Node [Program]

-- data 


-- interpret :: Statement ->  


-- type GenFunction = Node -> Set -> Set
-- type KillFunction = Node -> 

-- out :: GenFunction -> KillFunction -> Node -> [Label]

-- in' :: Node -> [Label]
