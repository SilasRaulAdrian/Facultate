class PersoaneValidator:
    def __init__(self):
        pass

    def valideaza_persoana(self, persoana):
        """
        Valideaza o persoana
        """
        erori = []
        if not int(persoana.get_personID()):
            erori.append("ID-ul persoanei trebuie sa fie un numar intreg!")
        elif int(persoana.get_personID()) <= 0:
            erori.append("ID-ul persoanei trebuie sa fie cel putin 1!")
        if persoana.get_nume() == "":
            erori.append("Numele persoanei nu poate fi vid!")
        if persoana.get_adresa() == "":
            erori.append("Adresa persoanei nu poate fi vida!")
        if len(erori) > 0:
            raise ValueError(erori)
        return True