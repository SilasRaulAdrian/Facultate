#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <limits>

using namespace std;

const int INF = 1e9;

struct Edge {
    int from, to, weight;
};

bool BellmanFord(int V, vector<Edge>& edges, vector<int>& h) {
    h.assign(V + 1, INF);
    h[V] = 0;

    for (int i = 0; i < V; ++i) {
        bool updated = false;
        for (const auto& edge : edges) {
            if (h[edge.from] != INF && h[edge.from] + edge.weight < h[edge.to]) {
                h[edge.to] = h[edge.from] + edge.weight;
                updated = true;
            }
        }
        if (!updated) break;
    }

    for (const auto& edge : edges) {
        if (h[edge.from] != INF && h[edge.from] + edge.weight < h[edge.to]) {
            return false;
        }
    }

    return true;
}

vector<int> Dijkstra(int src, int V, const vector<vector<pair<int, int>>>& adj) {
    vector<int> dist(V, INF);
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<>> pq;
    dist[src] = 0;
    pq.emplace(0, src);

    while (!pq.empty()) {
        auto [d, u] = pq.top();
        pq.pop();
        if (d > dist[u]) continue;

        for (auto [v, w] : adj[u]) {
            if (dist[u] + w < dist[v]) {
                dist[v] = dist[u] + w;
                pq.emplace(dist[v], v);
            }
        }
    }

    return dist;
}

int main(int argc, char* argv[]) {
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    int V, E;
    fin >> V >> E;
    vector<Edge> edges;
    vector<vector<pair<int, int>>> adj(V);

    for (int i = 0; i < E; ++i) {
        int x, y, w;
        fin >> x >> y >> w;
        edges.push_back({x, y, w});
        adj[x].emplace_back(y, w);
    }

    for (int i = 0; i < V; ++i) {
        edges.push_back({V, i, 0});
    }

    vector<int> h(V + 1);
    if (!BellmanFord(V, edges, h)) {
        fout << "-1\n";
        return 0;
    }

    vector<vector<pair<int, int>>> newAdj(V);
    for (const auto& edge : edges) {
        if (edge.from == V) continue;
        int newWeight = edge.weight + h[edge.from] - h[edge.to];
        newAdj[edge.from].emplace_back(edge.to, newWeight);
    }

    vector<vector<int>> distMatrix(V, vector<int>(V, INF));
    for (int i = 0; i < V; ++i) {
        distMatrix[i] = Dijkstra(i, V, newAdj);
        for (int j = 0; j < V; ++j) {
            if (distMatrix[i][j] != INF) {
                distMatrix[i][j] += h[j] - h[i];
            }
        }
    }

    for (int u = 0; u < V; ++u) {
        for (const auto& [v, w] : newAdj[u]) {
            fout << u << " " << v << " " << w << "\n";
        }
    }

    for (int i = 0; i < V; ++i) {
        for (int j = 0; j < V; ++j) {
            fout << (distMatrix[i][j] == INF ? "INF" : to_string(distMatrix[i][j])) << " ";
        }
        fout << "\n";
    }

    return 0;
}
