def salveaza_stare(tranzactii, istoric):
    """
        Salvează o copie a listei de tranzacții curente în istoric.
        :param tranzactii: lista de tranzacții curentă
        :param istoric: lista care păstrează istoricul stărilor anterioare
    """
    istoric.append([{'zi': tr.zi, 'suma': tr.suma, 'tip': tr.tip} for tr in tranzactii])

def undo(istoric):
    """
        Revine la ultima stare salvată din istoric, dacă există.
        :param istoric: lista care păstrează istoricul stărilor anterioare
        :return: ultima stare din istoric sau o listă goală dacă istoric este gol
    """

    if istoric:
        return istoric.pop()
    else:
        print("Nu există operații de undo disponibile.")
        return []