from datetime import datetime

from controller.tractoare_controller import TractoareController
from domain.tractoare import Tractor
from repository.tractoare_repo import TractorRepo
from validator.tractor_valid import TractorValidator


class Teste:
    def __init__(self):
        pass

    def setUp(self):
        with open("tractoare_test.txt", "w") as file:
            file.write("")

    # Teste Repo
    def test_adauga_tractor(self):
        """
        Functie care testeaza adaugarea unui tractor
        :return: None
        """
        self.setUp()
        repo = TractorRepo("tractoare_test.txt")
        data = datetime.strptime("18/01/2015", "%d/%m/%Y")
        data = data.strftime("%d/%m/%Y")
        tractor = Tractor(1, "Dacia", 5000, "Logan", data)
        repo.adauga(tractor)
        assert len(repo.get_all_tractoare()) == 1, "Tractorul nu a fost adaugat"
        assert repo.get_all_tractoare()[0].get_id() == 1, "Id-ul tractorului nu este corect"
        assert repo.get_all_tractoare()[0].get_denumire() == "Dacia", "Denumirea tractorului nu este corecta"
        assert repo.get_all_tractoare()[0].get_pret() == 5000, "Pretul tractorului nu este corect"
        assert repo.get_all_tractoare()[0].get_model() == "Logan", "Modelul tractorului nu este corect"
        assert repo.get_all_tractoare()[0].get_data() == data, "Data la care expira revizia tractorului nu este corecta"

    def test_sterge_tractor(self):
        """
        Functie care testeaza stergerea unui tractor
        :return: None
        """
        self.setUp()
        repo = TractorRepo("tractoare_test.txt")
        data = datetime.strptime("18/01/2015", "%d/%m/%Y")
        data = data.strftime("%d/%m/%Y")
        tractor = Tractor(1, "Dacia", 5000, "Logan", data)
        repo.adauga(tractor)
        assert len(repo.get_all_tractoare()) == 1, "Tractorul nu a fost adaugat"
        repo.sterge(5)
        assert len(repo.get_all_tractoare()) == 0, "Tractorul nu a fost sters"

    def test_get_all_tractoare(self):
        """
        Functie care testeaza afisarea tuturor tractoarelor
        :return: None
        """
        self.setUp()
        repo = TractorRepo("tractoare_test.txt")
        data = datetime.strptime("18/01/2015", "%d/%m/%Y")
        data = data.strftime("%d/%m/%Y")
        tractor1 = Tractor(1, "Dacia", 5000, "Logan", data)
        tractor2 = Tractor(2, "Ford", 7500, "Focus", data)
        repo.adauga(tractor1)
        repo.adauga(tractor2)
        assert len(repo.get_all_tractoare()) == 2, "Tractoarele nu au fost adaugate"
        tractoare = repo.get_all_tractoare()
        assert tractoare[0].get_id() == 1, "Id-ul tractorului nu este corect"
        assert tractoare[0].get_denumire() == "Dacia", "Denumirea tractorului nu este corecta"
        assert tractoare[0].get_pret() == 5000, "Pretul tractorului nu este corect"
        assert tractoare[0].get_model() == "Logan", "Modelul tractorului nu este corect"
        assert tractoare[0].get_data() == data, "Data la care expira revizia tractorului nu este corecta"
        assert tractoare[1].get_id() == 2, "Id-ul tractorului nu este corect"
        assert tractoare[1].get_denumire() == "Ford", "Denumirea tractorului nu este corecta"
        assert tractoare[1].get_pret() == 7500, "Pretul tractorului nu este corect"
        assert tractoare[1].get_model() == "Focus", "Modelul tractorului nu este corect"
        assert tractoare[1].get_data() == data, "Data la care expira revizia tractorului nu este corecta"

    def test_undo_adauga(self):
        """
        Functie care testeaza undo pentru adaugare
        :return: None
        """
        self.setUp()
        repo = TractorRepo("tractoare_test.txt")
        data = datetime.strptime("18/01/2015", "%d/%m/%Y")
        data = data.strftime("%d/%m/%Y")
        tractor = Tractor(1, "Dacia", 5000, "Logan", data)
        repo.adauga(tractor)
        assert len(repo.get_all_tractoare()) == 1, "Tractorul nu a fost adaugat"
        repo.undo()
        assert len(repo.get_all_tractoare()) == 0, "Undo nu a functionat"

    def test_undo_sterge(self):
        """
        Functie care testeaza undo pentru adaugare
        :return: None
        """
        self.setUp()
        repo = TractorRepo("tractoare_test.txt")
        data = datetime.strptime("18/01/2015", "%d/%m/%Y")
        data = data.strftime("%d/%m/%Y")
        tractor = Tractor(1, "Dacia", 5000, "Logan", data)
        repo.adauga(tractor)
        assert len(repo.get_all_tractoare()) == 1, "Tractorul nu a fost adaugat"
        repo.sterge(5)
        assert len(repo.get_all_tractoare()) == 0, "Tractorul nu a fost sters"
        repo.undo()
        assert len(repo.get_all_tractoare()) == 1, "Undo nu a functionat"

    # Teste Controller
    def test_adauga_ctrl(self):
        """
        Functie care testeaza adaugarea unui tractor
        :return: None
        """
        self.setUp()
        repo = TractorRepo("tractoare_test.txt")
        ctrl = TractoareController(repo)
        data = datetime.strptime("18/01/2015", "%d/%m/%Y")
        data = data.strftime("%d/%m/%Y")
        ctrl.adauga_tractor(1, "Dacia", 5000, "Logan", data)
        assert len(ctrl.get_all_tractoare()) == 1, "Tractorul nu a fost adaugat"
        assert ctrl.get_all_tractoare()[0].get_id() == 1, "Id-ul tractorului nu este corect"
        assert ctrl.get_all_tractoare()[0].get_denumire() == "Dacia", "Denumirea tractorului nu este corecta"
        assert ctrl.get_all_tractoare()[0].get_pret() == 5000, "Pretul tractorului nu este corect"
        assert ctrl.get_all_tractoare()[0].get_model() == "Logan", "Modelul tractorului nu este corect"
        assert ctrl.get_all_tractoare()[0].get_data() == data, "Data la care expira revizia tractorului nu este corecta"

    def test_sterge_ctrl(self):
        """
        Functie care testeaza stergerea unui tractor
        :return: None
        """
        self.setUp()
        repo = TractorRepo("tractoare_test.txt")
        ctrl = TractoareController(repo)
        data = datetime.strptime("18/01/2015", "%d/%m/%Y")
        data = data.strftime("%d/%m/%Y")
        ctrl.adauga_tractor(1, "Dacia", 5000, "Logan", data)
        assert len(ctrl.get_all_tractoare()) == 1, "Tractorul nu a fost adaugat"
        cnt = ctrl.sterge_tractor(5)
        assert cnt == 1, "Tractorul nu a fost sters"

    def test_get_all_tractoare_ctrl(self):
        """
        Functie care testeaza afisarea tuturor tractoarelor
        :return: None
        """
        self.setUp()
        repo = TractorRepo("tractoare_test.txt")
        ctrl = TractoareController(repo)
        data = datetime.strptime("18/01/2015", "%d/%m/%Y")
        data = data.strftime("%d/%m/%Y")
        ctrl.adauga_tractor(1, "Dacia", 5000, "Logan", data)
        ctrl.adauga_tractor(2, "Ford", 7500, "Focus", data)
        assert len(ctrl.get_all_tractoare()) == 2, "Tractoarele nu au fost adaugate"
        tractoare = ctrl.get_all_tractoare()
        assert tractoare[0].get_id() == 1, "Id-ul tractorului nu este corect"
        assert tractoare[0].get_denumire() == "Dacia", "Denumirea tractorului nu este corecta"
        assert tractoare[0].get_pret() == 5000, "Pretul tractorului nu este corect"
        assert tractoare[0].get_model() == "Logan", "Modelul tractorului nu este corect"
        assert tractoare[0].get_data() == data, "Data la care expira revizia tractorului nu este corecta"
        assert tractoare[1].get_id() == 2, "Id-ul tractorului nu este corect"
        assert tractoare[1].get_denumire() == "Ford", "Denumirea tractorului nu este corecta"
        assert tractoare[1].get_pret() == 7500, "Pretul tractorului nu este corect"
        assert tractoare[1].get_model() == "Focus", "Modelul tractorului nu este corect"
        assert tractoare[1].get_data() == data, "Data la care expira revizia tractorului nu este corecta"

    def test_filtreaza_tractoare(self):
        """
        Functie care testeaza filtrarea tractoarelor
        :return: None
        """
        self.setUp()
        repo = TractorRepo("tractoare_test.txt")
        ctrl = TractoareController(repo)
        data = datetime.strptime("18/01/2015", "%d/%m/%Y")
        data = data.strftime("%d/%m/%Y")
        ctrl.adauga_tractor(1, "Dacia", 7500, "Logan", data)
        ctrl.adauga_tractor(2, "Ford", 19000, "Focus", data)
        assert len(ctrl.get_all_tractoare()) == 2, "Tractoarele nu au fost adaugate"
        tractoare_filtrate1 = ctrl.filtreaza_tractoare("ac", 8000)
        assert len(tractoare_filtrate1) == 1, "Tractoarele nu au fost filtrate"
        tractoare_filtrate2 = ctrl.filtreaza_tractoare("", 20000)
        assert len(tractoare_filtrate2) == 2, "Tractoarele nu au fost filtrate"
        tractoare_filtrate3 = ctrl.filtreaza_tractoare("ac", -1)
        assert len(tractoare_filtrate3) == 1, "Tractoarele nu au fost filtrate"

    def test_id_incorect(self):
        """
        Testeaza validarea unui ID incorect (valoare negativa sau nenumerica)
        :return: None
        """
        self.setUp()
        validator = TractorValidator()

        # ID negativ
        tractor_invalid_id = Tractor(-1, "Dacia", 5000, "Logan", "18/01/2025")
        try:
            validator.valideaza_tractor(tractor_invalid_id)
            assert False, "Ar trebui sa fi primit o eroare pentru ID-ul negativ"
        except ValueError as e:
            assert "ID-ul trebuie sa fie un numar pozitiv mai mare ca 0!" in str(e), f"Mesajul de eroare este: {e}"

        # ID nenumeric
        tractor_invalid_id = Tractor("abc", "Dacia", 5000, "Logan", "18/01/2025")
        try:
            validator.valideaza_tractor(tractor_invalid_id)
            assert False, "Ar trebui sa fi primit o eroare pentru ID-ul nenumeric"
        except ValueError as e:
            assert "ID-ul tractorului trebuie sa fie un numar intreg!" in str(e), f"Mesajul de eroare este: {e}"

    def test_denumire_goala(self):
        """
        Testeaza validarea denumirii goale
        :return: None
        """
        self.setUp()
        validator = TractorValidator()

        tractor_invalid_denumire = Tractor(1, "", 5000, "Logan", "18/01/2025")
        try:
            validator.valideaza_tractor(tractor_invalid_denumire)
            assert False, "Ar trebui sa fi primit o eroare pentru denumirea goala"
        except ValueError as e:
            assert "Denumirea tractorului nu poate fi vida!" in str(e), f"Mesajul de eroare este: {e}"

    def test_pret_incorect(self):
        """
        Testeaza validarea pretului incorect (valoare negativa sau nenumerica)
        :return: None
        """
        self.setUp()
        validator = TractorValidator()

        # Pret negativ
        tractor_invalid_pret = Tractor(1, "Dacia", -5000, "Logan", "18/01/2025")
        try:
            validator.valideaza_tractor(tractor_invalid_pret)
            assert False, "Ar trebui sa fi primit o eroare pentru pretul negativ"
        except ValueError as e:
            assert "Pretul trebuie sa fie un numar pozitiv mai mare ca 0!" in str(e), f"Mesajul de eroare este: {e}"

        # Pret nenumeric
        tractor_invalid_pret = Tractor(1, "Dacia", "abc", "Logan", "18/01/2025")
        try:
            validator.valideaza_tractor(tractor_invalid_pret)
            assert False, "Ar trebui sa fi primit o eroare pentru pretul nenumeric"
        except ValueError as e:
            assert "Pretul tractorului trebuie sa fie un numar intreg!" in str(e), f"Mesajul de eroare este: {e}"

    def test_model_goala(self):
        """
        Testeaza validarea modelului gol
        :return: None
        """
        self.setUp()
        validator = TractorValidator()

        tractor_invalid_model = Tractor(1, "Dacia", 5000, "", "18/01/2025")
        try:
            validator.valideaza_tractor(tractor_invalid_model)
            assert False, "Ar trebui sa fi primit o eroare pentru modelul gol"
        except ValueError as e:
            assert "Modelul tractorului nu poate fi vid!" in str(e), f"Mesajul de eroare este: {e}"

    def test_data_incorecta(self):
        """
        Testeaza validarea unei date incorecte
        :return: None
        """
        self.setUp()
        validator = TractorValidator()

        # Data invalida (format greșit)
        tractor_invalid_data = Tractor(1, "Dacia", 5000, "Logan", "18-01-2025")
        try:
            validator.valideaza_tractor(tractor_invalid_data)
            assert False, "Ar trebui sa fi primit o eroare pentru data invalida"
        except ValueError as e:
            assert "Data introdusa trebuie sa fie o data valida de forma dd/mm/yyyy" in str(
                e), f"Mesajul de eroare este: {e}"

    def test_tractor_valid(self):
        """
        Testeaza validarea unui tractor valid
        :return: None
        """
        self.setUp()
        validator = TractorValidator()

        tractor_valid = Tractor(1, "Dacia", 5000, "Logan", "18/01/2025")
        assert validator.valideaza_tractor(tractor_valid), "Tractorul ar trebui sa fie valid"

    def runTests(self):
        print("Ruleaza testele...")
        self.test_adauga_tractor()
        print("test_adauga_tractor a trecut")
        self.test_sterge_tractor()
        print("test_sterge_tractor a trecut")
        self.test_get_all_tractoare()
        print("test_get_all_tractoare a trecut")
        self.test_undo_adauga()
        print("test_undo_adauga a trecut")
        self.test_undo_sterge()
        print("test_undo_sterge a trecut")
        self.test_adauga_ctrl()
        print("test_adauga_ctrl a trecut")
        self.test_sterge_ctrl()
        print("test_sterge_ctrl a trecut")
        self.test_get_all_tractoare_ctrl()
        print("test_get_all_tractoare_ctrl a trecut")
        self.test_filtreaza_tractoare()
        print("test_filtreaza_tractoare a trecut")
        self.test_id_incorect()
        print("test_id_incorect a trecut")
        self.test_denumire_goala()
        print("test_denumire_goala a trecut")
        self.test_pret_incorect()
        print("test_pret_incorect a trecut")
        self.test_model_goala()
        print("test_model_goala a trecut")
        self.test_data_incorecta()
        print("test_data_incorecta a trecut")
        self.test_tractor_valid()
        print("test_tractor_valid a trecut")
        print("Toate testele au trecut! :)")