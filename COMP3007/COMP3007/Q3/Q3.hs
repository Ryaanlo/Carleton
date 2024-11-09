module Q3 where
import Q3Base

{-
THIS QUIZ IS OUT OF 25 AND HAS TWO PARTS.

You can use any Haskell libraries or coding style you like, and even
reorganize the code in this file (probably not a good idea), as long
as you don't touch Q3Base.hs or change any of the provided function
names or their types.
-}

{-
PART 1. [15] EVALUATING EXPRESSIONS

These questions have you extend a simplified version of the evaluator
from Assignment 4 with several new expression coNstructors.  The Exp
data type, the parser, pretty printer and subst1 have already been
updated.  You just need to add a few clauses to eval (called evalExp
in the assignment).  

The comments with the data type in Q3Base describe how the new
constructs evaluate. There are some test cases below the definition of
eval.

Your solution doesn't have to work completely, but it *must* load into
ghci.  If it doesn't, it *will* be graded, but the grade will be ZERO.

Q1. [5] Extend the evaluator for Square.

Q2. [5] Extend the evaluator for Plus.

Q3. [5] Extend the evaluator for Add.

-}

ev :: String -> Exp
ev = eval . parse

-- subst1 e x e' substitutes the e' for x in e.  Assumes e is closed.
subst1 :: Exp -> Id -> Exp -> Exp
subst1 (Var y) x e = if x==y then e else Var y
subst1 (Plus e1 e2) x e = Plus (subst1 e1 x e) (subst1 e2 x e)
subst1 (Ifz e1 e2 e3) x e = Ifz (subst1 e1 x e) (subst1 e2 x e) (subst1 e3 x e) 
subst1 (Lam y e') x e = if x==y then Lam y e' else Lam y (subst1 e' x e)
subst1 (App e1 e2) x e = App (subst1 e1 x e) (subst1 e2 x e)
subst1 e _ _ = e

--- Evaluate a closed term using substitution.
eval :: Exp -> Exp

eval e@(Var (Id x)) = 
    error $ "eval: can't eval free variable: " ++ show e

eval (Const n) = Const n

eval Pred = Pred

eval Succ = Succ

eval Square = Square

eval Add = Add

eval (Plus e e') =
  Plus (eval e) (eval e')

eval (Ifz e1 e2 e3) = evalIfz (eval e1) e2 e3

eval e@(Lam _ _) = e

eval (App e e') = evalApp (eval e) e'

evalIfz :: Exp -> Exp -> Exp -> Exp
evalIfz (Const 0) e1 e2 = eval e1
evalIfz (Const n) e1 e2 = eval e2
evalIfz v e1 e2 = 
    error $ "eval: expected number in ifz, got " ++ (show v)

evalApp :: Exp -> Exp -> Exp
evalApp (Lam x e) e' = 
    eval (subst1 e x e')
evalApp Pred e = evalPrimitive Pred (eval e)
evalApp Succ e = evalPrimitive Succ (eval e)
evalApp Square e =  evalPrimitive Square (eval e)
evalApp Add e = evalPrimitive Add (eval e)
evalApp (App Add v) e =
  evalPrimitive Add (evalApp Add e)
  
evalApp e _ = 
    error $ "evalApp: cannot apply value as function: " ++ show e

evalPrimitive :: Exp -> Exp -> Exp
evalPrimitive Pred (Const n) = Const (max 0 $ n-1)
evalPrimitive Succ (Const n) = Const (n+1)
evalPrimitive Square (Const n) = Const (n^2)
evalPrimitive Add v = parse "%m -> %n -> ifz(m, n, succ (Add (pred m) n))"
evalPrimitive f arg = 
    error $ "evalPrimitive got: " ++ show f ++ " applied to " ++ show arg

-- all the followig should be true

t1 = "(%f -> f 3) square"
evt1 = Const 9

t2 = "plus(3,4)"
evt2 = Const 7

t3 = "add 3 4"
evt3 = Const 7

testSuccesses =
  map
  (\(t,res) -> ev t == res)
  [(t1,evt1), (t2,evt2), (t3,evt3)]

------------------------------------------------------------------

{-
PART 2. [10] HANDLING STATE IN HASKELL.

The questions are about a simplification of Assigment 5.  The
definition of the type S of states is now just [String], i.e. the
state is a list of strings.  The definition of (ST a), the type of
state transformers, is the same as in the assignment. See Q3Base.hs.

The state is to be thought of as the output collected from running a
program.  The program does whatever it wants, but working over ST a.
This lets it generate lines of "output" as it goes, and collection of
the output is done invisibly via the state transformers.

See Q3Base.hs, Part 2, for the type definitions and a couple of
functions you will need.  You can use "runWithIO :: Show a => ST a ->
IO ()" for testing. 

Q4. [5] Complete outputLines below.  Hint: you can use higher-order
stuff like mapM_ if you like, but it's very little extra work to just
use the usual pattern-matching recursion approach, except you'll need
to use "do" in the function bodies.

Q5. [5] Complete deleteIf below.  Hint: see Q6.  Also, recall that you
can use the function "show" to turn an object into a string.
-}

-- output the given lines
outputLines :: [String] -> ST()
outputLines [] = outputLine []
outputLines x = do
                outputLine (head x)
                outputLines (tail x)

{- outputLines example
λ> runWithIO (outputLines (words "foo bar bazola"))
()
foo
bar
bazola
-}

-- delete list elements satisfying p, printing them as well  
deleteIf :: Show a => (a -> Bool) -> [a] -> ST [a]
deleteIf = undefined 
-- deleteIf x [] = outputLines []
-- deleteIf x y = if x (head y) then deleteIf x (tail y) else deleteIf x (tail y)


{- deleteFi example
λ> runWithIO $ deleteIf (>2) [1,2,3,1,17]
[1,2,1]
3
17
-}



