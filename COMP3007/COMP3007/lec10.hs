foldr' :: (a -> b -> b) -> b -> [a] -> b
foldr' f z [] = z
foldr' f z (x:l) = f x (foldr' f z l)

foldl' :: (b -> a -> b) -> b -> [a] -> b
foldl' f z [] = z
foldl' f z (x:l) = foldl' f (f z x) l

-- example of foldl: reverse a list.
-- >>> rev [1,2,3]
-- [3,2,1]
rev :: [a] -> [a]
rev l = foldl (\z x -> x:z) [] l


{-
Symbolic execution:
foldl' f z [1,2,3] = 
foldl' f (f z 1) [2,3] =
foldl' f (f (f z 1) 2) [3] =
foldl' f (f (f (f z 1) 2) 3) [] =
f (f (f z 1) 2) 3 =
((z `f` 1) `f` 2) `f` 3
(note: the implementation of Haskell doesn't need a recursive call stack for this because there are no pending operations needing the result of a recursive call, but there is stack of pending calls to f because Haskell is lazy/CBN so, e.g. 0 `f` 1 (taking z=0) is not evaluated until all the recursive calls are done.

Interesting point about the above wrt recursion, cf factorial:
fact (n+1) = (n+1) * (fact n)
fact 3 = 3 * (fact 2) = 3 * 2 * (fact 1) =
    3 * 2 * 1 * (fact 0) = 3 * 2 * 1 * 1 = 6
Note that recursive calls of fact get buried under other operations.  For foldl': recursive calls are always at the top level. i.e. it is **tail recursive**.
We will *not* be covering efficiency of Haskell programs, so who cares about tail recursion.
foldr' f z [1,2,3] =
f x (foldr' f z [2,3]) =
f 1 (f 2 (foldr' f z [3])) =
f 1 (f 2 (f 3 (foldr' f z []))) =
f 1 (f 2 (f 3 z)) =
1 `f` (2 `f` (3 `f` z))
-}


{- Extended example: freq/rank from slides.
   For an example result, see text, textFreq and textRank.
-}

{-
The type Maybe (a useful built-in).  Idea: for computations  that might fail, return Nothing if failed otherwise return (Just result), i.e. Just is a just a wrapper for the result.
>>> :type Just
>>> :type Nothing
Just :: a -> Maybe a
Nothing :: Maybe a
-}

{- 
(Dict a) is Dictionaries with String keys and values of type a. 
Invariant: one entry (pair) per key.  Use (Dict a) abstractly through dictEmpty, DictAdd and dictFold.
-}
type Dict a = [(String,a)]

dictEmpty :: Dict a
dictEmpty = []

-- Assumes x not a key in dict
dictAdd :: String -> a -> Dict a -> Dict a
dictAdd x u d = (x,u) : d

{-
dictFold f z [(x1,v1), (x2,v2), (xn,vn)] 
=
f x1 v1 (f x2 v2 (...(f xn vn z)...)
-}
dictFold :: (String -> a -> b -> b) -> b -> Dict a -> b
dictFold f z [] = z
dictFold f z ((x,v):al) =
    f x v (dictFold f z al)

--------------
-- No recursion or access to Dict implementation after this point!

dictFromPairs :: [(String, a)] -> Dict a
dictFromPairs =
    foldr (\(x,v) z -> dictAdd x v z) dictEmpty

-- [("x",1),("y",2),("z",3)]
dictExample :: Dict Int
dictExample = dictFromPairs $ zip (words "x y z") [1,2,3]

d :: Dict Int
d = dictExample

dictLookup :: String -> Dict a -> Maybe a
dictLookup x =
    let f y v z = if x==y then Just v else z in
    dictFold f Nothing 

isJust :: Maybe a -> Bool
isJust Nothing = False
isJust (Just _) = True

dictBound :: String -> Dict a -> Bool
dictBound x = isJust . dictLookup x

dictUpdate :: String -> a -> Dict a -> Dict a
dictUpdate x u al = 
    let f y v z = if x==y then dictAdd x u z 
                  else dictAdd y v z in
    if dictBound x al then dictFold f dictEmpty al
    else dictAdd x u al

dictMap :: (String -> a -> b) -> Dict a -> Dict b
dictMap f =
    dictFold (\x v z -> dictAdd x (f x v) z) dictEmpty

dictRemove :: String -> Dict a -> Dict a
dictRemove x =
    dictFold (\y v z -> if x==y then z else dictAdd y v z) dictEmpty

-- freq text n: dictionary giving the number of occurrences 
-- of each word in text 
freq :: [String] -> Dict Int
freq = foldr updateFreq dictEmpty

updateFreq :: String -> Dict Int -> Dict Int
updateFreq x d =
    case dictLookup x d of
        Nothing -> dictAdd x 1 d
        Just v -> dictUpdate x (v+1) d

{- 
(rank freqs n): the first n of the list of all words appearing in freqs, in decreasing order of frequency, each paired with their frequency
-}
rank :: Int -> Dict Int -> [(String,Int)]
rank n =
    take n . dictFold insertPair dictEmpty

{- 
(insertPair x n l): assuming l is sorted according to >= on second components, insert (x,n) such that the resulting list is still sorted
-}
insertPair :: String -> Int -> [(String,Int)] -> [(String,Int)]
insertPair x u = 
    let headSwap (y,v) ((z,w):l) =
            if v >= w then (y,v):(z,w):l
            else (z,w):(y,v):l 
        headSwap p [] = [p] in
    foldr headSwap [(x,u)]

textFreq = freq text
textRank = rank 10 textFreq    

texts :: [String]
texts =
   ["k a s j f k l j q e w i o j e q w j f a d j v n e q r w u f",
    "e q w i o h q e r u w g h a k d a d h s j l k f h q e w i p",
    "t h q 4 u h d j s v n q e w p o t h w i l a j f s j e i w g",
    "h s a d i f h j e w i i u o h e v h i h f i u e h u h 4 i r",
    "4 3 h q u i o w h u q f h u e i w f e f e a f a f w e k j f",
    "g e u y i y g 4 y u r g o u r h q u f g e y u f g e o u i w",
    "f h g e q u w i r y 8 0 7 1 2 g q y u e i w r f g e w u q i",
    "f h q e f h e q i o p w q t 4 u i r t y q o r q 7 r y h e d",
    "i u o n r n 3 b o c 7 5 r y 7 t y 4 n o k u e e d e d e d d"]

text :: [String]
text = words (concat texts)

       



