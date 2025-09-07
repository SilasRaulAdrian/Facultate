import random

from domain.muzica import Muzica


class ConsoleUI:
    def __init__(self, controller):
        """
        Constructorul clasei
        :param controller: obiect de tip MuzicaController
        """
        self.__controller = controller

    def display_menu(self):
        """
        Functie care afiseaza meniul aplicatiei
        :return: None
        """
        print("MENIU")
        print("1. Modifica muzica")
        print("2. Afiseaza muzicile")
        print("3. Adauga muzici aleatorii")
        print("4. Exporta muzici in fisier CSV")
        print("0. Iesire")

    def modifica_muzica(self):
        """
        Functie care modifica o muzica
        :return: None
        """
        titlu = input("Introduceti un titlu: ")
        artist = input("Introduceti un artist: ")
        gen = input("Introduceti un gen: ")
        durata = input("Introduceti o durata: ")
        gen_nou = input("Introduceti un gen nou al muzicii: ")
        durata_noua = input("Introduceti o noua durata a muzicii: ")

        try:
            durata = int(durata)
            durata_noua = int(durata_noua)
            self.__controller.modifica_muzica(titlu, artist, gen, durata, gen_nou, durata_noua)
            print("Muzica modificata cu succes!")
        except ValueError as e:
            print(e)

    def afiseaza_muzicile(self):
        """
        Functie care afiseaza toate muzicile
        :return: None
        """
        muzici = self.__controller.get_all_muzici()
        for muzica in muzici:
            print(muzica)

    def adauga_muzici_aleatorii(self):
        """
        Functie care adauga muzici aleatorii
        :return: None
        """
        numar_melodii = int(input("Introduceti numarul de muzici de generat: "))
        titluri_input = input("Introduceti titlurile (separate prin virgula): ")
        artisti_input = input("Introduceti artistii (separati prin virgula): ")

        titluri = [titlu.strip() for titlu in titluri_input.split(",")]
        artisti = [artist.strip() for artist in artisti_input.split(",")]

        genuri = ["Rock", "Pop", "Jazz", "Altele"]

        melodii_adaugate = 0
        for _ in range(numar_melodii):
            titlu = random.choice(titluri)
            artist = random.choice(artisti)
            gen = random.choice(genuri)
            durata = random.randint(180, 300)

            muzica_noua = Muzica(titlu, artist, gen, durata)
            try:
                self.__controller.adauga_muzica(muzica_noua)
                melodii_adaugate += 1
                print(f"Muzica adaugata: {muzica_noua}")
            except ValueError as e:
                print(f"Eroare: {e}")

        print(f"Au fost adaugate {melodii_adaugate} muzici.")

    def export_csv(self):
        filename = input("Introduceti numele fisierului CSV: ")
        self.__controller.export_csv(filename)
        print(f"Muzicile au fost exportate in fisierul {filename}")

    def run(self):
        """
        Functie care ruleaza meniul aplicatiei
        :return: None
        """
        while True:
            self.display_menu()
            optiune = input("Optiune: ")
            if optiune == "1":
                self.modifica_muzica()
            elif optiune == "2":
                self.afiseaza_muzicile()
            elif optiune == "3":
                self.adauga_muzici_aleatorii()
            elif optiune == "4":
                self.export_csv()
            elif optiune == "0":
                break
            else:
                print("Optiune invalida!")