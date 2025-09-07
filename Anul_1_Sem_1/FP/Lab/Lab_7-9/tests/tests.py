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


class Tests:
    def __init__(self):
        pass

    def test_add_book(self):
        repo = BookRepository()
        book = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        repo.add_book(book)
        assert len(repo.get_all_books()) == 1, "Cartea nu a fost adaugata"
        assert repo.get_all_books()[0].book_id == 1, "Id-ul cartii nu este corect"
        assert repo.get_all_books()[0].title == "Ion", "Titlul cartii nu este corect"
        assert repo.get_all_books()[0].description == "Bac", "Descrierea cartii nu este corecta"
        assert repo.get_all_books()[0].author == "Liviu Rebreanu", "Autorul cartii nu este corect"

    def test_delete_book(self):
        repo = BookRepository()
        book = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        repo.add_book(book)
        assert len(repo.get_all_books()) == 1, "Cartea nu a fost adaugata"
        repo.delete_book(book.book_id)
        assert len(repo.get_all_books()) == 0, "Cartea nu a fost stearsa"

    def test_update_book(self):
        repo = BookRepository()
        book = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        repo.add_book(book)
        assert len(repo.get_all_books()) == 1, "Cartea nu a fost adaugata"
        repo.update_book(1, "Morometii", "Bac", "Marin Preda")
        assert repo.get_all_books()[0].title == "Morometii", "Titlul cartii nu a fost modificat"
        assert repo.get_all_books()[0].description == "Bac", "Descrierea cartii nu a fost modificata"
        assert repo.get_all_books()[0].author == "Marin Preda", "Autorul cartii nu a fost modificat"

    def test_add_client(self):
        repo = ClientRepository()
        client = Client(1, "Raul", "5050118060435")
        repo.add_client(client)
        assert len(repo.get_all_clients()) == 1, "Clientul nu a fost adaugat"
        assert repo.get_all_clients()[0].client_id == 1, "Id-ul clientului nu este corect"
        assert repo.get_all_clients()[0].name == "Raul", "Numele clientului nu este corect"
        assert repo.get_all_clients()[0].cnp == "5050118060435", "CNP-ul clientului nu este corect"

    def test_delete_client(self):
        repo = ClientRepository()
        client = Client(1, "Raul", "5050118060435")
        repo.add_client(client)
        assert len(repo.get_all_clients()) == 1, "Clientul nu a fost adaugat"
        repo.delete_client(client.client_id)
        assert len(repo.get_all_clients()) == 0, "Clientul nu a fost sters"

    def test_update_client(self):
        repo = ClientRepository()
        client = Client(1, "Raul", "5050118060435")
        repo.add_client(client)
        assert len(repo.get_all_clients()) == 1, "Clientul nu a fost adaugat"
        repo.update_client(1, "Raul", "5050118060436")
        assert repo.get_all_clients()[0].name == "Raul", "Numele clientului nu a fost modificat"
        assert repo.get_all_clients()[0].cnp == "5050118060436", "CNP-ul clientului nu a fost modificat"

    def test_search_books(self):
        repo = BookRepository()
        book1 = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        book2 = Book(2, "Morometii", "Bac", "Marin Preda")
        repo.add_book(book1)
        repo.add_book(book2)

        assert repo.search_book_by_id(1) == book1, "Cartea nu a fost gasita"
        books = repo.search_books_by_title("ion")
        assert len(books) == 1 and books[0] == book1, "Cartea cu titlul 'Ion' nu a fost gasita"
        books = repo.search_books_by_author("preda")
        assert len(books) == 1 and books[0] == book2, "Cartea cu autorul 'Preda' nu a fost gasita"

    def test_search_clients(self):
        repo = ClientRepository()
        client1 = Client(1, "Ion Popescu", "1234567890123")
        client2 = Client(2, "Maria Ionescu", "2345678901234")
        repo.add_client(client1)
        repo.add_client(client2)

        assert repo.search_client_by_id(1) == client1, "Clientul nu a fost gasit"
        clients = repo.search_clients_by_name("Ion")
        assert len(clients) == 2 and clients[0] == client1, "Clientul cu numele 'Ion' nu a fost gasit"
        clients = repo.search_clients_by_cnp("2345678901234")
        assert len(clients) == 1 and clients[0] == client2, "Clientul cu CNP-ul '2345678901234' nu a fost gasit"

    def test_rent_and_return_book(self):
        book_repo = BookRepository()
        client_repo = ClientRepository()
        rental_repo = RentalRepository()
        controller = LibraryController(book_repo, client_repo, rental_repo)

        book = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        client = Client(1, "Ion Popescu", "1234567890123")
        book_repo.add_book(book)
        client_repo.add_client(client)

        controller.rent_book(1, 1, 1)
        assert len(rental_repo.get_all_rentals()) == 1, "Cartea nu a fost inchiriata"
        assert rental_repo.get_all_rentals()[0].rental_id == 1, "Id-ul inchirierii nu este corect"
        assert rental_repo.get_all_rentals()[0].book_id == 1, "Id-ul cartii nu este corect"
        assert rental_repo.get_all_rentals()[0].client_id == 1, "Id-ul clientului nu este corect"

        controller.return_book(1)
        assert len(rental_repo.get_all_rentals()) == 0, "Cartea nu a fost returnata"

    def test_validate_book(self):
        book = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        try:
            BookValidator().validate_book(book)
        except ValueError as e:
            assert False, f"Validarea cartii a esuat: {e}"

        invalid_book_id = Book(-1, "Ion", "Bac", "Liviu Rebreanu")
        try:
            BookValidator().validate_book(invalid_book_id)
            assert False, "Validarea cartii nu a esuat pentru id negativ"
        except ValueError as e:
            assert str(e) == "Id-ul cartii trebuie sa fie un numar pozitiv!", f"Mesajul de eroare este incorect: {e}"

        invalid_book_title = Book(1, "", "Bac", "Liviu Rebreanu")
        try:
            BookValidator().validate_book(invalid_book_title)
            assert False, "Validarea cartii nu a esuat pentru titlu gol"
        except ValueError as e:
            assert str(e) == "Titlul cartii nu poate fi gol!", f"Mesajul de eroare este incorect: {e}"

        invalid_book_description = Book(1, "Ion", "", "Liviu Rebreanu")
        try:
            BookValidator().validate_book(invalid_book_description)
            assert False, "Validarea cartii nu a esuat pentru descriere goala"
        except ValueError as e:
            assert str(e) == "Descrierea cartii nu poate fi goala!", f"Mesajul de eroare este incorect: {e}"

        invalid_book_author = Book(1, "Ion", "Bac", "")
        try:
            BookValidator().validate_book(invalid_book_author)
            assert False, "Validarea cartii nu a esuat pentru autor gol"
        except ValueError as e:
            assert str(e) == "Autorul cartii nu poate fi gol!", f"Mesajul de eroare este incorect: {e}"

    def test_validate_client(self):
        client = Client(1, "Ion Popescu", "1234567890123")
        try:
            ClientValidator().validate_client(client)
        except ValueError as e:
            assert False, f"Validarea clientului a esuat: {e}"

        invalid_client_id = Client(-1, "Ion Popescu", "1234567890123")
        try:
            ClientValidator().validate_client(invalid_client_id)
            assert False, "Validarea clientului nu a esuat pentru id negativ"
        except ValueError as e:
            assert str(e) == "Id-ul clientului trebuie sa fie un numar pozitiv!", f"Mesajul de eroare este incorect: {e}"

        invalid_client_name = Client(1, "", "1234567890123")
        try:
            ClientValidator().validate_client(invalid_client_name)
            assert False, "Validarea clientului nu a esuat pentru nume gol"
        except ValueError as e:
            assert str(e) == "Numele clientului nu poate fi gol!", f"Mesajul de eroare este incorect: {e}"

        invalid_client_cnp = Client(1, "Ion Popescu", "")
        try:
            ClientValidator().validate_client(invalid_client_cnp)
            assert False, "Validarea clientului nu a esuat pentru CNP gol"
        except ValueError as e:
            assert str(e) == "CNP-ul clientului nu poate fi gol!", f"Mesajul de eroare este incorect: {e}"

    def test_validate_rental(self):
        rental = Rental(1, 2, 3)
        try:
            RentalValidator().validate_rental(rental)
        except ValueError as e:
            assert False, f"Validarea inchirierii a esuat: {e}"

        invalid_rental_id = Rental(-1, 2, 3)
        try:
            RentalValidator().validate_rental(invalid_rental_id)
            assert False, "Validarea inchirierii nu a esuat pentru id negativ"
        except ValueError as e:
            assert str(e) == "Id-ul inchirierii trebuie sa fie un numar pozitiv!", f"Mesajul de eroare este incorect: {e}"

        invalid_rental_book_id = Rental(1, -2, 3)
        try:
            RentalValidator().validate_rental(invalid_rental_book_id)
            assert False, "Validarea inchirierii nu a esuat pentru id carte negativ"
        except ValueError as e:
            assert str(e) == "Id-ul cartii trebuie sa fie un numar pozitiv!", f"Mesajul de eroare este incorect: {e}"

        invalid_rental_client_id = Rental(1, 2, -3)
        try:
            RentalValidator().validate_rental(invalid_rental_client_id)
            assert False, "Validarea inchirierii nu a esuat pentru id client negativ"
        except ValueError as e:
            assert str(e) == "Id-ul clientului trebuie sa fie un numar pozitiv!", f"Mesajul de eroare este incorect: {e}"

    def test_most_rented_books(self):
        book_repo = BookRepository()
        client_repo = ClientRepository()
        rental_repo = RentalRepository()
        controller = LibraryController(book_repo, client_repo, rental_repo)

        book1 = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        book2 = Book(2, "Morometii", "Bac", "Marin Preda")
        book3 = Book(3, "Enigma Otiliei", "Bac", "George Calinescu")
        client1 = Client(1, "Ion Popescu", "1234567890123")

        book_repo.add_book(book1)
        book_repo.add_book(book2)
        book_repo.add_book(book3)
        client_repo.add_client(client1)

        rental_repo.add_rental(Rental(1, 1, 1))
        rental_repo.add_rental(Rental(2, 2, 1))
        rental_repo.add_rental(Rental(3, 1, 1))

        most_rented_books = controller.most_rented_books()
        assert len(most_rented_books) == 2, "Ar trebui sa existe 2 carti inchiriate"
        assert most_rented_books[0][0].title == "Ion", "Prima carte ar trebui sa fie 'Ion'"
        assert most_rented_books[0][1] == 2, "Cartea 'Ion' ar trebui sa fie inchiriata de 2 ori"
        assert most_rented_books[1][0].title == "Morometii", "A doua carte ar trebui sa fie 'Morometii'"
        assert most_rented_books[1][1] == 1, "Cartea 'Morometii' ar trebui sa fie inchiriata de 1 data"

    def test_most_active_clients_sorted_by_name(self):
        book_repo = BookRepository()
        client_repo = ClientRepository()
        rental_repo = RentalRepository()
        controller = LibraryController(book_repo, client_repo, rental_repo)

        book1 = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        book2 = Book(2, "Morometii", "Bac", "Marin Preda")
        book3 = Book(3, "Enigma Otiliei", "Bac", "George Calinescu")
        client1 = Client(1, "Ion Popescu", "1234567890123")
        client2 = Client(2, "Maria Ionescu", "2345678901234")

        book_repo.add_book(book1)
        book_repo.add_book(book2)
        book_repo.add_book(book3)
        client_repo.add_client(client1)
        client_repo.add_client(client2)

        rental_repo.add_rental(Rental(1, 1, 1))
        rental_repo.add_rental(Rental(2, 2, 1))
        rental_repo.add_rental(Rental(3, 1, 1))
        rental_repo.add_rental(Rental(4, 1, 2))
        rental_repo.add_rental(Rental(5, 2, 2))

        most_active_clients = controller.clients_with_most_rentals(sort_by="name")
        assert len(most_active_clients) == 2, "Ar trebui sa existe 2 clienti activi"
        assert most_active_clients[0][0].name == "Ion Popescu", "Primul client ar trebui sa fie 'Ion Popescu'"
        assert most_active_clients[0][1] == 3, "Clientul 'Ion Popescu' ar trebui sa aiba 3 inchirieri"
        assert most_active_clients[1][0].name == "Maria Ionescu", "Al doilea client ar trebui sa fie 'Maria Ionescu'"
        assert most_active_clients[1][1] == 2, "Clientul 'Maria Ionescu' ar trebui sa aiba 2 inchirieri"

    def test_most_active_clients_sorted_by_rentals(self):
        book_repo = BookRepository()
        client_repo = ClientRepository()
        rental_repo = RentalRepository()
        controller = LibraryController(book_repo, client_repo, rental_repo)

        book1 = Book(1, "Ion", "Bac", "Liviu Rebreanu")
        book2 = Book(2, "Morometii", "Bac", "Marin Preda")
        book3 = Book(3, "Enigma Otiliei", "Bac", "George Calinescu")
        client1 = Client(1, "Ion Popescu", "1234567890123")
        client2 = Client(2, "Maria Ionescu", "2345678901234")

        book_repo.add_book(book1)
        book_repo.add_book(book2)
        book_repo.add_book(book3)
        client_repo.add_client(client1)
        client_repo.add_client(client2)

        rental_repo.add_rental(Rental(1, 1, 1))
        rental_repo.add_rental(Rental(2, 2, 1))
        rental_repo.add_rental(Rental(3, 1, 1))
        rental_repo.add_rental(Rental(4, 1, 2))
        rental_repo.add_rental(Rental(5, 2, 2))

        most_active_clients = controller.clients_with_most_rentals(sort_by="rentals")
        assert len(most_active_clients) == 2, "Ar trebui sa existe 2 clienti activi"
        assert most_active_clients[0][0].name == "Ion Popescu", "Primul client ar trebui sa fie 'Ion Popescu'"
        assert most_active_clients[0][1] == 3, "Clientul 'Ion Popescu' ar trebui sa aiba 3 inchirieri"
        assert most_active_clients[1][0].name == "Maria Ionescu", "Al doilea client ar trebui sa fie 'Maria Ionescu'"
        assert most_active_clients[1][1] == 2, "Clientul 'Maria Ionescu' ar trebui sa aiba 2 inchirieri"

    def test_top_20_percent_active_clients(self):
        book_repo = BookRepository()
        client_repo = ClientRepository()
        rental_repo = RentalRepository()
        controller = LibraryController(book_repo, client_repo, rental_repo)

        client1 = Client(1, "Ion Popescu", "1234567890123")
        client2 = Client(2, "Maria Ionescu", "2345678901234")
        client3 = Client(3, "George Marinescu", "3456789012345")
        client_repo.add_client(client1)
        client_repo.add_client(client2)
        client_repo.add_client(client3)

        rental_repo.add_rental(Rental(1, 1, 1))
        rental_repo.add_rental(Rental(2, 1, 2))
        rental_repo.add_rental(Rental(3, 2, 2))
        rental_repo.add_rental(Rental(4, 1, 3))
        rental_repo.add_rental(Rental(5, 3, 3))

        top_clients = controller.top_20_percent_active_clients()
        assert len(top_clients) == 1, "Ar trebui sa avem un singur client in top 20%"
        assert top_clients[0][0].name == "Maria Ionescu", "Primul client ar trebui sa fie 'Maria Ionescu'"
        assert top_clients[0][1] == 2, "Clientul 'Maria Ionescu' ar trebui sa aiba 2 inchirieri"

    def run_tests(self):
        print("Ruleaza testele...")
        self.test_add_book()
        print("test_add_book a trecut")
        self.test_delete_book()
        print("test_delete_book a trecut")
        self.test_update_book()
        print("test_update_book a trecut")
        self.test_add_client()
        print("test_add_client a trecut")
        self.test_delete_client()
        print("test_delete_client a trecut")
        self.test_update_client()
        print("test_update_client a trecut")
        self.test_search_books()
        print("test_search_books a trecut")
        self.test_search_clients()
        print("test_search_clients a trecut")
        self.test_rent_and_return_book()
        print("test_rent_and_return_book a trecut")
        self.test_validate_book()
        print("test_validate_book a trecut")
        self.test_validate_client()
        print("test_validate_client a trecut")
        self.test_validate_rental()
        print("test_validate_rental a trecut")
        self.test_most_rented_books()
        print("test_most_rented_books a trecut")
        self.test_most_active_clients_sorted_by_name()
        print("test_most_active_clients_sorted_by_name a trecut")
        self.test_most_active_clients_sorted_by_rentals()
        print("test_most_active_clients_sorted_by_rentals a trecut")
        self.test_top_20_percent_active_clients()
        print("test_top_20_percent_active_clients a trecut")
        print("Toate testele au trecut")