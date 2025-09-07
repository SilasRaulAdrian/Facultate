class EvenimenteValidator:
    def __init__(self):
        pass

    def valideaza_eveniment(self, evenimente):
        """
        Valideaza un eveniment
        """
        erori = []
        if not int(evenimente.get_eventID()):
            erori.append("ID-ul evenimentului trebuie sa fie un numar intreg!")
        elif int(evenimente.get_eventID()) <= 0:
            erori.append("ID-ul evenimentului trebuie sa fie cel putin 1!")
        if evenimente.get_data() == "":
            erori.append("Data evenimentului nu poate fi vida!")
        if evenimente.get_timp() == "":
            erori.append("Timpul evenimentului nu poate fi vid!")
        if evenimente.get_descriere() == "":
            erori.append("Descrierea evenimentului nu poate fi vida!")
        if len(erori) > 0:
            raise ValueError(erori)
        return True