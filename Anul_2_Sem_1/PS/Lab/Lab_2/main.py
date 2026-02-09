import random
from math import dist, pi

from matplotlib.pyplot import plot, grid, title, show, axis


def same_birthday_simulations(n, nr_sim):
    count = 0
    for _ in range(nr_sim):
        birthdays = []
        for _ in range(n):
            birthdays.append(random.randint(1, 365))
        if len(birthdays) != len(set(birthdays)):
            count += 1
    return count / nr_sim

def same_birthday_probability(n):
    if n > 365:
        return 1.0
    prob_unique = 1.0
    for i in range(n):
        prob_unique *= (365 - i) / 365
    return 1 - prob_unique

def plot_birthday_probabilities():
    nr_pers = range(2, 51)
    ys = [same_birthday_simulations(n, 1000) for n in nr_pers]
    plot(nr_pers, ys, "r*")
    ys_exact = [same_birthday_probability(n) for n in nr_pers]
    plot(nr_pers, ys_exact, "g*")
    show()

def puncte_patrat(N, cerinta):
    axis('square')
    axis((0, 1, 0, 1))
    count = 0
    A, B, C, D, E = [0, 0], [1, 0], [1, 1], [0, 1], [0.5, 0.5]
    match cerinta:
        case 1:
            for _ in range(N):
                P = [random.random(), random.random()]
                if dist(E, P) < 0.5:
                    plot(P[0], P[1], 'r*')
                    count += 1
                else:
                    plot(P[0], P[1], 'b.')
        case 2:
            for _ in range(N):
                P = [random.random(), random.random()]
                if dist(E, P) < min([dist(A, P), dist(B, P), dist(C, P), dist(D, P)]):
                    plot(P[0], P[1], 'r*')
                    count += 1
                else:
                    plot(P[0], P[1], 'b.')
        case 3:
            for _ in range(N):
                P = [random.random(), random.random()]
                nr_tri_obtuze = (pow(dist(P, A), 2) + pow(dist(P, D), 2) < 1) + (pow(dist(P, A), 2) + pow(dist(P, B), 2) < 1) + (pow(dist(P,B), 2) + pow(dist(P, C), 2) < 1) + (pow(dist(P, C), 2) + pow(dist(P, D), 2) < 1)
                if nr_tri_obtuze == 2:
                    plot(P[0], P[1], 'r*')
                    count += 1
                else:
                    plot(P[0], P[1], 'b.')
    return count / N

def first():
    n = 23
    nr_sim = 1000
    probability = same_birthday_simulations(n, nr_sim)
    print(f"Probabilitatea ca cel putin 2 persoane din {n} sa aiba aceeasi zi de nastere este: {probability:.4f}")
    exact_probability = same_birthday_probability(n)
    print(f"Probabilitatea exacta calculata matematic este: {exact_probability:.4f}")
    plot_birthday_probabilities()

def second():
    print(puncte_patrat(500, 1))
    #print(puncte_patrat(500, 2))
    #print(puncte_patrat(500, 3))
    print()
    show()
    #print(puncte_patrat(1000, 1))
    print(puncte_patrat(1000, 2))
    #print(puncte_patrat(1000, 3))
    print()
    show()
    #print(puncte_patrat(2000, 1))
    #print(puncte_patrat(2000, 2))
    print(puncte_patrat(2000, 3))
    print()
    show()
    print(pi / 4)
    print(1 / 2)
    print(pi / 2 - 1)

if __name__ == "__main__":
    n = int(input("Alegeti exercitiul (1 sau 2): "))
    if n == 1:
        first()
    if n == 2:
        second()