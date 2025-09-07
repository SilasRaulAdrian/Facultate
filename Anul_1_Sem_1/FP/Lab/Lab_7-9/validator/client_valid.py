class ClientValidator:
    def __init__(self):
        pass

    def validate_client(self, client):
        """
        Valideaza un client
        :param client: Client
        :return: True daca clientul este valid, altfel arunca exceptii
        """
        if client.client_id <= 0:
            raise ValueError("Id-ul clientului trebuie sa fie un numar pozitiv!")
        elif not isinstance(client.client_id, int):
            raise TypeError("Id-ul clientului trebuie sa fie un numar intreg!")
        elif client.name == "":
            raise ValueError("Numele clientului nu poate fi gol!")
        elif not isinstance(client.name, str):
            raise TypeError("Numele clientului trebuie sa fie un sir de caractere!")
        elif client.cnp == "":
            raise ValueError("CNP-ul clientului nu poate fi gol!")
        elif not isinstance(client.cnp, str):
            raise TypeError("CNP-ul clientului trebuie sa fie un sir de caractere!")
        else:
            return True