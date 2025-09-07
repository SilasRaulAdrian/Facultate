def bubble_sort(l):
    sortat = False
    while not sortat:
        sortat = True
        for i in range(len(l) - 1):
            if l[i + 1] < l[i]:
                l[i], l[i + 1] = l[i + 1], l[i]
                sortat = False

def main():
    l = [3, 2, 5, 4, 9]
    bubble_sort(l)
    for i in range(len(l)):
        print(l[i])

main()