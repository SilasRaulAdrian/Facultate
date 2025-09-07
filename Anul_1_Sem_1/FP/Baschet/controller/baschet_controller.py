from itertools import combinations
import random

from domain.baschet import Baschet
from validator.baschet_valid import BaschetValidator


class BaschetController:
    def __init__(self, baschet_repo):
        """
        Constructorul clasei
        :param baschet_repo: obiect de tip BaschetRepo
        """
        self.__baschet_repo = baschet_repo
        self.__validator = BaschetValidator()

    def adauga_jucator(self, nume, prenume, inaltime, post):
        """
        Functie care adauga un jucator
        :param nume: string
        :param prenume: string
        :param inaltime: int
        :param post: string
        :return: None
        """
        jucator = Baschet(nume, prenume, inaltime, post)
        self.__validator.valideaza_jucator(jucator)
        self.__baschet_repo.adauga(jucator)

    def modifica_jucator(self, nume, prenume, inaltime, post, inaltime_noua):
        """
        Functie care modifica inaltimea unui jucator
        :param nume: string
        :param prenume: string
        :param inaltime: int
        :param post: string
        :param inaltime_noua: int
        :return: None
        """
        jucator_vechi = Baschet(nume, prenume, inaltime, post)
        self.__validator.valideaza_jucator(jucator_vechi)
        jucator_nou = Baschet(nume, prenume, inaltime_noua, post)
        self.__validator.valideaza_jucator(jucator_nou)
        self.__baschet_repo.modifica(nume, prenume, inaltime, inaltime_noua)

    def get_all_jucatori(self):
        """
        Functie care returneaza toti jucatorii
        :return: List<Baschet>
        """
        return self.__baschet_repo.get_all_jucatori()

    def creeaza_echipa(self):
        """
        Returneaza echipa valida cu media de inaltime maxima
        :return: List<Baschet>
        """
        jucatori = self.__baschet_repo.get_all_jucatori()

        fundasi = [j for j in jucatori if j.get_post() == "Fundas"]
        extreme = [j for j in jucatori if j.get_post() == "Extrema"]
        pivoti = [j for j in jucatori if j.get_post() == "Pivot"]

        if len(fundasi) < 2 or len(extreme) < 2 or len(pivoti) < 1:
            raise ValueError("Nu sunt destui jucatori pentru a forma o echipa valida")

        echipa_max = []
        max_media = 0

        for f in combinations(fundasi, 2):
            for e in combinations(extreme, 2):
                for p in combinations(pivoti, 1):
                    echipa = list(f) + list(e) + list(p)
                    media = sum(j.get_inaltime() for j in echipa) / 5
                    if media > max_media:
                        max_media = media
                        echipa_max = echipa

        return echipa_max

    def importa_jucatori(self, file_path):
        """
        Functie care importa jucatori dintr-un fisier existent
        :param file_path: string (calea catre fisier)
        :return: int (numarul de jucatori adaugati)
        """
        try:
            with open(file_path, "r") as file:
                lines = file.readlines()

            jucatori_existenti = self.get_all_jucatori()
            nume_existente = {(j.get_nume(), j.get_prenume()) for j in jucatori_existenti}
            pozitii = ["Fundas", "Extrema", "Pivot"]
            jucatori_adaugati = 0

            for line in lines:
                parts = line.strip().split(",")
                if len(parts) != 2:
                    continue

                nume, prenume = parts
                if (nume, prenume) in nume_existente:
                    continue

                inaltime = random.randint(180, 220)
                post = random.choice(pozitii)

                self.adauga_jucator(nume, prenume, inaltime, post)
                jucatori_adaugati += 1

            return jucatori_adaugati
        except FileNotFoundError:
            return -1