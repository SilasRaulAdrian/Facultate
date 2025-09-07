import os

def prim():
    a = int(input("Provide a number:"))
    if a < 2:
        return 0
    if a == 2:
        return 1
    if a % 2 == 0:
        return 0
    for i in range(3, a, 2):
        if a % i == 0:
            return 0
    return 1
   
print(prim())
