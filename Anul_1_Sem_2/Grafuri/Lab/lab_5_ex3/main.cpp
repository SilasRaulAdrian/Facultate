#include <iostream>
#include <fstream>
#include <vector>
#include <stack>
#include <unordered_map>
#include <set>

using namespace std;

const int MAX_N = 101000;

vector<multiset<int>> adiacenta(MAX_N);
vector<int> ciclu;

void parcurgereDFS(int nod) {
    while (!adiacenta[nod].empty()) {
        int vecin = *adiacenta[nod].begin();
        adiacenta[nod].erase(adiacenta[nod].begin());
        adiacenta[vecin].erase(adiacenta[vecin].find(nod));
        parcurgereDFS(vecin);
    }
    ciclu.push_back(nod);
}

int main(int argc, char* argv[]) {
    if (argc < 3) {
        cerr << "Utilizare: input.txt output.txt\n";
        return 1;
    }

    ifstream intrare(argv[1]);
    ofstream iesire(argv[2]);

    int nrNoduri, nrMuchii;
    intrare >> nrNoduri >> nrMuchii;

    for (int i = 0; i < nrMuchii; ++i) {
        int x, y;
        intrare >> x >> y;
        adiacenta[x].insert(y);
        adiacenta[y].insert(x);
    }

    int start = 0;
    while (adiacenta[start].empty()) start++;

    parcurgereDFS(start);

    for (int i = ciclu.size() - 1; i >= 0; --i) {
        iesire << ciclu[i];
        if (i > 0) iesire << ' ';
    }

    iesire << '\n';
    intrare.close();
    iesire.close();
    return 0;
}
