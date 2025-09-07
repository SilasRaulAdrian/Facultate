from domain.inscriere import Inscriere
from domain.persoane import Persoane
from domain.evenimente import Evenimente
from validator.persoane_valid import PersoaneValidator
from validator.evenimente_valid import EvenimenteValidator
from validator.inscriere_valid import InscriereValidator

class EvenimenteController:
    def __init__(self, persoane_repo, evenimente_repo, inscriere_repo):
        self.__persoane_repo = persoane_repo
        self.__evenimente_repo = evenimente_repo
        self.__inscriere_repo = inscriere_repo

    def add_eveniment(self, eventID, data, timp, descriere):
        """
        Adauga un eveniment in lista de evenimente
        :param eventID: int
        :param data: string
        :param timp: string
        :param descriere: string
        :return: -
        """
        eveniment = Evenimente(eventID, data, timp, descriere)
        if EvenimenteValidator().valideaza_eveniment(eveniment):
            self.__evenimente_repo.add_eveniment(eveniment)
        else:
            raise ValueError("Evenimentul nu este valid!")

    def delete_eveniment(self, eventID):
        """
        Sterge un eveniment din lista de evenimente
        :param eventID: int
        :return: -
        """
        self.__evenimente_repo.delete_eveniment(eventID)

    def update_eveniment(self, eventID, new_data, new_timp, new_descriere):
        """
        Modifica un eveniment din lista de evenimente
        :param eventID: int
        :param new_data: string
        :param new_timp: string
        :param new_descriere: string
        :return: -
        """
        eveniment = Evenimente(eventID, new_data, new_timp, new_descriere)
        if EvenimenteValidator().valideaza_eveniment(eveniment):
            self.__evenimente_repo.update_eveniment(eventID, new_data, new_timp, new_descriere)
        else:
            raise ValueError("Evenimentul nu este valid!")

    def list_evenimente(self):
        """
        Returneaza toate evenimentele din lista de evenimente
        :return: list<Evenimente>
        """
        return self.__evenimente_repo.get_all_evenimente()

    def add_persoana(self, personID, nume, adresa):
        """
        Adauga o persoana in lista de persoane
        :param personID: int
        :param nume: string
        :param adresa: string
        :return: -
        """
        persoana = Persoane(personID, nume, adresa)
        if PersoaneValidator().valideaza_persoana(persoana):
            self.__persoane_repo.add_persoana(persoana)
        else:
            raise ValueError("Persoana nu este valida!")

    def delete_persoana(self, personID):
        """
        Sterge o persoana din lista de persoane
        :param personID: int
        :return: -
        """
        self.__persoane_repo.delete_persoana(personID)

    def update_persoana(self, personID, new_nume, new_adresa):
        """
        Modifica o persoana din lista de persoane
        :param personID: int
        :param new_nume: string
        :param new_adresa: string
        :return: -
        """
        persoana = Persoane(personID, new_nume, new_adresa)
        if PersoaneValidator().valideaza_persoana(persoana):
            self.__persoane_repo.update_persoana(personID, new_nume, new_adresa)
        else:
            raise ValueError("Persoana nu este valida!")

    def list_persoane(self):
        """
        Returneaza toate persoanele din lista de persoane
        :return: list<Persoane>
        """
        return self.__persoane_repo.get_all_persoane()

    def search_eveniment_by_id(self, eventID):
        """
        Cauta un eveniment dupa id
        :param eventID: int
        :return: Evenimente
        """
        return self.__evenimente_repo.search_eveniment_by_id(eventID)

    def search_evenimente_by_data(self, data):
        """
        Cauta evenimente dupa data
        :param data: string
        :return: list<Evenimente>
        """
        return self.__evenimente_repo.search_evenimente_by_data(data)

    def search_evenimente_by_timp(self, timp):
        """
        Cauta evenimente dupa timp
        :param timp: string
        :return: list<Evenimente>
        """
        return self.__evenimente_repo.search_evenimente_by_timp(timp)

    def search_evenimente_by_descriere(self, descriere):
        """
        Cauta evenimente dupa descriere
        :param descriere: string
        :return: list<Evenimente>
        """
        return self.__evenimente_repo.search_evenimente_by_descriere(descriere)

    def search_persoana_by_id(self, personID):
        """
        Cauta o persoana dupa id
        :param personID: int
        :return: Persoane
        """
        return self.__persoane_repo.search_persoana_by_id(personID)

    def search_persoane_by_name(self, nume):
        """
        Cauta persoane dupa nume
        :param nume: string
        :return: list<Persoane>
        """
        return self.__persoane_repo.search_persoane_by_name(nume)

    def search_persoane_by_adresa(self, adresa):
        """
        Cauta persoane dupa adresa
        :param adresa: string
        :return: list<Persoane>
        """
        return self.__persoane_repo.search_persoane_by_adresa(adresa)

    def inscrie_persoana(self, inscriere_id, persoana_id, eveniment_id):
        """
        Inscrie o persoana la un eveniment
        :param inscriere_id: int
        :param persoana_id: int
        :param eveniment_id: int
        :return: -
        """
        persoana = self.__persoane_repo.search_persoana_by_id(persoana_id)
        if not persoana:
            raise ValueError(f"Persoana cu id-ul {persoana_id} nu exista!")

        eveniment = self.__evenimente_repo.search_eveniment_by_id(eveniment_id)
        if not eveniment:
            raise ValueError(f"Evenimentul cu id-ul {eveniment_id} nu exista!")

        #inscriere_existenta = self.__inscriere_repo.search_inscriere_by_person_id(persoana_id)
        #if inscriere_existenta:
        #   raise ValueError(f"Persoana cu id-ul {persoana_id} este deja inscrisa la un eveniment!")

        inscriere = Inscriere(inscriere_id, persoana_id, eveniment_id)
        if InscriereValidator().valideaza_inscriere(inscriere):
            self.__inscriere_repo.add_inscriere(inscriere)
        else:
            raise ValueError("Inscrierea nu este valida!")

    def get_all_inscrieri(self):
        """
        Returneaza toate inscrierile
        :return: list<Inscriere>
        """
        return self.__inscriere_repo.get_all_inscrieri()

    def evenimente_la_care_participa_o_persoana(self, persoana_id, sortBy="descriere"):
        """
        Returneaza evenimentele la care participa o persoana sortate dupa descriere sau data
        :param persoana_id: int
        :param sortBy: string
        :return: list<Evenimente>
        """
        inscrieri = self.__inscriere_repo.get_all_inscrieri()
        evenimente_participate = []

        for inscriere in inscrieri:
            if inscriere.get_id_persoana() == persoana_id:
                eveniment = self.__evenimente_repo.search_eveniment_by_id(inscriere.get_id_eveniment())
                evenimente_participate.append(eveniment)

        if sortBy == "descriere":
            evenimente_participate.sort(key=lambda x: x.get_descriere())
        elif sortBy == "data":
            evenimente_participate.sort(key=lambda x: x.get_data())
        else:
            raise ValueError("Sortarea se poate face doar dupa descriere sau data!")

        return evenimente_participate

    def persoane_participante_la_cele_mai_multe_evenimente(self):
        """
        Returneaza persoanele care participa la cele mai multe evenimente
        :return: list<Persoane>
        """
        inscrieri = self.__inscriere_repo.get_all_inscrieri()
        # persoane = self.__persoane_repo.get_all_persoane()

        persoane_participante = {}
        for inscriere in inscrieri:
            persoane_participante[int(inscriere.get_id_persoana())] = persoane_participante.get(int(inscriere.get_id_persoana()), 0) + 1

        sorted_persoane = sorted(persoane_participante.items(), key=lambda x: x[1], reverse=True)

        persoane_participante1 = []
        for personID, participations in sorted_persoane:
            persoana = self.__persoane_repo.search_persoana_by_id(int(personID))
            persoane_participante1.append((persoana, participations))

        return persoane_participante1

    def top_20_procent_evenimente_cu_cei_mai_multi_participanti(self):
        """
        Returneaza top 20% evenimente cu cei mai multi participanti
        :return: list<Evenimente>
        """
        inscrieri = self.__inscriere_repo.get_all_inscrieri()
        evenimente_participanti = {}

        for inscriere in inscrieri:
            evenimente_participanti[int(inscriere.get_id_eveniment())] = evenimente_participanti.get(int(inscriere.get_id_eveniment()), 0) + 1

        detalii_evenimente = []
        for eventID, participations in evenimente_participanti.items():
            eveniment = self.__evenimente_repo.search_eveniment_by_id(int(eventID))
            detalii_evenimente.append((eveniment, participations))

        detalii_evenimente.sort(key=lambda x: x[1], reverse=True)
        top_20_procent = max(1, int(len(detalii_evenimente) * 0.2))

        return detalii_evenimente[:top_20_procent]
