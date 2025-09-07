n1 = int(input("Provide a number:"))
n2 = int(input("Provide a number:"))
cn1 = n1
cn2 = n2
fr = [0] * 10
if n1 == 0 and n2 == 0:
    print("Numerele au proprietatea P")
    exit()
ok = 0
while cn1 != 0:
    fr[cn1 % 10] = fr[cn1 % 10] + 1
    cn1 = cn1 // 10
while cn2 != 0:
    if fr[cn2 % 10] != 0:
        ok = 1
    else:
        ok = 0
        break
    cn2 = cn2 // 10

if ok == 1:
    print("Numerele au proprietatea P")
else:
    print("Numerele nu au proprietatea P")

exit()
