import random
from random import sample
from random import randrange
from matplotlib.pyplot import bar, hist, grid, show, legend
from scipy.stats import binom

def MontyHall_sim(schimba_usa, n_sim, afisare):
    cnt = 0
    for _ in range(n_sim):
        asezare = ['c', 'c', 'c']
        masina, capra1, capra2 = sample([0, 1, 2], 3)
        asezare[masina] = 'm'
        alegere_initiala = random.randint(0, 2)

        if alegere_initiala == masina:
            aleg_prezentator = sample([capra1, capra2], 1)[0]
        elif alegere_initiala == capra1:
            aleg_prezentator = capra2
        else:
            aleg_prezentator = capra1

        if schimba_usa:
            aleg_finala = ({0, 1, 2} - {alegere_initiala, aleg_prezentator}).pop()
        else:
            aleg_finala = alegere_initiala

        rezultat = aleg_finala == masina
        cnt += rezultat

        if afisare:
            print(f"Alegere initiala: Usa {alegere_initiala + 1} ({asezare[alegere_initiala]})")
            print(f"Prezintatorul deschide usa {aleg_prezentator + 1} ({asezare[aleg_prezentator]})")
            if schimba_usa:
                print(f"Schimbam la usa {aleg_finala + 1} ({asezare[aleg_finala]})")
            else:
                print(f"Ramane la usa {aleg_finala + 1} ({asezare[aleg_finala]})")
            if rezultat:
                print("Rezultat: Castigat masina!\n")
            else:
                print("Rezultat: Pierdut, era capra.\n")

    return cnt / n_sim

def zaruri():
    data = [randrange(1, 7) for _ in range(500)]
    bin_edges = [k + 0.5 for k in range(7)]
    hist(data, bin_edges, density = True, rwidth = 0.9, color = 'green', edgecolor = 'black',
        alpha = 0.5, label = 'frecvente relative')
    distribution = dict([(i, 1/6) for i in range(1, 7)])
    bar(distribution.keys(), distribution.values(), width = 0.85, color = 'red', edgecolor = 'black',
        alpha = 0.6, label = 'probabilitati')
    legend(loc='lower left')
    grid()
    show()

def bile():
    n = 5
    p = 0.6
    size = 1000

    # a)
    x = binom.rvs(n, p, size=size)

    # b)
    bin_edges = [k + 0.5 for k in range(6)]

    hist(x, bin_edges, density=True, rwidth=0.9, color='green',
         edgecolor='black', alpha=0.5, label='frecvente relative')

    distribution = dict([(i, binom.pmf(i, 5, 0.6)) for i in range(1, 6)])

    bar(distribution.keys(), distribution.values(), width=0.85, color='red',
        edgecolor='black', alpha=0.6, label='probabilitati')

    legend(loc='lower left')
    grid()
    show()

    # c)
    count = 0
    for val in x:
        if 2 < val <= 5:
            count += 1
    p_estimata = count / len(x)

    p_teoretic = binom.cdf(5, n, p) - binom.cdf(2, n, p)

    print(f"Probabilitate estimata P(2 < X <= 5): {p_estimata:.4f}")
    print(f"Probabilitate teoretica P(2 < X <= 5): {p_teoretic:.4f}")

if __name__ == "__main__":
    n = int(input("Alegeti exercitiul (1, 2 sau 3): "))

    if n == 1:
        MontyHall_sim(schimba_usa=False, n_sim=1, afisare=True)
        MontyHall_sim(schimba_usa=True, n_sim=1, afisare=True)

        n_sim = 10000

        print("Strategia 1: Ramane la alegerea initiala")
        prob1 = MontyHall_sim(schimba_usa=False, n_sim=n_sim, afisare=False)
        print(f"Probabilitate castig masina: {prob1:.2%}\n")

        print("Strategia 2: Schimba alegerea initiala")
        prob2 = MontyHall_sim(schimba_usa=True, n_sim=n_sim, afisare=False)
        print(f"Probabilitate castig masina: {prob2:.2%}\n")
    elif n == 2:
        zaruri()
    elif n == 3:
        bile()