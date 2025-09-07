from domain.emisiune import Emisiune


class EmisiuneRepo:
    def __init__(self, _file_path):
        """
        Constructorul clasei
        :param _file_path: calea fisierului
        """
        self._file_path = _file_path
        self.tipuri_blocate = []

    def _load_from_file(self):
        """
        Functie care incarca emisiunile din fisier
        :return: List<Emisiune>
        """
        with open(self._file_path, "r") as file:
            lines = file.readlines()
        emisiuni = []
        for line in lines:
            parts = line.strip().split(",")
            if len(parts) == 4:
                emisiune = Emisiune(parts[0], parts[1], int(parts[2]), parts[3])
                emisiuni.append(emisiune)
        return emisiuni

    def _save_from_file(self, emisiuni):
        """
        Functie care salveaza emisiunile in fisier
        :param emisiuni: obiect de tip Emisiune
        :return: None
        """
        with open(self._file_path, "w") as file:
            for emisiune in emisiuni:
                file.write(f"{emisiune.get_nume()},{emisiune.get_tip()},{emisiune.get_durata()},{emisiune.get_descriere()}\n")

    def get_all_emisiuni(self):
        """
        Functie care returneaza toate emisiunile
        :return: List<Emisiune>
        """
        return self._load_from_file()

    def sterge(self, nume, tip):
        """
        Functie care sterge o emisiune cu numele si tipul specificat
        :param nume: string
        :param tip: string
        :return: None
        """
        if tip in self.tipuri_blocate:
            raise ValueError("Emisiune blocata")

        emisiuni = self._load_from_file()
        emisiuni_actualizate = [emisiune for emisiune in emisiuni if emisiune.get_nume() != nume and emisiune.get_tip() != tip]
        if len(emisiuni) == len(emisiuni_actualizate):
            raise ValueError("Nu exista emisiunea introdusa")
        self._save_from_file(emisiuni_actualizate)

    def modifica(self, nume, tip, durata, descriere, durata_noua, descriere_noua):
        """
        Functie care modifica durata si descrierea unei emisiuni
        :param nume: numele emisiunii
        :param tip: tipul emisiunii
        :param durata: durata emisiunii
        :param descriere: descrierea emisiunii
        :param durata_noua: noua durata a emisiunii
        :param descriere_noua: descrierea noua a emisiunii
        :return: None
        """
        if tip in self.tipuri_blocate:
            raise ValueError("Emisiune blocata")

        emisiuni = self._load_from_file()
        found = False

        for emisiune in emisiuni:
            if (emisiune.get_nume() == nume and
                    emisiune.get_tip() == tip and
                    emisiune.get_durata() == durata and
                    emisiune.get_descriere() == descriere):
                emisiune.set_durata(durata_noua)
                emisiune.set_descriere(descriere_noua)
                found = True
                break

        if not found:
            raise ValueError("Nu exista emisiunea introdusa")

        self._save_from_file(emisiuni)

    def adauga_tip_blocat(self, tip):
        """
        Functie care blocheaza tipul emisiunii
        :param tip: string
        :return: None
        """
        if tip not in self.tipuri_blocate:
            self.tipuri_blocate.append(tip)
        else:
            print(f"Tipul {tip} este deja blocat")

    def sterge_tip_blocat(self, tip):
        """
        Functie care elimina blocarea tipului emisiunii
        :param tip: string
        :return: None
        """
        if tip in self.tipuri_blocate:
            self.tipuri_blocate.remove(tip)
        else:
            print(f"Tipul {tip} nu se afla in lista blocata")