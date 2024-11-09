import Test.HUnit
import A3Solution

s :: Int -> Int
s x = x + 1

add :: Int -> Int -> Int
add x y = x + y

list1 :: [Int]
list1 = [1, 2, 3, 4]

testQ1 = 
    TestCase
    (do let l = generateList 4 s 1
        assertBool 
            ("Result" ++ show l ++ " and " ++ show list1 ++ "do not have the same elements")
            (l == list1)
    )

res2 :: Maybe Int
res2 = (Just 3)

res3 :: Maybe Int
res3 = Nothing

testQ2 = 
    TestCase
    (do let e = firstWhen (\x -> x > 2) list1
        assertBool
            ("Result " ++ show e ++ " and " ++ show res2 ++ "are not the same")
            (res2 == e)
    )
testQ2T2 = 
    TestCase
    (do let e = firstWhen (\x -> x > 4) list1
        assertBool
            ("Result " ++ show e ++ " and " ++ show res3 ++ " are not the same")
            (res3 == e)
    )

resq3t1 :: Maybe Int
resq3t1 = (Just 4)

resq3t2 :: Maybe Int
resq3t2 = Nothing

testQ3 = 
    TestCase
    (do let e = index list1 3
        assertBool
            ("Result " ++ show e ++ " and " ++ show resq3t1 ++ " are not the same")
            (resq3t1 == e)
    )

testQ3T1 = 
    TestCase
    (do let e = index list1 4
        assertBool
            ("Result " ++ show e ++ " and " ++ show resq3t2 ++ " are not the same")
            (resq3t2 == e)
    )


resq4 = 7 

testQ4 = 
    TestCase
    (do let e = reduceSequence 2 4 s 0 add
        assertBool
            ("Result " ++ show e ++ " and " ++ show resq4 ++ " are not the same")
            (resq4 == e)
    )

tableQ5T1 :: Table Int
tableQ5T1 = Table [[1,2,3],[4,5,6],[7,8,9]]

tableQ5T2 :: Table Integer
tableQ5T2 = Table [[1,2,3,4],[4,5,6,7]]

tableQ5T3 :: Table Integer
tableQ5T3 = Table [[1,2],[3,4],[5,6],[7,8]]

tableQ5T4 :: Table Integer
tableQ5T4 = Table [[1]]

testQ5T1 = 
    TestCase
    (do let b1 = checkSquare tableQ5T1
        assertBool
            ("Result " ++ show b1 ++ "does not match the expected result: True")
            b1
    )

testQ5T2 =
    TestCase
    (do let b2 = checkSquare tableQ5T2
        assertBool
            ("Result " ++ show b2 ++ " does not match the expected result: False")
            (not b2) 
    )

testQ5T3 =
    TestCase
    (do let b3 = checkSquare tableQ5T3
        assertBool
            ("Result "++ show b3 ++ " does not match the expected result: False")
            (not b3)
    )

testQ5T4 =
    TestCase
    (do let b4 = checkSquare tableQ5T3
        assertBool
            ("Result "++ show b4 ++ " does not match the expected result: True")
            (not b4)
    )

tableQ6T1 :: Table Integer
tableQ6T1 = Table [[1,2,3,4],[5,6,7],[8,9],[10]]

testQ6T1 =
    TestCase
    (do let e = minRowLength tableQ6T1
        assertBool
           ("Result " ++ show e ++ " does not match the expected result: "++ show 1)
           (e == 1)
    )

testQ6T2 =
    TestCase
    (do let e = minRowLength tableQ5T1
        assertBool
            ("Result " ++ show e ++ " does not match the expected result: "++ show 3)
            (e == 3))

doubleNum :: Int -> Int
doubleNum x = 2 * x

tableQ7T1 :: Table Int
tableQ7T1 = Table [[2, 4, 6],[8, 10, 12],[14,16,18]]

tableQ7T2 :: Table Bool
tableQ7T2 = Table [[False, True, False, True],[False, True, False],[True, False],[True]]

binarize :: Bool -> Int
binarize True = 1
binarize False = 0

inTableQ7T3 :: Table Bool
inTableQ7T3 =  Table [[True, False, True],[True, True, False],[False,False,True]]

tableQ7T3 :: Table Int
tableQ7T3 = Table [[1, 0, 1],[1, 1, 0],[0, 0, 1]]

testQ7T1 =
    TestCase
    (do let e = (mapTable doubleNum tableQ5T1)
        assertEqual
            ("Result " ++ show e ++ " does not equal the expected result: " ++ show tableQ7T1)
            e
            tableQ7T1
    )

testQ7T2 =
    TestCase
    (do let e = (mapTable even tableQ6T1)
        assertBool
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show tableQ7T2)
            (e == tableQ7T2)
    )

testQ7T3 =
    TestCase
    (do let e = (mapTable binarize inTableQ7T3)
        assertBool
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show tableQ7T3)
            (e == tableQ7T3)
    )

tableQ8T1 :: Table (Int, Int)
tableQ8T1 = Table [[(0,0)]]

tableQ8T2 :: Table (Int, Int)
tableQ8T2 = Table [[(0,0),(0,1),(0,2)],[(1,0),(1,1),(1,2)],[(2,0),(2,1),(2,2)]]

testQ8T1 = 
    TestCase
    (do let e = indexesTable 1
        assertBool
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show tableQ8T1)
            (e == tableQ8T1)
    )

testQ8T2 =
    TestCase
    (do let e = indexesTable 3
        assertBool
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show tableQ8T2)
            (e == tableQ8T2)
    )

