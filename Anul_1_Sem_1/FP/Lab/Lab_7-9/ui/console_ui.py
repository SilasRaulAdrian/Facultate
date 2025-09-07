class LibraryUI:
    def __init__(self, controller):
        self._controller = controller

    def display_menu(self):
        """
        Afiseaza meniul
        :return: -
        """
        print("-----------------------------------------------------------------------------------------")
        print("1. Adauga carte")
        print("2. Sterge carte")
        print("3. Modifica carte")
        print("4. Afiseaza carti")
        print("5. Adauga client")
        print("6. Sterge client")
        print("7. Modifica client")
        print("8. Afiseaza clienti")
        print("9. Cauta carte dupa id")
        print("10. Cauta carti dupa titlu")
        print("11. Cauta carti dupa autor")
        print("12. Cauta client dupa id")
        print("13. Cauta clienti dupa nume")
        print("14. Cauta clienti dupa CNP")
        print("15. Inchiriaza carte")
        print("16. Returneaza carte")
        print("17. Afiseaza inchirieri")
        print("18. Cele mai inchiriate carti")
        print("19. Cei mai activi clienti sortati dupa nume")
        print("20. Cei mai activi clienti sortati dupa numarul de inchirieri")
        print("21. Top 20% clienti activi")
        print("0. Exit")

    def add_book(self):
        """
        Adauga o carte
        :return: -
        """
        book_id = int(input("Id carte: "))
        title = input("Titlu: ")
        description = input("Descriere: ")
        author = input("Autor: ")
        try:
            self._controller.add_book(book_id, title, description, author)
            print("Carte adaugata cu succes!")
        except ValueError as ve:
            print(ve)

    def delete_book(self):
        """
        Sterge o carte
        :return: -
        """
        book_id = int(input("Id carte: "))
        try:
            self._controller.delete_book(book_id)
            print("Carte stearsa cu succes!")
        except ValueError as ve:
            print(ve)

    def update_book(self):
        """
        Modifica o carte
        :return: -
        """
        book_id = int(input("Id carte: "))
        title = input("Titlu: ")
        description = input("Descriere: ")
        author = input("Autor: ")
        try:
            self._controller.update_book(book_id, title, description, author)
            print("Carte modificata cu succes!")
        except ValueError as ve:
            print(ve)

    def list_books(self):
        """
        Afiseaza toate cartile
        :return: -
        """
        books = self._controller.list_books()
        for book in books:
            print(book)

    def add_client(self):
        """
        Adauga un client
        :return: -
        """
        client_id = int(input("Id client: "))
        name = input("Nume: ")
        cnp = input("CNP: ")
        try:
            self._controller.add_client(client_id, name, cnp)
            print("Client adaugat cu succes!")
        except ValueError as ve:
            print(ve)

    def delete_client(self):
        """
        Sterge un client
        :return: -
        """
        client_id = int(input("Id client: "))
        try:
            self._controller.delete_client(client_id)
            print("Client sters cu succes!")
        except ValueError as ve:
            print(ve)

    def update_client(self):
        """
        Modifica un client
        :return: -
        """
        client_id = int(input("Id client: "))
        name = input("Nume: ")
        cnp = input("CNP: ")
        try:
            self._controller.update_client(client_id, name, cnp)
            print("Client modificat cu succes!")
        except ValueError as ve:
            print(ve)

    def list_clients(self):
        """
        Afiseaza toti clientii
        :return: -
        """
        clients = self._controller.list_clients()
        for client in clients:
            print(client)

    def search_book_by_id(self):
        """
        Cauta o carte dupa id
        :return: -
        """
        book_id = int(input("Id carte: "))
        try:
            book = self._controller.find_book_by_id(book_id)
            print(book)
        except ValueError as ve:
            print(ve)

    def search_books_by_title(self):
        """
        Cauta carti dupa titlu
        :return: -
        """
        title = input("Titlu: ")
        books = self._controller.find_books_by_title(title)
        for book in books:
            print(book)

    def search_books_by_author(self):
        """
        Cauta carti dupa autor
        :return: -
        """
        author = input("Autor: ")
        books = self._controller.find_books_by_author(author)
        for book in books:
            print(book)

    def search_client_by_id(self):
        """
        Cauta un client dupa id
        :return: -
        """
        client_id = int(input("Id client: "))
        try:
            client = self._controller.find_client_by_id(client_id)
            print(client)
        except ValueError as ve:
            print(ve)

    def search_clients_by_name(self):
        """
        Cauta client dupa nume
        :return: -
        """
        name = input("Nume: ")
        clients = self._controller.find_clients_by_name(name)
        for client in clients:
            print(client)

    def search_clients_by_cnp(self):
        """
        Cauta client dupa CNP
        :return: -
        """
        cnp = input("CNP: ")
        clients = self._controller.find_clients_by_cnp(cnp)
        for client in clients:
            print(client)

    def rent_book(self):
        """
        Inchiriaza o carte
        :return: -
        """
        rental_id = int(input("Id inchiriere: "))
        book_id = int(input("Id carte: "))
        client_id = int(input("Id client: "))
        try:
            self._controller.rent_book(rental_id, book_id, client_id)
            print("Carte inchiriata cu succes!")
        except ValueError as ve:
            print(ve)

    def return_book(self):
        """
        Returneaza o carte
        :return: -
        """
        book_id = int(input("Id carte: "))
        try:
            self._controller.return_book(book_id)
            print("Carte returnata cu succes!")
        except ValueError as ve:
            print(ve)

    def list_rentals(self):
        """
        Afiseaza toate inchirierile
        :return: -
        """
        rentals = self._controller.list_rentals()
        for rental in rentals:
            print(rental)

    def most_rented_books(self):
        """
        Afiseaza cele mai inchiriate carti
        :return: -
        """
        books = self._controller.most_rented_books()
        for book, count in books:
            print(f"Book: {book.title}, Rentals: {count}")

    def most_active_clients_sorted_by_name(self):
        """
        Afiseaza cei mai activi clienti sortati dupa nume
        :return: -
        """
        clients = self._controller.clients_with_most_rentals(sort_by="name")
        for client, count in clients:
            print(f"Client: {client.name}, Rentals: {count}")

    def most_active_clients_sorted_by_rentals(self):
        """
        Afiseaza cei mai activi clienti sortati dupa numarul de inchirieri
        :return: -
        """
        clients = self._controller.clients_with_most_rentals(sort_by="rentals")
        for client, count in clients:
            print(f"Client: {client.name}, Rentals: {count}")

    def top_20_percent_active_clients(self):
        """
        Afiseaza primii 20% clienti activi
        :return: -
        """
        top_clients = self._controller.top_20_percent_active_clients()
        for client, count in top_clients:
            print(f"Client: {client.name}, Rentals: {count}")

    def start(self):
        """
        Porneste aplicatia
        :return: -
        """
        while True:
            self.display_menu()
            option = input("Alege o optiune: ")
            if option == "1":
                self.add_book()
            elif option == "2":
                self.delete_book()
            elif option == "3":
                self.update_book()
            elif option == "4":
                self.list_books()
            elif option == "5":
                self.add_client()
            elif option == "6":
                self.delete_client()
            elif option == "7":
                self.update_client()
            elif option == "8":
                self.list_clients()
            elif option == "9":
                self.search_book_by_id()
            elif option == "10":
                self.search_books_by_title()
            elif option == "11":
                self.search_books_by_author()
            elif option == "12":
                self.search_client_by_id()
            elif option == "13":
                self.search_clients_by_name()
            elif option == "14":
                self.search_clients_by_cnp()
            elif option == "15":
                self.rent_book()
            elif option == "16":
                self.return_book()
            elif option == "17":
                self.list_rentals()
            elif option == "18":
                self.most_rented_books()
            elif option == "19":
                self.most_active_clients_sorted_by_name()
            elif option == "20":
                self.most_active_clients_sorted_by_rentals()
            elif option == "21":
                self.top_20_percent_active_clients()
            elif option == "0":
                break
            else:
                print("Optiune invalida!")