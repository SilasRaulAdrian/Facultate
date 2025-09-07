#include<iostream>
#include<fstream>
#include <queue>
#include <vector>
#include <climits>
using namespace std;

ifstream fin("graf.txt");
ofstream fout("graf.out");

vector<int> BfsOfGraph(vector<vector<int>>& adj, int s) {
    int V = adj.size();

    vector<int> res;
    queue<int> q;

    vector<bool> visited(V, false);

    visited[s] = true;
    q.push(s);

    while (!q.empty()) {
        int curr = q.front();
        q.pop();
        res.push_back(curr);

        for (int x : adj[curr]) {
            if (!visited[x]) {
                visited[x] = true;
                q.push(x);
            }
        }
    }

    return res;
}

vector<int>BfsDistance(vector<vector<int>>& adj, int s) {
    int V = adj.size();

    vector<bool> visited(V, false);
    vector<int> dist(V, INT_MAX);
    queue<int> q;

    visited[s] = true;
    dist[s] = 0;
    q.push(s);

    while (!q.empty()) {
        int curr = q.front();
        q.pop();

        for (int x : adj[curr]) {
            if (!visited[x]) {
                visited[x] = true;
                dist[x] = dist[curr] + 1;
                q.push(x);
            }
        }
    }

    return dist;

}

void DfsOFGraph(vector<vector<int>>& adj, vector<bool> &visited, int s) {
    visited[s] = true;

    cout << s << " ";

    for (int i : adj[s])
        if (!visited[i])
            DfsOFGraph(adj, visited, i);
}

void DFS(vector<vector<int>>& adj, int s) {
    vector<bool> visited(adj.size(), false);
    DfsOFGraph(adj, visited, s);

}

int main() {

    int V = 0;
    int nr_muchii = 0;
    fin >> V;
    cout << V << '\n';

    int nod_sursa = 0;
    cout << "Introduceti nodul sursa: ";
    cin >> nod_sursa;

    if (nod_sursa > V or nod_sursa < 1) {
        cout << "Nodul sursa nu exista in graf\n";
        return 0;
    }

    vector<vector<int>> adj(V + 1);

    int x, y;

    while (fin >> x >> y) {
        nr_muchii++;
        adj[x].push_back(y);
        //adj[y].push_back(x);
    }

    vector<int> bfs_res;
    bfs_res = BfsOfGraph(adj, nod_sursa);

    vector<int> dist = BfsDistance(adj, nod_sursa);

    for (int i = 0; i < bfs_res.size(); i++) {
        cout << bfs_res[i] << " ";
    }

    cout << '\n';

    for (int i = 1; i < dist.size(); ++i) {
        cout << "Distanta de la nodul " << nod_sursa << " la nodul " << i << " este: " << dist[i] << '\n';
    }

    cout << '\n';

    DFS(adj, nod_sursa);
}
