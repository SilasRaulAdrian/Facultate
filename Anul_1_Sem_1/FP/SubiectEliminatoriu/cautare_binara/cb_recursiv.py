def cauta(el, l, left, right):
    if left >= right - 1:
        return right
    m = (left  + right) // 2
    if el <= l[m]:
        return cauta(el, l, left, m)
    return cauta(el, l, m, right)

def cb_rec(el, l):
    if len(l) == 0:
        return 0
    if el < l[0]:
        return 0
    if el >= l[len(l) - 1]:
        return len(l)
    return cauta(el, l, 0, len(l))

def main():
    l = [2, 3, 4, 7, 8]
    el = 3
    print(cb_rec(el, l))

main()