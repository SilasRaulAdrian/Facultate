# from iteratia1 import ContBancar

def elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat(cont, suma, tip):
    """
        Elimină toate tranzacțiile mai mici decât o sumă dată și care au tipul specificat.
    """
    cont.tranzactii = [tr for tr in cont.tranzactii if not (tr.suma < suma and tr.tip == tip)]
    if cont.tranzactii is None:
        cont.tranzactii = []
    return cont.tranzactii