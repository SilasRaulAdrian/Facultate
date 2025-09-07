class Baschet:
    def __init__(self, nume, prenume, inaltime, post):
        """
        Constructorul clasei
        :param nume: string
        :param prenume: string
        :param inaltime: int
        :param post: string
        """
        self.__nume = nume
        self.__prenume = prenume
        self.__inaltime = inaltime
        self.__post = post

    def get_nume(self):
        """
        Functie care returneaza numele jucatorului
        :return: string
        """
        return self.__nume

    def get_prenume(self):
        """
        Functie care returneaza prenumele jucatorului
        :return: string
        """
        return self.__prenume

    def get_inaltime(self):
        """
        Functie care returneaza inaltimea jucatorului
        :return: int
        """
        return self.__inaltime

    def get_post(self):
        """
        Functie care returneaza postul jucatorului
        :return: string
        """
        return self.__post

    def set_nume(self, nume):
        """
        Functie care seteaza numele jucatorului
        :param nume: string
        :return: None
        """
        self.__nume = nume

    def set_prenume(self, prenume):
        """
        Functie care seteaza prenumele jucatorului
        :param prenume: string
        :return: None
        """
        self.__prenume = prenume

    def set_inaltime(self, inaltime):
        """
        Functie care seteaza inaltimea jucatorului
        :param inaltime: int
        :return: None
        """
        self.__inaltime = inaltime

    def set_post(self, post):
        """
        Functie care seteaza postul jucatorului
        :param post: string
        :return: None
        """
        self.__post = post

    def __eq__(self, other):
        """
        Functie care suprascrie eq
        :param other: obiect de tipul Baschet
        :return: True/False in functie de egalitatea celor doua obiecte
        """
        if isinstance(other, Baschet):
            return (self.__nume == other.__nume and
                        self.__prenume == other.__prenume and
                        self.__inaltime == other.__inaltime and
                        self.__post == other.__post)
        return False

    def __str__(self):
        """
        Functie care suprascrie str
        :return: string
        """
        return f"Jucator(nume={self.__nume}, prenume={self.__prenume}, inaltime={self.__inaltime}, post={self.__post})"