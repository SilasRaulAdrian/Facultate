class BookRepository:
    def __init__(self):
        self._books = {}

    def add_book(self, book):
        """
        Adauga o carte in lista de carti
        :param book: Book
        :return: -
        """
        if book.book_id in self._books:
            raise ValueError(f"Cartea cu id-ul {book.book_id} exista deja!")
        self._books[book.book_id] = book

    def delete_book(self, book_id):
        """
        Sterge o carte din lista de carti
        :param book_id: int
        :return: -
        """
        if book_id not in self._books:
            raise ValueError(f"Cartea cu id-ul {book_id} nu exista!")
        del self._books[book_id]

    def update_book(self, book_id, new_title, new_description, new_author):
        """
        Modifica o carte din lista de carti
        :param book_id: int
        :param new_title: str
        :param new_description: str
        :param new_author: str
        :return: -
        """
        if book_id not in self._books:
            raise ValueError(f"Cartea cu id-ul {book_id} nu exista!")
        book = self._books[book_id]
        book.title = new_title
        book.description = new_description
        book.author = new_author

    def get_all_books(self):
        """
        Returneaza toate cartile din lista de carti
        :return: list<Book>
        """
        return list(self._books.values())

    def search_book_by_id(self, book_id):
        """
        Cauta o carte dupa id
        :param book_id: int
        :return: Book
        """
        if book_id not in self._books:
            raise ValueError(f"Cartea cu id-ul {book_id} nu exista!")
        return self._books[book_id]

    def search_books_by_title(self, title):
        """
        Cauta carti dupa titlu
        :param title: str
        :return: list<Book>
        """
        books = []
        for book in self._books.values():
            if title.lower() in book.title.lower():
                books.append(book)
        return books

    def search_books_by_author(self, author):
        """
        Cauta carti dupa autor
        :param author: str
        :return: list<Book>
        """
        books = []
        for book in self._books.values():
            if author.lower() in book.author.lower():
                books.append(book)
        return books