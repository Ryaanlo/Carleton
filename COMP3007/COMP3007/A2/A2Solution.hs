-- COMP3007 Assignment 2
-- Ryan Lo
-- 101117765
{- **Assignment 2** -}

{- **Important Background**

This assignment assumes you know the following from the first assignment's Haskell part:
- how to define recursive functions
- the basic syntax of function calls
- the basics of "currying", i.e. viewing multi-argument functions as nested single argument functions, e.g. \x -> \y -> \z -> x+y+z can be viewed either as a three-argument function returning a number, or as a one-argument function returning a one-argument function...
- the "->" type constructor, as in Int -> Int

Also assumed is the lecture material from Wed Oct 12.  Everything else new will be introduced below, usually as a comment by the first use.

Some general new things.
- Whitespace is important (you might have run into this already).  The usual kind of indenting you do in most programming languages is usually fine in Haskell.
- An underscore "_" can be used in a pattern.  It matches anything but doesn't give a name to it and so you can't use the matched value on the right-hand side of the pattern.
- When a function is defined by pattern matching the clauses are tried in the order they are written.
- Haskell will complain if a definition by pattern-matching doesn't cover all the cases.  You can often fix this with a "catch-all" using "_".  For example, if f is a one-argument function defined by pattern-matching, you could put "f _ = ..." as the last clause and it would match anything the preceeding clauses didn't  match. 
- You will see "deriving Show", or "deriving (Eq, Show)" after data type definitions below.  This will cause Haskell to automatically generate implementations of the functions "show" and "==" for the type. The "show" function makes a readable string out of a member of the type, and members can be compared for equality using "==".
- Some types have "class" preconditions on the type variables.  We will look at type classes later in the term, but for now you just need to understand typings like "f :: Eq a => a -> a".  This means that for any type "a" that has "==" defined for it, f has type "a -> a".
- You *don't* need to use "guards" for definitions by pattern matching.  You can ignore it if Haskell suggests rewriting your code to use them.
- You don't need to use *any* functions/constructors that are predefined in Haskell except for the following: 
    - [] and infix : are the constructors for the lists, e.g. [1,2,3] is actually 1:2:3:[]
    - ++ appends two lists (infix, so write eg l ++ l')
    - max returns the maximum of two numbers
-}

{- **The Assignment**

In this assignment you will write some functions for expressions in high-school algebra.  The expressions are made from integers, variables, +, *, - and ^, where ^ is for exponentiation (so e.g. x^2 is x squared).

Example: "3*x^2 - 5*x + 2".  A function ppExp is supplied to "pretty-print" members of the expression data type (defined below).

You are to replace the "undefined" parts below by working code.  Part marks will be give only for code that works in most cases.  There are 10 of these, each worth 10 points.  There is also a bonus question worth 20 points.  The maximum score on the assignment is 100%.

**IMPORTANT SUBMISSION REQUIRMEENTS**

- Due **Friday Oct 22 11:50pm**.  This is an extension of 2 days.
- Submit your solutions as a single Haskell file.  
- Files that don't load into ghci without an error will receive ZERO. You will need to delete or comment out any parts that don't load before submitting the final copy. 
- Do not change any of the given function names or their types.  If you do, they will be treated as not present.
- Some supplemental test data may be provided in the next day or two. Check Ed regularly.  Your code will be expected to work on all the test data, and will be taken to be correct if it does.
-}

----------------------------------------

{- 
The data type of arithmetic expressions.  Constants, variables, negation, addition, multiplication and exponentiation ("Pow" for "power").  Exponents must be non-negative integers. Subtraction is omitted for simplicity since x-y = x + (-y).
-}
data Exp = 
    Const Int | Var [Char]| Neg Exp | Add Exp Exp |   -- [Char] is strings
    Mul Exp Exp | Pow Exp Int 
    deriving (Show, Eq) -- Eq generates == testing for Exp

{- 
In this definition and the ones that follow we will include sample output by using ">>>" to start a line of code to be evaluated.  The result of evaluating it is on the line after.
>>> exampleExp
Add (Add (Mul (Const 3) (Pow (Var "x") 2)) (Neg (Mul (Const 5) (Var "x")))) (Const 2)
-}
exampleExp :: Exp
exampleExp = 
    Add (Add 
            (Mul (Const 3) (Pow (Var "x") 2))
            (Neg (Mul (Const 5) (Var "x"))))
        (Const 2)

{- 
>>> parenthesise "Bjarne the Omniscient"
"(Bjarne the Omniscient)"
-}
parenthesise :: [Char] -> [Char]
parenthesise s = "(" ++ s ++ ")"

{-
A "pretty printer" for your convenience. It makes expressions easier to read than the default "show" method.  
>>> exampleExp
Add (Add (Mul (Const 3) (Pow (Var "x") 2)) (Neg (Mul (Const 5) (Var "x")))) (Const 2)
>>> show exampleExp
"Add (Add (Mul (Const 3) (Pow (Var \"x\") 2)) (Neg (Mul (Const 5) (Var \"x\")))) (Const 2)"
>>> ppExp exampleExp
"(((3 * x^2) + (-(5 * x))) + 2)"
-}
ppExp :: Exp -> String
ppExp (Const n) = 
    show n
ppExp (Var x) = 
    x
ppExp (Neg e1) = 
    parenthesise $ "-" ++ ppExp e1
ppExp (Add e1 e2) =  
    parenthesise $ ppExp e1 ++ " + " ++ ppExp e2
ppExp (Mul e1 e2) =  
    parenthesise $ ppExp e1 ++ " * " ++ ppExp e2
ppExp (Pow e1 n) =  
    ppExp e1 ++ "^" ++ show n


{- Question 1.
The number of variable occurrences in an expression.
>>> varOccCount exampleExp
2
>>> varOccCount (Add (Var "y") (Add (Var "y") exampleExp))
4
-}
varOccCount :: Exp -> Int
varOccCount (Const n) = 0
varOccCount (Var x) = 1
varOccCount (Neg x) = varOccCount x
varOccCount (Add e1 e2) = varOccCount e1 + varOccCount e2
varOccCount (Mul e1 e2) = varOccCount e1 + varOccCount e2
varOccCount (Pow e1 n) = varOccCount e1


{- Question 2.
(removeDuplicates l) is a list with the same set of elements as l but with no duplicates.
>>> removeDuplicates [1,2,3,2,4,3,3]
[1,2,3,4]
-}
removeDuplicates :: Eq a => [a] -> [a]
removeDuplicates = \x -> remove x []
                        where 
                            remove :: (Eq a) => [a] -> [a] -> [a]
                            remove [] _ = []
                            remove (x:xs) found
                                | elem x found = remove xs found
                                | otherwise = x : remove xs (x:found)

{- Question 3.
(remove x l) is l with all occurrences of x removed
>>> remove 4 [1,2,5,4,3,4]
[1,2,5,3]
-}
remove :: Eq a => a -> [a] -> [a]
remove = \x l -> if elem x l == False then l 
                    else remove x l []
                        where 
                            remove :: (Eq a) => a ->[a] -> [a] -> [a]
                            remove x [] _ = []
                            remove x (y:ys) found
                                | (x == y) = remove x ys found
                                | otherwise = y : remove x ys (y:found)


{- Question 4.
(vars e) is a list of the variables of e with duplicates removed
>>> vars exampleExp
["x"]
>>> vars (Add (Var "x") (Add (Var "y") (Var "x")))
["x","y"]
-}
vars :: Exp -> [String]
vars = \x -> removeDuplicates (remove x [])
                where
                    remove :: Exp -> [String] -> [String]
                    remove (Var x) y = x:y
                    remove (Const n) y = []
                    remove (Neg x) y = remove x y
                    remove (Add e1 e2) y = remove e1 y ++ remove e2 y
                    remove (Mul e1 e2) y = remove e1 y ++ remove e2 y
                    remove (Pow e1 n) y = remove e1 y


{- Question 5.
Number of unique vars.
>>> varCount (Add (Var "x") (Add (Var "y") (Var "x")))
2
-}
varCount :: Exp -> Int
varCount = \x -> length (vars x)

{-
A data structure assigning values to variables.  It's used in, e.g., writing a program for questions like this: If x is 3 and y is 4 then what is the value of 3*x + 11*y^3?  An "environment" is just a particular implementation of a dictionary with variable names as keys and integers for values.
-}
data Env = EnvEmpty | EnvAdd [Char]Int Env 
    deriving Show

{-
>>> exampleEnv
EnvAdd "x" 3 (EnvAdd "y" 17 EnvEmpty)
-}
exampleEnv :: Env
exampleEnv =
    EnvAdd "x" 3 (EnvAdd "y" 17 EnvEmpty)

{- Question 6.
The value of a variable in the environment, or 0 if the variable is not in the environment.
>>> lookupEnv "y" exampleEnv
17
-}
lookupEnv :: [Char]-> Env -> Int
lookupEnv n (EnvEmpty) = 0
lookupEnv n (EnvAdd x y z) = if x == n then y else lookupEnv n z

{- Question 7.
Change the environment so that x has the new value
>>> exampleEnv
>>> updateEnv "y" 18 exampleEnv
EnvAdd "x" 3 (EnvAdd "y" 17 EnvEmpty)
EnvAdd "x" 3 (EnvAdd "y" 18 EnvEmpty)
-}
updateEnv :: [Char]-> Int -> Env -> Env
updateEnv = \x y z -> update x y z
                        where 
                            update ::  [Char]-> Int -> Env -> Env
                            update x y EnvEmpty = EnvAdd x y EnvEmpty
                            update x y (EnvAdd a b c) = 
                                if a == x 
                                    then (EnvAdd a y (update x y c)) 
                                    else (EnvAdd a b (update x y c))

{-
Rebuild exampleEnv from EmptyEnv using updateEnv.
>>> (lookupEnv "x" exampleEnv , lookupEnv "x" exampleEnv')
>>> (lookupEnv "y" exampleEnv , lookupEnv "y" exampleEnv')
(3,3)
(17,17)
-}
exampleEnv' :: Env
exampleEnv' = updateEnv "x" 3 (updateEnv "y" 17 EnvEmpty)

{- Question 8.
Compute the value of an expression given values for the variables in it.  
>>> ppExp exampleExp
>>> exampleEnv
>>> evalExp exampleExp exampleEnv
"(((3 * x^2) + (-(5 * x))) + 2)"
EnvAdd "x" 3 (EnvAdd "y" 17 EnvEmpty)
14
-}
evalExp :: Exp -> Env -> Int
evalExp = \x y -> eval x y 
                    where
                        eval :: Exp -> Env -> Int
                        eval (Const n) y = n
                        eval (Var x) y = lookupEnv x y
                        eval (Neg x) y = eval x y * (-1)
                        eval (Add e1 e2) y = eval e1 y + eval e2 y
                        eval (Mul e1 e2) y = eval e1 y * eval e2 y
                        eval (Pow e1 n) y = eval e1 y ^ n
                        

-- example for simplify below
simpExample :: Exp
simpExample =
    Add 
    (Mul (Mul (Const 3) (Const 4)) (Pow (Var "x") 3)) 
    (Add (Const 2) (Pow(Const 4) 2))

{- Question 9.
Simplify an expression by "constant folding", i.e. replacing operations on constants by their values.  E.g. 3*4 gets replaced by 12.  Simplify1 replaces all such foldable subexpressions, but the replacements may create new ones, as is shown in the example below, where 4^2 gets replaced by 16, turning 2+4^2 into 2+16.
>>> ppExp simpExample
"(((3 * 4) * x^3) + (2 + 4^2))"
>>> ppExp (simplify1 simpExample)
"((12 * x^3) + (2 + 16))"
>>> ppExp (simplify1 (simplify1 simpExample))
"((12 * x^3) + 18)"
-}
simplify1 :: Exp -> Exp
simplify1 = \x -> simp x
                    where 
                        simp :: Exp -> Exp
                        simp (Const n) = Const n
                        simp (Var x) = Var x
                        simp (Neg x) = (Neg x)
                        simp (Add (Const a) (Const b)) = Const (a+b)
                        simp (Add e1 e2) = (Add (simp e1) (simp e2))
                        simp (Mul (Const a) (Const b)) = Const (a*b)
                        simp (Mul e1 e2) = (Mul (simp e1) (simp e2))
                        simp (Pow (Const a) n) = Const (a^n)
                        simp (Pow e1 n) = (Pow (simp e1) n)

{- Question 10.
Repeatedly apply simplify until the term is fully simplified, i.e. until apply simplify doesn't change the term.
>>> (ppExp simpExample, ppExp (simplify simpExample))
("(((3 * 4) * x^3) + (2 + 4^2))","((12 * x^3) + 18)")
-}
simplify :: Exp -> Exp
simplify = \x -> if x == simplify1 x then x else simplify (simplify1 x)

{- Bonus question! 

Write a function normalize :: Exp -> Exp that puts single-variable expressions into standard polynomial form, i.e. 

a[n]*x^n + a[n-1]*x^(n-1) + ... + a[1]*x + a[0] 

where a[0]...a[n] are integers.  You can assume the input always has at most one variable, and it's "x" (otherwise your program is free to crash and burn).  You're free to use any functions you like from the Haskell Prelude (i.e. the stuff that's there without importing anything).
-}    

-- normalize :: Exp -> Exp
-- normalize = \x -> simplify x