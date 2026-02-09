from scipy.stats import bernoulli, binom, hypergeom, geom
from matplotlib.pyplot import bar, show, hist, grid, legend, xticks

# 1. a)
def drunk_ass_puric(p, size):
    deplasari = bernoulli.rvs(p, size=size)
    poz = 0
    for pas in deplasari:
        poz += 2 * pas - 1
        print(poz)
    return poz

# 1. b)
def drunk_puric(p, n):
    size = 1000

    x = [drunk_ass_puric(0.5, 10) for i in range(size)]

    bin_edges = [k + 0.5 for k in range(-n - 1, n + 1)]

    hist(x, bin_edges, density=True, rwidth=0.9, color='green',
         edgecolor='black', alpha=0.5, label='frecvente relative')

    distribution = dict([(2 * k - n, binom.pmf(k, n, p)) for k in range(0, n + 1)])

    bar(distribution.keys(), distribution.values(), width=0.85, color='red',
        edgecolor='black', alpha=0.6, label='probabilitati')

    legend(loc='lower left')
    grid()
    show()

# 1. c)
def mers_cerc(p, n, N): # n - nr de pasi, N - nr de noduri
    deplasari = bernoulli.rvs(p, size=n)
    poz = 0
    for pas in deplasari:
        poz = (poz + (2 * pas - 1)) % N
        #print(poz)
    return poz

def drunk_as_fck_puric(p, n, N):
    x = [mers_cerc(p, n, N) for _ in range(1000)]

    bin_edges = [k + 0.5 for k in range(-1, N)]

    hist(x, bin_edges, density=True, rwidth=0.9, color='green',
         edgecolor='black', alpha=0.5, label='frecvente relative')

    prob_teoretice = n * [0]
    for k in range(0, N + 1):
        prob_teoretice[(2 * k - N) % n] += binom.pmf(k, N, p)

    bar(range(n), prob_teoretice, width=0.85, color='red',
        edgecolor='black', alpha=0.6, label='probabilitati')

    legend(loc='lower left')
    xticks(range(n))
    grid()
    show()

# 2. i)
def loto_addicted():
    #sum = 0
    pcastig = sum([hypergeom.pmf(k, 49, 6, 6) for k in range(3, 7)])
    bile_necastigatoare = geom.rvs(pcastig, size=100)
    return bile_necastigatoare

def loto_addicted_v2():
    pcastig = sum([hypergeom.pmf(k, 49, 6, 6) for k in range(3, 7)])
    bilete_necastigatoare = geom.rvs(pcastig, size=100)
    caz_fav = 0
    for i in bilete_necastigatoare:
        if i >= 10:
            caz_fav += 1
    return caz_fav / 100

def loto_addicted_v2_teoretic():
    pcastig = sum([hypergeom.pmf(k, 49, 6, 6) for k in range(3, 7)])
    p_necastig = (1 - pcastig)**10
    return p_necastig

if __name__ == '__main__':
    #drunk_ass_puric(0.5, 5)
    #drunk_puric(0.5, 10)
    #drunk_as_fck_puric(0.5, 10, 10)
    bilete = loto_addicted()
    for i in bilete:
        print(i)

    print(loto_addicted_v2())
    print(loto_addicted_v2_teoretic())