class Book:
    def __init__(self, book_id, title, description, author):
        self.book_id = book_id
        self.title = title
        self.description = description
        self.author = author

    def __str__(self):
        return f"Book(id={self.book_id}, title='{self.title}', description='{self.description}', author='{self.author}')"