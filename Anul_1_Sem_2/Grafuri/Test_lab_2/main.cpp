#include <iostream>
#include <fstream>
#include <cmath>
#include <iomanip>
using namespace std;

const int MAXN = 1005;
const float INF = 1e9;

int n;
pair<float, float> coords[MAXN];
bool inMST[MAXN];
float minDist[MAXN];

float distance(int i, int j) {
    float dx = coords[i].first - coords[j].first;
    float dy = coords[i].second - coords[j].second;

    return sqrt(dx * dx + dy * dy);
}

float primMST() {
    for (int i = 0; i < n; ++i) {
        minDist[i] = INF;
        inMST[i] = false;
    }

    minDist[0] = 0.0;
    float totalCost = 0.0;

    for (int count = 0; count < n; ++count) {
        float minEdge = INF;
        int u = -1;

        for (int v = 0; v < n; ++v) {
            if (!inMST[v] && minDist[v] < minEdge) {
                minEdge = minDist[v];
                u = v;
            }
        }

        inMST[u] = true;
        totalCost += minEdge;

        for (int v = 0; v < n; ++v) {
            if (!inMST[v]) {
                float dist = distance(u, v);
                if (dist < minDist[v]) {
                    minDist[v] = dist;
                }
            }
        }
    }

    return totalCost;
}

int main(int argc, char* argv[]) {
    if (argc != 3) {
        cerr << "Prea putine argumente!\n";
        return 1;
    }

    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    fin >> n;
    for (int i = 0; i < n; ++i) {
        fin >> coords[i].first >> coords[i].second;
    }

    float result = primMST();
    fout << fixed << setprecision(3) << result << '\n';

    return 0;
}
