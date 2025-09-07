def filtreaza_tranzactii_tip(tranzactii, tip):
    """
    Returnează o listă de tranzacții de un anumit tip.

    :param tranzactii: Lista de tranzacții
    :param tip: Tipul de tranzacție ('intrare' sau 'ieșire')
    :return: Lista tranzacțiilor de tipul specificat
    """
    return [tr for tr in tranzactii if tr.tip == tip]

def sorteaza_tranzactii_dupa_suma(tranzactii, crescator=True):
    """
    Sortează o listă de tranzacții după sumă.

    :param tranzactii: Lista de tranzacții de sortat
    :param crescator: Dacă True, ordonează în ordine crescătoare; altfel, în ordine descrescătoare
    :return: Lista tranzacțiilor sortate după sumă
    """
    return sorted(tranzactii, key=lambda tr: tr.suma, reverse=not crescator)