import Test.HUnit
import A2Solution

{-
To use this Assignment 2 testing file:
0. Get the library HUnit (Haskell Unit testing) by doing
   cabal install --lib HUnit
   in your terminal/shell
1. rename your solutions file to A2Solutions.hs
2. at the very top of your file put the line 
   "module A2Solutions where"
   without the quotes
3. put this testing file in the same directory as your file, and then load it into ghci (it will take care of loading your file)
4. in ghci, apply the function rt (for "run test") to a test to run the test.  The tests are: testAll, testQ1, ..., testQ9, testQBonus. The test testAll skips the bonus question.  
   rt $ testAll
-}

-- "( (x+y)^2 + 3*(z+(-y)) ) * ( z + 2*z^3 )"
bigE1 = Pow (Add (Var "x") (Var "y")) 2
bigE2 = Mul (Const 3) (Add (Var "z") (Neg (Var "y")))
bigE3 = Add (Var "x") (Mul (Const 2) (Pow (Var "z") 2))
bigE = Mul (Add bigE1 bigE2) bigE3

eT2 = Add (Const 2) (Const 1)
eT2_VarOccCount = 0

-- Only duplicates
eT3 = Add (Var "x") (Var "x")
-- No duplicates
eT4 = Add (Add (Var "x") (Const 2)) (Add (Var "y") (Const 1))

bigEVarOccCount = 6

-- Q1
-- varOccCount :: Exp -> Int
testQ1a = 
    TestCase 
    (assertEqual "(varOccCount bigE)" bigEVarOccCount (varOccCount bigE))
testQ1b =
    TestCase
    (assertEqual "(varOccCount eT2)" eT2_VarOccCount (varOccCount eT2))

testQ1 = TestList [testQ1a, testQ1b]


member :: (Eq a) => a -> [a] -> Bool
member x [] = False
member x (y:l) = x==y || member x l

eqAsSets :: (Eq a) => [a] -> [a] -> Bool
eqAsSets l1 l2 = all (`member` l1) l2 && all (`member` l2) l1

bigL :: [Int]
bigL = [4,3,6,5,3,5,5,7,2]
bigLNoDuplicates :: [Int]
bigLNoDuplicates = [4,3,6,5,7,2]

lOnlyDuplicates :: [Int]
lOnlyDuplicates = [1,1,1,1]

lOnlyNoDuplicates :: [Int]
lOnlyNoDuplicates = [1]

l2NoDuplicates :: [Int]
l2NoDuplicates = [1,2,3,4]

-- Q2
-- removeDuplicates :: Eq a => [a] -> [a]
testQ2a = 
    TestCase 
    (do let l = removeDuplicates bigL
        assertBool 
            ("Result " ++ show l ++ " and input "
                       ++ show bigL ++ " do not have the same elements ")
            (eqAsSets l bigL)
        assertBool
            ("Result " ++ show l ++ (" has duplicates"))
            (length l == length bigLNoDuplicates))

testQ2b = 
    TestCase 
    (do let l = removeDuplicates lOnlyDuplicates
        assertBool 
            ("Result " ++ show l ++ " and input "
                       ++ show lOnlyDuplicates ++ " do not have the same elements ")
            (eqAsSets l lOnlyDuplicates)
        assertBool
            ("Result " ++ show l ++ (" has duplicates"))
            (length l == length lOnlyNoDuplicates))

testQ2c = 
    TestCase 
    (do let l = removeDuplicates l2NoDuplicates
        assertBool 
            ("Result " ++ show l ++ " and input "
                       ++ show l2NoDuplicates ++ " do not have the same elements ")
            (eqAsSets l l2NoDuplicates)
        assertBool
            ("Result " ++ show l ++ (" has duplicates"))
            (length l == length l2NoDuplicates))
 
testQ2 = TestList [testQ2a,testQ2b,testQ2c]

-- Q3
-- remove :: Eq a => a -> [a] -> [a]
bigLNoFive = [4,3,6,3,7,2]
-- no occurences of x, only x
testQ3a =
    TestCase $ 
    assertEqual 
       ("For (remove 5 bigL" ++ show bigL ++ ") ") 
       bigLNoFive 
       (remove 5 bigL)
