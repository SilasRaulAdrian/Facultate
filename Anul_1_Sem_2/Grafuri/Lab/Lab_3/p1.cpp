#include <iostream>
#include <fstream>
#include <climits>
using namespace std;

int v, e, s, x, y, w;
int c[100][100], a[100], d[100];
const int INF = INT_MAX;

void CitesteGraf(ifstream &fin);
void Dijkstra(int x, int d[]);
void AfiseazaDrum(ofstream &fout);

int main(int argc, char* argv[]) {
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    CitesteGraf(fin);
    Dijkstra(s, d);
    AfiseazaDrum(fout);

    fin.close();
    fout.close();

    return 0;
}


void CitesteGraf(ifstream &fin) {
    fin >> v >> e >> s;

    for (int i = 0; i < v; i++)
        for (int j = 0; j < v; j++)
            c[i][j] = INF;

    for (int i = 0; i < e; i++) {
        fin >> x >> y >> w;
        c[x][y] = w;
    }
}

void Dijkstra(int x, int d[]) {
    d[x] = 0;
    a[x] = 1;

    for (int j = 0; j < v; j++)
        d[j] = c[x][j];

    d[x] = 0;

    for (int pas = 1; pas < v; ++pas) {
        int dmin = INF;

        for (int i = 0; i < v; ++i) {
            if (!a[i] && d[i] < dmin) {
                dmin = d[i];
                x = i;
            }
        }

        a[x] = 1;

        for (int y = 0; y < v; ++y)
            if (d[y] > d[x] + c[x][y] && c[x][y] != INF)
                d[y] = d[x] + c[x][y];
    }
}

void AfiseazaDrum(ofstream &fout) {
    for (int i = 0; i < v; i++) {
        if (d[i] == INF)
            fout << "INF ";
        else
            fout << d[i] << ' ';
    }
}
