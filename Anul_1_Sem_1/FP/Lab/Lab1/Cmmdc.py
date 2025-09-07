import os

def cmmdc():
    a = int(input("Provide a number:"))
    b = int(input("Provide a number:"))
    while b != 0:
        r = a % b
        a = b
        b = r
    return a

print(cmmdc())
