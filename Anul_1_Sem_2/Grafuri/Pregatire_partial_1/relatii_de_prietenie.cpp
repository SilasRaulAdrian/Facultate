#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

const int MAXN = 100005;
vector<int> graf[MAXN];
vector<bool> vizitat(MAXN);
vector<vector<int>> grupuri;

void dfs(int nod, vector<int>& grup) {
    vizitat[nod] = true;
    grup.push_back(nod);
    for (int vecin : graf[nod]) {
        if (!vizitat[vecin]) {
            dfs(vecin, grup);
        }
    }
}

int main() {
    int n, m;
    cin >> n >> m;

    // Citire relatii
    for (int i = 0; i < m; ++i) {
        int x, y;
        cin >> x >> y;
        graf[x].push_back(y);
        graf[y].push_back(x); // neorientat
    }

    for (int i = 1; i <= n; ++i) {
        if (!vizitat[i]) {
            vector<int> grup;
            dfs(i, grup);
            sort(grup.begin(), grup.end());
            grupuri.push_back(grup);
        }
    }

    // Afisare
    for (const auto& grup : grupuri) {
        for (int copil : grup) {
            cout << copil << ' ';
        }
        cout << '\n';
    }

    return 0;
}

