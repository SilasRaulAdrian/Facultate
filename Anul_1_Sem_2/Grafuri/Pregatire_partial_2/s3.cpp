#include <iostream>
#include <vector>
#include <cstring>
using namespace std;

const int MAX = 205;

int cap[MAX][MAX];
vector<int> adj[MAX];
bool vis[MAX];

int DFS(int u, int t, int flow) {
    if (u == t) return flow;

    vis[u] = true;
    for (int v : adj[u]) {
        if (!vis[v] && cap[u][v] > 0) {
            int new_flow = min(flow, cap[u][v]);
            int pushed = DFS(v, t, new_flow);
            if (pushed > 0) {
                cap[u][v] -= pushed;
                cap[v][u] += pushed;
                return pushed;
            }
        }
    }

    return 0;
}

int fordFulkerson(int s, int t) {
    int max_flow = 0;
    int flow;

    do {
        for (int i = 0; i < MAX; ++i) {
            vis[i] = false;
        }
        flow = DFS(s, t, 1);
        max_flow += flow;
    } while (flow > 0);

    return max_flow;
}

int main() {
    int N, M, E;
    cin >> N >> M >> E;
    int s = 0, t = N + M + 1;

    for (int i = 1; i <= N; ++i) {
        cap[s][i] = 1;
        adj[s].push_back(i);
        adj[i].push_back(s);
    }

    for (int i = 1; i <= M; ++i) {
        int nodeR = N + i;
        cap[nodeR][t] = 1;
        adj[nodeR].push_back(t);
        adj[t].push_back(nodeR);
    }

    for (int i = 0; i < E; ++i) {
        int u, v;
        cin >> u >> v;
        int nodeL = u;
        int nodeR = N + v;

        cap[nodeL][nodeR] = 1;
        adj[nodeL].push_back(nodeR);
        adj[nodeR].push_back(nodeL);
    }

    cout << fordFulkerson(s, t) << endl;

    return 0;
}