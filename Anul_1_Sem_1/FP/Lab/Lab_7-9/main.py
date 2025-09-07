from repository.book_file_repository import BookFileRepository
from repository.book_repository import BookRepository
from repository.client_file_repository import ClientFileRepository
from repository.client_repository import ClientRepository
from controller.library_controller import LibraryController
from repository.rental_file_repository import RentalFileRepository
from repository.rental_repository import RentalRepository
from ui.console_ui import LibraryUI
from tests.tests import Tests
from tests.tests_unittest import *

def main():
    #tests = Tests()
    #tests.run_tests()
    test_book_repo = TestBookRepository()
    test_book_repo.run_tests()
    test_client_repo = TestClientRepository()
    test_client_repo.run_tests()
    test_library_controller = TestLibraryController()
    test_library_controller.run_tests()
    use_file_storage = True # Schimba intre True/False pt a comuta intre fisier si memorie
    if use_file_storage:
        book_repo = BookFileRepository("books.txt")
        client_repo = ClientFileRepository("clients.txt")
        rental_repo = RentalFileRepository("rentals.txt")
    else:
        book_repo = BookRepository()
        client_repo = ClientRepository()
        rental_repo = RentalRepository()
    controller = LibraryController(book_repo, client_repo, rental_repo)
    ui = LibraryUI(controller)
    ui.start()

if __name__ == "__main__":
    main()