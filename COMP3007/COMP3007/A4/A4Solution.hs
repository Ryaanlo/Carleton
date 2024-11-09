module A4Solution where
    
-----------------------------------------------
-- An Interpreter for WHaskell (Wee Haskell) --
-----------------------------------------------

import Data.List ( find, union )
import Data.Maybe ( fromMaybe, isJust, fromJust )
import qualified Data.Map as Map
import Text.ParserCombinators.ReadP
import Control.Monad


----------------------
-- Type Definitions --
----------------------

-- Identifiers.  The Ord type class is needed because Ids are use as Map keys.
data Id = Id [Char] deriving (Show, Eq, Ord)

-- WHaskell expressions
data Exp = 
    Var Id 
    | Defd Id    -- a defined function; distinct from variables for clarity
    | Const Int  -- integer constants
    | Pred       -- predecessor over integers
    | Succ       -- successor over integers
    | Ifz Exp Exp Exp -- ifzero-then-else
    | Lam Id Exp
    | App Exp Exp 
    deriving (Show, Eq)

-- WHaskell type expressions
data TExp = 
    TVar Id | TInt | TArrow TExp TExp
    deriving Eq

{- 
WHaskell function definition with assumed-correct declared type.
The expressions must be closed (no free variables).
-}
data Def = Def Id TExp Exp deriving Show

{- 
Complete WHaskell programs.  The expression must be closed.
Restriction: the expression cannot contain Lam.
-}
data Program = Program [Def] Exp

{- 
Simple State Transformer (see the Wed Nov 16 lecture).
We give a general development of this monad, but the only use is for tracking what new identifiers have been generated.
-}
data SST s a = SST (s -> (a, s))

{-
An intermediate structure only used for computing type inference equations.
TTree is similar to Exp except that is has a field for a type expression, and it collapses all the different leaf nodes (Succ, Var etc) of Exp into a single kind of leaf.
-}
data TTree = 
    TTreeLeaf TExp | TTreeApp TExp TTree TTree | TTreeIfz TExp TTree TTree TTree
    deriving (Show, Eq)

{- 
A dictionary with Id keys and TExp values.  Map.Map is Haskell's implementation of dictionaries.  The dictionary specifies a substitution of types for type variables.
-}
type TSubst = Map.Map Id TExp

-- Type equations, for type inference.
type Equation = (TExp, TExp)

{-
A "solver" is a function that (partially) solves a set of equations.  It may fail, hence the Maybe type.  In an imperative language, StateS would just be [Equation], so a solver would just be a function mapping a set of equations into a (hopefully) simpler set of equations.  Keeping track of the substitution being built would likely be done with a global variable in other languages, but we don't have those.  Instead, our solvers have to work with substitutions as well as equations.  There are some operations for building solvers that hide the details.  See the Type Inference section below for more.
-}
type StateS = ([Equation], TSubst)
type Solver = StateS -> Maybe StateS


-------------------------------------    
-- Basic functions for expressions --
-------------------------------------

-- A somewhat sad, but better-than-nothing, pretty printer for Exp.
ppExp :: Exp -> String
ppExp (Var (Id x)) = x
ppExp (Defd (Id x)) = x
ppExp (Const n) = show n
ppExp Pred = "pred"
ppExp Succ = "succ"
ppExp (Ifz e1 e2 e3) = 
    "ifz(" ++ ppExp e1 ++ ", " ++ ppExp e2 ++ ", " ++ ppExp e3 ++ ")"
