#include <fstream>
#include <iostream>
#include <vector>
using namespace std;

ifstream fin("dijkstra.in");
ofstream fout("dijkstra.out");

const int N = 1001, Inf = 0x3f3f3f3f;
int c[N][N];
bool s[N];
int d[N];
int n, m, start, finish;
int parent[N];

void CitesteGraf();
void Dijkstra(int x);
void ScrieDrum(int y);

int main()
{
    CitesteGraf();
    Dijkstra(start);
    if (d[finish] == Inf)
        fout << "-1\n";  // Nu există drum
    else {
        fout << d[finish] << '\n';  // Afișează distanța minimă
        ScrieDrum(finish);          // Afișează drumul
    }
}

void Dijkstra(int x)
{
    for (int i = 1; i <= n; ++i) {
        d[i] = Inf;
        parent[i] = -1;
        s[i] = false;
    }

    d[x] = 0;

    for (int pas = 1; pas <= n; ++pas)
    {
        int dmin = Inf, nod = -1;
        for (int i = 1; i <= n; ++i)
            if (!s[i] && d[i] < dmin) {
                dmin = d[i];
                nod = i;
            }

        if (nod == -1) break; // toate nodurile accesibile au fost procesate

        s[nod] = true;

        for (int y = 1; y <= n; ++y)
            if (!s[y] && d[y] > d[nod] + c[nod][y]) {
                d[y] = d[nod] + c[nod][y];
                parent[y] = nod;
            }
    }
}

void ScrieDrum(int y)
{
    vector<int> drum;
    while (y != -1) {
        drum.push_back(y);
        y = parent[y];
    }
    for (int i = drum.size() - 1; i >= 0; --i)
        fout << drum[i] << ' ';
    fout << '\n';
}

void CitesteGraf()
{
    fin >> n >> m;
    for (int i = 1; i <= n; ++i)
        for (int j = 1; j <= n; ++j)
            c[i][j] = (i == j ? 0 : Inf);

    int x, y, cost;
    for (int i = 0; i < m; ++i) {
        fin >> x >> y >> cost;
        c[x][y] = cost;
        // Pentru graf neorientat: c[y][x] = cost;
    }

    fin >> start >> finish;
}
