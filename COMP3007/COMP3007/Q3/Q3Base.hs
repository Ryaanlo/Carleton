module Q3Base (Id(..), Exp(..), ppExp, parse, ST, run, runWithIO, outputLine)
where

import Text.ParserCombinators.ReadP
import Control.Monad

---------------------------------
--------- PART 1 ----------------
---------------------------------

data Id = Id [Char] deriving (Show, Eq, Ord)

data Exp = 
    Var Id 
    | Const Int
    | Pred     
    | Succ
    | Square  -- "square 4" evaluates to 16.
    | Plus Exp Exp  -- "plus(2,3)" evaluates to 5.
    | Add  -- "add 2 3" evaluates to 5.
    | Ifz Exp Exp Exp
    | Lam Id Exp
    | App Exp Exp 
    deriving (Show, Eq)

ppExp :: Exp -> String
ppExp (Var (Id x)) = x
ppExp (Const n) = show n
ppExp Pred = "pred"
ppExp Succ = "succ"
ppExp Square = "square"
ppExp Add = "add"
ppExp (Plus e1 e2) =
    "plus(" ++ ppExp e1 ++ ", " ++ ppExp e2 ++ ")"
ppExp (Ifz e1 e2 e3) = 
    "ifz(" ++ ppExp e1 ++ ", " ++ ppExp e2 ++ ", " ++ ppExp e3 ++ ")"
ppExp (Lam (Id x) e) = "%" ++ x ++ " -> " ++ ppExp e
ppExp (App e e') = ppExp e ++ "(" ++ ppExp e' ++ ")"


-- Parser: don't read, skip to Part 2  All you need is parse :: String -> Exp ---

primitiveFns =
  [("pred", Pred)
  ,("succ", Succ)
  ,("square", Square)
  ,("add", Add)
  ]

reservedWords = words ("plus  ifz") ++ map fst primitiveFns

isReservedWord s = s `elem` reservedWords 

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

isAlphanum :: Char -> Bool
isAlphanum =  liftM2 (||) isAlpha isNum
             
idParser :: ReadP String
idParser = do
  skipSpaces
  c1 <- satisfy isAlpha
  rest <- munch isAlphanum
  return $ c1:rest

natParser :: ReadP Exp
natParser =
  do
    skipSpaces
    num <- munch1 isNum
    return $ Const $ (read num :: Int)

wordParser :: String -> ReadP ()
wordParser w =
  do id <- idParser
     pfailIf (w /= id)
     return ()
     
primParser :: ReadP Exp
primParser =
  choice $ map (\(s,e) -> wordParser s >> return e) primitiveFns  

varParser :: ReadP Exp
varParser = do
  id <- idParser
  pfailIf (isReservedWord id)
  return $ Var $ Id id
 
atomParser :: ReadP Exp
atomParser = 
    primParser <++ varParser +++ natParser

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

plusParser :: ReadP Exp
plusParser = do
  wordParser "plus"
  [e1,e2] <- parenParser (sepBy expParser (nextChar ','))
  return $ Plus e1 e2

parenParser :: ReadP a -> ReadP a
parenParser p =
  between (nextChar '(') (nextChar ')') p

headParser :: ReadP Exp
headParser = atomParser +++ (parenParser expParser)

argParser :: ReadP Exp
argParser = atomParser +++ lamParser +++ (parenParser expParser) +++ ifzParser
            +++ plusParser 
  
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
            +++ lamParser +++ (parenParser expParser)
            +++ plusParser

parseWith :: ReadP a -> String -> a
parseWith p = fst . head . readP_to_S p

parse :: String -> Exp
parse = parseWith expParser

----------------------------
-------- PART 2 ------------
----------------------------

type S = [String]
data ST a = ST (S -> (a, S))

run :: ST a -> a
run (ST f) = fst (f [])

runWithIO :: Show a => ST a -> IO ()
runWithIO (ST f) =
  let (value, output) = f [] in
  print value >> mapM_ putStrLn (reverse output)

outputLine :: String -> ST()
outputLine line =
  ST (\s -> ((), line:s))

-- ignore
instance Functor ST where
    fmap f (ST st) =
        ST $ \s -> let (x,s') = st s in (f x, s')

-- ignore
instance Applicative ST where
    pure x = ST $ \s -> (x, s)
    (ST st1) <*> (ST st2) = 
        ST (\s -> let (f, s1) = st1 s
                      (x, s2) = st2 s1 in
                    (f x, s2))

-- exact same definition as for Assignment 5
instance Monad ST where
    (>>=) (ST st1) h = 
        ST $
        \s -> let (x, s1) = st1 s
                  ST st2 = h x
              in st2 s1
