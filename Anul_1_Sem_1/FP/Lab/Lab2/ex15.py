import os

def prim(x):
    if x < 2:
        return 0
    if x == 2:
        return 1
    if x % 2 == 0:
        return 0
    for i in range(3, x, 2):
        if x % i == 0:
            return 0
    return 1

a = int(input("Provide a number:"))
n = a - 1

if a <= 2:
    print("Nu exista un numar prim mai mic decat numarul dat")
    exit()

while n != 0:
    if prim(n) == 0:
        n = n - 1
    else:
        print(str(n) + " este cel mai mare numar prim mai mic decat numarul dat")
        exit()

if n == 0:
    print("Nu exista un numar prim mai mic decat numarul dat")

