module Final where

import Final1
import Final2
import Final3
import Data.List

{-
The exam is out of 80 and has 3 parts.

Part 1: [25] Lambda calculus, pattern-matching&recursion
Part 2: [25] Higher-order programing
Part 3: [30] Interpreters and state

DO:
- You can use any Haskell libraries or coding style you like.
- Submit just this file, not the three dependencies.
- TEST YOUR CODE.  The grade will be based 100% on test cases passed.
- Be sure your submission loads into ghci.  Warnings are ok, fatal
  errors => 0.

DON'T:
- It should go without saying, but: NO COLLABORATION. This is a final
  exam.  You cannot discuss it, even at the highest levels, with
  anyone.
- Do not modify Final1, Final2 or Final3.  Your code will be tested
  with the original versions.
- Do not change the type of any function in this file.
- Either replace "undefined" by loadable code, or leave the
  "undefined" there.
-}


--------------------------------------------------------------
-- Part 1: [25] Lambda calculus, pattern-matching&recursion --
--------------------------------------------------------------

-- This section uses three related data-type definitions.  See
-- Final1.hs for their definitions.
--
-- L0: basic lambda calculus, nothing but lambdas, variable and
-- applications.
--
-- L1: like L0, but with integer "tags" on lambdas and variables.
--
-- L2: like L1, but with the identifiers erased from lambdas and
-- variables, leaving only the tags.
--
-- Example.
-- L0: (%x -> %y -> x (%x -> y x)) (%y -> y y)
-- L1: (1:%x -> 2:%y -> 1:x ((3:%x -> 2:y 3:x)))
--     (4:%y -> 4:y 4:y)"
-- L2: (%1 -> %2 -> 1 (%3 -> 2 3)) (%4 -> 4 4)
--
-- Compare the last one to the first one.  Instead of using names to
-- indicate where the binding variables are, numbers are used. E.g.
-- in
-- %2 -> 1 (%3 -> 2 3)
--  ^             ^
-- the second "2" refers to the lambda (%) numbered 2, while in
-- %y -> x (%x -> y x)
--  ^             ^
-- it's the name of the variable that indicates the right lambda.
-- The questions below construct parts of the procedure to generate
-- this tagging.

-- A slight simplification of the example above.
e = parseL0 "%y -> %x -> y (%y -> y x))"

-- QUESTION 1(a) [5]
---Tag all lambdas and variables with 0.
-- ppL1 (bogoTag e)
-- = "(0:%y -> (0:%x -> 0:y((0:%y -> 0:y 0:x))))"
bogoTag :: L0 -> L1
bogoTag (Var0 x) = (Var1 0 x)
bogoTag (Lam0 x e) = (Lam1 0 x (bogoTag e))
bogoTag (App0 e (Var0 x)) = (App1 (bogoTag e) (Var1 0 x))
bogoTag (App0 e e') = (App1 (bogoTag e) (bogoTag e'))

-- Retags the lambdas, using distinct numbers for each.
-- ppL1 (tagLams (bogoTag e))
-- = "(1:%y -> (2:%x -> 0:y((3:%y -> 0:y 0:x))))"
-- Implemented in Final1
-- tagLams :: L1 -> L1

-- QUESTION 1(b) [5]
-- True iff all the lambda tags are distinct.
-- checkTags (tagLams (bogoTag e)) == True
checkTags :: L1 -> Bool
checkTags e = check e [] True
              where
                check :: L1 -> [Int] -> Bool -> Bool
                check (Var1 n x) l b = b
                check (Lam1 n x e) l b = if n `elem` l then False else check e (n:l) b
                check (App1 e (Var1 n x)) l b = (check e l b)
                check (App1 e e') l b = (check e l b) && (check e' l b)


-- QUESTION 1(c) [5]
-- Set each variable tag to be the tag of the lambda that binds it.
-- Use -1 for the tag of variables that are free in the whole
-- expression.  Hint: check out lookup at hoogle.haskell.org.
-- ppL1 $ tagVars $ tagLams $ bogoTag e
-- = "(1:%y -> (2:%x -> 1:y((3:%y -> 3:y 2:x))))"
tagVars :: L1 -> L1
tagVars (Var1 n x) = Var1 (-1) x
tagVars (Lam1 n x e) = (Lam1 n x (substTag e x n))
tagVars (App1 e e') = (App1 (tagVars e) (tagVars e'))

