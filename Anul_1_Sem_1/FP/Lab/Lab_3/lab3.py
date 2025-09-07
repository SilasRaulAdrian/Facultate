def menu():
    print("Pentru citirea unei liste de numere intregi apasati tasta 1")
    print("Pentru gasirea unei secvente de lungime maxima cu proprietatea ca are suma maxima apasati tasta 2")
    print("Pentru gasirea unei secvente de lungime maxima cu proprietatea ca reprezinta o secventa sub forma de munte apasati tasta 3")
    print("Pentru iesirea din aplicatie apasati tasta 4")

def prop11(n, a):
    smax = -1e9
    lmax = -1e9
    left = 0
    right = 0

    for i in range(n):
        s = 0
        for j in range(i, n):
            s += a[j]
            if s > smax:
                smax = s
                lmax = j - i + 1
                left = i
                right = j + 1
            elif s == smax:
                if j - i + 1 > lmax:
                    lmax = j - i + 1
                    left = i
                    right = j + 1

    if left != 0 and right != 0:
        for i in range(left, right):
            print(a[i])
    else:
        print("Nu exista o astfel de secventa")


def prop15(n, a):
    lmax = 0
    inc = 0
    sf = 0

    i = 1
    while i < n - 1:
        if a[i - 1] < a[i] > a[i + 1]:
            left = i - 1
            right = i + 1

            while left != 0 and a[left - 1] < a[left]:
                left -= 1

            while right != n - 1 and a[right] > a[right + 1]:
                right += 1

            len = right - left + 1

            if len > lmax:
                lmax = len
                inc = left
                sf = right + 1

            i = right
        else:
            i += 1
    if inc != 0 and sf != 0:
        for i in range(inc, sf):
            print(a[i])
    else:
        print("Nu exista o astfel de secventa")

def main():
    n = 0
    a = []
    while True:
        menu()
        option = int(input("Apasati o tasta:"))
        match option:
            case 1:
                n = int(input("Introduceti dimensiunea listei:"))
                for i in range(n):
                    a.append(int(input("Introduceti numere in lista:")))
            case 2:
                prop11(n, a)
                n = 0
                a = []
            case 3:
                prop15(n, a)
                n = 0
                a = []
            case 4:
                exit(0)

main()

