#include <iostream>
#include <fstream>
#include <queue>
#include <vector>
#include <string>
using namespace std;

ifstream fin("labirint_1.txt");

const int MAX = 100;
char labirint[MAX][MAX];
int n, m;
int start_x, start_y, end_x, end_y;

int dx[] = { -1, 1, 0, 0 };
int dy[] = { 0, 0, -1, 1 };

bool valid(int x, int y) {
    return x >= 0 && x < n && y >= 0 && y < m && labirint[x][y] != '1';
}

void citesteLabirint() {
    string linie;

    while (getline(fin, linie)) {
        m = linie.size();
        for (int j = 0; j < m; j++) {
            labirint[n][j] = linie[j];
            if (linie[j] == 'S') {
                start_x = n;
                start_y = j;
            }
            if (linie[j] == 'F') {
                end_x = n;
                end_y = j;
            }
        }
        n++;
    }
}

void gasesteDrum() {
    int parinteX[MAX][MAX], parinteY[MAX][MAX];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < m; j++)
            parinteX[i][j] = parinteY[i][j] = -1;

    queue<pair<int, int>> Q;
    Q.push({ start_x, start_y });
    parinteX[start_x][start_y] = start_x;
    parinteY[start_x][start_y] = start_y;

    while(!Q.empty()) {
        int x = Q.front().first;
        int y = Q.front().second;
        Q.pop();

        if (x == end_x && y == end_y)
            break;

        for (int d = 0; d < 4; d++) {
            int xnou = x + dx[d];
            int ynou = y + dy[d];

            if (valid(xnou, ynou) && parinteX[xnou][ynou] == -1) {
                Q.push({ xnou, ynou });
                parinteX[xnou][ynou] = x;
                parinteY[xnou][ynou] = y;
            }
        }
    }

    int x = end_x, y = end_y;
    while (x != start_x || y != start_y) {
        int px = parinteX[x][y];
        int py = parinteY[x][y];
        if (x != end_x || y != end_y)
            labirint[x][y] = '*';
        x = px;
        y = py;
    }
}

void afiseazaLabirint() {
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++)
            cout << labirint[i][j];
        cout << '\n';
    }
}

int main() {
    citesteLabirint();
    gasesteDrum();
    afiseazaLabirint();

    return 0;
}