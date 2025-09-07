class RentalValidator:
    def __init__(self):
        pass

    def validate_rental(self, rental):
        """
        Valideaza o inchiriere
        :param rental: Rental
        :return: True daca inchirierea este valida, altfel arunca exceptii
        """
        if rental.rental_id <= 0:
            raise ValueError("Id-ul inchirierii trebuie sa fie un numar pozitiv!")
        elif not isinstance(rental.rental_id, int):
            raise TypeError("Id-ul inchirierii trebuie sa fie un numar intreg!")
        elif rental.book_id <= 0:
            raise ValueError("Id-ul cartii trebuie sa fie un numar pozitiv!")
        elif not isinstance(rental.book_id, int):
            raise TypeError("Id-ul cartii trebuie sa fie un numar intreg!")
        elif rental.client_id <= 0:
            raise ValueError("Id-ul clientului trebuie sa fie un numar pozitiv!")
        elif not isinstance(rental.client_id, int):
            raise TypeError("Id-ul clientului trebuie sa fie un numar intreg!")
        else:
            return True