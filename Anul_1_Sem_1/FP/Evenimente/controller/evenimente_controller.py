from domain.evenimente import Eveniment
from validator.evenimente_valid import EvenimenteValidator


class EvenimenteController:
    def __init__(self, evenimente_repo):
        """
        Constructorul clasei
        :param evenimente_repo: obiect de tip EvenimenteRepo
        """
        self.__evenimente_repo = evenimente_repo
        self.__validator = EvenimenteValidator()

    def get_all_evenimente(self):
        """
        Functie care returneaza toate evenimentele
        :return: List<Eveniment>
        """
        return self.__evenimente_repo.get_all_evenimente()

    def sorteaza_evenimente(self):
        """
        Functie care sorteaza evenimentele
        :return: List<Eveniment>
        """
        return self.__evenimente_repo.sorteaza()

    def adauga_eveniment(self, data, ora, descriere):
        """
        Functie care adauga un eveniment
        :param data: string
        :param ora: string
        :param descriere: string
        :return: None
        """
        eveniment = Eveniment(data, ora, descriere)
        self.__validator.valideaza_eveniment(eveniment)
        self.__evenimente_repo.adauga(eveniment)

    def sorteaza_evenimente_data(self, data):
        """
        Functie care sorteaza evenimentele dupa ora cu o data specificata
        :param data: data evenimentului
        :return: List<Eveniment>
        """
        return self.__evenimente_repo.sorteaza_data(data)

    def export_evenimente(self, filename, str):
        """
        Functie care exporta evenimentele intr-un fisier
        :param filename: string, numele fisierului
        :param str: string, sirul de caractere pentru cautare in descriere
        :return: None
        """
        evenimente = self.__evenimente_repo.filtreaza_si_sorteaza(str)

        with open(filename, "w") as file:
            for ev in evenimente:
                file.write(f"{ev.get_data()}, {ev.get_ora()}, {ev.get_descriere()}\n")