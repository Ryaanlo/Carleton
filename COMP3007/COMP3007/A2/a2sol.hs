module A2Solution where
    
data Exp = 
    Const Int | Var [Char]| Neg Exp | Add Exp Exp |   -- [Char] is strings
    Mul Exp Exp | Pow Exp Int 
    deriving (Show, Eq) -- Eq generates == testing for Exp

exampleExp :: Exp
exampleExp = 
    Add (Add 
            (Mul (Const 3) (Pow (Var "x") 2))
            (Neg (Mul (Const 5) (Var "x"))))
        (Const 2)

parenthesise :: [Char] -> [Char]
parenthesise s = "(" ++ s ++ ")"

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

-- Q1
varOccCount :: Exp -> Int
varOccCount (Var _) = 1
varOccCount (Const _) = 0
varOccCount (Neg e) = varOccCount e
varOccCount (Add e e') = varOccCount e + varOccCount e'
varOccCount (Mul e e') = varOccCount e + varOccCount e'
varOccCount (Pow e _) = varOccCount e

-- Q2
removeDuplicates :: Eq a => [a] -> [a]
removeDuplicates [] = []
removeDuplicates (x:l) = x : removeDuplicates (remove x l)

-- Q3
remove :: Eq a => a -> [a] -> [a]
remove x [] = 
    []
remove x (y:l) =  
    if x==y then remove x l
    else y : 
    remove x l

-- Q4
vars :: Exp -> [String]
vars = removeDuplicates . vars'

vars' :: Exp -> [String]
vars' (Var x) = [x]
vars' (Const _) = []
vars' (Neg e) = vars' e
vars' (Add e e') = vars' e ++ vars' e'
vars' (Mul e e') = vars' e ++ vars' e'
vars' (Pow e _) = vars' e

-- Q5
varCount :: Exp -> Int
varCount = length . vars

data Env = EnvEmpty | EnvAdd [Char] Int Env 
    deriving Show

exampleEnv :: Env
exampleEnv =
    EnvAdd "x" 3 (EnvAdd "y" 17 EnvEmpty)

-- Q6
lookupEnv :: [Char]-> Env -> Int
lookupEnv x EnvEmpty = 
    0  -- default value for unbound variable
lookupEnv x (EnvAdd y v env) = 
    if x==y then v
    else lookupEnv x env

-- Q7
updateEnv :: [Char]-> Int -> Env -> Env
updateEnv x u EnvEmpty = 
    EnvAdd x u EnvEmpty
updateEnv x u (EnvAdd y v env) = 
    if x==y then EnvAdd x u env
    else EnvAdd y v (updateEnv x u env)

exampleEnv' :: Env
exampleEnv' = updateEnv "x" 3 (updateEnv "y" 17 EnvEmpty)

-- Q8
evalExp :: Exp -> Env -> Int
evalExp (Var x) env = lookupEnv x env
evalExp (Const n) env = n
evalExp (Neg e) env = - evalExp e env
evalExp (Add e e') env = evalExp e env + evalExp e' env
evalExp (Mul e e') env = evalExp e env * evalExp e' env
evalExp (Pow e n) env = evalExp e env ^ max 0 n

simpExample :: Exp
simpExample =
    Add 
    (Mul (Mul (Const 3) (Const 4)) (Pow (Var "x") 3)) 
    (Add (Const 2) (Pow(Const 4) 2))

simplify1 :: Exp -> Exp
simplify1 (Neg (Const n)) = Const (-n)
simplify1 (Add (Const m) (Const n)) = Const (m+n)
simplify1 (Mul (Const m) (Const n)) = Const (m*n)
simplify1 (Pow (Const m) n) = Const (m^n)
simplify1 (Neg e) = Neg (simplify1 e)
simplify1 (Add e e') = Add (simplify1 e) (simplify1 e')
simplify1 (Mul e e') = Mul (simplify1 e) (simplify1 e')
simplify1 (Pow e n) = Pow (simplify1 e) n
simplify1 e = e

-- Q9
simplify :: Exp -> Exp
simplify e =
    if simplify1 e == e then e else simplify (simplify1 e)
