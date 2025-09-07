# from iteratia1 import ContBancar

def tranzactii_inainte_de_zi_mai_mari_de_suma(cont, zi, suma):
    """
        Returnează toate tranzacțiile dinaintea unei zile și cu sume mai mari decât o sumă specificată.
    """
    return [tr for tr in cont.tranzactii if tr.zi < zi and tr.suma > suma]