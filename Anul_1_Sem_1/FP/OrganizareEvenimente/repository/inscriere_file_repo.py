import os
from domain.inscriere import Inscriere

class InscriereFileRepository:
    def __init__(self, file_path):
        self.__file_path = file_path

    def _load_from_file(self):
        """
        Incarca inscrierile din fisier
        """
        with open(self.__file_path, "r") as file:
            lines = file.readlines()
        inscrieri = []
        for line in lines:
            parts = line.strip().split(",")
            if len(parts) == 3:
                inscriere = Inscriere(int(parts[0]), int(parts[1]), int(parts[2]))
                inscrieri.append(inscriere)
        return inscrieri

    def _save_from_file(self, inscrieri):
        """
        Salveaza inscrierile in fisier
        """
        with open(self.__file_path, "w") as file:
            for inscriere in inscrieri:
                file.write(f"{int(inscriere.get_id_inscriere())},{inscriere.get_id_persoana()},{inscriere.get_id_eveniment()}\n")

    def add_inscriere(self, inscriere):
        """
        Adauga o inscriere in lista de inscrieri din fisier
        :param inscriere: Inscriere
        :return: -
        """
        inscrieri = self._load_from_file()
        if any(int(i.get_id_inscriere()) == int(inscriere.get_id_inscriere()) for i in inscrieri):
            raise ValueError(f"Inscrierea cu id-ul {int(inscriere.get_id_inscriere())} exista deja!")
        inscrieri.append(inscriere)
        self._save_from_file(inscrieri)

    def delete_inscriere(self, inscriere_id):
        """
        Sterge o inscriere din lista de inscrieri din fisier
        :param inscriere_id: int
        :return: -
        """
        inscrieri = self._load_from_file()
        inscrieri = [i for i in inscrieri if int(i.get_id_inscriere()) != int(inscriere_id)]
        self._save_from_file(inscrieri)

    def update_inscriere(self, inscriere_id, new_persoana_id, new_eveniment_id):
        """
        Modifica o inscriere din lista de inscrieri din fisier
        :param inscriere_id: int
        :param new_persoana_id: int
        :param new_eveniment_id: int
        :return: -
        """
        inscrieri = self._load_from_file()
        for i in inscrieri:
            if int(i.get_id_inscriere()) == int(inscriere_id):
                i.set_id_persoana(new_persoana_id)
                i.set_id_eveniment(new_eveniment_id)
        self._save_from_file(inscrieri)

    def get_all_inscrieri(self):
        """
        Returneaza toate inscrierile din lista de inscrieri din fisier
        :return: list<Inscriere>
        """
        return self._load_from_file()

    def search_inscriere_by_person_id(self, person_id):
        """
        Cauta o inscriere dupa id-ul persoanei
        :param person_id: int
        :return: Inscriere
        """
        inscrieri = self._load_from_file()
        for inscriere in inscrieri:
            if int(inscriere.get_id_persoana()) == int(person_id):
                return inscriere
        return None