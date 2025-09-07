class ConsoleUI:
    def __init__(self, controller):
        """
        Constructorul clasei
        :param controller: obiect de tip BaschetController
        """
        self.__controller = controller

    def display_menu(self):
        """
        Functie care afiseaza meniul aplcatiei
        :return: None
        """
        print("MENIU")
        print("1. Adauga jucator")
        print("2. Modifica jucator")
        print("3. Afiseaza jucatori")
        print("4. Creeaza echipa")
        print("5. Importa jucatori")
        print("0. Iesire")

    def adauga_jucator(self):
        """
        Functie care adauga un jucator
        :return: None
        """
        nume = input("Introduceti numele jucatorului: ")
        prenume = input("Introduceti prenumele jucatorului: ")
        inaltime = int(input("Introduceti inaltimea jucatorului: "))
        post = input("Introduceti postul jucatorului: ")
        try:
            self.__controller.adauga_jucator(nume, prenume, inaltime, post)
            print("Jucator adaugat cu succes!")
        except Exception as e:
            print(e)

    def modifica_jucator(self):
        """
        Functie care modifica inaltimea unui jucator
        :return: None
        """
        nume = input("Introduceti numele jucatorului: ")
        prenume = input("Introduceti prenumele jucatorului: ")
        inaltime = int(input("Introduceti inaltimea jucatorului: "))
        post = input("Introduceti postul jucatorului: ")
        inaltime_noua = int(input("Introduceti noua inaltime a jucatorului: "))
        try:
            self.__controller.modifica_jucator(nume, prenume, inaltime, post, inaltime_noua)
            print("Jucator modificat cu succes!")
        except Exception as e:
            print(e)

    def get_all_jucatori(self):
        """
        Functie care afiseaza toti jucatorii
        :return: None
        """
        jucatori = self.__controller.get_all_jucatori()
        for j in jucatori:
            print(j)

    def creeaza_echipa(self):
        """
        Afiseaza echipa cu media de inaltime maxima
        :return: None
        """
        try:
            echipa = self.__controller.creeaza_echipa()
            print("\nEchipa cu media cea mai mare:")
            print(f"{'Prenume':<12}{'Nume':<12}{'Post':<10}{'Inaltime'}")
            print("-" * 40)
            for j in echipa:
                print(f"{j.get_prenume():<12}{j.get_nume():<12}{j.get_post():<10}{j.get_inaltime()}")
        except ValueError as e:
            print(e)

    def importa_jucatori(self):
        """
        Functie care importa jucatori dintr-un fisier existent
        :return: None
        """
        file_path = input("Introduceti numele fisierului pentru import: ")
        rezultat = self.__controller.importa_jucatori(file_path)

        if rezultat == -1:
            print("Fisierul specificat nu exista!")
        else:
            print(f"Import finalizat. Au fost adaugati {rezultat} jucatori")

    def run(self):
        """
        Functie care ruleaza aplicatia
        :return: None
        """
        while True:
            self.display_menu()
            optiune = input("Introduceti o optiune: ")
            if optiune == "1":
                self.adauga_jucator()
            elif optiune == "2":
                self.modifica_jucator()
            elif optiune == "3":
                self.get_all_jucatori()
            elif optiune == "4":
                self.creeaza_echipa()
            elif optiune == "5":
                self.importa_jucatori()
            elif optiune == "0":
                break
            else:
                print("Optiune invalida!")