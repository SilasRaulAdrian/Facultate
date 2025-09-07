#include <iostream>
#include <fstream>
#include <vector>
#include <climits>
using namespace std;

const int INF = INT_MAX;

void algoritmPrim(const string& inputFile, const string& outputFile) {
    ifstream inFile(inputFile);
    ofstream outFile(outputFile);

    int V, E;
    inFile >> V >> E;

    vector<vector<pair<int, int>>> graph(V);
    for (int i = 0; i < E; ++i) {
        int u, v, w;
        inFile >> u >> v >> w;
        graph[u].emplace_back(v, w);
        graph[v].emplace_back(u, w);
    }

    vector<int> key(V, INF);
    vector<bool> inMST(V, false);
    vector<int> parent(V, -1);

    key[0] = 0;

    for (int i = 0; i < V - 1; ++i) {
        int u = -1;

        for (int j = 0; j < V; ++j) {
            if (!inMST[j] && (u == -1 || key[j] < key[u])) {
                u = j;
            }
        }

        inMST[u] = true;

        for (const auto& [v, w] : graph[u]) {
            if (!inMST[v] && w < key[v]) {
                key[v] = w;
                parent[v] = u;
            }
        }
    }

    int mstCost = 0;
    vector<pair<int, int>> mstEdges;
    for (int i = 1; i < V; ++i) {
        mstCost += key[i];
        mstEdges.emplace_back(parent[i], i);
    }

    outFile << mstCost << '\n';
    outFile << mstEdges.size() << '\n';
    for (const auto& [u, v] : mstEdges) {
        outFile << u << ' ' << v << '\n';
    }
}

int main(int argc, char* argv[]) {
    algoritmPrim(argv[1], argv[2]);

    return 0;
}
