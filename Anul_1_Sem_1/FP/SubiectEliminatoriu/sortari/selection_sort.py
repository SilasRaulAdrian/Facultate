def selectionSort(l):
    for i in range(0, len(l) - 1):
        ind = i
        for j in range(i + 1, len(l)):
            if l[j] < l[ind]:
                ind = j
        if i < ind:
            aux = l[i]
            l[i] = l[ind]
            l[ind] = aux

def main():
    l = [3, 2, 5, 4, 9]
    selectionSort(l)
    for i in range(len(l)):
        print(l[i])

main()