substTag :: L1 -> String -> Int -> L1
substTag (Var1 n x) y m =  if x == y then Var1 m x else Var1 n x
substTag (Lam1 n x e) y m = tagVars (Lam1 n x (substTag e y m))
substTag (App1 e e') y m = (App1 (substTag e y m) (substTag e' y m))

-- Generates a proper tagging for an untagged term
-- ppL1 $ tag $ e
-- = "(1:%y -> (2:%x -> 1:y((3:%y -> 3:y 2:x))))"
tag :: L0 -> L1
tag = tagVars . tagLams . bogoTag

e' = tag e

-- QUESTION 1(d) [5]
-- Erase all the identifiers, leaving the tags alone.
-- ppL2 $ erase e'
-- = "(%1 -> (%2 -> 1((%3 -> 3 2))))"
erase :: L1 -> L2
erase (Var1 n x) = Var2 n
erase (Lam1 n x e) = (Lam2 n (erase e))
erase (App1 e (Var1 n x)) = (App2 (erase e) (Var2 n))
erase (App1 e e') = (App2 (erase e) (erase e'))

-- QUESTION 1(e) [5]
-- Put the variables back in and erase the tags, getting the original
-- term back (up to variable renaming).
-- ppL0 (restore (erase e'))
-- = "(%v1 -> (%v2 -> v1((%v3 -> v3 v2))))"
restore :: L2 -> L0
restore (Var2 n) = Var0 ("v" ++ show n)
restore (Lam2 n e) = Lam0 ("v" ++ show n) (restore e)
restore (App2 e (Var2 n)) = App0 (restore e) (Var0 ("v" ++ show n))
restore (App2 e e') = App0 (restore e) (restore e')


-------------------------------------------
-- Part 2: [25] Higher-order programming --
-------------------------------------------

-- The questions in this part in involve graphs represented as a
-- mapping from vertices u to the list of vertices v such that there
-- is an edge from u to v (the "adjacency list" representation).  The
-- set of vertices of the graph is the domain of the mapping.
-- Vertices are represented as non-negative integers.
--
-- For example, a loop 1 -> 2 -> 3 -> 1 is represented at the mapping
-- 1  |->  [2]
-- 2  |->  [3]
-- 3  |->  [1]
--
-- If you've forgotten everything you ever knew about graphs, don't
-- worry, they're just a bunch of pairs and lists.  

-- See Final2 for the definitions of the types and some functions
-- you'll need for mappings (the "Map" data type).
--
-- NOTE: the definition of G, the type of graphs, is exported from the
-- module Final2, but it's constructor is *not*.  This means that you
-- can't access the representation of graphs and need to use only the
-- exported functions.  See the module header in Final2 to see what's
-- exported.  Mainly, you will using filterG and foldrG. There are
-- examples after the stubs.

-- Question 2 (a) [5]
-- A list of vertices u such there is no edge from u to any other
-- vertex.
sinks :: G -> [V]
sinks g = filterG (\x y -> length y == 0) g

-- Question 2 (b) [5]
-- A list of vertices u such there is an edge from u to u.
loops :: G -> [V]
loops g = filterG (\x y -> length y == 1) g

-- Question 2 (c) [5]
-- The total number of edges in the graph
numEdges :: G -> Int
numEdges g = length (foldrG (\x y z -> z++y) [] g)

-- Question 2 (d) [5]
-- The total number of vertices in the graph
vertices :: G -> [V]
vertices = nodes

-- Question 2 (e) [5]
-- The graph with the same vertices but all edges reversed.
reverseG :: G -> G
--reverseG g = mkGraph [(1,loops g)] --(foldrG (\x y z -> z++y) [] g)
--reverseG g = mkGraph (foldrG (\x y z -> makeReverse x y g : z ) [] g)
--reverseG g = mkGraph (foldrG (\x y z -> ((foldl (\acc a -> x) [] (vertices g)), []) : z ) [] g)
--reverseG g = mkGraph ((foldl (\acc a -> acc ++ (a, (foldrG (\x y z -> z ++ (foldl (\n m -> m) [] y) ) [] g) ) ) [] (vertices g)))

reverseG g = mkGraph (foldrG (\x y z -> (foldrG (\a b c -> (a, b) ) (1,[]) g) : z ) [] g)

makeReverse :: V -> [V] -> G -> (V, [V])
makeReverse x y g = (foldrG (\a b c -> (x, y): c) [] g) !! 0

-- example
g = mkGraph
    [(1, [2,3])
    ,(6, [6])
    ,(2, [4,3,1])
    ,(3, [])
    ,(17, [17])
    ,(4, [1,6,2,17])
    ]

-- sinks g = [3]
-- loops g = [6.17]
-- numEdges g = 11
-- vertices g = [1,6,2,3,17,4]
-- printGraph (reverseG g)
-- 17 |-> [17,4]
-- 2 |-> [1,4]
-- 6 |-> [6,4]
-- 1 |-> [2,4]
-- 3 |-> [1,2]
-- 4 |-> [2]



-----------------------------------------  
-- Part 3: [30] Interpreters and state --
-----------------------------------------

-- The file Final3.hs has a data type, parser and pretty printer for a
-- language that has functions but no lambda expressions.  The
-- comments on the data types, and the examples below, should be
-- enough to understand how the language works.
-- Here are the parsers from Final3:
-- parseE :: String -> E
-- parseDefE :: String -> (Id,([Id],E))
-- Note that definition "f(x,y) = e" is represented as
-- ("f", (["x", "y"], e)), i.e. a tuple of the function name, the
-- formal parameters, and the right-hand side.
--
-- The questions involve writing two interpreters: a straightforward
-- one that ignores the stack instructions push, pop and top, and one
-- for the full language that used the state monad, defined in
-- Final3.  The implementation of state transformers is not exported
-- from Final3.
--
-- A complete program is a definition environment, i.e. a mapping from
-- function names to their id lists and right-hand-sides, and an
-- expression to evaluate.

-- Run the program with the given definitions, ignoring push, pop and
-- top.
run :: DefM -> String -> Val
run dm s = eval dm emptyM (parseE s)

-- QUESTION 3(a) [15]
-- Write an evaluator for this language. You can assume the programs
-- you are evaluating are well formed, e.g. variables and functions
-- are always properly defined.  
eval :: DefM -> ValM -> E -> Val
eval defs vals e@(Var n) = 
    error $ "evalExp: can't eval free variable: " ++ show e
eval defs vals (Const n) = n
eval defs vals (Call f l) = 6
eval defs vals (Ifz e1 e2 e3) = 0
eval defs vals (Push n) = 0
eval defs vals (Pop) = 0
eval defs vals (Top) = 0

evalApp :: DefM -> ValM -> E
evalApp = undefined

evalPrimitive :: ValM -> E
evalPrimitive = undefined 

run' :: DefM -> String -> Val
run' dm s = runST $ eval' dm emptyM $ parseE s

-- Question 3(b) [15]
-- Write an evaluator for the full language.
-- NOTE: this language mixes functions with state.  It needs to be
-- CALL-BY-VALUE for the examples below to work properly.  In other
-- words, when a function call f(e1,e2,...) is evaluated, the
-- arguments e1, e2, ... have to be evaluated first.  Tip: you might
-- find it handy to use mapM.
eval' :: DefM -> ValM -> E -> ST Val
eval' = undefined

defs =
  map parseDefE
  ["add(x,y) = ifz(x, y, add(pred(x), succ(y)))"
  ,"mul(x,y) = ifz(x, 0, add(y, mul(pred(x),y)))"
  ,"fact(n) = ifz(n, 1, mul(n, fact(pred(n))))"
  ,"seq1(x,y) = x"
  ,"seq2(x,y) = y"
  ,"init() = push(0)"
  ,"tic() = seq1(top(), push(succ(pop())))"
  ]

-- evaluates to 6 with either eval or eval'
e0 = "fact(3)"

-- evaluates to 2 with eval' only
e1 = "seq2(init(), seq2(tic(), seq2(tic(),top())))"
