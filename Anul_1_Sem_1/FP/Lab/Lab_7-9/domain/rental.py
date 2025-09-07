class Rental:
    def __init__(self, rental_id, book_id, client_id):
        self.rental_id = rental_id
        self.book_id = book_id
        self.client_id = client_id

    def __str__(self):
        return f"Rental(id={self.rental_id}, book_id={self.book_id}, client_id={self.client_id})"