testQ3b =
    TestCase $ 
    assertEqual 
       ("For (remove 1 bigL" ++ show bigL ++ ") ") 
       bigL 
       (remove 1 bigL) 
testQ3c =
    TestCase $ 
    assertEqual 
       ("For (remove 1 " ++ show lOnlyDuplicates ++ ") ") 
       [] 
       (remove 1 lOnlyDuplicates) 
testQ3 = TestList [testQ3a, testQ3b, testQ3c]

bigEVars = ["x","y","z"]

-- cases:
-- no variables, no duplicates, only var to remove
-- T2
eT2Vars :: [String]
eT2Vars = []


eT3Vars = ["x"]
eT4Vars = ["x", "y"]

-- Q4
-- vars :: Exp -> [String]
testQ4a =
    TestCase $
    assertBool 
        ("(vars bigE) is not equal as a set to " ++ show bigEVars )
        (eqAsSets (vars bigE) bigEVars)

testQ4b =
    TestCase $
    assertBool 
        ("(vars eT2) is not equal as a set to " ++ show eT2Vars )
        (eqAsSets (vars eT2) eT2Vars)
       
testQ4c =
    TestCase $
    assertBool 
        ("(vars eT3) is not equal as a set to " ++ show eT3Vars )
        (eqAsSets (vars eT3) eT3Vars)

testQ4d =
    TestCase $
    assertBool 
        ("(vars eT4) is not equal as a set to " ++ show eT4Vars )
        (eqAsSets (vars eT4) eT4Vars)

testQ4 = TestList [testQ4a, testQ4b, testQ4c, testQ4d]        

bigECount = 3
-- cases only unique vars, no vars, all the same var
eT2Count = 0
eT3Count = 1
eT4Count = 2

-- Q5
-- varCount :: Exp -> Int
testQ5a = 
    TestCase $ 
    assertEqual "For (varCount bigE) " bigECount (varCount bigE) 

testQ5b = 
    TestCase $ 
    assertEqual "For (varCount eT2) " eT2Count (varCount eT2) 
testQ5c = 
    TestCase $ 
    assertEqual "For (varCount eT3) " eT3Count (varCount eT3) 
testQ5d = 
    TestCase $ 
    assertEqual "For (varCount eT4) " eT4Count (varCount eT4) 
testQ5 = TestList [testQ5a, testQ5b, testQ5c, testQ5d]

envEq EnvEmpty EnvEmpty = 
    True
