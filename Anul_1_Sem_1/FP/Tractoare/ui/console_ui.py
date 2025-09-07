from datetime import datetime

from domain.tractoare import Tractor


class ConsoleUI:
    def __init__(self, controller):
        """
        Constructorul clasei
        :param controller: obiect de tip TractorController
        """
        self.__controller = controller

    def display_menu(self):
        """
        Functie care afiseaza meniul aplicatiei
        :return: None
        """
        print("MENIU")
        print("1. Adauga tractor")
        print("2. Sterge tractor")
        print("3. Afiseaza tractoare")
        print("4. Filtreaza tractoare")
        print("5. Undo")
        print("0. Iesire")

    def adauga_tractor(self):
        """
        Functie care adauga un tractor
        :return: None
        """
        id = int(input("Id-ul tractorului: "))
        denumire = input("Denumirea tractorului: ")
        pret = int(input("Pretul tractorului: "))
        model = input("Modelul tractorului: ")
        date = input("Data la care expira revizia tractorului: ")

        try:
            data = datetime.strptime(date, "%d/%m/%Y").date()
            data_formatata = data.strftime("%d/%m/%Y")
            self.__controller.adauga_tractor(id, denumire, pret, model, data_formatata)
            print("Tractor adaugat cu succes!")
        except Exception as e:
            print(e)

    def sterge_tractor(self):
        """
        Functie care sterge tractoarele pentru care pretul contine cifra data
        :return: None
        """
        cifra = input("Introduceti o cifra: ")
        try:
            cifra = int(cifra)
            numar_sterse = self.__controller.sterge_tractor(cifra)
            if numar_sterse > 0:
                print(f"S-au sters {numar_sterse} tractoare care contineau cifra {cifra} in pret.")
            else:
                print(f"Nu exista tractoare care sa contina cifra {cifra} in pret.")
        except ValueError as e:
            print(e)

    def get_all_tractoare(self):
        """
        Functie care afiseaza toate tractoarele
        :return: None
        """
        tractoare = self.__controller.get_all_tractoare()
        for t in tractoare:
            print(t)

    def filtreaza_tractoare(self):
        """
        Functie care filtreaza tractoarele
        :return: None
        """
        text = input("Introduceti un text: ")
        numar = input("Introduceti un numar: ")
        try:
            numar = int(numar)
        except Exception:
            print("Trebuie sa fie un numar natural sau -1 in cazul in care nu se doreste filtru")

        if self.__controller.filtreaza_tractoare(text, numar) is not []:
            for tractor in self.__controller.filtreaza_tractoare(text, numar):
                print(tractor)

    def undo(self):
        """
        Functie care anuleaza ultima modificare
        :return: None
        """
        message = self.__controller.undo()
        print(message)

    def run(self):
        """
        Functie care ruleaza meniul aplicatiei
        :return: None
        """
        while True:
            self.display_menu()
            optiune = input("Optiune: ")
            if optiune == "1":
                self.adauga_tractor()
            elif optiune == "2":
                self.sterge_tractor()
            elif optiune == "3":
                self.get_all_tractoare()
            elif optiune == "4":
                self.filtreaza_tractoare()
            elif optiune == "5":
                self.undo()
            elif optiune == "0":
                break
            else:
                print("Optiune invalida!")


