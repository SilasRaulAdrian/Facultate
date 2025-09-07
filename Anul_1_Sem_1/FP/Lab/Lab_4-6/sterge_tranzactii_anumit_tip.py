# from iteratia1 import ContBancar

def sterge_tranzactii_tip(cont, tip):
    """
        Șterge toate tranzacțiile din cont de un anumit tip.
    """
    cont.tranzactii = [tr for tr in cont.tranzactii if tr.tip != tip]
    if cont.tranzactii is None:
        cont.tranzactii = []
    return cont.tranzactii
