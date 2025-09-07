#include <iostream>
#include <vector>

using namespace std;

const int MAXN = 100005;
vector<int> graf[MAXN];
vector<bool> vizitat(MAXN, false);

bool dfs(int nod, int parinte) {
    vizitat[nod] = true;

    for (int vecin : graf[nod]) {
        if (!vizitat[vecin]) {
            if (dfs(vecin, nod))
                return true;
        } else if (vecin != parinte) {
            // Am găsit ciclu
            return true;
        }
    }

    return false;
}

int main() {
    int n, m;
    cin >> n >> m;

    // Citire muchii
    for (int i = 0; i < m; ++i) {
        int x, y;
        cin >> x >> y;
        graf[x].push_back(y);
        graf[y].push_back(x); // graful este neorientat
    }

    for (int i = 1; i <= n; ++i) {
        if (!vizitat[i]) {
            if (dfs(i, -1)) {
                cout << "NU\n"; // are ciclu
                return 0;
            }
        }
    }

    cout << "DA\n"; // este aciclic
    return 0;
}

