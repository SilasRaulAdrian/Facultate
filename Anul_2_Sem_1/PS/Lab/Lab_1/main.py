from random import sample
from math import factorial, perm, comb
from itertools import permutations, combinations, combinations_with_replacement

# 2
# a)
print(list(permutations("word")))

# b)
print(factorial(len("word")))

# c)
print(sample("word", 4))

# 3
def aranjamente(lista, k, nr_total, aleator):
    if aleator:
        return sample(lista, k)
    elif nr_total:
        return perm(len(lista), k)
    else:
        return list(permutations(lista, k))

# a)
print(aranjamente("word", 2, False, False))

# b)
print(aranjamente("word", 2, True, False))

# c)
print(aranjamente("word", 2, False, True))

# d)

def combinari(lista, k, nr_total, aleator):
    if aleator:
        return sample(lista, k)
    elif nr_total:
        return comb(len(lista), k)
    else:
        return list(combinations(lista, k))

print(combinari("word", 2, False, False))

# e)
print(combinari("word", 2, True, False))

# f)
print(combinari("word", 2, False, True))

# 4
print(list(combinations_with_replacement("ABCDE", 4)))

# 5
def asezare_persoane(nr_persoane, nr_scaune):
    scaune_alege = comb(nr_scaune - nr_persoane + 1, nr_persoane)

    aranjamente_persoane = factorial(nr_persoane)
    return scaune_alege * aranjamente_persoane

print(asezare_persoane(5, 12))