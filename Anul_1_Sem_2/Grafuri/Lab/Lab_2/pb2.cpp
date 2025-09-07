#include <iostream>
#include <fstream>
using namespace std;

ifstream fin("fisier.txt");
ofstream fout("fisier.out");

void CitesteGraf(int a[][101], int &n)
{
    fin >> n;
    for(int i = 1; i <= n; i++)
        for(int j = 1; j <= n; j++)
            fin >> a[i][j];
}

void InchidereTranzitiva(int a[][101], int n)
{
    for(int k = 1; k <= n; k++)
        for(int i = 1; i <= n; i++)
            for(int j = 1; j <= n; j++)
                if(a[i][j] == 0 && a[i][k] == 1 && a[k][j] == 1)
                    a[i][j] = 1;
}

void AfiseazaGraf(int a[][101], int n)
{
    for(int i = 1; i <= n; i++)
    {
        for(int j = 1; j <= n; j++)
            fout << a[i][j] << " ";
        fout << '\n';
    }
}

int main()
{
    int a[101][101], n;

    CitesteGraf(a, n);
    InchidereTranzitiva(a, n);
    AfiseazaGraf(a, n);

    return 0;
}
