/**
  Parcurgerea in Adancime
  Depth First Search

*/
#include <fstream>
using namespace std;

ifstream fin("dfs.in");
ofstream fout("dfs.out");

const int MaxN = 101;
bool a[MaxN][MaxN];
bool v[MaxN];  /// retine starea de vizitare a nodurilor
int n, m, X;

void CitesteGraf();
void Dfs(int x);

int main()
{
    CitesteGraf();
    Dfs(X);
}

void Dfs(int x)
{
    v[x] = true;
    fout << x << ' ';
    for (int y = 1; y <= n; ++y)
        if (a[x][y] && !v[y]) /// daca y este vecin nevizitat al lui x
            Dfs(y);
}

void CitesteGraf()
{
    fin >> n >> m >> X;
    for (int i = 1, x, y; i <= m; ++i)
    {
        fin >> x >> y;
        a[x][y] = a[y][x] = true;
    }
}

