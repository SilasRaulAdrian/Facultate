import os
from domain.client import Client

class ClientFileRepository:
    def __init__(self, file_path):
        self._file_path = file_path

    def _load_from_file(self):
        """
        Incarca clientii din fisier
        :return: list<Client>
        """
        if not os.path.exists(self._file_path):
            return []
        with open(self._file_path, 'r') as file:
            lines = file.readlines()
        clients = []
        for line in lines:
            parts = line.strip().split(',')
            if len(parts) == 3:
                client = Client(int(parts[0]), parts[1], parts[2])
                clients.append(client)
        return clients

    def _save_from_file(self, clients):
        """
        Salveaza clientii in fisier
        :param clients: list<Client>
        :return: -
        """
        with open(self._file_path, 'w') as file:
            for client in clients:
                file.write(f"{client.client_id},{client.name},{client.cnp}\n")

    def add_client(self, client):
        """
        Adauga un client in lista de clienti din fisier
        :param client: Client
        :return: -
        """
        clients = self._load_from_file()
        if any(c.client_id == client.client_id for c in clients):
            raise ValueError(f"Clientul cu id-ul {client.client_id} exista deja!")
        clients.append(client)
        self._save_from_file(clients)

    def delete_client(self, client_id):
        """
        Sterge un client din lista de clienti din fisier
        :param client_id: int
        :return: -
        """
        clients = self._load_from_file()
        clients = [c for c in clients if c.client_id != client_id]
        self._save_from_file(clients)

    def update_client(self, client_id, new_name, new_cnp):
        """
        Modifica un client din lista de clienti din fisier
        :param client_id: int
        :param new_name: string
        :param new_cnp: string
        :return: -
        """
        clients = self._load_from_file()

        def update_client_recursiv(clients, index=0):
            """
            Modifica un client din lista de clienti din fisier recursiv
            :param clients: Clients
            :param index: int
            :return: True daca a fost modificat, False altfel
            """
            if index == len(clients):
                return False
            if clients[index].client_id == client_id:
                clients[index].name = new_name
                clients[index].cnp = new_cnp
                return True
            return update_client_recursiv(clients, index + 1)

        if not update_client_recursiv(clients):
            raise ValueError(f"Clientul cu id-ul {client_id} nu exista!")

        self._save_from_file(clients)

    def get_all_clients(self):
        """
        Returneaza toti clientii din lista de clienti din fisier
        :return: list<Client>
        """
        return self._load_from_file()

    def search_client_by_id(self, client_id):
        """
        Cauta un client dupa id in lista de clienti din fisier
        :param client_id: int
        :return: Client
        """
        clients = self._load_from_file()
        for client in clients:
            if client.client_id == client_id:
                return client
        return None

    def search_clients_by_name(self, name):
        """
        Cauta client dupa nume in lista de clienti din fisier
        :param name: string
        :return: list<Client>
        """
        clients = self._load_from_file()
        return [client for client in clients if name.lower() in client.name.lower()]

    def search_clients_by_cnp(self, cnp):
        """
        Cauta client dupa cnp in lista de clienti din fisier
        :param cnp: string
        :return: list<Client>
        """
        clients = self._load_from_file()
        return [client for client in clients if cnp in client.cnp]