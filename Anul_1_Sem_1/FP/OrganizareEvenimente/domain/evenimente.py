class Evenimente:
    def __init__(self, eventID, data, timp, descriere):
        self.__eventID = eventID
        self.__data = data
        self.__timp = timp
        self.__descriere = descriere

    def get_eventID(self):
        return self.__eventID

    def get_data(self):
        return self.__data

    def get_timp(self):
        return self.__timp

    def get_descriere(self):
        return self.__descriere

    def set_eventID(self, eventID):
        self.__eventID = eventID

    def set_data(self, data):
        self.__data = data

    def set_timp(self, timp):
        self.__timp = timp

    def set_descriere(self, descriere):
        self.__descriere = descriere

    def __eq__(self, other):
        if isinstance(other, Evenimente):
            return (self.__eventID == other.__eventID and
                    self.__data == other.__data and
                    self.__timp == other.__timp and
                    self.__descriere == other.__descriere)
        return False

    def __str__(self):
        return f"Eveniment(id={self.__eventID}, data='{self.__data}', timp='{self.__timp}', descriere='{self.__descriere}')"