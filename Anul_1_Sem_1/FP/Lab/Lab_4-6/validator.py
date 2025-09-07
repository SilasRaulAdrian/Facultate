def valideaza_tranzactie(ziua, suma, tip):
    """
        Validează datele unei tranzacții.

        :param ziua: Ziua tranzacției (int)
        :param suma: Suma tranzacției (float)
        :param tip: Tipul tranzacției (str), fie 'intrare', fie 'ieșire'
        :raises ValueError: Dacă datele sunt invalide
    """
    if ziua < 0:
        raise ValueError("Ziua trebuie să fie un număr întreg pozitiv.")
    if suma < 0:
        raise ValueError("Suma trebuie să fie un număr real pozitiv.")
    #if tip not in ['intrare', 'ieșire']:
        #raise ValueError("Tipul tranzacției trebuie să fie 'intrare' sau 'ieșire'.")