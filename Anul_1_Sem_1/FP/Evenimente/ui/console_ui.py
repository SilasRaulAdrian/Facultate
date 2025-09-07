from datetime import datetime

from domain.evenimente import Eveniment


class ConsoleUI:
    def __init__(self, ctrl):
        """
        Constructorul clasei
        :param ctrl: obiect de tip EvenimentController
        """
        self.__ctrl = ctrl

    def display_menu(self):
        """
        Functie care afiseaza meniul aplicatiei
        :return: None
        """
        print("MENIU")
        print("1. Adauga eveniment")
        print("2. Afiseaza evenimente")
        print("3. Afiseaza evenimente sortate dupa ora cu o data specificata")
        print("4. Exporta evenimente in fisier")
        print("0. Iesire")

    def get_all_evenimente(self):
        """
        Functie care afiseaza toate evenimentele
        :return: None
        """
        evenimente = self.__ctrl.get_all_evenimente()
        for e in evenimente:
            print(e)

    def sorteaza_evenimente(self):
        """
        Functie care afiseaza evenimentele sortate dupa ora
        :return: None
        """
        evenimente_sortate = self.__ctrl.sorteaza_evenimente()
        for e in evenimente_sortate:
            print(e)

    def adauga_evenimente(self):
        """
        Functie care adauga un eveniment
        :return: None
        """
        date = input("Introduceti data evenimentului: ")
        hour = input("Introduceti ora evenimentului: ")
        descriere = input("Introduceti descrierea evenimentului: ")

        try:
            data = datetime.strptime(date, "%d.%m.%Y").date()
            data_formatata = data.strftime("%d.%m.%Y")
            ora = datetime.strptime(hour, "%H:%M").time()
            ora_formatata = ora.strftime("%H:%M")
            self.__ctrl.adauga_eveniment(data_formatata, ora_formatata, descriere)
            print("Eveniment adaugat cu succes!")
        except Exception as e:
            print(e)

    def sorteaza_eveimente_data(self, data):
        """
        Functie care afiseaza evenimentele sortate dupa ora pentru o data specificata
        :param data: data selectata (format dd.MM.yyyy)
        :return: None
        """
        try:
            evenimente_sortate = self.__ctrl.sorteaza_evenimente_data(data)
            if len(evenimente_sortate) == 0:
                print(f"Nu există evenimente pentru data {data}.")
            else:
                for ev in evenimente_sortate:
                    print(ev)
        except Exception as e:
            print(e)

    def export_evenimente(self):
        """
        Functie care permite utilizatorului sa exporte evenimentele intr-un fisier
        :return: None
        """
        filename = input("Introduceti numele fisierului pentru export: ")
        str = input("Introduceti sirul de caractere pentru cautare in descriere: ")

        try:
            self.__ctrl.export_evenimente(filename, str)
            print("Evenimentele au fost exportate cu succes!")
        except Exception as e:
            print(f"Eroare la export: {e}")

    def run(self):
        """
        Functie care ruleaza meniul aplicatiei
        :return: None
        """
        data_selectata = None
        while True:
            self.display_menu()
            if data_selectata is None:
                print("\nEvenimentele din ziua curenta: ")
                self.sorteaza_evenimente()
            else:
                print(f"\nEvenimentele din data {data_selectata}: ")
                self.sorteaza_eveimente_data(data_selectata)
            optiune = input("Introduceti o optiune: ")
            if optiune == "1":
                self.adauga_evenimente()
            elif optiune == "2":
                self.get_all_evenimente()
            elif optiune == "3":
                date = input("Introduceti data pentru afisarea evenimentelor (format dd.MM.yyyy): ")
                try:
                    data = datetime.strptime(date, "%d.%m.%Y").date()
                    data_selectata = data.strftime("%d.%m.%Y")
                    print(f"Data selectata: {data_selectata}")
                except ValueError:
                    print("Data introdusa nu este valida!")
            elif optiune == "4":
                self.export_evenimente()
            elif optiune == "0":
                break
            else:
                print("Optiune invalida!")