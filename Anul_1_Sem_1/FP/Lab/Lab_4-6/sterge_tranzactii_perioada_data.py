def sterge_tranzactii_perioada(cont, zi_inceput, zi_sfarsit):
    """
        Elimină toate tranzacțiile din cont care au loc într-o perioadă specificată.
    """
    cont.tranzactii = [tr for tr in cont.tranzactii if not (zi_inceput <= tr.zi <= zi_sfarsit)]
    if cont.tranzactii is None:
        cont.tranzactii = []
    return cont.tranzactii