def generate_parentheses_recursive(n):
    def backtrack(sequence, open_count, close_count):
        if len(sequence) == n:
            result.append("".join(sequence))
            return

        if open_count < n // 2:
            sequence.append("(")
            backtrack(sequence, open_count + 1, close_count)
            sequence.pop()

        if close_count < open_count:
            sequence.append(")")
            backtrack(sequence, open_count, close_count + 1)
            sequence.pop()

    result = []
    backtrack([], 0, 0)
    return result

def generate_parentheses_iterative(n):
    stack = [("", 0, 0)]
    result = []

    while stack:
        sequence, open_count, close_count = stack.pop()

        if len(sequence) == n:
            result.append(sequence)
            continue

        if open_count < n // 2:
            stack.append((sequence + "(", open_count + 1, close_count))

        if close_count < open_count:
            stack.append((sequence + ")", open_count, close_count + 1))

    return result

def main():
    n = int(input("n = "))
    print("Rezultate recursiv:", generate_parentheses_recursive(n))
    print("Rezultate iterativ:", generate_parentheses_iterative(n))

if __name__ == "__main__":
    main()