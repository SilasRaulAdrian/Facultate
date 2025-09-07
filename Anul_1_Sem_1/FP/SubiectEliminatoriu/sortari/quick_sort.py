def partition(l, left, right):
    pivot = l[left]
    i = left
    j = right
    while i != j:
        while l[j] >= pivot and i < j:
            j -= 1
        l[i] = l[j]
        while l[i] <= pivot and i < j:
            i += 1
        l[j] = l[i]
    l[i] = pivot
    return i

def quick_sort(l, left, right):
    pos = partition(l, left, right)

    if left < pos - 1:
        quick_sort(l, left, pos - 1)
    if pos + 1 < right:
        quick_sort(l, pos + 1, right)

def main():
    l = [3, 2, 5, 4, 9]
    quick_sort(l, 0, len(l) - 1)
    for i in range(len(l)):
        print(l[i])

main()