import os

def suma():
    l = int(input("Provide a number:"))
    s = 0
    for i in range(l):
        a = int(input("Provide a number:"))
        s = s + a
    return s

print(suma())