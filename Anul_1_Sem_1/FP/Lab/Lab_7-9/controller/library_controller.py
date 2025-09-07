from domain.book import Book
from domain.client import Client
from domain.rental import Rental
from repository.rental_repository import RentalRepository
from validator.book_valid import BookValidator
from validator.client_valid import ClientValidator
from validator.rental_valid import RentalValidator


class LibraryController:
    def __init__(self, book_repo, client_repo, rental_repo):
        self._book_repo = book_repo
        self._client_repo = client_repo
        self._rental_repo = rental_repo

    def add_book(self, book_id, title, description, author):
        """
        Adauga o carte in lista de carti
        :param book_id: int
        :param title: str
        :param description: str
        :param author: str
        :return: -
        """
        book = Book(book_id, title, description, author)
        if BookValidator().validate_book(book):
            self._book_repo.add_book(book)
        else:
            raise ValueError("Cartea nu este valida!")

    def delete_book(self, book_id):
        """
        Sterge o carte din lista de carti
        :param book_id: int
        :return: -
        """
        self._book_repo.delete_book(book_id)

    def update_book(self, book_id, title, description, author):
        """
        Modifica o carte din lista de carti
        :param book_id: int
        :param title: str
        :param description: str
        :param author: str
        :return: -
        """
        book = Book(book_id, title, description, author)
        if BookValidator().validate_book(book):
            self._book_repo.update_book(book_id, title, description, author)
        else:
            raise ValueError("Cartea nu este valida!")

    def list_books(self):
        """
        Returneaza toate cartile din lista de carti
        :return: list<Book>
        """
        return self._book_repo.get_all_books()

    def add_client(self, client_id, name, cnp):
        """
        Adauga un client in lista de clienti
        :param client_id: int
        :param name: str
        :param cnp: str
        :return: -
        """
        client = Client(client_id, name, cnp)
        if ClientValidator().validate_client(client):
            self._client_repo.add_client(client)
        #else:
         #   raise ValueError("Clientul nu este valid!")

    def delete_client(self, client_id):
        """
        Sterge un client din lista de clienti
        :param client_id: int
        :return: -
        """
        self._client_repo.delete_client(client_id)

    def update_client(self, client_id, name, cnp):
        """
        Modifica un client din lista de clienti
        :param client_id: int
        :param name: str
        :param cnp: str
        :return: -
        """
        client = Client(client_id, name, cnp)
        if ClientValidator().validate_client(client):
            self._client_repo.update_client(client_id, name, cnp)
        else:
            raise ValueError("Clientul nu este valid!")

    def list_clients(self):
        """
        Returneaza toti clientii din lista de clienti
        :return: list<Client>
        """
        return self._client_repo.get_all_clients()

    def find_book_by_id(self, book_id):
        """
        Cauta o carte dupa id
        :param book_id: int
        :return: Book
        """
        return self._book_repo.search_book_by_id(book_id)

    def find_books_by_title(self, title):
        """
        Cauta carti dupa titlu
        :param title: str
        :return: list<Book>
        """
        return self._book_repo.search_books_by_title(title)

    def find_books_by_author(self, author):
        """
        Cauta carti dupa autor
        :param author: str
        :return: list<Book>
        """
        return self._book_repo.search_books_by_author(author)

    def find_client_by_id(self, client_id):
        """
        Cauta un client dupa id
        :param client_id: int
        :return: Client
        """
        return self._client_repo.search_client_by_id(client_id)

    def find_clients_by_name(self, name):
        """
        Cauta client dupa nume
        :param name: str
        :return: list<Client>
        """
        return self._client_repo.search_clients_by_name(name)

    def find_clients_by_cnp(self, cnp):
        """
        Cauta client dupa CNP
        :param cnp: str
        :return: list<Client>
        """
        return self._client_repo.search_clients_by_cnp(cnp)

    def rent_book(self, rental_id, book_id, client_id):
        """
        Inchiriaza o carte
        :param rental_id: int
        :param book_id: int
        :param client_id: int
        :return: -
        """
        book = self._book_repo.search_book_by_id(book_id)
        if not book:
            raise ValueError(f"Cartea cu id-ul {book_id} nu exista!")

        client = self._client_repo.search_client_by_id(client_id)
        if not client:
            raise ValueError(f"Clientul cu id-ul {client_id} nu exista!")

        existing_rental = self._rental_repo.find_rental_by_book_id(book_id)
        if existing_rental:
            raise ValueError(f"Cartea cu id-ul {book_id} este deja inchiriata!")

        rental = Rental(rental_id, book_id, client_id)
        if RentalValidator().validate_rental(rental):
            self._rental_repo.add_rental(rental)
        else:
            raise ValueError("Inchirierea nu este valida!")

    def return_book(self, book_id):
        """
        Returneaza o carte
        :param book_id: int
        :return: -
        """
        rental = self._rental_repo.find_rental_by_book_id(book_id)
        if not rental:
            raise ValueError(f"Cartea cu id-ul {book_id} nu este inchiriata!")

        self._rental_repo.delete_rental(rental.rental_id)

    def list_rentals(self):
        """
        Returneaza toate inchirierile din lista de inchirieri
        :return: list<Rental>
        """
        return self._rental_repo.get_all_rentals()

    def quick_sort(self, arr, key_func, low=0, high=None, reverse=False):
        """
        Sorteaza un array folosind quick sort
        :param arr: list
        :param key_func: function
        :param low: int
        :param high: int
        :param reverse: bool - daca True, va sorta descrescator
        :return: -
        """
        if high is None:
            high = len(arr) - 1

        if low < high:
            pi = self.partition(arr, key_func, low, high, reverse)
            self.quick_sort(arr, key_func, low, pi - 1, reverse)
            self.quick_sort(arr, key_func, pi + 1, high, reverse)

    def partition(self, arr, key_func, low, high, reverse=False):
        """
        Partitie pentru quick sort
        :param arr: list
        :param key_func: function
        :param low: int
        :param high: int
        :param reverse: bool - daca True, va sorta descrescator
        :return: int
        """
        i = low - 1
        pivot = arr[high]

        for j in range(low, high):
            if (key_func(arr[j]) >= key_func(pivot) if reverse else key_func(arr[j]) <= key_func(pivot)):
                i += 1
                arr[i], arr[j] = arr[j], arr[i]

        arr[i + 1], arr[high] = arr[high], arr[i + 1]
        return i + 1

    def gnome_sort(self, arr, key_func):
        """
        Sorteaza un array folosind gnome sort
        :param arr: list
        :param key_func: function
        :return: -
        """

        """
        COMPLEXITATEA:
        
        Descriere:
        - GnomeSort este un algoritm de sortare relativ simplu, dar ineficient pentru liste mari.
        - Algoritmul se bazeaza pe o abordare similara cu InsertionSort, dar in loc sa incerce sa plaseze
          elementele in pozitia corecta, muta elementele una cate una inapoi (in functie de comparatii),
          pana cand ajunge intr-o pozitie corecta.
          
        Complexitatea:
        - Cazul cel mai rau si cel mai bun:
            - Cazul cel mai bun (lista deja sortata) presupune ca algoritmul va parcurge lista o singura
              data, cu fiecare element avand comparatii constante. Astfel, complexitatea pentru cazul cel
              mai bun este O(n).
            - Cazul cel mai rau (lista invers sortata) presupune ca pentru fiecare element se vor face 
              comparatii si mutari pentru toate celelalte elemente, deci in total, pentru fiecare element se
              fac aproximativ n comparatii.
            - Complexitatea totala este de O(n^2) in cel mai rau caz.
        - Complexitatea in functie de cheia de sortare:
            - Similar cu QuickSort, fiecare comparatie presupune apelarea functiei key_func, care poate
              avea un cost suplimentar de O(k).
            - Astfel, complexitatea devine O(n^2 * k), unde k este complexitatea functiei key_func.
            
        Rezumat:
        - Cazul cel mai bun: O(n).
        - Cazul cel mai rau si mediu: O(n^2).
        - Complexitatea cu key_func: O(n^2 * k). 
        """

        index = 0
        while index < len(arr):
            # Verifica daca elementul curent este mai mic decât elementul anterior
            if index == 0 or key_func(arr[index - 1]) <= key_func(arr[index]):
                index += 1
            else:
                arr[index], arr[index - 1] = arr[index - 1], arr[index]
                index -= 1

    def merge_sort(self, arr, key_func, reverse=False, low=0, high=None):
        """
        Sorteaza un array folosind merge sort
        :param arr: list
        :param key_func: function
        :param reverse: bool - daca True, va sorta descrescator
        :param low: int
        :param high: int
        :return: -
        """
        if high is None:
            high = len(arr) - 1

        if low < high:
            mid = (low + high) // 2
            self.merge_sort(arr, key_func, reverse, low, mid)
            self.merge_sort(arr, key_func, reverse, mid + 1, high)
            self.merge(arr, key_func, reverse, low, mid, high)

    def merge(self, arr, key_func, reverse, low, mid, high):
        """
        Combina doua subliste sortate
        :param arr: list
        :param key_func: function
        :param reverse: bool - daca True, va sorta descrescator
        :param low: int
        :param mid: int
        :param high: int
        :return: -
        """
        left = arr[low:mid + 1]
        right = arr[mid + 1:high + 1]

        i = j = 0
        k = low

        while i < len(left) and j < len(right):
            if (key_func(left[i]) >= key_func(right[j]) if reverse else key_func(left[i]) <= key_func(right[j])):
                arr[k] = left[i]
                i += 1
            else:
                arr[k] = right[j]
                j += 1
            k += 1

        while i < len(left):
            arr[k] = left[i]
            i += 1
            k += 1

        while j < len(right):
            arr[k] = right[j]
            j += 1
            k += 1

    def most_rented_books(self):
        """
        Returneaza cartile cele mai inchiriate
        :return: list<Book>
        """
        rentals = self._rental_repo.get_all_rentals()
        book_rentals = {}

        for rental in rentals:
            book_rentals[rental.book_id] = book_rentals.get(rental.book_id, 0) + 1

        book_rentals_list = list(book_rentals.items())

        self.merge_sort(book_rentals_list, key_func=lambda x: x[1], reverse=True)

        most_rented_books = []
        for book_id, rental_count in book_rentals_list:
            book = self._book_repo.search_book_by_id(book_id)
            most_rented_books.append((book, rental_count))

        return most_rented_books

    def clients_with_most_rentals(self, sort_by="name"):
        """
        Returneaza clientii cu cele mai multe inchirieri
        :return: list<Client>
        """
        rentals = self._rental_repo.get_all_rentals()
        client_rentals = {}

        for rental in rentals:
            client_rentals[rental.client_id] = client_rentals.get(rental.client_id, 0) + 1

        client_details = []
        for client_id, rental_count in client_rentals.items():
            client = self._client_repo.search_client_by_id(client_id)
            client_details.append((client, rental_count))

        if sort_by == "name":
            self.quick_sort(client_details, key_func=lambda x: x[0].name, reverse=False)
        elif sort_by == "rentals":
            self.gnome_sort(client_details, key_func=lambda x: x[1])

        return client_details

    def top_20_percent_active_clients(self):
        """
        Returneaza primii 20% clienti activi
        :return: list<Client>
        """
        rentals = self._rental_repo.get_all_rentals()
        client_rentals = {}

        for rental in rentals:
            client_rentals[rental.client_id] = client_rentals.get(rental.client_id, 0) + 1

        active_clients = []
        for client_id, rental_count in client_rentals.items():
            client = self._client_repo.search_client_by_id(client_id)
            active_clients.append((client, rental_count))

        self.quick_sort(active_clients, key_func=lambda x: x[1], low=0, high=len(active_clients) - 1, reverse=False)

        top_20_percent_count = max(1, len(active_clients) * 20 // 100)

        return active_clients[:top_20_percent_count]