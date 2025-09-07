import random

from domain.emisiune import Emisiune
from validator.emisiune_valid import EmisiuneValidator


class Emisiune_Controller:
    def __init__(self, emisiune_repo):
        """
        Constructorul clasei
        :param emisiune_repo: obiect de tip EmisiuneRepo
        """
        self.__emisiune_repo = emisiune_repo
        self.__validator = EmisiuneValidator()

    def sterge_emisiune(self, nume, tip, durata, descriere):
        """
        Functie care sterge emisiunea cu numele si tipul specificat
        :param nume: string
        :param tip: string
        :return: None
        """
        emisiune = Emisiune(nume, tip, durata, descriere)
        self.__validator.valideaza_emisiune(emisiune)
        if tip in self.__emisiune_repo.tipuri_blocate:
            raise ValueError("Emisiune blocata si nu poate fi ștearsa")
        self.__emisiune_repo.sterge(nume, tip)

    def modifica_emisiune(self, nume, tip, durata, descriere, durata_noua, descriere_noua):
        """
        Functie care modifica durata si descrierea emisiunii
        :param nume: numele emisiunii
        :param tip: tipul emisiunii
        :param durata: durata emisiunii
        :param descriere: descrierea emisiunii
        :param durata_noua: noua durata a emisiunii
        :param descriere_noua: noua descriere a emisiunii
        :return: None
        """
        emisiune_veche = Emisiune(nume, tip, durata, descriere)
        self.__validator.valideaza_emisiune(emisiune_veche)
        emisiune_noua = Emisiune(nume, tip, durata_noua, descriere_noua)
        self.__validator.valideaza_emisiune(emisiune_noua)
        if tip in self.__emisiune_repo.tipuri_blocate:
            raise ValueError("Emisiune blocata si nu poate fi modificata")
        self.__emisiune_repo.modifica(nume, tip, durata, descriere, durata_noua, descriere_noua)

    def adauga_tip_blocat(self, tip):
        self.__emisiune_repo.adauga_tip_blocat(tip)

    def sterge_tip_blocat(self, tip):
        self.__emisiune_repo.sterge_tip_blocat(tip)

    def get_all_emisiuni(self):
        """
        Functie care returneaza toate emisiunile
        :return: List<Emisiune>
        """
        return self.__emisiune_repo.get_all_emisiuni()

    def genereaza_program(self, ora_inceput, ora_sfarsit):
        """
        Genereaza un program TV pentru intervalul dat.
        :param ora_inceput: int, ora de inceput
        :param ora_sfarsit: int, ora de sfarsit
        :return: List<tuple> (ora, emisiune)
        """
        if ora_inceput >= ora_sfarsit:
            raise ValueError("Ora de inceput trebuie sa fie mai mica decat ora de sfarsit")

        emisiuni = self.get_all_emisiuni()
        if not emisiuni:
            raise ValueError("Nu exista emisiuni disponibile")

        program = []
        ora_curenta = ora_inceput
        emisiuni_adaugate = set()

        while ora_curenta < ora_sfarsit:
            # Selectam o emisiune care nu a mai fost adaugata
            emisiune = random.choice(emisiuni)
            if emisiune not in emisiuni_adaugate:
                program.append((ora_curenta, emisiune))
                emisiuni_adaugate.add(emisiune)
                ora_curenta += emisiune.get_durata()
            else:
                # Adaugam reluari daca timpul permite
                emisiune_reluare = Emisiune(emisiune.get_nume(), emisiune.get_tip(), emisiune.get_durata(),
                                            f"{emisiune.get_descriere()} ****")
                program.append((ora_curenta, emisiune_reluare))
                ora_curenta += emisiune_reluare.get_durata()

            # Daca durata a depasit, iesim
            if ora_curenta > ora_sfarsit:
                break

        return program


