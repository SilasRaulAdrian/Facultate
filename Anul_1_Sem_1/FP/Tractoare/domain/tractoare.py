class Tractor:
    def __init__(self, id, denumire, pret, model, data):
        """
        Constructorul clasei Tractor
        :param id: int
        :param denumire: string
        :param pret: int
        :param model: string
        :param data: datetime
        """
        self.__id = id
        self.__denumire = denumire
        self.__pret = pret
        self.__model = model
        self.__data = data

    def get_id(self):
        """
        Functie care returneaza id-ul tractorului
        :return: id-ul tractorului
        """
        return self.__id

    def get_denumire(self):
        """
        Functie care returneaza denumirea tractorului
        :return: denumirea tractorului
        """
        return self.__denumire

    def get_pret(self):
        """
        Functie care returneaza pretul tractorului
        :return: pretul tractorului
        """
        return self.__pret

    def get_model(self):
        """
        Functie care returneaza modelul tractorului
        :return: modelul tractorului
        """
        return self.__model

    def get_data(self):
        """
        Functie care returneaza data la care expira revizia tractorului
        :return: data
        """
        return self.__data

    def set_id(self, id):
        """
        Functie care seteaza id-ul tractorului
        :return: None
        """
        self.__id = id

    def set_denumire(self, denumire):
        """
        Functie care seteaza denumirea tractorului
        :return: None
        """
        self.__denumire = denumire

    def set_pret(self, pret):
        """
        Functie care seteaza pretul tractorului
        :return: None
        """
        self.__pret = pret

    def set_model(self, model):
        """
        Functie care seteaza modelul tractorului
        :return: None
        """
        self.__model = model

    def set_data(self, data):
        """
        Functie care seteaza data la care expira revizia tractorului
        :return: None
        """
        self.__data = data

    def __eq__(self, other):
        """
        Suprascrie functia eq
        :param other: un obiect de tip Tractor
        :return: True/False in functie de egalitatea celor doua obiecte
        """
        if isinstance(other, Tractor):
            return (self.__id == other.__id and
                    self.__denumire == other.__denumire and
                    self.__pret == other.__pret and
                    self.__model == other.__model and
                    self.__data == other.__data)
        return False

    def __str__(self):
        """
        Functie care suprascrie str
        :return: string
        """
        return f"Tractor(id={self.__id}, denumire={self.__denumire}, pret={self.__pret}, model={self.__model}, data={self.__data})"