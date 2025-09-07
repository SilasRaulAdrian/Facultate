#include <fstream>
#include <vector>
#include <queue>
#include <tuple>
using namespace std;

ifstream fin("dijkstra2.in");
ofstream fout("dijkstra2.out");

const int Inf = 0x3f3f3f3f;
using VI = vector<int>;
using PI = pair<int, int>;
using VP = vector<PI>;
using VVP = vector<VP>;
VVP G;    
VI d;   
int n;
int m, p;

void ReadGraph();
void Dijkstra(int x, VI& d);
void WriteDistances();


int main()
{
    ReadGraph();
    Dijkstra(p, d);
    WriteDistances();
}

void Dijkstra(int x, VI& d) // O(n * log2(m))
{
    d = VI(n + 1, Inf);
    priority_queue<PI, vector<PI>, greater<PI>> Q;

    d[x] = 0;
    Q.emplace(0, x);
    int y, dx, w;
    while (!Q.empty())
    {
        x = Q.top().second;
        dx = Q.top().first;
        Q.pop();
        if (dx > d[x])
            continue;

        for (auto p : G[x])
        {
            y = p.first;
            w = p.second;
            if (d[y] > d[x] + w)
            {
                d[y] = d[x] + w;
                Q.emplace(d[y], y);
            }
        }
    }
}

void WriteDistances()
{
    for (int i = 1; i <= n; ++i)
    {
        if (d[i] == Inf)
            d[i] = -1;
        fout << d[i] << ' ';
    }
}

void ReadGraph()
{
    fin >> n >> m >> p;

    G = VVP(n + 1); 
    int x, y, w;  

    while (m--)
    {
        fin >> x >> y >> w;
        G[x].emplace_back(y, w);
        G[y].emplace_back(x, w);
    }
}

