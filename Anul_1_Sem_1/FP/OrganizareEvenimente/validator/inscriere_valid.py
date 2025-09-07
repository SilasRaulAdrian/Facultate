class InscriereValidator:
    def __init__(self):
        pass

    def valideaza_inscriere(self, inscriere):
        """
        Valideaza o inscriere
        """
        erori = []
        if not int(inscriere.get_id_inscriere()):
            erori.append("ID-ul inscrierii trebuie sa fie un numar intreg!")
        elif int(inscriere.get_id_inscriere()) <= 0:
            erori.append("ID-ul inscrierii trebuie sa fie cel putin 1!")
        if not int(inscriere.get_id_persoana()):
            erori.append("ID-ul persoanei trebuie sa fie un numar intreg!")
        elif int(inscriere.get_id_persoana()) <= 0:
            erori.append("ID-ul persoanei trebuie sa fie cel putin 1!")
        if not int(inscriere.get_id_eveniment()):
            erori.append("ID-ul evenimentului trebuie sa fie un numar intreg!")
        elif int(inscriere.get_id_eveniment()) <= 0:
            erori.append("ID-ul evenimentului trebuie sa fie cel putin 1!")
        if len(erori) > 0:
            raise ValueError(erori)
        return True