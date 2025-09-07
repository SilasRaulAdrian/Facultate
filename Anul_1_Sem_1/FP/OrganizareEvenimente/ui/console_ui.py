class ConsoleUI:
    def __init__(self, controller):
        self.controller = controller

    def display_menu(self):
        print("1. Adauga persoana")
        print("2. Sterge persoana")
        print("3. Modifica persoana")
        print("4. Afiseaza persoane")
        print("5. Cauta persoana dupa id")
        print("6. Cauta persoane dupa nume")
        print("7. Cauta persoane dupa adresa")
        print("8. Adauga eveniment")
        print("9. Sterge eveniment")
        print("10. Modifica eveniment")
        print("11. Afiseaza evenimente")
        print("12. Cauta eveniment dupa id")
        print("13. Cauta evenimente dupa data")
        print("14. Cauta evenimente dupa timp")
        print("15. Cauta evenimente dupa descriere")
        print("16. Inscrie persoana la eveniment")
        print("17. Afiseaza inscrieri")
        print("18. Evenimente la care participa o persoana sortate dupa descriere")
        print("19. Evenimente la care participa o persoana sortate dupa data")
        print("20. Persoane care participa la cele mai multe evenimente")
        print("21. Top 20% evenimente cu cei mai multi participanti")
        print("0. Exit")

    def add_persoana(self):
        """
        Adauga o persoana in lista de persoane din fisier
        :return: -
        """
        personID = input("Id persoana: ")
        nume = input("Nume: ")
        adresa = input("Adresa: ")
        try:
            self.controller.add_persoana(personID, nume, adresa)
            print("Persoana adaugata cu succes!")
        except ValueError as ve:
            print(ve)

    def delete_persoana(self):
        """
        Sterge o persoana din lista de persoane din fisier
        :return: -
        """
        personID = input("Id persoana: ")
        try:
            self.controller.delete_persoana(personID)
            print("Persoana stearsa cu succes!")
        except ValueError as ve:
            print(ve)

    def update_persoana(self):
        """
        Modifica o persoana din lista de persoane din fisier
        :return: -
        """
        personID = input("Id persoana: ")
        new_nume = input("Nume nou: ")
        new_adresa = input("Adresa noua: ")
        try:
            self.controller.update_persoana(personID, new_nume, new_adresa)
            print("Persoana modificata cu succes!")
        except ValueError as ve:
            print(ve)

    def list_persoane(self):
        """
        Afiseaza toate persoanele din lista de persoane din fisier
        :return: -
        """
        persoane = self.controller.list_persoane()
        for persoana in persoane:
            print(persoana)

    def search_persoana_by_id(self):
        """
        Cauta o persoana dupa id in lista de persoane din fisier
        :return: -
        """
        personID = int(input("Id persoana: "))
        persoana = self.controller.search_persoana_by_id(personID)
        if persoana is not None:
            print(persoana)
        else:
            print("Persoana nu exista!")

    def search_persoane_by_name(self):
        """
        Cauta persoane dupa nume in lista de persoane din fisier
        :return: -
        """
        nume = input("Nume: ")
        persoane = self.controller.search_persoane_by_name(nume)
        for persoana in persoane:
            print(persoana)

    def search_persoane_by_adresa(self):
        """
        Cauta persoane dupa adresa in lista de persoane din fisier
        :return: -
        """
        adresa = input("Adresa: ")
        persoane = self.controller.search_persoane_by_adresa(adresa)
        for persoana in persoane:
            print(persoana)

    def add_eveniment(self):
        """
        Adauga un eveniment in lista de evenimente din fisier
        :return: -
        """
        eventID = input("Id eveniment: ")
        data = input("Data: ")
        timp = input("Timp: ")
        descriere = input("Descriere: ")
        try:
            self.controller.add_eveniment(eventID, data, timp, descriere)
            print("Eveniment adaugat cu succes!")
        except ValueError as ve:
            print(ve)

    def delete_eveniment(self):
        """
        Sterge un eveniment din lista de evenimente din fisier
        :return: -
        """
        eventID = input("Id eveniment: ")
        try:
            self.controller.delete_eveniment(eventID)
            print("Eveniment sters cu succes!")
        except ValueError as ve:
            print(ve)

    def update_eveniment(self):
        """
        Modifica un eveniment din lista de evenimente din fisier
        :return: -
        """
        eventID = input("Id eveniment: ")
        new_data = input("Data noua: ")
        new_timp = input("Timp nou: ")
        new_descriere = input("Descriere noua: ")
        try:
            self.controller.update_eveniment(eventID, new_data, new_timp, new_descriere)
            print("Eveniment modificat cu succes!")
        except ValueError as ve:
            print(ve)

    def list_evenimente(self):
        """
        Afiseaza toate evenimentele din lista de evenimente din fisier
        :return: -
        """
        evenimente = self.controller.list_evenimente()
        for eveniment in evenimente:
            print(eveniment)

    def search_eveniment_by_id(self):
        """
        Cauta un eveniment dupa id in lista de evenimente din fisier
        :return: -
        """
        eventID = int(input("Id eveniment: "))
        eveniment = self.controller.search_eveniment_by_id(eventID)
        if eveniment is not None:
            print(eveniment)
        else:
            print("Evenimentul nu exista!")

    def search_evenimente_by_data(self):
        """
        Cauta evenimente dupa data in lista de evenimente din fisier
        :return: -
        """
        data = input("Data: ")
        evenimente = self.controller.search_evenimente_by_data(data)
        for eveniment in evenimente:
            print(eveniment)

    def search_evenimente_by_timp(self):
        """
        Cauta evenimente dupa timp in lista de evenimente din fisier
        :return: -
        """
        timp = input("Timp: ")
        evenimente = self.controller.search_evenimente_by_timp(timp)
        for eveniment in evenimente:
            print(eveniment)

    def search_evenimente_by_descriere(self):
        """
        Cauta evenimente dupa descriere in lista de evenimente din fisier
        :return: -
        """
        descriere = input("Descriere: ")
        evenimente = self.controller.search_evenimente_by_descriere(descriere)
        for eveniment in evenimente:
            print(eveniment)

    def inscrie_persoana_la_eveniment(self):
        """
        Inscrie o persoana la un eveniment
        :return: -
        """
        inscriereID = input("Id inscriere: ")
        personID = input("Id persoana: ")
        eventID = input("Id eveniment: ")
        try:
            self.controller.inscrie_persoana(int(inscriereID), int(personID), int(eventID))
            print("Inscriere realizata cu succes!")
        except ValueError as ve:
            print(ve)

    def list_inscrieri(self):
        """
        Afiseaza toate inscrierile din lista de inscrieri din fisier
        :return: -
        """
        inscrieri = self.controller.get_all_inscrieri()
        for inscriere in inscrieri:
            print(inscriere)

    def evenimente_la_care_participa_o_persoana_descriere(self):
        """
        Returneaza evenimentele la care participa o persoana sortate dupa descriere
        :return: -
        """
        personID = int(input("Id persoana: "))
        evenimente = self.controller.evenimente_la_care_participa_o_persoana(personID, sortBy="descriere")
        for eveniment in evenimente:
            print(eveniment)

    def evenimente_la_care_participa_o_persoana_data(self):
        """
        Returneaza evenimentele la care participa o persoana sortate dupa data
        :return: -
        """
        personID = int(input("Id persoana: "))
        evenimente = self.controller.evenimente_la_care_participa_o_persoana(personID, sortBy="data")
        for eveniment in evenimente:
            print(eveniment)

    def persoane_participante_la_cele_mai_multe_evenimente(self):
        """
        Returneaza persoanele care participa la cele mai multe evenimente
        :return: -
        """
        persoane = self.controller.persoane_participante_la_cele_mai_multe_evenimente()
        for persoana, participations in persoane:
            print(f"Persoana: {persoana.get_nume()}, Participations: {participations}")

    def top_20_procent_evenimente_cu_cei_mai_multi_participanti(self):
        """
        Returneaza top 20 evenimente
        :return: -
        """
        evenimente = self.controller.top_20_procent_evenimente_cu_cei_mai_multi_participanti()
        for eveniment, participations in evenimente:
            print(f"Eveniment: {eveniment.get_eventID()}, Participations: {participations}")

    def run(self):
        """
        Ruleaza interfata
        :return: -
        """
        while True:
            self.display_menu()
            option = input("Optiune: ")
            if option == "1":
                self.add_persoana()
            elif option == "2":
                self.delete_persoana()
            elif option == "3":
                self.update_persoana()
            elif option == "4":
                self.list_persoane()
            elif option == "5":
                self.search_persoana_by_id()
            elif option == "6":
                self.search_persoane_by_name()
            elif option == "7":
                self.search_persoane_by_adresa()
            elif option == "8":
                self.add_eveniment()
            elif option == "9":
                self.delete_eveniment()
            elif option == "10":
                self.update_eveniment()
            elif option == "11":
                self.list_evenimente()
            elif option == "12":
                self.search_eveniment_by_id()
            elif option == "13":
                self.search_evenimente_by_data()
            elif option == "14":
                self.search_evenimente_by_timp()
            elif option == "15":
                self.search_evenimente_by_descriere()
            elif option == "16":
                self.inscrie_persoana_la_eveniment()
            elif option == "17":
                self.list_inscrieri()
            elif option == "18":
                self.evenimente_la_care_participa_o_persoana_descriere()
            elif option == "19":
                self.evenimente_la_care_participa_o_persoana_data()
            elif option == "20":
                self.persoane_participante_la_cele_mai_multe_evenimente()
            elif option == "21":
                self.top_20_procent_evenimente_cu_cei_mai_multi_participanti()
            elif option == "0":
                break
            else:
                print("Optiune invalida!")