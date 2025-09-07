from domain.persoane import Persoane

class PersoaneFileRepository:
    def __init__(self, file_path):
        self._file_path = file_path

    def _load_from_file(self):
        """
        Incarca persoanele din fisier
        """
        with open(self._file_path, 'r') as file:
            lines = file.readlines()
        persoane = []
        for line in lines:
            parts = line.strip().split(',')
            if len(parts) == 3:
                persoana = Persoane(int(parts[0]), parts[1], parts[2])
                persoane.append(persoana)
        return persoane

    def _save_from_file(self, persoane):
        """
        Salveaza persoanele in fisier
        """
        with open(self._file_path, 'w') as file:
            for persoana in persoane:
                file.write(f"{persoana.get_personID()},{persoana.get_nume()},{persoana.get_adresa()}\n")

    def add_persoana(self, persoana):
        """
        Adauga o persoana in lista de persoane din fisier
        :param persoana: Persoane
        :return: -
        """
        persoane = self._load_from_file()
        if any(int(p.get_personID()) == int(persoana.get_personID()) for p in persoane):
            raise ValueError(f"Persoana cu id-ul {int(persoana.get_personID())} exista deja!")
        persoane.append(persoana)
        self._save_from_file(persoane)

    def delete_persoana(self, personID):
        """
        Sterge o persoana din lista de persoane din fisier
        :param personID: int
        :return: -
        """
        persoane = self._load_from_file()
        persoane = [p for p in persoane if int(p.get_personID()) != int(personID)]
        self._save_from_file(persoane)

    def update_persoana(self, personID, new_nume, new_adresa):
        """
        Modifica o persoana din lista de persoane din fisier
        :param personID: int
        :param new_nume: string
        :param new_adresa: string
        :return: -
        """
        persoane = self._load_from_file()
        for persoana in persoane:
            if int(persoana.get_personID()) == int(personID):
                persoana.set_nume(new_nume)
                persoana.set_adresa(new_adresa)
        self._save_from_file(persoane)

    def get_all_persoane(self):
        """
        Returneaza toate persoanele din lista de persoane din fisier
        :return: list<Persoane>
        """
        return self._load_from_file()

    def search_persoana_by_id(self, personID):
        """
        Cauta o persoana dupa id in lista de persoane din fisier
        :param personID: int
        :return: Persoane
        """
        persoane = self._load_from_file()
        for persoana in persoane:
            if int(persoana.get_personID()) == int(personID):
                return persoana
        return None

    def search_persoane_by_name(self, nume):
        """
        Cauta persoane dupa nume in lista de persoane din fisier
        :param nume: str
        :return: list<Persoane>
        """
        persoane = self._load_from_file()
        return [p for p in persoane if p.get_nume().lower() == nume.lower()]

    def search_persoane_by_adresa(self, adresa):
        """
        Cauta persoane dupa adresa in lista de persoane din fisier
        :param adresa: str
        :return: list<Persoane>
        """
        persoane = self._load_from_file()
        return [p for p in persoane if p.get_adresa().lower() == adresa.lower()]