from itertools import combinations
from wsgiref.validate import validator

from controller.baschet_controller import BaschetController
from domain.baschet import Baschet
from repository.baschet_repo import BaschetRepo
from validator.baschet_valid import BaschetValidator


class Teste:
    def __init__(self):
        pass

    def setUp(self):
        with open("test_baschet.txt", "w") as file:
            file.write("")

    # Teste Repo
    def test_adauga_jucator(self):
        self.setUp()
        repo = BaschetRepo("test_baschet.txt")
        jucator = Baschet("Dragusin", "Radu", 192, "Fundas")
        repo.adauga(jucator)
        assert len(repo.get_all_jucatori()) == 1, "Jucatorul nu a fost adaugat"
        assert repo.get_all_jucatori()[0].get_nume() == "Dragusin", "Numele jucatorului nu este corect"
        assert repo.get_all_jucatori()[0].get_prenume() == "Radu", "Prenumele jucatorului nu este corect"
        assert repo.get_all_jucatori()[0].get_inaltime() == 192, "Inaltimea jucatorului nu este corecta"
        assert repo.get_all_jucatori()[0].get_post() == "Fundas", "Postul jucatorului nu este corect"

    def test_modifica_jucator(self):
        self.setUp()
        repo = BaschetRepo("test_baschet.txt")
        jucator = Baschet("Dragusin", "Radu", 192, "Fundas")
        repo.adauga(jucator)
        assert len(repo.get_all_jucatori()) == 1, "Jucatorul nu a fost adaugat"
        repo.modifica("Dragusin", "Radu", 192, 190)
        assert repo.get_all_jucatori()[0].get_nume() == "Dragusin", "Numele jucatorului nu este corect"
        assert repo.get_all_jucatori()[0].get_prenume() == "Radu", "Prenumele jucatorului nu este corect"
        assert repo.get_all_jucatori()[0].get_inaltime() == 190, "Inaltimea jucatorului nu este corecta"
        assert repo.get_all_jucatori()[0].get_post() == "Fundas", "Postul jucatorului nu este corect"

    # Teste Controller
    def test_adauga_jucator_ctrl(self):
        self.setUp()
        repo = BaschetRepo("test_baschet.txt")
        ctrl = BaschetController(repo)
        ctrl.adauga_jucator("Dragusin", "Radu", 192, "Fundas")
        assert len(ctrl.get_all_jucatori()) == 1, "Jucatorul nu a fost adaugat"
        assert ctrl.get_all_jucatori()[0].get_nume() == "Dragusin", "Numele jucatorului nu este corect"
        assert ctrl.get_all_jucatori()[0].get_prenume() == "Radu", "Prenumele jucatorului nu este corect"
        assert ctrl.get_all_jucatori()[0].get_inaltime() == 192, "Inaltimea jucatorului nu este corecta"
        assert ctrl.get_all_jucatori()[0].get_post() == "Fundas", "Postul jucatorului nu este corect"

    def test_modifica_jucator_ctrl(self):
        self.setUp()
        repo = BaschetRepo("test_baschet.txt")
        ctrl = BaschetController(repo)
        ctrl.adauga_jucator("Dragusin", "Radu", 192, "Fundas")
        assert len(ctrl.get_all_jucatori()) == 1, "Jucatorul nu a fost adaugat"
        ctrl.modifica_jucator("Dragusin", "Radu", 192, "Fundas", 190)
        assert ctrl.get_all_jucatori()[0].get_nume() == "Dragusin", "Numele jucatorului nu este corect"
        assert ctrl.get_all_jucatori()[0].get_prenume() == "Radu", "Prenumele jucatorului nu este corect"
        assert ctrl.get_all_jucatori()[0].get_inaltime() == 190, "Inaltimea jucatorului nu este corecta"
        assert ctrl.get_all_jucatori()[0].get_post() == "Fundas", "Postul jucatorului nu este corect"

    def test_creeaza_echipa_ctrl(self):
        self.setUp()
        repo = BaschetRepo("test_baschet.txt")
        ctrl = BaschetController(repo)

        # Adaugam jucatori pentru a putea forma o echipa valida
        ctrl.adauga_jucator("Popescu", "Andrei", 198, "Pivot")
        ctrl.adauga_jucator("Ionescu", "Mihai", 185, "Fundas")
        ctrl.adauga_jucator("Dumitrescu", "Radu", 192, "Extrema")
        ctrl.adauga_jucator("Stanescu", "Alexandru", 200, "Pivot")
        ctrl.adauga_jucator("Georgescu", "Cristian", 188, "Fundas")
        ctrl.adauga_jucator("Marinescu", "Stefan", 195, "Extrema")

        # Testam daca echipa se creeaza corect
        echipa = ctrl.creeaza_echipa()

        assert len(echipa) == 5, "Echipa trebuie sa aiba exact 5 jucatori"
        assert any(j.get_post() == "Pivot" for j in echipa), "Echipa trebuie sa aiba un Pivot"
        assert sum(1 for j in echipa if j.get_post() == "Fundas") == 2, "Echipa trebuie sa aiba 2 Fundasi"
        assert sum(1 for j in echipa if j.get_post() == "Extrema") == 2, "Echipa trebuie sa aiba 2 Extreme"

        # Verificam daca media de inaltime este maxima
        media_calculata = sum(j.get_inaltime() for j in echipa) / 5
        toate_echipele = [
            list(f) + list(e) + list(p)
            for f in combinations([j for j in repo.get_all_jucatori() if j.get_post() == "Fundas"], 2)
            for e in combinations([j for j in repo.get_all_jucatori() if j.get_post() == "Extrema"], 2)
            for p in combinations([j for j in repo.get_all_jucatori() if j.get_post() == "Pivot"], 1)
        ]
        max_media = max(sum(j.get_inaltime() for j in echipa) / 5 for echipa in toate_echipele)

        assert media_calculata == max_media, "Echipa nu are media maxima de inaltime"

    # Teste Validator
    def test_nume_gol(self):
        self.setUp()
        validator = BaschetValidator()

        jucator_invalid_nume = Baschet("", "Radu", 192, "Fundas")
        try:
            validator.valideaza_jucator(jucator_invalid_nume)
            assert False, "Ar trebui sa fi primit o eroare pentru numele vid"
        except ValueError as e:
            assert "Numele jucatorului nu poate fi vid!" in str(e), f"Mesajul de eroare este: {e}"

    def test_prenume_gol(self):
        self.setUp()
        validator = BaschetValidator()

        jucator_invalid_prenume = Baschet("Dragusin", "", 192, "Fundas")
        try:
            validator.valideaza_jucator(jucator_invalid_prenume)
            assert False, "Ar trebui sa fi primit o eroare pentru prenumele vid"
        except ValueError as e:
            assert "Prenumele jucatorului nu poate fi vid!" in str(e), f"Mesajul de eroare este: {e}"

    def test_inaltime_invalida(self):
        self.setUp()
        validator = BaschetValidator()

        # Inaltime negativa
        jucator_invalid_inaltime = Baschet("Dragusin", "Radu", -31, "Fundas")
        try:
            validator.valideaza_jucator(jucator_invalid_inaltime)
            assert False, "Ar trebui sa fi primit eroare pentru inaltime negativa"
        except ValueError as e:
            assert "Inaltimea jucatorului trebuie sa fie un numar pozitiv mai mare ca 0!" in str(e), f"Mesajul de eroare este: {e}"

            # Inaltime nenumerica
            jucator_invalid_inaltime = Baschet("Dragusin", "Radu", "abc", "Fundas")
            try:
                validator.valideaza_jucator(jucator_invalid_inaltime)
                assert False, "Ar trebui sa fi primit eroare pentru inaltime nenumerica"
            except ValueError as e:
                assert "Inaltimea trebuie sa fie un numar intreg!" in str(e), f"Mesajul de eroare este: {e}"

    def test_post_invalid(self):
        self.setUp()
        validator = BaschetValidator()

        jucator_invalid_post = Baschet("Dragusin", "Radu", 192, "Atacant")
        try:
            validator.valideaza_jucator(jucator_invalid_post)
            assert False, "Ar trebui sa fi primit eroare pentru postul invalid"
        except ValueError as e:
            assert "Postul jucatorului nu este valid!" in str(e), f"Mesajul de eroare este: {e}"

    def runTests(self):
        print("Ruleaza testele...")
        self.test_adauga_jucator()
        print("test_adauga_jucator a trecut")
        self.test_modifica_jucator()
        print("test_modifica_jucator a trecut")
        self.test_adauga_jucator_ctrl()
        print("test_adauga_jucator_ctrl a trecut")
        self.test_modifica_jucator_ctrl()
        print("test_modifica_jucator_ctrl a trecut")
        self.test_creeaza_echipa_ctrl()
        print("test_creeaza_echipa_ctrl a trecut")
        self.test_nume_gol()
        print("test_nume_gol a trecut")
        self.test_prenume_gol()
        print("test_prenume_gol a trecut")
        self.test_inaltime_invalida()
        print("test_inaltime_invalida a trecut")
        self.test_post_invalid()
        print("test_post_invalid a trecut")
        print("Toate testele au trecut! :)")
