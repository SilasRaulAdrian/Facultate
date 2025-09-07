from wsgiref.validate import validator

from controller.muzica_controller import MuzicaController
from domain.muzica import Muzica
from repository.muzica_repo import MuzicaRepo
from validator.muzica_valid import MuzicaValidator


class Teste:
    def __init__(self):
        pass

    def setUp(self):
        with open("test_muzica.txt", "w") as file:
            file.write("")

    # Teste Controller
    def test_adauga_muzica_ctrl(self):
        """
        Functie care testeaza aduagarea unei muzici
        :return: None
        """
        self.setUp()
        repo = MuzicaRepo("test_muzica.txt")
        ctrl = MuzicaController(repo)
        muzica = Muzica("Undeva-n Balkani", "Puya", "Altele", 210)
        ctrl.adauga_muzica(muzica)
        assert len(ctrl.get_all_muzici()) == 1, "Muzica nu a fost adaugata"
        assert ctrl.get_all_muzici()[0].get_titlu() == "Undeva-n Balkani", "Titlul muzicii nu este corect"
        assert ctrl.get_all_muzici()[0].get_artist() == "Puya", "Artistul muzicii nu este corect"
        assert ctrl.get_all_muzici()[0].get_gen() == "Altele", "Genul muzicii nu este corect"
        assert ctrl.get_all_muzici()[0].get_durata() == 210, "Durata muzicii nu este corecta"

    def test_modifica_muzica_ctrl(self):
        """
        Functie care testeaza modificarea unei muzici
        :return: None
        """
        self.setUp()
        repo = MuzicaRepo("test_muzica.txt")
        ctrl = MuzicaController(repo)
        muzica = Muzica("Undeva-n Balkani", "Puya", "Altele", 210)
        ctrl.adauga_muzica(muzica)
        assert len(ctrl.get_all_muzici()) == 1, "Muzica nu a fost adaugata"
        ctrl.modifica_muzica("Undeva-n Balkani", "Puya", "Altele", 210, "Pop", 209)
        assert ctrl.get_all_muzici()[0].get_titlu() == "Undeva-n Balkani", "Titlul muzicii nu este corect"
        assert ctrl.get_all_muzici()[0].get_artist() == "Puya", "Artistul muzicii nu este corect"
        assert ctrl.get_all_muzici()[0].get_gen() == "Pop", "Genul muzicii nu este corect"
        assert ctrl.get_all_muzici()[0].get_durata() == 209, "Durata muzicii nu este corecta"

    def test_sorteaza_muzici_ctrl(self):
        """
        Functie care testeaza sortarea muzicilor dupa artist si dupa titlu
        :return: None
        """
        self.setUp()
        repo = MuzicaRepo("test_muzica.txt")
        ctrl = MuzicaController(repo)
        muzica1 = Muzica("Undeva-n Balkani", "Puya", "Altele", 210)
        muzica2 = Muzica("Strazile", "B.U.G Mafia", "Altele", 222)
        ctrl.adauga_muzica(muzica1)
        ctrl.adauga_muzica(muzica2)
        assert len(ctrl.get_all_muzici()) == 2, "Muzicile nu au fost adaugate"
        muzici = ctrl.sorteaza_muzici()

    # Teste Repo
    def test_adauga_muzica(self):
        """
        Functie care testeaza aduagarea unei muzici
        :return: None
        """
        self.setUp()
        repo = MuzicaRepo("test_muzica.txt")
        muzica = Muzica("Undeva-n Balkani", "Puya", "Altele", 210)
        repo.adauga(muzica)
        assert len(repo.get_all_muzici()) == 1, "Muzica nu a fost adaugata"
        assert repo.get_all_muzici()[0].get_titlu() == "Undeva-n Balkani", "Titlul muzicii nu este corect"
        assert repo.get_all_muzici()[0].get_artist() == "Puya", "Artistul muzicii nu este corect"
        assert repo.get_all_muzici()[0].get_gen() == "Altele", "Genul muzicii nu este corect"
        assert repo.get_all_muzici()[0].get_durata() == 210, "Durata muzicii nu este corecta"

    def test_modifica_muzica(self):
        """
        Functie care testeaza modificarea unei muzici
        :return: None
        """
        self.setUp()
        repo = MuzicaRepo("test_muzica.txt")
        muzica = Muzica("Undeva-n Balkani", "Puya", "Altele", 210)
        repo.adauga(muzica)
        assert len(repo.get_all_muzici()) == 1, "Muzica nu a fost adaugata"
        repo.modifica("Undeva-n Balkani", "Puya", "Altele", 210, "Pop", 209)
        assert repo.get_all_muzici()[0].get_titlu() == "Undeva-n Balkani", "Titlul muzicii nu este corect"
        assert repo.get_all_muzici()[0].get_artist() == "Puya", "Artistul muzicii nu este corect"
        assert repo.get_all_muzici()[0].get_gen() == "Pop", "Genul muzicii nu este corect"
        assert repo.get_all_muzici()[0].get_durata() == 209, "Durata muzicii nu este corecta"

    def test_sorteaza_muzici(self):
        """
        Functie care testeaza sortarea muzicilor dupa artist si dupa titlu
        :return: None
        """
        self.setUp()
        muzici = []
        repo = MuzicaRepo("test_muzica.txt")
        muzica1 = Muzica("Undeva-n Balkani", "Puya", "Altele", 210)
        muzica2 = Muzica("Strazile", "B.U.G Mafia", "Altele", 222)
        repo.adauga(muzica1)
        repo.adauga(muzica2)
        assert len(repo.get_all_muzici()) == 2, "Muzicile nu au fost adaugate"
        muzici.append(muzica1)
        muzici.append(muzica2)
        repo.sorteaza_muzici(muzici)

    # Teste Validari
    def test_titlu_gol(self):
        """
        Functie care testeaza validarea titlului gol
        :return: None
        """
        self.setUp()
        validator = MuzicaValidator()

        muzica_invalida_titlu = Muzica("", "Puya", "Altele", 210)
        try:
            validator.valideaza_muzica(muzica_invalida_titlu)
            assert False, "Ar trebui sa fi primit eroare pentru titlul gol"
        except ValueError as e:
            assert "Titlul muzicii nu poate fi vid!" in str(e), f"Mesajul de eroare este: {e}"

    def test_artist_gol(self):
        """
        Functie care testeaza validarea artistului gol
        :return: None
        """
        self.setUp()
        validator = MuzicaValidator()

        muzica_invalida_artist = Muzica("Undeva-n Balkani", "", "Altele", 210)
        try:
            validator.valideaza_muzica(muzica_invalida_artist)
            assert False, "Ar trebui sa fi primit eroare pentru artistul gol"
        except ValueError as e:
            assert "Artistul muzicii nu poate fi vid!" in str(e), f"Mesajul de eroare este: {e}"

    def test_durata_incorecta(self):
        """
        Functie care testeaza validarea duratei incorecte (valoare negativa sau nenumerica)
        :return: None
        """
        self.setUp()
        validator = MuzicaValidator()

        # Durata negativa
        muzica_invalida_durata = Muzica("Undeva-n Balkani", "Puya", "Altele", -10)
        try:
            validator.valideaza_muzica(muzica_invalida_durata)
            assert False, "Ar trebui sa fi primit eroare pentru durata negativa"
        except ValueError as e:
            assert "Durata trebuie sa fie un numar pozitiv mai mare ca 0!" in str(e), f"Mesajul de eroare este: {e}"

        # Durata nenumerica
        muzica_invalida_durata = Muzica("Undeva-n Balkani", "Puya", "Altele", "abc")
        try:
            validator.valideaza_muzica(muzica_invalida_durata)
            assert False, "Ar trebui sa fi primit eroare pentru durata nenumerica"
        except ValueError as e:
            assert "Durata muzicii trebuie sa fie un numar intreg!" in str(e), f"Mesajul de eroare este: {e}"

    def test_gen_incorect(self):
        """
        Functie care testeaza validarea genului incorect (nu este "Rock", "Pop", "Jazz" sau "Altele")
        :return: None
        """
        self.setUp()
        validator = MuzicaValidator()

        # Durata negativa
        muzica_invalida_gen = Muzica("Undeva-n Balkani", "Puya", "Hip-Hop", 210)
        try:
            validator.valideaza_muzica(muzica_invalida_gen)
            assert False, "Ar trebui sa fi primit eroare pentru genul incorect"
        except ValueError as e:
            assert "Genul muzicii nu este valid!" in str(e), f"Mesajul de eroare este: {e}"

    def runTests(self):
        print("Ruleaza testele...")
        self.test_adauga_muzica_ctrl()
        print("test_adauga_muzica_ctrl a trecut")
        self.test_modifica_muzica_ctrl()
        print("test_modifica_muzica_ctrl a trecut")
        self.test_sorteaza_muzici_ctrl()
        print("test_sorteaza_muzici_ctrl a trecut")
        self.test_adauga_muzica()
        print("test_adauga_muzica a trecut")
        self.test_modifica_muzica()
        print("test_modifica_muzica a trecut")
        self.test_sorteaza_muzici()
        print("test_sorteaza_muzici a trecut")
        self.test_titlu_gol()
        print("test_titlu_gol a trecut")
        self.test_artist_gol()
        print("test_artist_gol a trecut")
        self.test_durata_incorecta()
        print("test_durata_incorecta a trecut")
        self.test_gen_incorect()
        print("test_gen_incorect a trecut")
        print("Toate testele au trecut! :)")
