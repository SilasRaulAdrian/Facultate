class ConsoleUI:
    def __init__(self, controller):
        """
        Constructorul clasei
        :param controller: obiect de tip EmisiuneController
        """
        self.__controller = controller

    def display_menu(self):
        """
        Functie care afiseaza meniul aplicatiei
        :return:
        """
        print("MENIU")
        print("1. Sterge emisiune")
        print("2. Modifica emisiune")
        print("3. Afiseaza emisiunile")
        print("4. Genereaza programe")
        print("5. Adauga tip blocat")
        print("6. Sterge tip blocat")
        print("0. Iesire aplicatie")

    def sterge_emisiune(self):
        """
        Functie care sterge o emisiune
        :return: None
        """
        nume = input("Introduceti un nume: ")
        tip = input("Introduceti un tip: ")
        durata = int(input("Introduceti o durata: "))
        descriere = input("Introduceti o descriere: ")
        try:
            self.__controller.sterge_emisiune(nume, tip, durata, descriere)
            print("Emisiune stearsa cu succes!")
        except ValueError as e:
            print(e)

    def modifica_emisiune(self):
        """
        Functie care modifica o emisiune
        :return: None
        """
        nume = input("Introduceti un nume: ")
        tip = input("Introduceti un tip: ")
        durata = int(input("Introduceti o durata: "))
        descriere = input("Introduceti o descriere: ")
        durata_noua = int(input("Introduceti o noua durata: "))
        descriere_noua = input("Introduceti o noua descriere: ")
        try:
            self.__controller.modifica_emisiune(nume, tip, durata, descriere, durata_noua, descriere_noua)
            print("Emisiune modificata cu succes")
        except ValueError as e:
            print(e)

    def get_all_emisiuni(self):
        """
        Functie care afiseaza toate emisiunile
        :return: None
        """
        emisiuni = self.__controller.get_all_emisiuni()
        for e in emisiuni:
            print(e)

    def genereaza_program(self):
        """
        Functie care genereaza si afiseaza un program TV.
        :return: None
        """
        try:
            ora_inceput = int(input("Introduceti ora de inceput: "))
            ora_sfarsit = int(input("Introduceti ora de sfarsit: "))
            program = self.__controller.genereaza_program(ora_inceput, ora_sfarsit)
            print(f"{'Ora':<5} {'Nume':<20} {'Tip':<15} {'Descriere':<25}")
            print("-" * 65)
            for ora, emisiune in program:
                print(f"{ora:<5} {emisiune.get_nume():<20} {emisiune.get_tip():<15} {emisiune.get_descriere():<25}")
        except ValueError as e:
            print(e)

    def adauga_tip_blocat(self):
        tip = input("Introduceti tipul de emisiune care va fi blocat: ")
        self.__controller.adauga_tip_blocat(tip)
        print(f"Tipul '{tip}' a fost blocat.")

    def sterge_tip_blocat(self):
        tip = input("Introduceti tipul de emisiune care va fi deblocat: ")
        self.__controller.sterge_tip_blocat(tip)
        print(f"Tipul '{tip}' a fost deblocat.")

    def run(self):
        """
        Functie care ruleaza meniul aplicatiei
        :return: None
        """
        while True:
            self.display_menu()
            optiune = input("Introduceti o optiune: ")
            if optiune == "1":
                self.sterge_emisiune()
            elif optiune == "2":
                self.modifica_emisiune()
            elif optiune == "3":
                self.get_all_emisiuni()
            elif optiune == "4":
                self.genereaza_program()
            elif optiune == "5":
                self.adauga_tip_blocat()
            elif optiune == "6":
                self.sterge_tip_blocat()
            elif optiune == "0":
                break
            else:
                print("Optiune invalida")