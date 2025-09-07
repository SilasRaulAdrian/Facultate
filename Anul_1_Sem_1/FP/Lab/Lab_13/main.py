def is_valid(v, pos):
    """ Verifica daca permutarea curenta respecta conditia """
    for j in range(pos):
        if abs(v[pos] - v[j]) == 1:
            return True
    return False

def backtrack_recursiv(n, perm, used, result):
    """ Functie recursiva pentru generarea permutarilor """
    if len(perm) == n:
        result.append(perm[:])
        return

    for i in range(1, n + 1):
        if not used[i]:
            perm.append(i)
            if len(perm) == 1 or is_valid(perm, len(perm) - 1):
                used[i] = True
                backtrack_recursiv(n, perm, used, result)
                used[i] = False
            perm.pop()

def generate_permutations_recursiv(n):
    """ Genereaza toate permutarile de lungime n recursiv """
    result = []
    used = [False] * (n + 1)
    perm = []
    backtrack_recursiv(n, perm, used, result)
    return result

def generate_permutations_iterativ(n):
    """ Genereaza toate permutarile de lungime n iterativ """
    stack = [(0, [], [False] * (n + 1))]  # (nivelul curent, permutarea curenta, starea used)
    result = []

    while stack:
        level, perm, used = stack.pop()

        if level == n:
            result.append(perm)
            continue

        for i in range(1, n + 1):
            if not used[i]:
                if level == 0 or any(abs(i - perm[j]) == 1 for j in range(level)):
                    new_perm = perm + [i]
                    new_used = used[:]
                    new_used[i] = True
                    stack.append((level + 1, new_perm, new_used))

    return result


def main():
    n = int(input("n = "))
    perm_recursiv = generate_permutations_recursiv(n)
    print("Permutari valide (recursiv):")
    for p in perm_recursiv:
        print(p)
    perm_iterativ = generate_permutations_iterativ(n)
    print("Permutari valide (iterativ):")
    for p in perm_iterativ:
        print(p)

if __name__ == "__main__":
    main()