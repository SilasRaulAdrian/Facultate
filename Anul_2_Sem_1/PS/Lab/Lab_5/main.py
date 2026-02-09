from scipy.stats import uniform, expon
from math import log, e
from matplotlib.pyplot import show, hist, grid, legend, xticks, plot, bar, yticks

def ex1(x, p, N):
    u = uniform.rvs(size=N)
    X = []

    for i in range(N):
        p1 = 0
        for k in range(len(p)):
            if p1 < u[i] < p1 + p[k]:
                X.append(x[k])
                break
            p1 += p[k]

    return X

def hist_ex1(X, x, p):
    freq = [0] * len(x)
    for value in X:
        freq[x.index(value)] += 1

    prob = [f / len(X) for f in freq]

    positions = range(len(x))

    bar(positions, prob, width=0.4, alpha=0.7, label='Simulated Probabilities')
    plot(positions, p, 'ro-', label='Theoretical Probabilities')

    xticks(positions, x)

    yticks([i / 10 for i in range(11)])
    grid()
    legend()
    show()

def hist_ex2i():
    # alpha = 1/12
    data = expon.rvs(scale=12, size=10000)
    hist(data, bins = 12, density = True, range = (0, 60))
    x = range(60)
    plot(x, expon.pdf(x, loc=0, scale =12), 'r-')
    xticks(range(0, 60, 5))
    grid()
    show()

def ex2i(N, alpha):
    U = uniform.rvs(size=N)
    X = [-(1 / alpha) * log(1 - u) for u in U]
    return X

def ex2ii():
    alpha = 1 / 12
    data = ex2i(10000, alpha)
    prob = sum(1 for t in data if t >= 5) / len(data)

    val_teoretica = 1 - expon.cdf(5, scale = 1 / alpha)

    print("\nProbabilitatea estimata: ")
    print(prob)
    print("\nProbabilitatea teoretica: ")
    print(val_teoretica)

if __name__ == "__main__":
    x = ['0', 'A', 'B', 'AB']
    p = [0.46, 0.4, 0.1, 0.04]
    print(ex1(x, p, 10))
    hist_ex1(ex1(x, p, 1000), x, p)
    print(ex2i(10, 1/12))
    hist_ex2i()
    ex2ii()
