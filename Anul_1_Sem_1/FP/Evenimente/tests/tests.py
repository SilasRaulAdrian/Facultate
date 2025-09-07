from wsgiref.validate import validator

from controller.evenimente_controller import EvenimenteController
from domain.evenimente import Eveniment
from repository.evenimente_repo import EvenimenteRepo
from validator.evenimente_valid import EvenimenteValidator


class Teste:
    def __init__(self):
        pass

    def setUp(self):
        with open("test_evenimente.txt", "w") as file:
            file.write("")

    # Teste Repo
    def test_adauga_eveniment(self):
        self.setUp()
        repo = EvenimenteRepo("test_evenimente.txt")
        eveniment = Eveniment("28.01.2025", "09:00", "Wake up")
        repo.adauga(eveniment)
        assert len(repo.get_all_evenimente()) == 1, "Evenimentul nu a fost adaugat"
        assert repo.get_all_evenimente()[0].get_data() == "28.01.2025", "Data evenimentului nu este corecta"
        assert repo.get_all_evenimente()[0].get_ora() == "09:00", "Ora evenimentului nu este corecta"
        assert repo.get_all_evenimente()[0].get_descriere() == "Wake up", "Descrierea evenimentului nu este corecta"

    def test_sorteaza_eveniment(self):
        self.setUp()
        repo = EvenimenteRepo("test_evenimente.txt")
        eveniment1 = Eveniment("28.01.2025", "12:00", "Eating")
        eveniment2 = Eveniment("28.01.2025", "09:00", "Wake up")
        repo.adauga(eveniment1)
        repo.adauga(eveniment2)
        assert len(repo.get_all_evenimente()) == 2, "Evenimentele nu au fost adaugate"
        evenimente = repo.sorteaza()
        assert evenimente[0].get_data() == "28.01.2025", "Data evenimentului nu este corecta"
        assert evenimente[0].get_ora() == "09:00", "Ora evenimentului nu este corecta"
        assert evenimente[0].get_descriere() == "Wake up", "Descrierea evenimentului nu este corecta"
        assert evenimente[1].get_data() == "28.01.2025", "Data evenimentului nu este corecta"
        assert evenimente[1].get_ora() == "12:00", "Ora evenimentului nu este corecta"
        assert evenimente[1].get_descriere() == "Eating", "Descrierea evenimentului nu este corecta"

    def test_sorteaza_eveniment_data(self):
        self.setUp()
        repo = EvenimenteRepo("test_evenimente.txt")
        eveniment1 = Eveniment("25.01.2025", "12:00", "Eating")
        eveniment2 = Eveniment("25.01.2025", "09:00", "Wake up")
        repo.adauga(eveniment1)
        repo.adauga(eveniment2)
        assert len(repo.get_all_evenimente()) == 2, "Evenimentele nu au fost adaugate"
        evenimente = repo.sorteaza_data("25.01.2025")
        assert evenimente[0].get_data() == "25.01.2025", "Data evenimentului nu este corecta"
        assert evenimente[0].get_ora() == "09:00", "Ora evenimentului nu este corecta"
        assert evenimente[0].get_descriere() == "Wake up", "Descrierea evenimentului nu este corecta"
        assert evenimente[1].get_data() == "25.01.2025", "Data evenimentului nu este corecta"
        assert evenimente[1].get_ora() == "12:00", "Ora evenimentului nu este corecta"
        assert evenimente[1].get_descriere() == "Eating", "Descrierea evenimentului nu este corecta"

    def test_filtreaza_si_sorteaza(self):
        self.setUp()
        repo = EvenimenteRepo("test_evenimente.txt")
        eveniment1 = Eveniment("18.01.2025", "12:00", "Learning")
        eveniment2 = Eveniment("18.01.2025", "09:00", "Eating")
        repo.adauga(eveniment1)
        repo.adauga(eveniment2)
        assert len(repo.get_all_evenimente()) == 2, "Evenimentele nu au fost adaugate"
        evenimente = repo.filtreaza_si_sorteaza("ea")
        assert evenimente[0].get_data() == "18.01.2025", "Data evenimentului nu este corecta"
        assert evenimente[0].get_ora() == "09:00", "Ora evenimentului nu este corecta"
        assert evenimente[0].get_descriere() == "Eating", "Descrierea evenimentului nu este corecta"
        assert evenimente[1].get_data() == "18.01.2025", "Data evenimentului nu este corecta"
        assert evenimente[1].get_ora() == "12:00", "Ora evenimentului nu este corecta"
        assert evenimente[1].get_descriere() == "Learning", "Descrierea evenimentului nu este corecta"

    # Teste Controller
    def test_adauga_eveniment_ctrl(self):
        self.setUp()
        repo = EvenimenteRepo("test_evenimente.txt")
        ctrl = EvenimenteController(repo)
        ctrl.adauga_eveniment("28.01.2025", "09:00", "Wake up")
        assert len(ctrl.get_all_evenimente()) == 1, "Evenimentul nu a fost adaugat"
        assert ctrl.get_all_evenimente()[0].get_data() == "28.01.2025", "Data evenimentului nu este corecta"
        assert ctrl.get_all_evenimente()[0].get_ora() == "09:00", "Ora evenimentului nu este corecta"
        assert ctrl.get_all_evenimente()[0].get_descriere() == "Wake up", "Descrierea evenimentului nu este corecta"

    def test_sorteaza_eveniment_ctrl(self):
        self.setUp()
        repo = EvenimenteRepo("test_evenimente.txt")
        ctrl = EvenimenteController(repo)
        ctrl.adauga_eveniment("28.01.2025", "12:00", "Eating")
        ctrl.adauga_eveniment("28.01.2025", "09:00", "Wake up")
        assert len(ctrl.get_all_evenimente()) == 2, "Evenimentele nu au fost adaugate"
        evenimente = ctrl.sorteaza_evenimente()
        assert evenimente[0].get_data() == "28.01.2025", "Data evenimentului nu este corecta"
        assert evenimente[0].get_ora() == "09:00", "Ora evenimentului nu este corecta"
        assert evenimente[0].get_descriere() == "Wake up", "Descrierea evenimentului nu este corecta"
        assert evenimente[1].get_data() == "28.01.2025", "Data evenimentului nu este corecta"
        assert evenimente[1].get_ora() == "12:00", "Ora evenimentului nu este corecta"
        assert evenimente[1].get_descriere() == "Eating", "Descrierea evenimentului nu este corecta"

    def test_sorteaza_eveniment_data_ctrl(self):
        self.setUp()
        repo = EvenimenteRepo("test_evenimente.txt")
        ctrl = EvenimenteController(repo)
        ctrl.adauga_eveniment("25.01.2025", "12:00", "Eating")
        ctrl.adauga_eveniment("25.01.2025", "09:00", "Wake up")
        assert len(ctrl.get_all_evenimente()) == 2, "Evenimentele nu au fost adaugate"
        evenimente = ctrl.sorteaza_evenimente_data("25.01.2025")
        assert evenimente[0].get_data() == "25.01.2025", "Data evenimentului nu este corecta"
        assert evenimente[0].get_ora() == "09:00", "Ora evenimentului nu este corecta"
        assert evenimente[0].get_descriere() == "Wake up", "Descrierea evenimentului nu este corecta"
        assert evenimente[1].get_data() == "25.01.2025", "Data evenimentului nu este corecta"
        assert evenimente[1].get_ora() == "12:00", "Ora evenimentului nu este corecta"
        assert evenimente[1].get_descriere() == "Eating", "Descrierea evenimentului nu este corecta"

    # Teste Validator
    def test_descriere_goala(self):
        self.setUp()
        validator = EvenimenteValidator()

        eveniment_invalid_descriere = Eveniment("25.01.2025", "09:00", "")
        try:
            validator.valideaza_eveniment(eveniment_invalid_descriere)
            assert False, "Ar trebui sa fi primit eroare pentru descrierea goala"
        except ValueError as e:
            assert "Descrierea evenimentului nu trebuie sa fie vida!" in str(e), f"Mesajul de eroare este {e}"

    def test_data_incorecta(self):
        self.setUp()
        validator = EvenimenteValidator()

        eveniment_invalid_data = Eveniment("18-01-2025", "09:00", "Wake up")
        try:
            validator.valideaza_eveniment(eveniment_invalid_data)
            assert False, "Ar trebui sa fi primit o eroare pentru data invalida"
        except ValueError as e:
            assert "Data introdusa trebuie sa fie o data valida de forma dd.mm.yyyy" in str(e), f"Mesajul de eroare este: {e}"

    def test_ora_incorecta(self):
        self.setUp()
        validator = EvenimenteValidator()

        eveniment_invalid_ora = Eveniment("18.01.2025", "09.00", "Wake up")
        try:
            validator.valideaza_eveniment(eveniment_invalid_ora)
            assert False, "Ar trebui sa fi primit o eroare pentru ora invalida"
        except ValueError as e:
            assert "Ora introdusa trebuie sa fie o ora valida de forma hh:mm" in str(e), f"Mesajul de eroare este: {e}"

    def runTests(self):
        print("Ruleaza testele...")
        self.test_adauga_eveniment()
        print("test_adauga_eveniment a trecut")
        self.test_sorteaza_eveniment()
        print("test_sorteaza_eveniment a trecut")
        self.test_sorteaza_eveniment_data()
        print("test_sorteaza_eveniment_data a trecut")
        self.test_filtreaza_si_sorteaza()
        print("test_filtreaza_si_sorteaza a trecut")
        self.test_adauga_eveniment_ctrl()
        print("test_adauga_eveniment_ctrl a trecut")
        self.test_sorteaza_eveniment_ctrl()
        print("test_sorteaza_eveniment_ctrl a trecut")
        self.test_sorteaza_eveniment_data_ctrl()
        print("test_sorteaza_eveniment_data_ctrl a trecut")
        self.test_descriere_goala()
        print("test_descriere_goala a trecut")
        self.test_data_incorecta()
        print("test_data_incorecta a trecut")
        self.test_ora_incorecta()
        print("test_ora_incorecta a trecut")
        print("Toate testele au trecut! :)")