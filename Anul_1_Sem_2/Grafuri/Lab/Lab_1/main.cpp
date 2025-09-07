#include <iostream>
#include <fstream>
using namespace std;

ifstream fin("../fisier.in");
ofstream fout("../fisier.out");

bool a[101][101];
int ladj[101][101];
int inc[101][101];
int n, m, x, y;

void CitesteGraf();
void MatriceAdiacenta();
void MatriceIncidenta();
void ListaAdiacenta();

int main()
{
    CitesteGraf();
    MatriceAdiacenta();
    fout << '\n';
    MatriceIncidenta();
    fout << '\n';
    ListaAdiacenta();

    return 0;
}

void CitesteGraf()
{
    fin >> n >> m;

    for (int i = 1; i <= m; ++i)
    {
        fin >> x >> y;
        a[x][y] = true;
        a[y][x] = true;
        inc[x][i] = 1;
        inc[y][i] = 1;
    }
}

void MatriceAdiacenta()
{
    for (int i = 1; i <= n; ++i)
    {
        for (int j = 1; j <= n; ++j)
        {
            if (a[i][j])
                fout << 1 << ' ';
            else
                fout << 0 << ' ';
        }
        fout << '\n';
    }
}

void MatriceIncidenta()
{
    for (int i = 1; i <= n; ++i)
    {
        for (int j = 1; j <= m; ++j)
        {
            if (inc[i][j])
                fout << 1 << ' ';
            else
                fout << 0 << ' ';
        }
        fout << '\n';
    }
}

void ListaAdiacenta()
{
    for (int i = 1; i <= n; ++i)
    {
        int k = 0;
        for (int j = 1; j <= n; ++j)
        {
            if (a[i][j])
                ladj[i][++k] = j;
        }
        ladj[i][0] = k;
    }

    for (int i = 1; i <= n; ++i)
    {
        fout << i << ": ";
        for (int j = 1; j <= ladj[i][0]; ++j)
            fout << ladj[i][j] << ' ';
        fout << '\n';
    }
}