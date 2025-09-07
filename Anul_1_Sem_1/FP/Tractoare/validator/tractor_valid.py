from datetime import datetime

class TractorValidator:
    def __init__(self):
        pass

    def valideaza_tractor(self, tractor):
        """
        Functie care valideaza tractorul
        :param tractor: obiect de tip Tractor
        :return: True daca tractorul este valid
        """
        erori = []
        try:
            id_tractor = int(tractor.get_id())
            if id_tractor <= 0:
                erori.append("ID-ul trebuie sa fie un numar pozitiv mai mare ca 0!")
        except ValueError:
            erori.append("ID-ul tractorului trebuie sa fie un numar intreg!")

        if tractor.get_denumire() == "":
            erori.append("Denumirea tractorului nu poate fi vida!")

        try:
            pret = int(tractor.get_pret())
            if pret <= 0:
                erori.append("Pretul trebuie sa fie un numar pozitiv mai mare ca 0!")
        except ValueError:
            erori.append("Pretul tractorului trebuie sa fie un numar intreg!")

        if tractor.get_model() == "":
            erori.append("Modelul tractorului nu poate fi vid!")

        try:
            data = tractor.get_data()
            data = datetime.strptime(data, "%d/%m/%Y")
            data = data.strftime("%d/%m/%Y")
        except ValueError:
            erori.append("Data introdusa trebuie sa fie o data valida de forma dd/mm/yyyy")

        if len(erori) > 0:
            raise ValueError("\n".join(erori))

        return True
