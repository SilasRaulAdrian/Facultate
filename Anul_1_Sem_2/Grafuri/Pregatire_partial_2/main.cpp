#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAX = 1000;
int cap[MAX][MAX];
bool vis[MAX];

int DFS(int u, int t, int flow, int V) {
    if (u == t) return flow;

    vis[u] = true;
    for (int v = 0; v < V; ++v) {
        if (!vis[v] && cap[u][v] > 0) {
            int new_flow = min(flow, cap[u][v]);
            int pushed = DFS(v, t, new_flow, V);
            if (pushed > 0) {
                cap[u][v] -= pushed;
                cap[v][u] += pushed;
                return pushed;
            }
        }
    }

    return 0;
}

int fordFulkerson(int s, int t, int V) {
    int max_flow = 0;
    while (true) {
        for (int i = 0; i < V; ++i) {
            vis[i] = false;
        }
        int flow = DFS(s, t, INT_MAX, V);
        if (flow == 0) break;
        max_flow += flow;
    }
    return max_flow;
}

int main() {
    int V, E;
    cout << "Introduceti numarul de varfuri si muchii: ";
    cin >> V >> E;

    for (int i = 0; i < V; ++i) {
        for (int j = 0; j < V; ++j) {
            cap[i][j] = 0;
        }
    }

    cout << "Introduceti muchiile (u v capacitate):" << endl;
    for (int i = 0; i < E; ++i) {
        int u, v, c;
        cin >> u >> v >> c;
        cap[u][v] += c;
    }

    int s = 0, t = V - 1;

    int max_flow = fordFulkerson(s, t, V);
    cout << "Fluxul maxim este: " << max_flow << endl;

    return 0;
}