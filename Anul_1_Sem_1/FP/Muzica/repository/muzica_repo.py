from domain.muzica import Muzica


class MuzicaRepo:
    def __init__(self, _file_path):
        """
        Constructorul clasei
        :param _file_path: calea fisierului muzica.txt
        """
        self._file_path = _file_path

    def _load_from_file(self):
        """
        Functie care incarca muzicile din fisier
        :return: List<Muzica>
        """
        with open(self._file_path, "r") as file:
            lines = file.readlines()

        muzici = []

        for line in lines:
            parts = line.strip().split(",")
            if len(parts) == 4:
                muzica = Muzica(parts[0], parts[1], parts[2], int(parts[3]))
                muzici.append(muzica)
        return muzici

    def _save_from_file(self, muzici):
        """
        Functie care salveaza muzicile in fisier
        :param muzici: obiect de tip Muzica
        :return: None
        """
        with open(self._file_path, "w") as file:
            for muzica in muzici:
                file.write(f"{muzica.get_titlu()},{muzica.get_artist()},{muzica.get_gen()},{muzica.get_durata()}\n")

    def get_all_muzici(self):
        """
        Functie care returneaza toate muzicile din fisier
        :return: List<Muzica>
        """
        return self._load_from_file()

    def modifica(self, titlu, artist, gen, durata, gen_nou, durata_noua):
        """
        Functie care modifica o muzica
        :param titlu: titlul muzicii
        :param artist: artistul muzicii
        :param gen: genul muzicii
        :param durata: durata muzicii
        :param gen_nou: noul gen al muzicii
        :param durata_noua: noua durata a muzicii
        :return: None
        """
        muzici = self._load_from_file()
        found = False

        for muzica in muzici:
            if (muzica.get_titlu() == titlu and
                    muzica.get_artist() == artist and
                    muzica.get_gen() == gen and
                    muzica.get_durata() == durata):
                muzica.set_gen(gen_nou)
                muzica.set_durata(durata_noua)
                found = True
                break

        if not found:
            raise ValueError("Nu exista nicio melodie cu titlul, artistul, genul si durata introduse")

        self._save_from_file(muzici)

    def adauga(self, muzica):
        """
        Functie care adauga o muzica in fisier
        :param muzica: obiect de tip Muzica
        :return: None
        """
        muzici = self._load_from_file()
        muzici.append(muzica)
        self._save_from_file(muzici)

    def sorteaza_muzici(self, muzici):
        """
        Functie care sorteaza muzicile dupa artist si titlu
        :param muzici: lista de muzici
        :return: lista sortata de muzici
        """
        return sorted(muzici, key=lambda x: (x.get_artist(), x.get_titlu()))