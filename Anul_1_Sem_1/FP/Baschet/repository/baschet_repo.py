from domain.baschet import Baschet


class BaschetRepo:
    def __init__(self, _file_path):
        """
        Constructorul clasei
        :param _file_path: calea fisierului
        """
        self._file_path = _file_path

    def _load_from_file(self):
        """
        Functie care incarca jucatorii din fisier
        :return: List<Baschet>
        """
        with open(self._file_path, "r") as file:
            lines = file.readlines()
        jucatori = []
        for line in lines:
            parts = line.strip().split(",")
            if len(parts) == 4:
                jucator = Baschet(parts[0], parts[1], int(parts[2]), parts[3])
                jucatori.append(jucator)
        return jucatori

    def _save_from_file(self, jucatori):
        """
        Functie care salveaza jucatorii in fisier
        :param jucatori: obiect de tip Baschet
        :return: None
        """
        with open(self._file_path, "w") as file:
            for jucator in jucatori:
                file.write(f"{jucator.get_nume()},{jucator.get_prenume()},{jucator.get_inaltime()},{jucator.get_post()}\n")

    def get_all_jucatori(self):
        """
        Functie care returneaza toti jucatorii
        :return: List<Baschet>
        """
        return self._load_from_file()

    def adauga(self, jucator):
        """
        Functie care adauga un jucator
        :param jucator: obiect de tip Baschet
        :return: None
        """
        jucatori = self._load_from_file()
        jucatori.append(jucator)
        self._save_from_file(jucatori)

    def modifica(self, nume, prenume, inaltime, inaltime_noua):
        """
        Modifica un inaltimea unui jucator
        :param nume: string
        :param prenume: string
        :param inaltime: int
        :param inaltime_noua: int
        :return: None
        """
        jucatori = self._load_from_file()
        found = False

        for jucator in jucatori:
            if (jucator.get_nume() == nume and
                    jucator.get_prenume() == prenume and
                    jucator.get_inaltime() == inaltime):
                jucator.set_inaltime(inaltime_noua)
                found = True
                break

        if not found:
            raise ValueError("Nu exista jucatorul introdus")

        self._save_from_file(jucatori)