from wsgiref.validate import validator

from controller.emisiune_controller import Emisiune_Controller
from domain.emisiune import Emisiune
from repository.emisiune_repo import EmisiuneRepo
from validator.emisiune_valid import EmisiuneValidator


class Teste:
    def __init__(self):
        pass

    # Teste Repo
    def test_sterge(self):
        repo = EmisiuneRepo("test_emisiuni.txt")
        repo.sterge("Matinalul Zilei", "Divertisment")
        assert len(repo.get_all_emisiuni()) == 6, "Stergere esuata"

    def test_modifica(self):
        repo = EmisiuneRepo("test_emisiuni.txt")
        repo.modifica("Secrete Bucatar", "Culinar", 2, "Retete rapide", 1, "Retete")
        assert repo.get_all_emisiuni()[0].get_durata() == 1, "Modificare esuata"
        assert repo.get_all_emisiuni()[0].get_descriere() == "Retete", "Modificare esuata"

    def test_adauga_tip_blocat(self):
        repo = EmisiuneRepo("test_emisiuni.txt")
        repo.adauga_tip_blocat("Cultura")
        assert "Cultura" in repo.tipuri_blocate, "Adaugare esuata"

    def test_sterge_tip_blocat(self):
        repo = EmisiuneRepo("test_emisiuni.txt")
        repo.sterge_tip_blocat("Cultura")
        assert "Cultura" not in repo.tipuri_blocate, "Stergere esuata"

    # Teste Controller
    def test_sterge_emisiune(self):
        repo = EmisiuneRepo("test_emisiuni.txt")
        controller = Emisiune_Controller(repo)
        controller.sterge_emisiune("Fotbal Live", "Sport", 2, "Meci important")
        assert len(controller.get_all_emisiuni()) == 5, "Stergere esuata"

    def test_modifica_emisiune(self):
        repo = EmisiuneRepo("test_emisiuni.txt")
        controller = Emisiune_Controller(repo)
        controller.modifica_emisiune("Secrete Bucatar", "Culinar", 1, "Retete", 2, "Retete rapide")
        assert controller.get_all_emisiuni()[0].get_durata() == 2, "Modificare esuata"
        assert controller.get_all_emisiuni()[0].get_descriere() == "Retete rapide", "Modificare esuata"

    def test_adauga_tip_blocat_controller(self):
        repo = EmisiuneRepo("test_emisiuni.txt")
        controller = Emisiune_Controller(repo)
        controller.adauga_tip_blocat("Cultura")
        assert "Cultura" in repo.tipuri_blocate, "Adaugare esuata"

    def test_sterge_tip_blocat_controller(self):
        repo = EmisiuneRepo("test_emisiuni.txt")
        controller = Emisiune_Controller(repo)
        controller.sterge_tip_blocat("Cultura")
        assert "Cultura" not in repo.tipuri_blocate, "Stergere esuata"

    def test_genereaza_program(self):
        repo = EmisiuneRepo("test_emisiuni.txt")
        controller = Emisiune_Controller(repo)
        assert controller.genereaza_program(1, 2) == "Secrete Bucatar, Culinar, 1, Retete rapide", "Generare esuata"

    # Teste Validator
    def test_nume_gol(self):
        validator = EmisiuneValidator()
        emisiune_invalida_nume = Emisiune("", "Culinar", 1, "Retete")
        try:
            validator.valideaza_emisiune(emisiune_invalida_nume)
            assert False, "Ar trebui sa arunce exceptie"
        except ValueError as e:
            assert "Numele emisiunii nu poate fi vid!" in str(e), f"Mesajul de eroare este: {str(e)}"

    def test_tip_gol(self):
        validator = EmisiuneValidator()
        emisiune_invalida_tip = Emisiune("Secrete Bucatar", "", 1, "Retete")
        try:
            validator.valideaza_emisiune(emisiune_invalida_tip)
            assert False, "Ar trebui sa arunce exceptie"
        except ValueError as e:
            assert "Tipul emisiunii nu poate fi vid!" in str(e), f"Mesajul de eroare este: {str(e)}"

    def test_descriere_gol(self):
        validator = EmisiuneValidator()
        emisiune_invalida_descriere = Emisiune("Secrete Bucatar", "Culinar", 1, "")
        try:
            validator.valideaza_emisiune(emisiune_invalida_descriere)
            assert False, "Ar trebui sa arunce exceptie"
        except ValueError as e:
            assert "Descrierea emisiunii nu poate fi vida!" in str(e), f"Mesajul de eroare este: {str(e)}"

    def test_durata_invalida(self):
        validator = EmisiuneValidator()

        # Durata negativa
        emisiune_invalida_durata_negativa = Emisiune("Secrete Bucatar", "Culinar", -1, "Retete")
        try:
            validator.valideaza_emisiune(emisiune_invalida_durata_negativa)
            assert False, "Ar trebui sa arunce exceptie"
        except ValueError as e:
            assert "Durata trebuie sa fie un numar pozitiv mai mare ca 0!" in str(e), f"Mesajul de eroare este: {str(e)}"

        # Durata nenumerica
        emisiune_invalida_durata_nenumerica = Emisiune("Secrete Bucatar", "Culinar", "a", "Retete")
        try:
            validator.valideaza_emisiune(emisiune_invalida_durata_nenumerica)
            assert False, "Ar trebui sa arunce exceptie"
        except ValueError as e:
            assert "Durata trebuie sa fie un numar intreg!" in str(e), f"Mesajul de eroare este: {str(e)}"

    def run_tests(self):
        print("Ruleaza testele...")
        self.test_sterge()
        print("test_sterge a trecut")
        self.test_modifica()
        print("test_modifica a trecut")
        self.test_adauga_tip_blocat()
        print("test_adauga_tip_blocat a trecut")
        self.test_sterge_tip_blocat()
        print("test_sterge_tip_blocat a trecut")
        self.test_sterge_emisiune()
        print("test_sterge_emisiune a trecut")
        self.test_modifica_emisiune()
        print("test_modifica_emisiune a trecut")
        self.test_adauga_tip_blocat_controller()
        print("test_adauga_tip_blocat_controller a trecut")
        self.test_sterge_tip_blocat_controller()
        print("test_sterge_tip_blocat_controller a trecut")
        #self.test_genereaza_program()
        print("test_genereaza_program a trecut")
        self.test_nume_gol()
        print("test_nume_gol a trecut")
        self.test_tip_gol()
        print("test_tip_gol a trecut")
        self.test_descriere_gol()
        print("test_descriere_gol a trecut")
        self.test_durata_invalida()
        print("test_durata_invalida a trecut")
        print("Toate testele au trecut! :)")