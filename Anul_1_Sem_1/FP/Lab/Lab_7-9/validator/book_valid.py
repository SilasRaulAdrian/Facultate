class BookValidator:
    def __init__(self):
        pass

    def validate_book(self, book):
        """
        Valideaza o carte
        :param book: Book
        :return: True daca cartea este valida, altfel arunca exceptii
        """
        if book.book_id <= 0:
            raise ValueError("Id-ul cartii trebuie sa fie un numar pozitiv!")
        elif not isinstance(book.book_id, int):
            raise TypeError("Id-ul cartii trebuie sa fie un numar intreg!")
        elif book.title == "":
            raise ValueError("Titlul cartii nu poate fi gol!")
        elif not isinstance(book.title, str):
            raise TypeError("Titlul cartii trebuie sa fie un sir de caractere!")
        elif book.description == "":
            raise ValueError("Descrierea cartii nu poate fi goala!")
        elif not isinstance(book.description, str):
            raise TypeError("Descrierea cartii trebuie sa fie un sir de caractere!")
        elif book.author == "":
            raise ValueError("Autorul cartii nu poate fi gol!")
        elif not isinstance(book.author, str):
            raise TypeError("Autorul cartii trebuie sa fie un sir de caractere!")
        else:
            return True