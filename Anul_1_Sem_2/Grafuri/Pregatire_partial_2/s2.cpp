#include <iostream>
#include <vector>
#include <cmath>
#include <iomanip>
using namespace std;

const int MAXN = 1005;
const double INF = 1e9;

int N;
pair<double, double> coords[MAXN];
bool inMST[MAXN];
double minDist[MAXN];

double distance(int i, int j) {
    double dx = coords[i].first - coords[j].first;
    double dy = coords[i].second - coords[j].second;
    return sqrt(dx * dx + dy * dy);
}

double primMST() {
    for (int i = 0; i < N; ++i) {
        minDist[i] = INF;
        inMST[i] = false;
    }

    minDist[0] = 0.0;
    double totalCost = 0.0;

    for (int count = 0; count < N; ++count) {
        double minEdge = INF;
        int u = -1;

        for (int v = 0; v < N; ++v) {
            if (!inMST[v] && minDist[v] < minEdge) {
                minEdge = minDist[v];
                u = v;
            }
        }

        inMST[u] = true;
        totalCost += minEdge;

        for (int v = 0; v < N; ++v) {
            if (!inMST[v]) {
                double dist = distance(u, v);
                if (dist < minDist[v]) {
                    minDist[v] = dist;
                }
            }
        }
    }

    return totalCost;
}

int main() {
    cin >> N;
    for (int i = 0; i < N; ++i) {
        cin >> coords[i].first >> coords[i].second;
    }

    double result = primMST();
    cout << fixed << setprecision(3) << result << endl;

    return 0;
}