module A5 where

import A5Base

{-
Assignment 5.

SUBMISSION INSTRUCTIONS:
- do not modify A5Base
- do not modify this file except for the "undefined" parts
- do not change the name of this file
- your submission *must* load without error (warnings are ok)

The assignment is to implement a simulator for a very simple CPU.  The details of the machine language and how the machines state is represented are given in the A5Base module/file

Fill in the "undefined" parts below.  Do not change any of the surrounding code.

You will need to understand the code in A5Base, but you will only be using the objects exported by the module; the rest the types and functions defined there are hidden.

In particular, you get:

Instr: the type of machine-language instructions, together will all the constructors

ST a: the type of state transformers for the machine

The following operations:

setPC :: Int -> ST ()
incrPC :: ST ()
readMem :: Int -> ST Int
writeMem :: Int -> Int -> ST ()
[ load :: [Instr] -> [Int] -> ST () ]
[ fetch :: ST Instr ]
[ runST :: ST a -> a ]
[ runSTAndDump :: Show a => ST a -> IO() ]

See A5Base for more on the above.  You won't need to use the last four of the above in your own code, since the code already provided takes care of using them.
-}

-- run the program on the given inputs, returning the final output
run :: [Instr] -> [Int] -> Int
run p input =
    runST $ do {load p input; stepper}

-- run the program on the given inputs and print out the final state
dump :: [Instr] -> [Int] -> IO ()
dump p input= 
    runSTAndDump $ do {load p input; stepper}

stepper :: ST Int
stepper = do
    instr <- fetch
    case instr of
        Add i j k -> doAdd i j k >> stepper
        Sub i j k -> doSub i j k >> stepper
        Set i j -> doSet i j >> stepper
        Copy i j -> doCopy i j >> stepper
        Branch i j k -> doBranch i j k >> stepper
        Goto i -> doGoto i >> stepper
        Quit i -> doQuit i

doAdd :: Int -> Int -> Int -> ST()
doAdd i j k = do
    a<-(readMem i)
    b<-(readMem j)
    writeMem k (a+b)
    incrPC

doSub :: Int -> Int -> Int -> ST()
doSub i j k = do
    a<-(readMem i)
    b<-(readMem j)
    writeMem k (a-b)
    incrPC

doSet :: Int -> Int -> ST()
doSet i j = do
    writeMem i j
    incrPC

doCopy :: Int -> Int -> ST()
doCopy i j = do
    a<-(readMem i)
    writeMem j a
    incrPC

doBranch :: Int -> Int -> Int -> ST()
doBranch i j k = do
    a<-(readMem i)
    if a == 0 then setPC j else setPC k
    

doGoto :: Int -> ST()
doGoto i = do
    setPC i

doQuit :: Int -> ST Int
doQuit i = do
    readMem i


-- Example programs --

-- Example 1: do one operation and quit.
program1 = [Add 0 1 2, Quit 2]
input1 = [4,5] :: [Int]

{- 
Example 2: multiply two numbers.

Memory usage:
0,1: inputs
2: accumulated product
3: counter
4: the value 1
-}
program2 =
    [Set 2 0
    ,Copy 0 3
    ,Set 4 1
    ,Branch 3 4 5
    ,Quit 2
    ,Add 1 2 2
    ,Sub 3 4 3
    ,Goto 3
    ]

input2 = [4,5] :: [Int]