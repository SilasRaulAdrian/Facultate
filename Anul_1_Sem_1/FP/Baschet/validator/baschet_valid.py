class BaschetValidator:
    def __init__(self):
        pass

    def valideaza_jucator(self, baschet):
        """
        Functie care valideaza un jucator
        :param baschet: obiect de tip Baschet
        :return: True/False in functie de validarea jucatorului
        """
        erori = []

        if baschet.get_nume() == "":
            erori.append("Numele jucatorului nu poate fi vid!")

        if baschet.get_prenume() == "":
            erori.append("Prenumele jucatorului nu poate fi vid!")

        try:
            inaltime = int(baschet.get_inaltime())
            if inaltime <= 0:
                erori.append("Inaltimea jucatorului trebuie sa fie un numar pozitiv mai mare ca 0!")
        except ValueError:
            erori.append("Inaltimea trebuie sa fie un numar intreg!")

        post_valide = ["Fundas", "Extrema", "Pivot"]
        if baschet.get_post() not in post_valide:
            erori.append("Postul jucatorului nu este valid!")

        if len(erori) > 0:
            raise ValueError("\n".join(erori))

        return True