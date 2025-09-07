# from iteratia1 import ContBancar

def sterge_tranzactii_zi(cont, zi):
    """
        Șterge toate tranzacțiile din cont pentru ziua specificată.
    """
    cont.tranzactii = [tr for tr in cont.tranzactii if tr.zi != zi]
    if cont.tranzactii is None:
        cont.tranzactii = []
    return cont.tranzactii