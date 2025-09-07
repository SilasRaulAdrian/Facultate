from domain.muzica import Muzica
from validator.muzica_valid import MuzicaValidator


class MuzicaController:
    def __init__(self, muzica_repo):
        """
        Constructorul clasei
        :param muzica_repo: un obiect de tip MuzicaRepository
        """
        self.__muzica_repo = muzica_repo
        self.__validator = MuzicaValidator()

    def modifica_muzica(self, titlu, artist, gen, durata, gen_nou, durata_noua):
        """
        Functie care modifica muzica
        :param titlu: titlul muzicii
        :param artist: artistul muzicii
        :param gen: genul muzicii
        :param durata: durata muzicii
        :param gen_nou: noul gen al muzicii
        :param durata_noua: noua durata a muzicii
        :return: None
        """
        muzica_veche = Muzica(titlu, artist, gen, durata)
        self.__validator.valideaza_muzica(muzica_veche)

        muzica_noua = Muzica(titlu, artist, gen_nou, durata_noua)
        self.__validator.valideaza_muzica(muzica_noua)

        self.__muzica_repo.modifica(titlu, artist, gen, durata, gen_nou, durata_noua)

    def get_all_muzici(self):
        """
        Functie care returneaza toate muzicile
        :return: List<Muzica>
        """
        return self.__muzica_repo.get_all_muzici()

    def adauga_muzica(self, muzica):
        """
        Functie care adauga o muzica
        :param muzica: obiect de tip Muzica
        :return: None
        """
        self.__validator.valideaza_muzica(muzica)
        self.__muzica_repo.adauga(muzica)

    def sorteaza_muzici(self):
        """
        Sorteaza muzicile dupa artist si titlu
        :return: None
        """
        muzici = self.get_all_muzici()
        sorted_muzici = self.__muzica_repo.sorteaza_muzici(muzici)
        return sorted_muzici

    def export_csv(self, filename):
        """
        Exporta muzicile intr-un fisier csv
        :param filename: numele fisierului de export
        :return: None
        """
        muzici = self.get_all_muzici()
        muzici_sorted = self.sorteaza_muzici()

        with open(filename, "w") as file:
            for muzica in muzici_sorted:
                file.write(f'"{muzica.get_artist()}","{muzica.get_titlu()}","{muzica.get_gen()}","{muzica.get_durata()}"\n')
