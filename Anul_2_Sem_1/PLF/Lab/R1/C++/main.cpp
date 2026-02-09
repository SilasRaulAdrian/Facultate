#include "lista.h"
#include <iostream>
using namespace std;

bool incluziuneListeRec(Lista l1, Lista l2, PNod p) {
      if (p == NULL) {
         return true;
      }

      PNod current = l1._prim;
      while (current != NULL) {
         if (current->e == p->e)
               return incluziuneListeRec(l1, l2, p->urm);
         current = current->urm;
      }

      return false;
}

void inserareDupaRec(PNod p, TElem e, TElem e1) {
    if (p == NULL) {
        return;
    }

    if (p->e == e) {
        PNod nou = new Nod();
        nou->e = e1;
        nou->urm = p->urm;
        p->urm = nou;
        return;
    }

    inserareDupaRec(p->urm, e, e1);
}

int main()
{
    int ex;
    cout << "Nr. exercitiului (1, 2): ";
    cin >> ex;

    if (ex == 1) {
        Lista l1, l2;
        l1 = creare();
        tipar(l1);
        l2 = creare();
        tipar(l2);

        if (incluziuneListeRec(l1, l2, l2._prim))
            cout << "\nl2 este inclusa in l1";
        else
            cout << "\nl2 nu este inclusa in l1";

        distruge(l1);
        distruge(l2);
    } else if (ex == 2) {
        Lista l;
        TElem e, e1;
        l = creare();
        tipar(l);

        cout << "Elementul dupa care se insereaza: ";
        cin >> e;
        cout << "Elementul care se insereaza: ";
        cin >> e1;

        inserareDupaRec(l._prim, e, e1);
        tipar(l);
        distruge(l);
    } else {
        return 0;
    }

    return 0;
}
