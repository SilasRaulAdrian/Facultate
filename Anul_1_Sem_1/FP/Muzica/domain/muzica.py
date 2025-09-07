class Muzica:
    def __init__(self, titlu, artist, gen, durata):
        """
        Constructorul clasei
        :param titlu: string
        :param artist: string
        :param gen: string
        :param durata: int
        """
        self.__titlu = titlu
        self.__artist = artist
        self.__gen = gen
        self.__durata = durata

    def get_titlu(self):
        """
        Functie care returneaza titlul muzicii
        :return: titlul muzicii
        """
        return self.__titlu

    def get_artist(self):
        """
        Functie care returneaza artistul muzicii
        :return: artistul muzicii
        """
        return self.__artist

    def get_gen(self):
        """
        Functie care returneaza genul muzicii
        :return: genul muzicii
        """
        return self.__gen

    def get_durata(self):
        """
        Functie care returneaza durata muzicii
        :return: durata muzicii
        """
        return self.__durata

    def set_titlu(self, titlu):
        """
        Functie care seteaza titlul muzicii
        :param titlu: string
        :return: None
        """
        self.__titlu = titlu

    def set_artist(self, artist):
        """
        Functie care seteaza artistul muzicii
        :param artist: string
        :return: None
        """
        self.__artist = artist

    def set_gen(self, gen):
        """
        Functie care seteaza genul muzicii
        :param gen: string
        :return: None
        """
        self.__gen = gen

    def set_durata(self, durata):
        """
        Functie care seteaza durata muzicii
        :param durata: int
        :return: None
        """
        self.__durata = durata

    def __eq__(self, other):
        """
        Functie care suprascrie eq
        :param other: obiect de tip Muzica
        :return: True/False in functie de egalitatea celor doua obiecte
        """
        if isinstance(other, Muzica):
            return (self.__titlu == other.__titlu and
                    self.__artist == other.__artist and
                    self.__gen == other.__gen and
                    self.__durata == other.__durata)
        return False

    def __str__(self):
        """
        Functie care suprascrie str
        :return: string
        """
        return f"Muzica(titlul={self.__titlu}, artist={self.__artist}, gen={self.__gen}, durata={self.__durata})"
