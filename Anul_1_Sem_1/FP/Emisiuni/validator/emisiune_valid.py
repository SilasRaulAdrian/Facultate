class EmisiuneValidator:
    def __init__(self):
        pass

    def valideaza_emisiune(self, emisiune):
        """
        Functie care valideaza emisiunea
        :param emisiune: obiect de tip Emisiune
        :return: True/False in functie de validarea emisiunii
        """
        erori = []
        if emisiune.get_nume() == "":
            erori.append("Numele emisiunii nu poate fi vid!")
        if emisiune.get_tip() == "":
            erori.append("Tipul emisiunii nu poate fi vid!")
        if emisiune.get_descriere() == "":
            erori.append("Descrierea emisiunii nu poate fi vida!")
        try:
            durata = int(emisiune.get_durata())
            if durata <= 0:
                erori.append("Durata trebuie sa fie un numar pozitiv mai mare ca 0!")
        except ValueError:
            erori.append("Durata trebuie sa fie un numar intreg!")

        if len(erori) > 0:
            raise ValueError("\n".join(erori))

        return True