ppExp (Lam (Id x) e) = "%" ++ x ++ " -> " ++ ppExp e
ppExp (App e e') = ppExp e ++ "(" ++ ppExp e' ++ ")"

parens s = "("  ++ s ++ ")"
arrow s s' = s ++ "->" ++ s'

{-
A custom show method for TExp (instead of the default one you get using "deriving")
-}
instance Show TExp where
    show (TVar (Id x)) = x
    show TInt = "Int"
    show (TArrow TInt t) = arrow "Int" (show t)
    show (TArrow (TVar (Id x)) t) = arrow x (show t)
    show (TArrow t t') = arrow (parens $ show t) (show t')

tVarId :: TExp -> Id
tVarId (TVar id) = id
tVarId texp = error $ "tVarId applied to non-variable: " ++ show texp

-- tvarLookup id m = (Var id) if id is not present in the map.
tvarLookup :: Id -> TSubst -> TExp
tvarLookup id m = fromMaybe (TVar id) (Map.lookup id m)

{- 
Substitute for the variables in the type expression according to the TSubst map.
-}
tSubst :: TSubst -> TExp -> TExp
tSubst m (TVar x) = fromMaybe (TVar x) $ Map.lookup x m
tSubst m TInt = TInt
tSubst m (TArrow t1 t2) = TArrow (tSubst m t1) (tSubst m t2)

-- The type variables in a type expression.  The result list has no duplicates.
texpVars :: TExp -> [Id]
texpVars (TVar x) = [x]
texpVars TInt = []
texpVars (TArrow t1 t2) = texpVars t1 `union` texpVars t2

-- Keep applying the substitution until the type expression doesn't change.
recursiveTSubst :: TSubst -> TExp -> TExp
recursiveTSubst tsubst texp =
    let texp' = tSubst tsubst texp 
    in
        if texp == texp' then texp
        else recursiveTSubst tsubst texp'

-- True iff the type variable appears in the type expression.
occursCheck :: Id -> TExp -> Bool
occursCheck id (TArrow texp texp') = occursCheck id texp && occursCheck id texp'
occursCheck id TInt = True
occursCheck id (TVar id') = id /= id'

isDef :: Id -> Def -> Bool
isDef id (Def id' _ _) = id==id'

defType :: Def -> TExp
defType (Def _ texp _) = texp

defRHS :: Def -> Exp
defRHS (Def _ _ exp) = exp

lookupDef :: Id -> [Def] -> Def
lookupDef id defs = fromMaybe explode (find (isDef id) defs)
    where explode = error $ "No definition for " ++ show id

----------------------------------------------------0---
-- Parser for expressions                             --
-- Ignore the code, you only need the final function: --
-- parse :: String -> Exp                             --
--------------------------------------------------------

nextChar :: Char -> ReadP Char
nextChar c = skipSpaces >> char c

pfailIf :: Bool -> ReadP ()
pfailIf b = if b then pfail else return ()

isLowerAlpha :: Char -> Bool
isLowerAlpha c = c `elem` "abcdefghijklmnopqrstuvwxyz"

isUpperAlpha :: Char -> Bool
isUpperAlpha c = c `elem` "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

isAlpha :: Char -> Bool
isAlpha c = isUpperAlpha c || isLowerAlpha c

isNum :: Char -> Bool
isNum c = c `elem` "0123456789"

isReservedWord :: String -> Bool
isReservedWord s = s `elem` words "ifz succ pred cons head tail null"

isAlphanum :: Char -> Bool
isAlphanum =  liftM2 (||) isAlpha isNum
             
idParser :: ReadP String
idParser = do
  skipSpaces
  c1 <- satisfy isAlpha
  rest <- munch isAlphanum
  return $ c1:rest

cappedIdParser :: ReadP String
cappedIdParser = do
  skipSpaces
  c1 <- satisfy isUpperAlpha
  rest <- munch isAlphanum
  return $ c1:rest

natParser :: ReadP Exp
natParser =
  do
    skipSpaces
    num <- munch1 isNum
    return $ Const $ (read num :: Int)

wordParser :: String -> ReadP String
wordParser w =
  do id <- idParser
     pfailIf (w /= id)
     return w
     
succParser :: ReadP Exp
succParser =
  wordParser "succ" >> return Succ

predParser :: ReadP Exp
predParser =
  wordParser "pred" >> return Pred

varParser :: ReadP Exp
varParser = do
  id <- idParser
  pfailIf (isReservedWord id)
  return $ Var $ Id id
 
defdParser :: ReadP Exp
defdParser =
  liftM (Defd . Id) cappedIdParser

atomParser :: ReadP Exp
atomParser = 
    succParser <++ predParser +++ defdParser +++ varParser +++ natParser

lamParser :: ReadP Exp
lamParser =
  liftM2
  Lam
  (nextChar '%' >> idParser >>= return . Id)
  (nextChar '-' >> char '>'  >>  expParser)

ifzParser :: ReadP Exp
ifzParser = do
    wordParser "ifz"
    nextChar '('
    e1 <- expParser
    nextChar ',' 
    e2 <- expParser
    nextChar ',' 
    e3 <- expParser
    nextChar ')'
    return $ Ifz e1 e2 e3

ifzParser' :: ReadP Exp
ifzParser' = do
    nextChar '['
    e1 <- expParser
    nextChar ',' 
    e2 <- expParser
    nextChar ',' 
    e3 <- expParser
    nextChar ']'
    return $ Ifz e1 e2 e3

parenParser :: ReadP Exp
parenParser =
  between (nextChar '(') (nextChar ')') expParser

headParser :: ReadP Exp
headParser = atomParser +++ parenParser

argParser :: ReadP Exp
argParser = atomParser +++ lamParser +++ parenParser +++ ifzParser
  
argsParser :: ReadP [Exp]
argsParser =
  args1Parser <++ return []

args1Parser :: ReadP [Exp]
args1Parser =
  do e <- argParser
     es <- argsParser
     return $ e:es

appParser :: ReadP Exp
appParser =
  do e <- headParser
     args <- args1Parser
     return $ foldl1 App $ e : args
               
expParser :: ReadP Exp
expParser = appParser <++ atomParser +++ ifzParser
            +++ lamParser +++ parenParser 

parseWith :: ReadP a -> String -> a
parseWith p = fst . head . readP_to_S p

parse :: String -> Exp
parse = parseWith expParser



----------------
-- Evaluation --
----------------    

{- 
subst1 e x e' substitutes the closed term e' for all free occurrences of x in e
-}
subst1 :: Exp -> Id -> Exp -> Exp

{- 
Evaluate the expression.
Assume that 
1. the expression is closed,
2. it does not contain Lam, and
3. in any (Def id), id is declared in the provided definitions.
-}
evalExp :: [Def] -> Exp -> Exp

----------------------
-- State operations --
----------------------

getState :: SST s s
getState = SST $ \s -> (s,s)

updateState :: (s -> s) -> SST s ()
updateState f = SST $ \s -> ((), f s)

runSST :: s -> SST s a -> a
runSST initialState (SST st) = fst (st initialState)

-- IGNORE - have to do this before declaring SST to be a monad
instance Functor (SST s) where
    fmap f (SST st) =
        SST $ \s -> let (x,s') = st s in (f x, s')

-- IGNORE - have to do this before declaring SST to be a monad
instance Applicative (SST s) where
    pure x = SST $ \s -> (x, s)
    (SST st1) <*> (SST st2) = 
        SST (\s -> let (f, s1) = st1 s
                       (x, s2) = st2 s1 in
                     (f x, s2))

-- covered in Wed Nov 16 lecture
instance Monad (SST s) where
    (>>=) (SST st1) h = 
        SST $
        \s -> let (x, s1) = st1 s
                  SST st2 = h x
              in st2 s1

---------------------------
-- Fresh/new identifiers --
---------------------------

{-
To generate new identifiers we keep track of a single number.  We append the number to "v" to make a new id, e.g. Id "v34".  We want to increment the number every time we make a new identifier, so we need to keep track of that number as we do other processing.  
The "boxes" of (SST Int a) let us pass around that number implicitly, in a hidden way.  We can think of some b :: (SST Int a) as a box that has a value of type a that we're interested in, and also a hidden state, of type Int, that's the highest number used so var in a new id.
-}

{- 
A state transformer that, when executed, takes the state (an int), uses it to build a new id, and returns the new id along with the new state which is just the starting state plus 1.
-}
newId :: SST Int Id
newId = do
    k <- getState
    updateState (1+)
    return $ Id $ "v" ++ show (max 0 k)

newIds :: Int -> SST Int [Id]
newIds n | n>0 = do
    v <- newId
    vs <- newIds $ n-1
    return $ v : vs
newIds n = return []

freshenTExp :: TExp -> SST Int TExp
freshenTExp texp = do
    let vars = texpVars texp
    newIds <- newIds (length vars)
    let newVars = map TVar newIds
        substMap = Map.fromList $ zip vars newVars
    return $ tSubst substMap texp

--------------------
-- Type Inference --
--------------------

exp2TTree :: [Def] -> Exp -> SST Int TTree
exp2TTree defs e = do
    id <- newId
    let v = TVar id
    case e of
        Defd id -> do
            texp <- freshenTExp $ defType (lookupDef id defs)
            return $ TTreeLeaf texp
        Const _ -> return $ TTreeLeaf TInt 
        Pred -> return $ TTreeLeaf (TArrow TInt TInt)
        Succ -> return $ TTreeLeaf (TArrow TInt TInt)
        Ifz e1 e2 e3 -> do
            tree1 <- exp2TTree defs e1
            tree2 <- exp2TTree defs e2
            tree3 <- exp2TTree defs e3
            return $ TTreeIfz v tree1 tree2 tree3
        Lam x e' -> error "typing not implemented for lambda expressions"
        App e e' -> do
            treeE <- exp2TTree defs e
            treeE' <- exp2TTree defs e'
            return $ TTreeApp v treeE treeE'
        otherwise -> return $ TTreeLeaf v

rootType :: TTree -> TExp
rootType (TTreeLeaf texp) = texp
rootType (TTreeApp texp _ _) = texp
rootType (TTreeIfz texp _ _ _) = texp

initS :: [Equation] -> StateS
initS eqs = (eqs, Map.empty)

orelseS :: Solver -> Solver -> Solver
orelseS f f' st =
    let st' = f st in
    if isJust st' then st' else f' st

thenS :: Solver -> Solver -> Solver
thenS f f' st = do
    st' <- f st
    f' st'

idS :: Solver
idS = Just . id

-- always succeeds
repeatS :: Solver -> Solver
repeatS f = (f `thenS` repeatS f) `orelseS` idS

equations :: [Def] -> Exp -> ([Equation],TExp)
equations defs e = 
    let ttree = runSST 0 (exp2TTree defs e) 
        eType = rootType ttree in
    (equations' ttree, eType)

mainS :: Solver
mainS = repeatS (splitS `orelseS` reduceS)

typecheckExp :: [Def] -> Exp -> TExp
typecheckExp defs e =
    let (eqs, eType) = equations defs e 
        initialSolverState = initS eqs
        (eqsFinal, tsubstFinal) = fromJust $ mainS initialSolverState 
    in
        if not $ null eqsFinal then 
            error $ "typecheckExp: unsolvable constraints: " ++ show eqsFinal
        else recursiveTSubst tsubstFinal eType

{-
Split all splittable equations in input, returning Nothing if none.  Note: there may be splittable equations in the result.
-}
splitS :: Solver

splitEquation :: Equation -> [Equation]
splitEquation (TArrow s1 t1, TArrow s2 t2) = [(s1,s2), (t1,t2)]
splitEquation eq = [eq]

reduceS :: Solver

isReducer :: Equation -> Bool
isReducer (TVar _, _) = True
isReducer (_, TVar _) = True
isReducer _ = False

convertReducer :: Equation -> (Id, TExp)
convertReducer (TVar id, e) = (id, e)
convertReducer (e, TVar id) = (id, e)
convertReducer _ = error "convertReducer"

findReducer :: [Equation] -> Maybe (Id, TExp)
findReducer l =  
    fmap convertReducer $ find isReducer l 

removeTrivial :: [Equation] -> [Equation]
removeTrivial = filter (\(l,r) -> l /= r)

-----------------------------
-- Putting it all together --
-----------------------------

{- 
Assumes the definitions all typecheck with the provided types.  
Typecheck the given expression in the context of the given definitions, and if it it typechecks, evaluate it, returning a string showing the result and its type.
-}
runProgram :: Program -> String
runProgram (Program defs e) =
    let resultType = show $ typecheckExp defs e 
    in
        (resultType `seq` ppExp (evalExp defs e))
        ++ " :: " ++ resultType

----------------------
-- Testing examples --
----------------------

[a,b,c] = map (TVar . Id) $ words "a b c"
[x,y,z,m,n,k,i] = map (Var . Id) $ words "x y z m n k i"

addDef =
    Def
    (Id "Add")
    (TInt `TArrow` (TInt `TArrow` TInt)) 
    (parse "%m -> %n -> ifz(m, n, succ (Add (pred m) n))")

subDef =
    Def
    (Id "Sub")
    (TInt `TArrow` (TInt `TArrow` TInt)) 
    (parse "%m -> %n -> ifz(m, 0, ifz(n, m, Sub (pred m) (pred n)))")

mulDef =
    Def
    (Id "Mul")
    (TInt `TArrow` (TInt `TArrow` TInt)) 
    (parse "%m -> %n -> ifz(m, 0, Add n (Mul (pred m) n))")

eqDef =
    Def
    (Id "Eq")
    (TInt `TArrow` (TInt `TArrow` TInt)) 
    (parse "%m -> %n -> ifz(Sub m n, ifz(Sub n m, 0, 1), 1)")

greaterDef =
    Def 
    (Id "Greater")
    (TInt `TArrow` (TInt `TArrow` TInt))
    (parse "%m -> %n -> ifz(m, 1, ifz(n, 0, Greater (pred m) (pred n)))")

gcdDef =
    Def
    (Id "Gcd")
    (TInt `TArrow` (TInt `TArrow` TInt))
    (parse $ "%m -> %n -> ifz(Eq m n, m, " ++
             "ifz(Greater m n, Gcd (Sub m n) n, Gcd m (Sub n m)))")  

idDef =
    Def
    (Id "Id")
    (a `TArrow` a)
    (parse "%x -> x")

iterDef =
    Def
    (Id "Iter")
    ((a `TArrow` a) `TArrow` (a `TArrow` (TInt `TArrow` a)))
    (parse "%f -> %x -> %n -> ifz(n, x, (Iter f (f x) (pred n)))")

pairDef =
    Def
    (Id "Pair")
    (a `TArrow` (b `TArrow` ((a `TArrow` (b `TArrow`c)) `TArrow` c)))
    (parse "%x -> %y -> %f -> f x y")

defs = 
    [addDef, mulDef, subDef, eqDef, greaterDef, gcdDef, idDef, iterDef, pairDef]

-- >>> run gcdTest
-- "4 :: Int"
gcdTest = "Gcd 8 12"

-- >>> run pairTest
-- "%f -> f(id(id))(succ) :: (v6->(Int->Int)->v5)->v5"
pairTest = "Pair (id id) succ"

ev :: String -> Exp
ev = evalExp defs . parse
tc = typecheckExp defs . parse

-- typecheck and evaluate
run :: String -> String
run s = runProgram (Program defs (parse s))


------------------------------------------------------------
-- Your code goes below.  Do not change *anything* above. --
------------------------------------------------------------

subst1 (Var y) x e = if x==y then e else Var y
subst1 (Ifz e1 e2 e3) x e = Ifz (subst1 e1 x e) (subst1 e2 x e) (subst1 e3 x e) 
subst1 (Lam y e') x e = if x==y then Lam y e' else Lam y (subst1 e' x e)
subst1 (App e1 e2) x e = App (subst1 e1 x e) (subst1 e2 x e)
subst1 e _ _ = e

evalExp defs e@(Var (Id x)) = 
    error $ "evalExp: can't eval free variable: " ++ show e
evalExp defs (Defd id) = defRHS (lookupDef id defs)
evalExp defs (Const n) = Const n
evalExp defs Pred = Pred
evalExp defs Succ = Succ
evalExp defs (Ifz e1 e2 e3) = evalIfz defs (evalExp defs e1) e2 e3
evalExp defs e@(Lam _ _) = e
evalExp defs (App e e') = evalApp defs (evalExp defs e) e'

evalIfz :: [Def] -> Exp -> Exp -> Exp -> Exp
evalIfz defs (Const 0) e1 e2 = evalExp defs e1
evalIfz defs (Const n) e1 e2 = evalExp defs e2
evalIfz defs v e1 e2 = 
    error $ "evalExp: expected number in ifz, got " ++ (show v)

evalApp :: [Def] -> Exp -> Exp -> Exp
evalApp defs (Lam x e) e' = 
    evalExp defs (subst1 e x e')
evalApp defs Pred e' = evalPrimitive Pred (evalExp defs e')
evalApp defs Succ e' = evalPrimitive Succ (evalExp defs e')
evalApp defs e _ = 
    error $ "evalApp: cannot apply value as function: " ++ show e

evalPrimitive :: Exp -> Exp -> Exp
evalPrimitive Pred (Const n) = Const (max 0 $ n-1)
evalPrimitive Succ (Const n) = Const (n+1)
evalPrimitive f arg = 
    error $ "evalPrimitive got: " ++ show f ++ " applied to " ++ show arg

equations' :: TTree -> [Equation]
equations' (TTreeLeaf texp) = []
equations' (TTreeApp texp tree1 tree2) = 
    (rootType tree1, TArrow (rootType tree2) texp) 
    : (equations' tree1 ++ equations' tree2)
equations' (TTreeIfz texp tree1 tree2 tree3) =
    (rootType tree1, TInt) 
    : (texp, rootType tree2) 
    : (rootType tree2, rootType tree3)
    : (equations' tree1 ++ equations' tree2 ++ equations' tree3)

splitS  (eqs, tsubst) = 
    let splits = map splitEquation eqs in
    if any (\l -> length l > 1) splits then Just (concat splits, tsubst)
    else Nothing

reduceS (eqs, tsubst) = do
    (id, texp) <- findReducer eqs
    if occursCheck id texp then Just () else Nothing
    let s = tSubst $ Map.singleton id texp
    let eqs' = removeTrivial $ map (\(l,r) -> (s l, s r)) eqs
    let tsubst' = Map.insert id texp tsubst
    return (eqs', tsubst')

-- 1. Replace any free "y". Look at the second lambda function and look at App

-- subst1 (Ifz (Var (Id "x")) (Lam (Id "x") (App (Pred) (Var (Id "y")))) (Lam (Id "x") (App (Pred) (Var (Id "y")))) ) (Id "y") (Var (Id "Hello")) 

-- ---> Ifz (Var (Id "x")) (Lam (Id "x") (App Pred (Var (Id "Hello")))) (Lam (Id "x") (App Pred (Var (Id "Hello"))))



-- 2. Replace any free y. Changed second Lamda function to Lam (Id "y"). Only y in App gets changed

-- subst1 (Ifz (Var (Id "x")) (Lam (Id "x") (App (Pred) (Var (Id "y")))) (Lam (Id "y") (App (Pred) (Var (Id "y")))) ) (Id "y") (Var (Id "Hello")) 

-- ---> Ifz (Var (Id "x")) (Lam (Id "x") (App Pred (Var (Id "Hello")))) (Lam (Id "y") (App Pred (Var (Id "y"))))



-- 3. Replace any free x. Look at first x (first arguments of ifz

-- subst1 (Ifz (Var (Id "x")) (Lam (Id "x") (App (Pred) (Var (Id "y")))) (Lam (Id "x") (App (Pred) (Var (Id "y")))) ) (Id "x") (Var (Id "Hello"))

-- ---> Ifz (Var (Id "Hello")) (Lam (Id "x") (App Pred (Var (Id "y")))) (Lam (Id "x") (App Pred (Var (Id "y"))))



-- 4. New Function: Replace any free y.

-- subst1 (Ifz (Var (Id "x")) (Lam (Id "x") (Lam (Id "y")(App (Pred) (Var (Id "y"))))) (Lam (Id "x") (App (Pred) (Var (Id "y")))) ) (Id "y") (Var (Id "Hello"))  

-- ---> Ifz (Var (Id "x")) (Lam (Id "x") (Lam (Id "y") (App Pred (Var (Id "y"))))) (Lam (Id "x") (App Pred (Var (Id "Hello"))))



-- 5. Replace any free x. (Initial var is changed)

-- subst1 (Ifz (Var (Id "x")) (Lam (Id "x") (Lam (Id "y")(App (Pred) (Var (Id "y"))))) (Lam (Id "x") (App (Pred) (Var (Id "y")))) ) (Id "x") (Var (Id "Hello")) 

-- ---> Ifz (Var (Id "Hello")) (Lam (Id "x") (Lam (Id "y") (App Pred (Var (Id "y"))))) (Lam (Id "x") (App Pred (Var (Id "y"))))



-- 6. Replace any free x, Changed initial var from x to y

-- subst1 (Ifz (Var (Id "y")) (Lam (Id "x") (Lam (Id "y")(App (Pred) (Var (Id "y"))))) (Lam (Id "y") (App (Pred) (Var (Id "y")))) ) (Id "x") (Var (Id "Hello"))

-- ---> Ifz (Var (Id "y")) (Lam (Id "x") (Lam (Id "y") (App Pred (Var (Id "y"))))) (Lam (Id "y") (App Pred (Var (Id "y"))))     