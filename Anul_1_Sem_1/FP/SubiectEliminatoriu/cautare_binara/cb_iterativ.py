def cb_iterativ(el, l):
    if len(l) == 0:
        return False
    if el < l[0]:
        return False
    if el > l[len(l) - 1]:
        return False
    left = 0
    right = len(l)
    while right - left > 1:
        m = (left + right) // 2
        if el <= l[m]:
            right = m
        else:
            left = m
    return right

def main():
    l = [2, 3, 5, 7, 8]
    el = 3
    print(cb_iterativ(el, l))

main()