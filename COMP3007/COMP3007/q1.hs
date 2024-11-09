type N = Integer
type B = N -> N -> N
aye = \x y -> x
nay = \x y -> y

-- ite b r s = r if b is aye and s if b is nay
ite :: B -> N -> N -> N
ite b r s = if (b == aye) then r else s