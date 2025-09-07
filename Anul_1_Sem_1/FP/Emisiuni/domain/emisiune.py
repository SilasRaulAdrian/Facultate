class Emisiune:
    def __init__(self, nume, tip, durata, descriere):
        """
        Constructorul clasei
        :param nume: string
        :param tip: string
        :param durata: int
        :param descriere: string
        """
        self.__nume = nume
        self.__tip = tip
        self.__durata = durata
        self.__descriere = descriere

    def get_nume(self):
        """
        Functie care returneaza numele emisiunii
        :return: numele emisiunii
        """
        return self.__nume

    def get_tip(self):
        """
        Functie care returneaza tipul emisiunii
        :return: tipul emisiunii
        """
        return self.__tip

    def get_durata(self):
        """
        Functie care returneaza durata emisiunii
        :return: durata emisiunii
        """
        return self.__durata

    def get_descriere(self):
        """
        Functie care returneaza descrierea emisiunii
        :return: descrierea emisiunii
        """
        return self.__descriere

    def set_nume(self, nume):
        """
        Functie care seteaza numele emisiunii
        :param nume: string
        :return: None
        """
        self.__nume = nume

    def set_tip(self, tip):
        """
        Functie care seteaza tipul emisiunii
        :param tip: string
        :return: None
        """
        self.__tip = tip

    def set_durata(self, durata):
        """
        Functie care seteaza durata emisiunii
        :param durata: int
        :return: None
        """
        self.__durata = durata

    def set_descriere(self, descriere):
        """
        Functie care seteaza descrierea emisiunii
        :param descriere: string
        :return: None
        """
        self.__descriere = descriere

    def __eq__(self, other):
        """
        Functie care suprascrie eq
        :param other: obiect de tip Emisiune
        :return: True/False in functie de egalitatea celor doua obbiecte
        """
        if isinstance(other, Emisiune):
            return (self.__nume == other.__nume and
                    self.__tip == other.__tip and
                    self.__durata == other.__durata and
                    self.__descriere == other.__descriere)
        return False

    def __hash__(self):
        return hash((self.__nume, self.__tip, self.__durata, self.__descriere))

    def __str__(self):
        """
        Functie care suprascrie str
        :return: string
        """
        return f"Emisiune(nume={self.__nume}, tip={self.__tip}, durata={self.__durata}, descriere={self.__descriere})"
