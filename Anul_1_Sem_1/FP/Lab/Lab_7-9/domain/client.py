class Client:
    def __init__(self, client_id, name, cnp):
        self.client_id = client_id
        self.name = name
        self.cnp = cnp

    def __str__(self):
        return f"Client(id={self.client_id}, name='{self.name}', CNP='{self.cnp}')"