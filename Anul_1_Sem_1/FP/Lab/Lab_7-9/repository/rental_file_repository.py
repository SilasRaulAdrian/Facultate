import os
from domain.rental import Rental

class RentalFileRepository:
    def __init__(self, file_path):
        self._file_path = file_path

    def _load_from_file(self):
        """
        Incarca inchirierile din fisier
        :return: list<Rental>
        """
        if not os.path.exists(self._file_path):
            return []
        with open(self._file_path, 'r') as file:
            lines = file.readlines()
        rentals = []
        for line in lines:
            parts = line.strip().split(',')
            if len(parts) == 3:
                rental = Rental(int(parts[0]), int(parts[1]), int(parts[2]))
                rentals.append(rental)
        return rentals

    def _save_from_file(self, rentals):
        """
        Salveaza inchirierile in fisier
        :param rentals: list<Rental>
        :return: -
        """
        with open(self._file_path, 'w') as file:
            for rental in rentals:
                file.write(f"{rental.rental_id},{rental.book_id},{rental.client_id}\n")

    def add_rental(self, rental):
        """
        Adauga o inchiriere in lista de inchirieri din fisier
        :param rental: Rental
        :return: -
        """
        rentals = self._load_from_file()
        if any(r.rental_id == rental.rental_id for r in rentals):
            raise ValueError(f"Inchirierea cu id-ul {rental.rental_id} exista deja!")
        rentals.append(rental)
        self._save_from_file(rentals)

    def delete_rental(self, rental_id):
        """
        Sterge o inchiriere din lista de inchirieri din fisier
        :param rental_id: int
        :return: -
        """
        rentals = self._load_from_file()
        rentals = [r for r in rentals if r.rental_id != rental_id]
        self._save_from_file(rentals)

    def update_rental(self, rental_id, new_book_id, new_client_id):
        """
        Modifica o inchiriere din lista de inchirieri din fisier
        :param rental_id: int
        :param new_book_id: int
        :param new_client_id: int
        :return: -
        """
        rentals = self._load_from_file()
        for rental in rentals:
            if rental.rental_id == rental_id:
                rental.book_id = new_book_id
                rental.client_id = new_client_id
        self._save_from_file(rentals)

    def get_all_rentals(self):
        """
        Returneaza toate inchirierile din lista de inchirieri din fisier
        :return: list<Rental>
        """
        return self._load_from_file()

    def find_rental_by_book_id(self, book_id):
        """
        Cauta o inchiriere dupa id in lista de inchirieri din fisier
        :param book_id: int
        :return: Rental
        """
        rentals = self._load_from_file()
        for rental in rentals:
            if rental.book_id == book_id:
                return rental
        return None