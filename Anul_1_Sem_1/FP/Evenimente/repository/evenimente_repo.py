from datetime import datetime

from domain.evenimente import Eveniment


class EvenimenteRepo:
    def __init__(self, _file_path):
        """
        Constructorul clasei
        :param _file_path: calea fisierului
        """
        self._file_path = _file_path

    def _load_from_file(self):
        """
        Functie care incarca evenimentele din fisier
        :return: List<Eveniment>
        """
        with open(self._file_path, "r") as file:
            lines = file.readlines()
        evenimente = []
        for line in lines:
            parts = line.strip().split(",")
            if len(parts) == 3:
                data = datetime.strptime(parts[0], "%d.%m.%Y").date()
                data_formatata = data.strftime("%d.%m.%Y")
                ora = datetime.strptime(parts[1], "%H:%M").time()
                ora_formatata = ora.strftime("%H:%M")
                eveniment = Eveniment(data_formatata, ora_formatata, parts[2])
                evenimente.append(eveniment)
        return evenimente

    def _save_from_file(self, evenimente):
        """
        Functie care salveaza evenimentele in fisier
        :param evenimente: obiect de tip Eveniment
        :return: None
        """
        with open(self._file_path, "w") as file:
            for eveniment in evenimente:
                file.write(f"{eveniment.get_data()},{eveniment.get_ora()},{eveniment.get_descriere()}\n")

    def get_all_evenimente(self):
        """
        Functie care returneaza toate evenimentele din fisier
        :return: List<Eveniment>
        """
        return self._load_from_file()

    def sorteaza(self):
        """
        Functie care sorteaza evenimentele dupa ora
        :return: List<Eveniment>
        """
        evenimente = self._load_from_file()
        data_azi = datetime.now().strftime("%d.%m.%Y")
        evenimente_azi = [ev for ev in evenimente if ev.get_data() == data_azi]
        evenimente_azi.sort(key=lambda ev: datetime.strptime(ev.get_ora(), "%H:%M").time())
        return evenimente_azi

    def adauga(self, ev):
        """
        Functie care adauga un eveniment
        :param ev: obiect de tip Eveniment
        :return: None
        """
        evenimente = self._load_from_file()
        evenimente.append(ev)
        self._save_from_file(evenimente)

    def sorteaza_data(self, data):
        """
        Functie care sorteaza evenimentele dupa ora cu o data specificata
        :param data: data evenimentului
        :return: List<Eveniment>
        """
        evenimente = self._load_from_file()
        data = datetime.strptime(data, "%d.%m.%Y").date()
        data = data.strftime("%d.%m.%Y")
        evenimente_sortate = [ev for ev in evenimente if ev.get_data() == data]
        evenimente_sortate.sort(key=lambda ev: datetime.strptime(ev.get_ora(), "%H:%M").time())
        return evenimente_sortate

    def filtreaza_si_sorteaza(self, str):
        """
        Functie care filtreaza si sorteaza evenimentele dupa un sir in descriere
        :param str: string
        :return: List<Eveniment>
        """
        evenimente = self._load_from_file()
        evenimente_filtrate = [ev for ev in evenimente if str.lower() in ev.get_descriere().lower()]
        evenimente_filtrate.sort(key=lambda ev: (
            datetime.strptime(ev.get_data(), "%d.%m.%Y").date(),
            datetime.strptime(ev.get_ora(), "%H:%M").time()
        ))
        return evenimente_filtrate