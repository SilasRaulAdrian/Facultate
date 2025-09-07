from datetime import date, datetime
from domain.tractoare import Tractor
from validator.tractor_valid import TractorValidator


class TractoareController:
    def __init__(self, tractor_repo):
        """
        Constructorul clasei
        :param tractor_repo: obiect de tip TractorRepository
        """
        self.__tractor_repo = tractor_repo

    def adauga_tractor(self, id, denumire, pret, model, data):
        """
        Functie care adauga un tractor
        :param id: int
        :param denumire: string
        :param pret: int
        :param model: string
        :param data: datetime
        :return: None
        """
        tractor = Tractor(id, denumire, pret, model, data)
        if TractorValidator().valideaza_tractor(tractor):
            self.__tractor_repo.adauga(tractor)
        else:
            raise ValueError("Tractorul nu este valid!")

    def sterge_tractor(self, cifra):
        """
        Functie care sterge tractoarele pentru care pretul contine cifra data
        :param cifra: int
        :return: None
        """
        if cifra < 0 or cifra > 9:
            raise ValueError("Cifra trebuie sa fie intre 0 si 9!")

        tractoare_initiale = self.__tractor_repo.get_all_tractoare()
        self.__tractor_repo.sterge(cifra)
        tractoare_actualizate = self.__tractor_repo.get_all_tractoare()

        numar_sterse = len(tractoare_initiale) - len(tractoare_actualizate)
        return numar_sterse

    def get_all_tractoare(self):
        """
        Functie care returneaza toate tractoarele
        :return: List<Tractor>
        """
        return self.__tractor_repo.get_all_tractoare()

    def filtreaza_tractoare(self, text, numar):
        """
        Functie care filtreaza tractoarele
        :param text: string
        :param numar: int
        :return: List<Tractor>
        """
        lista_filtrata = []

        for tractor in self.get_all_tractoare():
            adauga_asterisc = False

            data = tractor.get_data()
            data_revizie = datetime.strptime(data, "%d/%m/%Y").date()
            if data_revizie < datetime.today().date():
                adauga_asterisc = True

            if text == "":
                if int(tractor.get_pret()) < numar:
                    if adauga_asterisc:
                        lista_filtrata.append(f"Tractor(id={tractor.get_id()}, denumire=*{tractor.get_denumire()}, pret={tractor.get_pret()}, model={tractor.get_model()}, data={tractor.get_data()})")
                    else:
                        lista_filtrata.append(tractor)
            elif text == "" and numar == -1:
                if adauga_asterisc:
                    lista_filtrata.append(f"Tractor(id={tractor.get_id()}, denumire=*{tractor.get_denumire()}, pret={tractor.get_pret()}, model={tractor.get_model()}, data={tractor.get_data()})")
                else:
                    lista_filtrata.append(tractor)
            elif numar == -1:
                if tractor.get_denumire().find(text) != -1:
                    if adauga_asterisc:
                        lista_filtrata.append(f"Tractor(id={tractor.get_id()}, denumire=*{tractor.get_denumire()}, pret={tractor.get_pret()}, model={tractor.get_model()}, data={tractor.get_data()})")
                    else:
                        lista_filtrata.append(tractor)
            else:
                if tractor.get_denumire().find(text) != -1 and int(tractor.get_pret()) < numar:
                    if adauga_asterisc:
                        lista_filtrata.append(f"Tractor(id={tractor.get_id()}, denumire=*{tractor.get_denumire()}, pret={tractor.get_pret()}, model={tractor.get_model()}, data={tractor.get_data()})")
                    else:
                        lista_filtrata.append(tractor)

        return lista_filtrata

    def undo(self):
        """
        Functie care anuleaza ultima modificare
        :return: None
        """
        if self.__tractor_repo.undo():
            return "Ultima modificare a fost anulata cu succes!"
        else:
            return "Nu exista modificari anterioare pentru undo!"