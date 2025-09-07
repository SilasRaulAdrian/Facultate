class Inscriere:
    def __init__(self, id_inscriere, id_persoana, id_eveniment):
        self.__id_inscriere = id_inscriere
        self.__id_persoana = id_persoana
        self.__id_eveniment = id_eveniment

    def get_id_inscriere(self):
        return self.__id_inscriere

    def get_id_persoana(self):
        return self.__id_persoana

    def get_id_eveniment(self):
        return self.__id_eveniment

    def set_id_inscriere(self, id_inscriere):
        self.__id_inscriere = id_inscriere

    def set_id_persoana(self, id_persoana):
        self.__id_persoana = id_persoana

    def set_id_eveniment(self, id_eveniment):
        self.__id_eveniment = id_eveniment

    def __eq__(self, other):
        if isinstance(other, Inscriere):
            return (self.__id_inscriere == other.__id_inscriere and
                    self.__id_persoana == other.__id_persoana and
                    self.__id_eveniment == other.__id_eveniment)
        return False

    def __str__(self):
        return f"Inscriere(id={self.__id_inscriere}, id_persoana={self.__id_persoana}, id_eveniment={self.__id_eveniment})"
