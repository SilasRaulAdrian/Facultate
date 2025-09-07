#include <iostream>
#include <fstream>
#include <queue>
#include <vector>
#include <climits>
using namespace std;

ifstream fin("graf.txt");
ofstream fout("graf.out");

const int INF = INT_MAX;

void AlgoritmulLuiMoore(vector<vector<int>>& adj, int s) {
    int n = adj.size() - 1;

    vector<int> dist(n + 1, INF);
    vector<int> parinte(n + 1, -1);

    queue<int> Q;

    dist[s] = 0;
    Q.push(s);

    while(!Q.empty()) {
        int x = Q.front();
        Q.pop();

        for(int y : adj[x]) {
            if(dist[y] == INF) {
                parinte[y] = x;
                dist[y] = dist[x] + 1;
                Q.push(y);
            }
        }
    }

    cout << "Nod    Distanta    Parinte" << '\n';
    for (int i = 1; i <= n; ++i) {
        cout << i << "\t\t";
        if (dist[i] == INF)
            cout << "INF";
        else
            cout << dist[i];
        cout << "\t\t\t";
        if (parinte[i] == -1)
            cout << "-1";
        else
            cout << parinte[i];
        cout << '\n';
    }
}

int main() {
    int n, u, v;
    fin >> n;

    vector<vector<int>> adj(n + 1);

    while (fin >> u >> v) {
        adj[u].push_back(v);
    }

    int source;
    cout << "Introduceti nodul sursa: ";
    cin >> source;

    if (source < 1 or source > n) {
        cout << "Nodul sursa nu exista in graf\n";
        return 0;
    }

    AlgoritmulLuiMoore(adj, source);

    return 0;
}