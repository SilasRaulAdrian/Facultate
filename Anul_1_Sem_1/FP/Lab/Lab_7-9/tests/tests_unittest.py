import unittest
from controller.library_controller import LibraryController
from domain.rental import Rental
from repository.book_repository import BookRepository
from domain.book import Book
from repository.client_repository import ClientRepository
from domain.client import Client
from repository.rental_repository import RentalRepository
from validator.book_valid import BookValidator
from validator.client_valid import ClientValidator
from validator.rental_valid import RentalValidator
from repository.book_file_repository import BookFileRepository
from repository.client_file_repository import ClientFileRepository
from repository.rental_file_repository import RentalFileRepository

class TestBookRepository(unittest.TestCase):
    def test_add_book(self):
        repo = BookRepository()
        book = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        repo.add_book(book)
        self.assertEqual(len(repo.get_all_books()), 1, "Cartea nu a fost adaugata!")
        self.assertEqual(repo.get_all_books()[0].book_id, 1, "Id-ul cartii nu este corect!")
        self.assertEqual(repo.get_all_books()[0].title, "Ion", "Titlul cartii nu este corect!")
        self.assertEqual(repo.get_all_books()[0].description, "Bac", "Descrierea cartii nu este corecta!")
        self.assertEqual(repo.get_all_books()[0].author, "Liviu Rebreanu", "Autorul cartii nu este corect!")

    def test_delete_book(self):
        repo = BookRepository()
        book = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        repo.add_book(book)
        self.assertEqual(len(repo.get_all_books()), 1, "Cartea nu a fost adaugata!")
        repo.delete_book(1)
        self.assertEqual(len(repo.get_all_books()), 0, "Cartea nu a fost stearsa!")

    def test_update_book(self):
        repo = BookRepository()
        book = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        repo.add_book(book)
        repo.update_book(1, "Moara cu noroc", "Bac", "Ioan Slavici")
        new_book = repo.get_all_books()[0]
        self.assertEqual(new_book.book_id, 1, "Id-ul cartii nu este corect!")
        self.assertEqual(new_book.title, "Moara cu noroc", "Titlul cartii nu este corect!")
        self.assertEqual(new_book.description, "Bac", "Descrierea cartii nu este corecta!")
        self.assertEqual(new_book.author, "Ioan Slavici", "Autorul cartii nu este corect!")

    def run_tests(self):
        print("Ruleaza testele pentru BookRepository...")
        self.test_add_book()
        print("test_add_book a trecut cu succes!")
        self.test_delete_book()
        print("test_delete_book a trecut cu succes!")
        self.test_update_book()
        print("test_update_book a trecut cu succes!")
        print("Toate testele pentru BookRepository au trecut cu succes!")

class TestClientRepository(unittest.TestCase):
    def test_add_client(self):
        repo = ClientRepository()
        client = Client(1, "Ion", "1234567890123")
        repo.add_client(client)
        self.assertEqual(len(repo.get_all_clients()), 1, "Clientul nu a fost adaugat!")
        self.assertEqual(repo.get_all_clients()[0].client_id, 1, "Id-ul clientului nu este corect!")
        self.assertEqual(repo.get_all_clients()[0].name, "Ion", "Numele clientului nu este corect!")
        self.assertEqual(repo.get_all_clients()[0].cnp, "1234567890123", "CNP-ul clientului nu este corect!")

    def test_delete_client(self):
        repo = ClientRepository()
        client = Client(1, "Ion", "1234567890123")
        repo.add_client(client)
        self.assertEqual(len(repo.get_all_clients()), 1, "Clientul nu a fost adaugat!")
        repo.delete_client(1)
        self.assertEqual(len(repo.get_all_clients()), 0, "Clientul nu a fost sters!")

    def test_update_client(self):
        repo = ClientRepository()
        client = Client(1, "Ion", "1234567890123")
        repo.add_client(client)
        repo.update_client(1, "Vasile", "1234567890123")
        new_client = repo.get_all_clients()[0]
        self.assertEqual(new_client.client_id, 1, "Id-ul clientului nu este corect!")
        self.assertEqual(new_client.name, "Vasile", "Numele clientului nu este corect!")
        self.assertEqual(new_client.cnp, "1234567890123", "CNP-ul clientului nu este corect!")

    def run_tests(self):
        print("Ruleaza testele pentru ClientRepository...")
        self.test_add_client()
        print("test_add_client a trecut cu succes!")
        self.test_delete_client()
        print("test_delete_client a trecut cu succes!")
        self.test_update_client()
        print("test_update_client a trecut cu succes!")
        print("Toate testele pentru ClientRepository au trecut cu succes!")

