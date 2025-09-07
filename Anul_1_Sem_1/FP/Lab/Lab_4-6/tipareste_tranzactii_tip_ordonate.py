from utils import *

def tipareste_tranzactii_tip_ordonate_dupa_suma(cont_bancar, tip, crescator=True):
    """
        Tipărește toate tranzacțiile de un anumit tip, ordonate după sumă.

        :param cont_bancar: Obiect de tip ContBancar
        :param tip: Tipul de tranzacție ('intrare' sau 'ieșire')
        :param crescator: Dacă este True, ordonează în ordine crescătoare; altfel, în ordine descrescătoare.
    """
    tranzactii_filtrate = filtreaza_tranzactii_tip(cont_bancar.tranzactii, tip)
    tranzactii_sortate = sorteaza_tranzactii_dupa_suma(tranzactii_filtrate, crescator)

    return tranzactii_sortate