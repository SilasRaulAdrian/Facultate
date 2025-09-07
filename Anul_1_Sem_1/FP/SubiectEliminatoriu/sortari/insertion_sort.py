def insertion_sort(l):
    for i in range(1, len(l)):
        ind = i - 1
        a = l[i]
        while ind >= 0 and a < l[ind]:
            l[ind + 1] = l[ind]
            ind -= 1
        l[ind + 1] = a

def main():
    l = [3, 2, 5, 4, 9]
    insertion_sort(l)
    for i in range(len(l)):
        print(l[i])

main()