#include <iostream>
#include <vector>
#include <queue>
#include <cmath>
#include <iomanip>
#include <limits>

using namespace std;

struct Oras {
    double x, y;
};

double distanta(const Oras& a, const Oras& b) {
    return sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y));
}

int main() {
    int n, m;
    cin >> n >> m;

    vector<Oras> orase(n + 1); // indexare de la 1
    for (int i = 1; i <= n; ++i) {
        cin >> orase[i].x >> orase[i].y;
    }

    vector<vector<pair<int, double>>> graf(n + 1);
    for (int i = 0; i < m; ++i) {
        int a, b;
        cin >> a >> b;
        double cost = distanta(orase[a], orase[b]);
        graf[a].emplace_back(b, cost); // drum orientat de la a la b
    }

    int S, F;
    cin >> S >> F;

    // Dijkstra
    vector<double> dist(n + 1, numeric_limits<double>::infinity());
    vector<bool> vizitat(n + 1, false);
    priority_queue<pair<double, int>, vector<pair<double, int>>, greater<>> pq;

    dist[S] = 0.0;
    pq.emplace(0.0, S);

    while (!pq.empty()) {
        int nod = pq.top().second;
        pq.pop();

        if (vizitat[nod]) continue;
        vizitat[nod] = true;

        for (auto [vecin, cost] : graf[nod]) {
            if (dist[nod] + cost < dist[vecin]) {
                dist[vecin] = dist[nod] + cost;
                pq.emplace(dist[vecin], vecin);
            }
        }
    }

    if (dist[F] == numeric_limits<double>::infinity()) {
        cout << "Nu exista drum de la " << S << " la " << F << '\n';
    } else {
        cout << fixed << setprecision(6) << dist[F] << '\n';
    }

    return 0;
}

