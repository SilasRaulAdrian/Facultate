class RentalRepository:
    def __init__(self):
        self._rentals = {}

    def add_rental(self, rental):
        """
        Adauga o inchiriere in lista de inchirieri
        :param rental: Rental
        :return: -
        """
        if rental.rental_id in self._rentals:
            raise ValueError(f"Inchirierea cu id-ul {rental.rental_id} exista deja!")
        self._rentals[rental.rental_id] = rental

    def update_rental(self, rental_id, new_book_id, new_client_id):
        """
        Modifica o inchiriere din lista de inchirieri
        :param rental_id: int
        :param new_book_id: int
        :param new_client_id: int
        :return: -
        """
        if rental_id not in self._rentals:
            raise ValueError(f"Inchirierea cu id-ul {rental_id} nu exista!")
        rental = self._rentals[rental_id]
        rental.book_id = new_book_id
        rental.client_id = new_client_id

    def delete_rental(self, rental_id):
        """
        Sterge o inchiriere din lista de inchirieri
        :param rental_id: int
        :return: -
        """
        if rental_id not in self._rentals:
            raise ValueError(f"Inchirierea cu id-ul {rental_id} nu exista!")
        del self._rentals[rental_id]

    def get_all_rentals(self):
        """
        Returneaza toate inchirierile din lista de inchirieri
        :return: list<Rental>
        """
        return list(self._rentals.values())

    def find_rental_by_book_id(self, book_id):
        """
        Cauta o inchiriere dupa id
        :param book_id: int
        :return: Rental
        """
        for rental in self._rentals.values():
            if rental.book_id == book_id:
                return rental
        return None