tableQ9T1 :: Table Int
tableQ9T1 = Table [[0,1,2],[1,2,3],[2,3,4]]

diag :: Int -> Int -> Bool
diag a b  = (a == b)

tableQ9T2 :: Table Bool
tableQ9T2 = Table [[True, False, False],[False, True, False], [False, False, True]]

tableQ9T3 :: Table Int
tableQ9T3 = Table [[0]]

testQ9T1 = 
    TestCase
    (do let e = tableFromElements add 3
        assertBool
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show tableQ9T1)
            (e == tableQ9T1)
    )

testQ9T2 = 
    TestCase
    (do let e = tableFromElements diag 3
        assertBool
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show tableQ9T2)
            (e == tableQ9T2)
    )

testQ9T3 = 
    TestCase
    (do let e = tableFromElements add 1
        assertEqual
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show tableQ9T3)
            e
            tableQ9T3
    )


inTableQ10T2 :: Table Integer
inTableQ10T2 = Table [[10],[8,9],[5,6,7],[1,2,3,4]]

tableQ10T1 :: Table Integer
tableQ10T1 = Table [[1]]

tableQ10T2 :: Table Integer
tableQ10T2 = Table [[10]]

inTableQ10T3 :: Table Integer
inTableQ10T3 = Table [[1],[2],[3],[4],[5],[6]]

tableQ10T3 :: Table Integer
tableQ10T3 = Table [[1]]

testQ10T1 =
    TestCase
    (do let e = squarifyTable tableQ6T1
        assertBool
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show tableQ10T1)
            (e == tableQ10T1)
    )

testQ10T2 =
    TestCase
    (do let e = squarifyTable inTableQ10T2
        assertBool
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show tableQ10T2)
            (e == tableQ10T2)
    )


testQ10T3 =
    TestCase
    (do let e = squarifyTable inTableQ10T3
        assertBool
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show tableQ10T3)
            (e == tableQ10T3)
    )

arrQ11T1 :: [Integer]
arrQ11T1 = [1, 2, 3, 4, 5, 6]

arrQ11T2 :: [Integer]
arrQ11T2 = [10, 8, 5, 1]

testQ11T1 =
    TestCase
    (do let e = firstColumn inTableQ10T3
        assertBool
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show arrQ11T1)
            (e == arrQ11T1)
    )


testQ11T2 =
    TestCase
    (do let e = firstColumn inTableQ10T2
        assertEqual
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show arrQ11T2)
            e
            arrQ11T2
    )

inTableQ12T1 :: Table (Int, Int)
inTableQ12T1 =  Table [[(0,0),(0,1),(0,2)], [(1,0),(1,1),(1,2)],[(2,0),(2,1),(2,2)]]

tableQ12T1 :: Table (Int, Int)
tableQ12T1 = Table [[(0,0),(1,0),(2,0)], [(0,1),(1,1),(2,1)], [(0,2),(1,2),(2,2)]]

inTableQ12T2 :: Table Bool
inTableQ12T2 = Table [[True, False, True],[True, False, False],[True, True, False]]

tableQ12T2 :: Table Bool
tableQ12T2 = Table [[True, True, True], [False, False, True], [True, False, False]]

tableQ12T3 :: Table Integer
tableQ12T3 = Table [[1, 0, 0], [0, 1, 0], [0, 0, 1]]

testQ12T1 =
    TestCase
    (do let e = transposeTable inTableQ12T1
        assertEqual
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show tableQ12T1)
            e
            tableQ12T1
    )

testQ12T2 =
    TestCase
    (do let e = transposeTable inTableQ12T2
        assertEqual
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show tableQ12T2)
            e 
            tableQ12T2
    )

testQ12T3 =
    TestCase
    (do let e = transposeTable tableQ12T3
        assertEqual
            ("Result " ++ show e ++ "does not equal the expected result: " ++ show tableQ12T3)
            e
            tableQ12T3
    )

testAll = 
    TestList [
        TestLabel "Q1" testQ1,
        TestLabel "Q2" testQ2,
        TestLabel "Q2T2" testQ2T2,
        TestLabel "Q3" testQ3,
        TestLabel "Q3T1" testQ3T1,
        TestLabel "Q4T1" testQ4,
        TestLabel "Q5T1" testQ5T1,
        TestLabel "Q5T2" testQ5T2,
        TestLabel "Q5T3" testQ5T3,
        TestLabel "Q5T4" testQ5T4,
        TestLabel "Q6T1" testQ6T1,
        TestLabel "Q6T2" testQ6T2,
        TestLabel "Q7T1" testQ7T1,
        TestLabel "Q7T2" testQ7T2,
        TestLabel "Q7T3" testQ7T3,
        TestLabel "Q8T1" testQ8T1,
        TestLabel "Q8T2" testQ8T2,
        TestLabel "Q9T1" testQ9T1,
        TestLabel "Q9T2" testQ9T2,
        TestLabel "Q9T3" testQ9T3,
        TestLabel "Q10T1" testQ10T1,
        TestLabel "Q10T2" testQ10T2,
        TestLabel "Q10T3" testQ10T3,
        TestLabel "Q11T1" testQ11T1,
        TestLabel "Q11T2" testQ11T2,
        TestLabel "Q12T1" testQ12T1,
        TestLabel "Q12T2" testQ12T2,
        TestLabel "Q12T3" testQ12T3
    ]

rt = runTestTT

main = do
    rt $ testAll