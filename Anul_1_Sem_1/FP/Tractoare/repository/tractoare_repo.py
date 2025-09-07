from datetime import datetime

from domain.tractoare import Tractor

class TractorRepo:
    def __init__(self, _file_path):
        """
        Constructorul clasei TractorRepo
        """
        self._file_path = _file_path
        self._history_data = None

    def _load_from_file(self):
        """
        Functie care incarca tractoarele din fisier
        :return: List<Tractor>
        """
        with open(self._file_path, "r") as file:
            lines = file.readlines()
        tractoare = []
        for line in lines:
            parts = line.strip().split(",")
            if len(parts) == 5:
                data = datetime.strptime(parts[4], "%d/%m/%Y").date()
                data_formatata = data.strftime("%d/%m/%Y")
                tractor = Tractor(int(parts[0]), parts[1], int(parts[2]), parts[3], data_formatata)
                tractoare.append(tractor)
        return tractoare

    def _backup_file(self):
        """
        Salveaza continutul fisierului actual intr-o variabila pentru undo
        :return: None
        """
        with open(self._file_path, "r") as file:
            self._history_data = file.read()

    def _save_in_file(self, tractoare):
        """
        Functie care salveaza tractoarele in fisier
        :param tractoare: obiect de tip Tractor
        :return: None
        """
        self._backup_file()
        with open(self._file_path, "w") as file:
            for tractor in tractoare:
                file.write(f"{int(tractor.get_id())},{tractor.get_denumire()},{int(tractor.get_pret())},{tractor.get_model()},{tractor.get_data()}\n")

    def get_all_tractoare(self):
        """
        Functie care returneaza toate tractoarele
        :return: List<Tractor>
        """
        return self._load_from_file()

    def adauga(self, tr):
        """
        Functie care adauga un tractor
        :param tr: obiect de tip Tractor
        :return: None
        """
        tractoare = self._load_from_file()
        if any(int(t.get_id()) == int(tr.get_id()) for t in tractoare):
            raise ValueError(f"Tractorul cu id-ul {int(tr.get_id())} exista deja!")
        tractoare.append(tr)
        self._save_in_file(tractoare)

    def sterge(self, cifra):
        """
        Functie care sterge tractoarele pentru care pretul contine cifra data
        :param cifra: int
        :return: None
        """
        tractoare = self._load_from_file()
        tractoare_actualizate = [tractor for tractor in tractoare if str(cifra) not in str(tractor.get_pret())]
        self._save_in_file(tractoare_actualizate)

    def undo(self):
        """
        Functie care anuleaza ultima modificare
        :return: None
        """
        if self._history_data is None:
            return False

        with open(self._file_path, "w") as file:
            file.write(self._history_data)

        return True
