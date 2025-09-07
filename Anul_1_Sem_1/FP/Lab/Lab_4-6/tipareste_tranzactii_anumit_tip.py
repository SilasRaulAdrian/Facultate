def tranzactii_tip(cont, tip):
    """
        Returnează toate tranzacțiile de un anumit tip.
    """
    return [tr for tr in cont.tranzactii if tr.tip == tip]