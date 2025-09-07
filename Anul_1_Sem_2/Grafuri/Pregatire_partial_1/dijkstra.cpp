#include <fstream>
using namespace std;

ifstream fin("dijkstra.in");
ofstream fout("dijkstra.out");

const int N = 1001, Inf = 0x3f3f3f3f;
int c[N][N];
bool s[N];    
int d[N];    
int n, p;

void CitesteGraf();
void Dijkstra(int x, int d[]);
void ScrieDistMinime();

int main()
{
    CitesteGraf();
    Dijkstra(p, d);
    ScrieDistMinime();
}

void Dijkstra(int x, int d[])
{
    d[x] = 0;
    s[x] = 1;

    for (int j = 1; j <= n; ++j)
        d[j] = c[x][j];

    for (int pas = 1; pas <= n - 1; ++pas) /// O(n * n)
    {
        int dmin = Inf;
        for (int i = 1; i <= n; ++i)
            if (!s[i] && d[i] < dmin)
            {
                dmin = d[i];
                x = i;
            }

        s[x] = true;
        for (int y = 1; y <= n; ++y)
            if (d[y] > d[x] + c[x][y])
                d[y] = d[x] + c[x][y];
    }
}

void ScrieDistMinime()
{
    for (int i = 1; i <= n; ++i)
        if (d[i] != Inf)
            fout << d[i] << ' ';
        else
            fout << "-1 ";
}

void CitesteGraf()
{
    fin >> n >> p;
    for (int i = 1; i <= n; ++i)
        for (int j = 1; j <= n; ++j)
            if (i != j)
                c[i][j] = Inf;

    int x, y, cost;
    while (fin >> x >> y >> cost)
        c[x][y] = cost;
}