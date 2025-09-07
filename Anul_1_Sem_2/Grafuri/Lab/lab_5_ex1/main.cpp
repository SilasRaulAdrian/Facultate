#include <iostream>
#include <fstream>
#include <vector>
#include <climits>

using namespace std;

const int MAX_N = 1005;

int capacitate[MAX_N][MAX_N];
vector<int> graf[MAX_N];
bool vizitat[MAX_N];

int nrVarfuri;

int dfs(int sursa, int destinatie, int flux) {
    if (sursa == destinatie) return flux;
    vizitat[sursa] = true;

    for (int vecin : graf[sursa]) {
        if (!vizitat[vecin] && capacitate[sursa][vecin] > 0) {
            int impins = dfs(vecin, destinatie, min(flux, capacitate[sursa][vecin]));
            if (impins > 0) {
                capacitate[sursa][vecin] -= impins;
                capacitate[vecin][sursa] += impins;
                return impins;
            }
        }
    }
    return 0;
}

int fluxMaxim(int sursa, int destinatie) {
    int fluxTotal = 0;
    while (true) {
        for (int i = 0; i < nrVarfuri; ++i) vizitat[i] = false;
        int flux = dfs(sursa, destinatie, INT_MAX);
        if (flux == 0) break;
        fluxTotal += flux;
    }
    return fluxTotal;
}

int main(int argc, char* argv[]) {
    if (argc < 3) {
        cerr << "Utilizare: input.txt output.txt\n";
        return 1;
    }

    ifstream intrare(argv[1]);
    ofstream iesire(argv[2]);

    int nrMuchii;
    intrare >> nrVarfuri >> nrMuchii;

    for (int i = 0; i < nrMuchii; ++i) {
        int x, y, capac;
        intrare >> x >> y >> capac;
        capacitate[x][y] += capac;
        graf[x].push_back(y);
        graf[y].push_back(x);
    }

    iesire << fluxMaxim(0, nrVarfuri - 1) << '\n';

    intrare.close();
    iesire.close();
    return 0;
}
