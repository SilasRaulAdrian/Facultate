import os
from domain.book import Book

class BookFileRepository:
    def __init__(self, file_path):
        self._file_path = file_path

    def _load_from_file(self):
        """
        Incarca cartile din fisier
        :return: list<Book>
        """
        if not os.path.exists(self._file_path):
            return []
        with open(self._file_path, 'r') as file:
            lines = file.readlines()
        books = []
        for line in lines:
            parts = line.strip().split(',')
            if len(parts) == 4:
                book = Book(int(parts[0]), parts[1], parts[2], parts[3])
                books.append(book)
        return books

    def _save_from_file(self, books):
        """
        Salveaza cartile in fisier
        :param books: list<Book>
        :return: -
        """
        with open(self._file_path, 'w') as file:
            for book in books:
                file.write(f"{book.book_id},{book.title},{book.description},{book.author}\n")

    def add_book(self, book):
        """
        Adauga o carte in lista de carti din fisier
        :param book: Book
        :return: -
        """
        books = self._load_from_file()

        def check_book_exists_recursiv(books, index=0):
            """
            Verifica daca o carte exista in lista de carti recursiv
            :param books: Books
            :param index: int
            :return: True daca este gasita, False altfel
            """
            if index == len(books):
                return False
            if books[index].book_id == book.book_id:
                return True
            return check_book_exists_recursiv(books, index + 1)

        if check_book_exists_recursiv(books):
            raise ValueError(f"Cartea cu id-ul {book.book_id} exista deja!")

        books.append(book)
        self._save_from_file(books)

    def delete_book(self, book_id):
        """
        Sterge o carte din lista de carti din fisier
        :param book_id: int
        :return: -
        """
        books = self._load_from_file()
        books = [b for b in books if b.book_id != book_id]
        self._save_from_file(books)

    def update_book(self, book_id, new_title, new_description, new_author):
        """
        Modifica o carte din lista de carti din fisier
        :param book_id: int
        :param new_title: string
        :param new_description: string
        :param new_author: string
        :return: -
        """
        books = self._load_from_file()
        for book in books:
            if book.book_id == book_id:
                book.title = new_title
                book.description = new_description
                book.author = new_author
        self._save_from_file(books)

    def get_all_books(self):
        """
        Returneaza toate cartile din lista de carti din fisier
        :return: list<Book>
        """
        return self._load_from_file()

    def search_book_by_id(self, book_id):
        """
        Cauta o carte dupa id in lista de carti din fisier
        :param book_id: int
        :return: Book
        """
        books = self._load_from_file()
        for book in books:
            if book.book_id == book_id:
                return book
        return None

    def search_books_by_title(self, title):
        """
        Cauta carti dupa titlu in lista de carti din fisier
        :param title: string
        :return: list<Book>
        """
        books = self._load_from_file()
        return [book for book in books if title.lower() in book.title.lower()]

    def search_books_by_author(self, author):
        """
        Cauta carti dupa autor in lista de carti din fisier
        :param author: string
        :return: list<Book>
        """
        books = self._load_from_file()
        return [book for book in books if author.lower() in book.author.lower()]