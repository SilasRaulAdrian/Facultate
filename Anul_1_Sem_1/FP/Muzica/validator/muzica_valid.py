class MuzicaValidator:
    def __init__(self):
        pass

    def valideaza_muzica(self, muzica):
        """
        Functie care valideaza muzica
        :param muzica: obiect de tip Muzica
        :return: True daca muzica este valida
        """
        erori = []

        if muzica.get_titlu() == "":
            erori.append("Titlul muzicii nu poate fi vid!")

        if muzica.get_artist() == "":
            erori.append("Artistul muzicii nu poate fi vid!")

        try:
            durata = int(muzica.get_durata())
            if durata <= 0:
                erori.append("Durata trebuie sa fie un numar pozitiv mai mare ca 0!")
        except ValueError:
            erori.append("Durata muzicii trebuie sa fie un numar intreg!")

        genuri_valide = ["Rock", "Pop", "Jazz", "Altele"]
        if muzica.get_gen() not in genuri_valide:
            erori.append("Genul muzicii nu este valid!")

        if len(erori) > 0:
            raise ValueError("\n".join(erori))

        return True