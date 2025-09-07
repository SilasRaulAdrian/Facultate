# from iteratia1 import ContBancar

def elimina_tranzactii_tip(cont, tip):
    """
        Elimină toate tranzacțiile din cont de un anumit tip ('intrare' sau 'ieșire').
    """
    cont.tranzactii = [tr for tr in cont.tranzactii if tr.tip != tip]
    if cont.tranzactii is None:
        cont.tranzactii = []
    return cont.tranzactii