class Eveniment:
    def __init__(self, data, ora, descriere):
        """
        Constructorul clasei
        :param data: string
        :param ora: string
        :param descriere: string
        """
        self.__data = data
        self.__ora = ora
        self.__descriere = descriere

    def get_data(self):
        """
        Functie care returneaza data evenimentului
        :return: data evenimentului
        """
        return self.__data

    def get_ora(self):
        """
        Functie care returneaza ora evenimentului
        :return: ora evenimentului
        """
        return self.__ora

    def get_descriere(self):
        """
        Functie care returneaza descrierea evenimentului
        :return: descrierea evenimentului
        """
        return self.__descriere

    def set_data(self, data):
        """
        Functie care seteaza data evenimentului
        :param data: data evenimentului
        :return: None
        """
        self.__data = data

    def set_ora(self, ora):
        """
        Functie care seteaza ora evenimentului
        :param ora: ora evenimentului
        :return: None
        """
        self.__ora = ora

    def set_descriere(self, descriere):
        """
        Functie care seteaza descrierea evenimentului
        :param descriere: descrierea evenimentului
        :return: None
        """
        self.__descriere = descriere

    def __eq__(self, other):
        """
        Functie care suprascrie eq
        :param other: obiect de tip Eveniment
        :return: True/False in functie de egalitatea celor doua obiecte
        """
        if isinstance(other, Eveniment):
            return (self.__data == other.__data and
                    self.__ora == other.__ora and
                    self.__descriere == other.__descriere)
        return False

    def __str__(self):
        """
        Functie care suprascrie str
        :return: string
        """
        return f"Eveniment(data={self.__data}, ora={self.__ora}, descriere={self.__descriere})"