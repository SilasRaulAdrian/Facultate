def merge_sort(l, start, end):
    if end - start <= 1:
        return
    m = (start + end) // 2
    merge_sort(l, start, m)
    merge_sort(l, m, end)
    merge(l, start, end, m)

def merge(l, start, end, m):
    left = l[start:m]
    right = l[m:end]

    i = j = 0
    k = start

    while i < len(left) and j < len(right):
        if left[i] < right[j]:
            l[k] = left[i]
            i += 1
        else:
            l[k] = right[j]
            j += 1
        k += 1

    while i < len(left):
        l[k] = left[i]
        i += 1
        k += 1

    while j < len(right):
        l[k] = right[j]
        j += 1
        k += 1

def main():
    l = [3, 2, 5, 4, 9]
    merge_sort(l, 0, len(l) - 1)
    for i in range(len(l)):
        print(l[i])

main()