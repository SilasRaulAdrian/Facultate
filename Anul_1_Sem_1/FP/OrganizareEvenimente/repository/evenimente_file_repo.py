from domain.evenimente import Evenimente

class EvenimenteFileRepository:
    def __init__(self, file_path):
        self._file_path = file_path

    def _load_from_file(self):
        """
        Incarca evenimentele din fisier
        """
        with open(self._file_path, "r") as file:
            lines = file.readlines()
        evenimente = []
        for line in lines:
            parts = line.strip().split(",")
            if len(parts) == 4:
                eveniment = Evenimente(int(parts[0]), parts[1], parts[2], parts[3])
                evenimente.append(eveniment)
        return evenimente

    def _save_from_file(self, evenimente):
        """
        Salveaza evenimentele in fisier
        """
        with open(self._file_path, "w") as file:
            for eveniment in evenimente:
                file.write(f"{int(eveniment.get_eventID())},{eveniment.get_data()},{eveniment.get_timp()},{eveniment.get_descriere()}\n")

    def add_eveniment(self, eveniment):
        """
        Adauga un eveniment in lista de evenimente din fisier
        :param eveniment: Evenimente
        :return: -
        """
        evenimente = self._load_from_file()
        if any(int(e.get_eventID()) == int(eveniment.get_eventID()) for e in evenimente):
            raise ValueError(f"Evenimentul cu id-ul {int(eveniment.get_eventID())} exista deja!")
        evenimente.append(eveniment)
        self._save_from_file(evenimente)

    def delete_eveniment(self, eventID):
        """
        Sterge un eveniment din lista de evenimente din fisier
        :param eventID: int
        :return: -
        """
        evenimente = self._load_from_file()
        evenimente = [e for e in evenimente if int(e.get_eventID()) != int(eventID)]
        self._save_from_file(evenimente)

    def update_eveniment(self, eventID, new_data, new_timp, new_descriere):
        """
        Modifica un eveniment din lista de evenimente din fisier
        :param eventID: int
        :param new_data: string
        :param new_timp: string
        :param new_descriere: string
        :return: -
        """
        evenimente = self._load_from_file()
        for eveniment in evenimente:
            if int(eveniment.get_eventID()) == int(eventID):
                eveniment.set_data(new_data)
                eveniment.set_timp(new_timp)
                eveniment.set_descriere(new_descriere)
        self._save_from_file(evenimente)

    def get_all_evenimente(self):
        """
        Returneaza toate evenimentele din lista de evenimente din fisier
        :return: list<Evenimente>
        """
        return self._load_from_file()

    def search_eveniment_by_id(self, eventID):
        """
        Cauta un eveniment dupa id in lista de evenimente din fisier
        :param eventID: int
        :return: Evenimente
        """
        evenimente = self._load_from_file()
        for eveniment in evenimente:
            if int(eveniment.get_eventID()) == int(eventID):
                return eveniment
        return None

    def search_evenimente_by_data(self, data):
        """
        Cauta evenimente dupa data in lista de evenimente din fisier
        :param data: string
        :return: list<Evenimente>
        """
        evenimente = self._load_from_file()
        return [e for e in evenimente if e.get_data() == data]

    def search_evenimente_by_timp(self, timp):
        """
        Cauta evenimente dupa timp in lista de evenimente din fisier
        :param timp: string
        :return: list<Evenimente>
        """
        evenimente = self._load_from_file()
        return [e for e in evenimente if e.get_timp() == timp]

    def search_evenimente_by_descriere(self, descriere):
        """
        Cauta evenimente dupa descriere in lista de evenimente din fisier
        :param descriere: string
        :return: list<Evenimente>
        """
        evenimente = self._load_from_file()
        return [e for e in evenimente if descriere.lower() in e.get_descriere().lower()]