envEq (EnvAdd x v env) (EnvAdd x' v' env') =
    x==x' && v==v' && envEq env env'
envEq _ _ =
    False

instance Eq Env where
    (==) = envEq

envZip [] _ = EnvEmpty
envZip _ [] = EnvEmpty
envZip (x:xs) (v:vs) = EnvAdd x v (envZip xs vs)

bigEnv = 
    envZip
    (words "u v w x y z")
    [6,5,4,3,2,1]


-- 2 cases: the variable is in the environment, the variable is not in the environment
-- Q6
-- lookupEnv :: [Char]-> Env -> Int
testQ6 =
    TestCase
    (do assertEqual "For (lookupEnv \"w\" bigEnv" 4 (lookupEnv "w" bigEnv)
        assertEqual "For (lookupEnv \"z\" bigEnv" 1 (lookupEnv "z" bigEnv)
        assertEqual "For (lookupEnv \"z\" bigEnv" 0 (lookupEnv "a" bigEnv))

alRemove :: [Char] -> [([Char],Int)] -> [([Char],Int)]
alRemove x = filter ( (/= x) . fst )
envToAL :: Env -> [([Char], Int)]
envToAL EnvEmpty = []
envToAL (EnvAdd x v env) = (x,v) : (alRemove x $ envToAL env)

envSimilar env env' = 
    let (al, al') = (envToAL env, envToAL env') in
    all (`member` al') al && all (`member` al) al'

-- Q7
-- updateEnv :: [Char]-> Int -> Env -> Env
-- exampleEnv' :: Env
envT1 :: Env
envT1 = EnvEmpty

envT1Result :: Env
envT1Result = EnvAdd "x" 3 EnvEmpty

envT1' :: Env
envT1' = updateEnv "x" 3 envT1

-- 2 cases: var is in env, var is not in env
testQ7a =
    TestCase
    (do let old = show exampleEnv
        let new = show exampleEnv'
        assertBool
            ("The new exampleEnv\n" ++ new
             ++"\nis not the same as the old one\n" ++ old)
            (envSimilar exampleEnv exampleEnv'))
testQ7b =
    TestCase
    (do let old = show envT1
        let new = show envT1'
        assertBool
            ("The new envT1'\n" ++ new
             ++"\nis the same as the old one\n" ++ old)
            (envT1' == envT1Result))
testQ7 = TestList [testQ7a, testQ7b]

-- 1 case for 8
-- Q8
-- evalExp :: Exp -> Env -> Int
testQ8 = 
    TestCase 
        (assertEqual 
            "For (evalExp bigE bigEnv)" 
            110
            (evalExp bigE bigEnv))


-- bigSimp is ((3*1)^2+(5+(-5)) * (x * (-(3+4))) 
bs1 = Pow (Mul (Const 3) (Const 1)) 2
bs2 = Add (Const 5) (Neg (Const 5))
bs3 = Mul (Var "x") (Neg (Add (Const 3) (Const 4)))
bigSimp = Mul (Add bs1 bs2) bs3

simpedBigSimp = simplify bigSimp
-- >>> simpedBigSimp
-- Mul (Const 9) (Mul (Var "x") (Const (-7)))
-- >>> ppExp simpedBigSimp
-- "(9 * (x * -7))"

sT1 = Add (Add (Const 5) (Var "x")) (Const 1)
-- 2 cases: the expression can be simplified, and expression can't be simplified
-- Q9
-- simplify :: Exp -> Exp
testQ9a = 
    TestCase $ 
    assertEqual "For (simplify bigSimp) " simpedBigSimp (simplify bigSimp)

testQ9b = 
    TestCase $ 
    assertEqual "For (simplify sT1) " sT1 (simplify sT1)

testQ9 = TestList [testQ9a, testQ9b]

isPower (Var "x") = True
isPower (Pow (Var "x") _) = True
isPower _ = False

isTerm (Mul (Const _) e) = isPower e
isTerm (Const _) = True
isTerm _ = False

isTermSum (Add e e') = isTerm e && isTermSum e'
isTermSum e = isTerm e

powerExponent (Var "x") = 1
powerExponent (Pow (Var "x") n) = n
powerExponent e = -1

termExponent (Mul (Const _) e) = powerExponent e
termExponent (Const _) = 0
termExponent _ = -1

termSumToList (Add e e') = e : termSumToList e'
termSumToList e = [e]

termSumExponents = map termExponent . termSumToList

decreasing [] = True
decreasing l = all (uncurry (>)) (zip l (tail l))

inStandardForm :: Exp -> Bool
inStandardForm e =
    isTermSum e && decreasing (termSumExponents e)

bonusIn = Add (Const 1) (Const 2)
bonusOut = Const 3

-- QBonus
normalize :: Exp -> Exp
normalize = id

testQBonusForm = 
    TestCase $ 
    assertBool 
      ("(normalize bonusIn) not in standard form ")
      (inStandardForm $ normalize bonusIn)

evalMonomial :: Exp -> Int -> Int
evalMonomial e n = evalExp e (EnvAdd "x" n EnvEmpty)

n :: Int
n = 3

bonusInValue :: Int
bonusInValue = evalMonomial bonusIn n

testQBonusSemantics :: Test
testQBonusSemantics = 
    TestCase $
    assertEqual
        "Value of normalized expression"
        bonusInValue
        (evalMonomial (normalize bonusIn) n)

testQBonus :: Test
testQBonus = 
    TestList [testQBonusForm, testQBonusSemantics]

testAll :: Test
testAll = 
    TestList [
        TestLabel "Q1" testQ1, 
        TestLabel "Q2" testQ2, 
        TestLabel "Q3" testQ3, 
        TestLabel "Q4" testQ4, 
        TestLabel "Q5" testQ5, 
        TestLabel "Q6" testQ6, 
        TestLabel "Q7" testQ7, 
        TestLabel "Q8" testQ8, 
        TestLabel "Q9" testQ9
    ] 

rt = runTestTT
