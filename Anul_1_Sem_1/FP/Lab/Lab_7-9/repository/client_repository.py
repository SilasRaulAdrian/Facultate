class ClientRepository:
    def __init__(self):
        self._clients = {}

    def add_client(self, client):
        """
        Adauga un client in lista de clienti
        :param client: Client
        :return: -
        """
        if client.client_id in self._clients:
            raise ValueError(f"Clientul cu id-ul {client.client_id} exista deja!")
        self._clients[client.client_id] = client

    def delete_client(self, client_id):
        """
        Sterge un client din lista de clienti
        :param client_id: int
        :return: -
        """
        if client_id not in self._clients:
            raise ValueError(f"Clientul cu id-ul {client_id} nu exista!")
        del self._clients[client_id]

    def update_client(self, client_id, new_name, new_cnp):
        """
        Modifica un client din lista de clienti
        :param client_id: int
        :param new_name: str
        :param new_cnp: str
        :return: -
        """
        if client_id not in self._clients:
            raise ValueError(f"Clientul cu id-ul {client_id} nu exista!")
        client = self._clients[client_id]
        client.name = new_name
        client.cnp = new_cnp

    def get_all_clients(self):
        """
        Returneaza toti clientii din lista de clienti
        :return: list<Client>
        """
        return list(self._clients.values())

    def search_client_by_id(self, client_id):
        """
        Cauta un client dupa id
        :param client_id: int
        :return: Client
        """
        if client_id not in self._clients:
            raise ValueError(f"Clientul cu id-ul {client_id} nu exista!")
        return self._clients[client_id]

    def search_clients_by_name(self, name):
        """
        Cauta client dupa nume
        :param name: str
        :return: list<Client>
        """
        results = []
        for client in self._clients.values():
            if name.lower() in client.name.lower():
                results.append(client)
        return results

    def search_clients_by_cnp(self, cnp):
        """
        Cauta client dupa CNP
        :param cnp: str
        :return: list<Client>
        """
        results = []
        for client in self._clients.values():
            if cnp.lower() == client.cnp.lower():
                results.append(client)
        return results