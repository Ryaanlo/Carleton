module A5Base 
    -- export only the named types and functions; the ".." means to 
    -- export all the constructors of Instr
    (Instr (..), ST, incrPC, setPC, readMem, writeMem, 
     load, fetch, runST, runSTAndDump)
where

{-
The type S represents the state of our machine.

An object (S p pc mem) of type S represents a state as follows.

p: the program being executed.  A program is just a list of instructions.

pc: the "program counter", which is the position in the list p of instructions of the instruction to execute next.  It is set to 0 (the first instruction) when a new program is loaded.

mem: memory.  Memory is a list of pairs represent a mapping from memory addresses (integers) to values (also integers).
-}
data S = S [Instr] Int [(Int,Int)]

-- machine language instructions, semantics below
data Instr =
        Add Int Int Int
    |   Sub Int Int Int
    |   Set Int Int
    |   Copy Int Int
    |   Branch Int Int Int
    |   Goto Int
    |   Quit Int
    deriving Show

{-
Instruction semantics.

The Instr constructor arguments are all of type Int.  They usually refer to memory addresses, but, depending on the instruction, they might instead refer to "program counters", i.e. the "line number" the program execution is currently at.  We we say "at k" below, for k an integer, we are referring to a memory address.

Add i j k: add the values at (memory address) i and (memory address) j and store the result at (memory address) k.  Add one to the program counter.

Sub i j k: subtract the value at j from the value at i and store the result at k.  Add one to the program counter.

Set i j: set the address i to store the number j.  Add one to the program counter.

Copy i j: store the number at i at j as well.  Add one to the program counter.

Branch i j k: if the number at i is 0 then set the program counter to j else set it to k.

Goto k: set the the program counter to k.

Quit i: exit the program, returning the value at i as the result.

-}

{-
Program semantics.  

Given a program p (list of instructions) and a list of inputs l, the initial state is

S p 0 mem0

where mem0 is a list of pairs representing the mapping where address 0 has the first input (first member of l), address 1 has the second input (second member of l), and so on.

To run the program we repeatedly execute the current instruction, stopping when the instruction is Quit k.  The value k is the final result of running the program.  The "current" instruction in state (S p pc mem) is the instruction at position pc in the list p. 

-}

{-
The semantics above is written in an imperative style: at each step we update state.  The usual way to represent these kinds of operations is with state transformers.  Every operation on state, such as the semantics of an instruction, updating memory, fetching an instruction from the state etc, will be a state transformer.

ST is a "monad", so the operations return and >>= (and >>), plus the "do" syntax, are available for it.  You will only need the "do" syntax.
-}
data ST a = ST (S -> (a, S))

{-
Any program that works with state (S) and state transformers (ST a) is limited to using the following operations.  They are the only ones exported from A5Base.  Note that there are no operations involving S directly.
-}
setPC :: Int -> ST ()
incrPC :: ST ()
readMem :: Int -> ST Int
writeMem :: Int -> Int -> ST ()
load :: [Instr] -> [Int] -> ST ()
fetch :: ST Instr
runST :: ST a -> a
runSTAndDump :: Show a => ST a -> IO()

---

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

-- same definitions as for the "counter" example in class
instance Monad ST where
    (>>=) (ST st1) h = 
        ST $
        \s -> let (x, s1) = st1 s
                  ST st2 = h x
              in st2 s1

{- 
Implementations of the exported functions, plus some helper functions.
-}

setPC i = ST $ \(S p pc mem) -> ((), S p i mem)
incrPC = ST $ \(S p pc mem) -> ((), S p (pc+1) mem)
readMem i = ST $ \s@(S p pc mem) -> (memLookup i mem, s)
writeMem i k = ST $ \(S p pc mem) -> ((), S p pc $ memUpdate i k mem)
load p inputs = ST $ \_ -> ((), S p 0 (initMem inputs))
fetch = ST $ \s@(S p pc mem) -> (programLookup pc p, s)
runST (ST st) = fst $ st (S [] 0 [])

initMem :: [Int] -> [(Int,Int)]
initMem l = zip [0 .. length l -1] l

runSTAndDump (ST st) = do
    let (result, S p pc mem) = st (S [] 0 [])
    putStrLn $ "Result of program: " ++ show result
    putStrLn $ "Stored program:"
    dumpProgram p
    putStrLn $ "Program counter: " ++ show pc
    putStrLn "Memory dump:"
    mapM (\x -> (putStrLn $ "    " ++ show x)) mem -- print out each pair in mem
    return ()

dumpProgram :: [Instr] -> IO()
dumpProgram l = do
    mapM printInstr (zip [0 .. length l - 1] l) -- print instrs with line #s
    return ()
    where printInstr (lineNum, instr) =
            putStrLn $ "    " ++ show lineNum ++ ": " ++ show instr


programLookup :: Int -> [Instr] -> Instr
programLookup pc p =
    if pc>=0 && pc < length p then p!!pc
    else error "programLookup: pc out of range"
memLookup :: Int -> [(Int,Int)] -> Int
memLookup i l = 
    case lookup i l of
        Just k -> k
        _ -> 0

memUpdate :: Int -> Int -> [(Int,Int)] -> [(Int,Int)]
memUpdate i k [] = [(i,k)]
memUpdate i k ((j,k'):l) | i==j = (i,k):l
memUpdate i k (p:l) = p : memUpdate i k l