class TestLibraryController(unittest.TestCase):
    def setUp(self):
        self._book_repo = BookRepository()
        self._client_repo = ClientRepository()
        self._rental_repo = RentalRepository()
        self._book_validator = BookValidator()
        self._client_validator = ClientValidator()
        self._rental_validator = RentalValidator()
        self._controller = LibraryController(self._book_repo, self._client_repo, self._rental_repo)

    def test_search_books(self):
        self.setUp()
        book = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        self._book_repo.add_book(book)
        self.assertEqual(self._controller.find_book_by_id(1), book, "Cartea nu a fost gasita dupa id!")
        self.assertEqual(self._controller.find_books_by_title("Ion"), [book], "Cartea nu a fost gasita dupa titlu!")
        self.assertEqual(self._controller.find_books_by_author("Liviu Rebreanu"), [book], "Cartea nu a fost gasita dupa autor!")

    def test_search_clients(self):
        self.setUp()
        client = Client(1, "Ion", "1234567890123")
        self._client_repo.add_client(client)
        self.assertEqual(self._controller.find_client_by_id(1), client, "Clientul nu a fost gasit dupa id!")
        self.assertEqual(self._controller.find_clients_by_name("Ion"), [client], "Clientul nu a fost gasit dupa nume!")
        self.assertEqual(self._controller.find_clients_by_cnp("1234567890123"), [client], "Clientul nu a fost gasit dupa cnp!")

    def test_rent_and_return_book_white_box(self):
        """ Testeaza inchirierea si returnarea unei carti cu metoda white-box """
        self.setUp()
        book = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        client = Client(1, "Ion", "1234567890123")
        self._book_repo.add_book(book)
        self._client_repo.add_client(client)

        self._controller.rent_book(1, 1, 1)
        self.assertEqual(len(self._rental_repo.get_all_rentals()), 1, "Cartea nu a fost inchiriata!")

        self._controller.return_book(1)
        self.assertEqual(len(self._rental_repo.get_all_rentals()), 0, "Cartea nu a fost returnata!")

    def test_validate_book(self):
        self.setUp()
        book = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        self.assertTrue(self._book_validator.validate_book(book), "Cartea nu a trecut de validare!") # Cartea este valida
        with self.assertRaises(ValueError):
            invalid_book = Book(-1, "Ion", "Bac", "Liviu Rebreanu")
            self._book_validator.validate_book(invalid_book)

    def test_validate_client(self):
        self.setUp()
        client = Client(1, "Ion", "1234567890123")
        self.assertTrue(self._client_validator.validate_client(client), "Clientul nu a trecut de validare!") # Clientul este valid
        with self.assertRaises(ValueError):
            invalid_client = Client(-1, "Ion", "1234567890123")
            self._client_validator.validate_client(invalid_client)

    def test_validate_rental(self):
        self.setUp()
        rental = Rental(1, 1, 1)
        self.assertTrue(self._rental_validator.validate_rental(rental), "Inchirierea nu a trecut de validare!") # Inchirierea este valida
        with self.assertRaises(ValueError):
            invalid_rental = Rental(-1, 1, 1)
            self._rental_validator.validate_rental(invalid_rental)

    def test_most_rented_books_black_box(self):
        """ Testeaza metoda most_rented_books cu metoda black-box """
        self.setUp()
        book1 = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        book2 = Book(2, "Moara cu noroc", "Bac", "Ioan Slavici")
        client = Client(1, "Ion", "1234567890123")
        self._book_repo.add_book(book1)
        self._book_repo.add_book(book2)
        self._client_repo.add_client(client)
        self._rental_repo.add_rental(Rental(1, 1, 1))
        self._rental_repo.add_rental(Rental(2, 2, 1))
        self._rental_repo.add_rental(Rental(3, 1, 1))

        most_rented_books = self._controller.most_rented_books()
        self.assertEqual(most_rented_books[0][0].title, "Ion", "Cartea 'Ion' ar trebui sa fie cea mai inchiriata!")
        self.assertEqual(most_rented_books[0][1], 2, "Cartea 'Ion' ar trebui sa fie inchiriata de 2 ori!")
        self.assertEqual(most_rented_books[1][0].title, "Moara cu noroc", "Cartea 'Moara cu noroc' ar trebui sa fie a doua cea mai inchiriata!")
        self.assertEqual(most_rented_books[1][1], 1, "Cartea 'Moara cu noroc' ar trebui sa fie inchiriata o data!")

    def test_most_active_clients_sorted_by_name(self):
        self.setUp()
        client1 = Client(1, "Ion", "1234567890123")
        client2 = Client(2, "Vasile", "1234567890124")
        self._client_repo.add_client(client1)
        self._client_repo.add_client(client2)
        self._rental_repo.add_rental(Rental(1, 1, 1))
        self._rental_repo.add_rental(Rental(2, 2, 1))

        sorted_clients = self._controller.clients_with_most_rentals(sort_by="name")
        self.assertEqual(sorted_clients[0][0].name, "Ion", "Clientul 'Ion' ar trebui sa fie cel mai activ!")
        # self.assertEqual(sorted_clients[1][0].name, "Vasile", "Clientul 'Vasile' ar trebui sa fie al doilea cel mai activ!")

    def test_most_active_clients_sorted_by_rentals(self):
        self.setUp()
        client1 = Client(1, "Ion", "1234567890123")
        client2 = Client(2, "Vasile", "1234567890124")
        self._client_repo.add_client(client1)
        self._client_repo.add_client(client2)
        self._rental_repo.add_rental(Rental(1, 1, 1))
        self._rental_repo.add_rental(Rental(2, 2, 1))

        sorted_clients = self._controller.clients_with_most_rentals(sort_by="rentals")
        self.assertEqual(sorted_clients[0][0].name, "Ion", "Clientul 'Ion' ar trebui sa fie cel mai activ!")
        # self.assertEqual(sorted_clients[1][0].name, "Vasile", "Clientul 'Vasile' ar trebui sa fie al doilea cel mai activ!")

    def test_top_20_percent_active_clients(self):
        self.setUp()
        client1 = Client(1, "Ion", "1234567890123")
        client2 = Client(2, "Vasile", "1234567890124")
        client3 = Client(3, "Gheorghe", "1234567890125")
        self._client_repo.add_client(client1)
        self._client_repo.add_client(client2)
        self._client_repo.add_client(client3)
        self._rental_repo.add_rental(Rental(1, 1, 1))
        self._rental_repo.add_rental(Rental(2, 2, 2))
        self._rental_repo.add_rental(Rental(3, 1, 3))

        top_clients = self._controller.top_20_percent_active_clients()
        self.assertEqual(len(top_clients), 1, "Trebuie să fie un singur client în top 20%")
        self.assertEqual(top_clients[0][1], 1, "Clientul cu cele mai multe închirieri nu este corect")

    def run_tests(self):
        print("Ruleaza testele pentru LibraryController...")
        self.test_search_books()
        print("test_search_books a trecut cu succes!")
        self.test_search_clients()
        print("test_search_clients a trecut cu succes!")
        self.test_rent_and_return_book_white_box()
        print("test_rent_and_return_book_white_box a trecut cu succes!")
        self.test_validate_book()
        print("test_validate_book a trecut cu succes!")
        self.test_validate_client()
        print("test_validate_client a trecut cu succes!")
        self.test_validate_rental()
        print("test_validate_rental a trecut cu succes!")
        self.test_most_rented_books_black_box()
        print("test_most_rented_books_black_box a trecut cu succes!")
        self.test_most_active_clients_sorted_by_name()
        print("test_most_active_clients_sorted_by_name a trecut cu succes!")
        self.test_most_active_clients_sorted_by_rentals()
        print("test_most_active_clients_sorted_by_rentals a trecut cu succes!")
        self.test_top_20_percent_active_clients()
        print("test_top_20_percent_active_clients a trecut cu succes!")
        print("Toate testele pentru LibraryController au trecut cu succes!")