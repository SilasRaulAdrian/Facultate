#include <iostream>
#include <fstream>
using namespace std;

ifstream fin("fisier.in");
ofstream fout("fisier.out");

int a[101][101], n, g[101];
int INF = 1000000;

void CitesteGraf()
{
    fin >> n;
    for(int i = 1; i <= n; ++i)
        for(int j = 1; j <= n; ++j)
        {
            fin >> a[i][j];
            if (a[i][j] == 1 && i < j)
                g[i]++, g[j]++;
        }
}

void NoduriIzolate()
{
    for(int i = 1; i <= n; ++i)
        if (g[i] == 0)
            fout << i << " ";
}

bool GrafRegular()
{
    for(int i = 1; i <= n; ++i)
        if (g[i] != g[1])
            return false;
    return true;
}

void MatriceaDistantelor()
{
    int d[101][101];
    for(int i = 1; i <= n; ++i)
        for(int j = 1; j <= n; ++j)
        {
            if (a[i][j] == 1)
                d[i][j] = 1;
            else if (i == j)
                d[i][j] = 0;
            else
                d[i][j] = INF;
        }

    for(int k = 1; k <= n; ++k)
        for(int i = 1; i <= n; ++i)
            for(int j = 1; j <= n; ++j)
                if (d[i][j] > d[i][k] + d[k][j])
                    d[i][j] = d[i][k] + d[k][j];

    for(int i = 1; i <= n; ++i)
    {
        for(int j = 1; j <= n; ++j)
        {
            if (d[i][j] == INF)
                fout << "INF ";
            else
                fout << d[i][j] << " ";
        }
        fout << "\n";
    }
}

bool GrafConex()
{
    int p = 1, u = 1;
    bool v[101] = {0};
    int Q[101] = {0};
    v[1] = true;
    Q[1] = 1;

    while(p <= u)
    {
        int k = Q[p];
        p++;
        for(int y = 1; y <= n; ++y)
            if (a[k][y] == 1 && !v[y])
            {
                v[y] = true;
                Q[++u] = y;
            }
    }

    for(int i = 1; i <= n; ++i)
        if (!v[i])
            return false;
    return true;
}

int main()
{
    CitesteGraf();
    NoduriIzolate();
    fout << '\n';
    if (GrafRegular())
        fout << "\nGraful este regular";
    else
        fout << "\nGraful nu este regular";
    fout << "\n\n";
    MatriceaDistantelor();
    if (GrafConex())
        fout << "\nGraful este conex";
    else
        fout << "\nGraful nu este conex";

    return 0;
}
