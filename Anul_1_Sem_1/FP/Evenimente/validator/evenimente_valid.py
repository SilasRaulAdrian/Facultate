from datetime import datetime


class EvenimenteValidator:
    def __init__(self):
        pass

    def valideaza_eveniment(self, eveniment):
        """
        Functie care valideaza un eveniment
        :param eveniment: obiect de tip Eveniment
        :return: True daca evenimentul este valid, in caz contrar raise ValueError
        """
        erori = []

        if eveniment.get_descriere() == "":
            erori.append("Descrierea evenimentului nu trebuie sa fie vida!")

        try:
            data = eveniment.get_data()
            data = datetime.strptime(data, "%d.%m.%Y")
            data = data.strftime("%d.%m.%Y")
        except ValueError:
            erori.append("Data introdusa trebuie sa fie o data valida de forma dd.mm.yyyy")

        try:
            ora = eveniment.get_ora()
            ora = datetime.strptime(ora, "%H:%M")
            ora = ora.strftime("%H:%M")
        except ValueError:
            erori.append("Ora introdusa trebuie sa fie o ora valida de forma hh:mm")

        if len(erori) > 0:
            raise ValueError("\n".join(erori))

        return True

