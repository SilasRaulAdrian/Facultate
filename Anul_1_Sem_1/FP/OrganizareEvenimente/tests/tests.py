import unittest
from domain.persoane import Persoane
from domain.evenimente import Evenimente
from repository.inscriere_file_repo import InscriereFileRepository
from repository.persoane_file_repo import PersoaneFileRepository
from repository.evenimente_file_repo import EvenimenteFileRepository
from controller.evenimente_controller import EvenimenteController
from validator.inscriere_valid import InscriereValidator
from validator.persoane_valid import PersoaneValidator
from validator.evenimente_valid import EvenimenteValidator

class TestPersoaneRepository(unittest.TestCase):
    def setUp(self):
        with open("test_persoane.txt", "w") as file:
            file.write("")

    def test_add_persoana(self):
        self.setUp()
        persoane_repo = PersoaneFileRepository("test_persoane.txt")
        persoana = Persoane(1, "Ion", "Cluj")
        persoane_repo.add_persoana(persoana)
        self.assertEqual(len(persoane_repo.get_all_persoane()), 1, "Persoana nu a fost adaugata")
        self.assertEqual(persoane_repo.get_all_persoane()[0].get_personID(), 1, "ID-ul persoanei nu este corect")
        self.assertEqual(persoane_repo.get_all_persoane()[0].get_nume(), "Ion", "Numele persoanei nu este corect")
        self.assertEqual(persoane_repo.get_all_persoane()[0].get_adresa(), "Cluj", "Adresa persoanei nu este corecta")

    def test_delete_persoana(self):
        self.setUp()
        persoane_repo = PersoaneFileRepository("test_persoane.txt")
        persoana = Persoane(1, "Ion", "Cluj")
        persoane_repo.add_persoana(persoana)
        self.assertEqual(len(persoane_repo.get_all_persoane()), 1, "Persoana nu a fost adaugata")
        persoane_repo.delete_persoana(1)
        self.assertEqual(len(persoane_repo.get_all_persoane()), 0, "Persoana nu a fost stearsa")

    def test_update_persoana(self):
        self.setUp()
        persoane_repo = PersoaneFileRepository("test_persoane.txt")
        persoana = Persoane(1, "Ion", "Cluj")
        persoane_repo.add_persoana(persoana)
        self.assertEqual(len(persoane_repo.get_all_persoane()), 1, "Persoana nu a fost adaugata")
        persoane_repo.update_persoana(1, "Gheorghe", "Bucuresti")
        self.assertEqual(persoane_repo.get_all_persoane()[0].get_nume(), "Gheorghe", "Numele persoanei nu a fost actualizat")
        self.assertEqual(persoane_repo.get_all_persoane()[0].get_adresa(), "Bucuresti", "Adresa persoanei nu a fost actualizata")

    def runTests(self):
        print("Ruleaza testele pentru TestPersoaneRepository...")
        self.test_add_persoana()
        print("test_add_persoana a trecut cu succes!")
        self.test_delete_persoana()
        print("test_delete_persoana a trecut cu succes!")
        self.test_update_persoana()
        print("test_update_persoana a trecut cu succes!")
        print("Toate testele pentru TestPersoaneRepository au trecut cu succes!")

class TestEvenimenteRepository(unittest.TestCase):
    def setUp(self):
        with open("test_evenimente.txt", "w") as file:
            file.write("")

    def test_add_eveniment(self):
        self.setUp()
        evenimente_repo = EvenimenteFileRepository("test_evenimente.txt")
        eveniment = Evenimente(1, "12.12.2020", "12:00", "Concert")
        evenimente_repo.add_eveniment(eveniment)
        self.assertEqual(len(evenimente_repo.get_all_evenimente()), 1, "Evenimentul nu a fost adaugat")
        self.assertEqual(evenimente_repo.get_all_evenimente()[0].get_eventID(), 1, "ID-ul evenimentului nu este corect")
        self.assertEqual(evenimente_repo.get_all_evenimente()[0].get_data(), "12.12.2020", "Data evenimentului nu este corecta")
        self.assertEqual(evenimente_repo.get_all_evenimente()[0].get_timp(), "12:00", "Timpul evenimentului nu este corect")
        self.assertEqual(evenimente_repo.get_all_evenimente()[0].get_descriere(), "Concert", "Descrierea evenimentului nu este corecta")

    def test_delete_eveniment(self):
        self.setUp()
        evenimente_repo = EvenimenteFileRepository("test_evenimente.txt")
        eveniment = Evenimente(1, "12.12.2020", "12:00", "Concert")
        evenimente_repo.add_eveniment(eveniment)
        self.assertEqual(len(evenimente_repo.get_all_evenimente()), 1, "Evenimentul nu a fost adaugat")
        evenimente_repo.delete_eveniment(1)
        self.assertEqual(len(evenimente_repo.get_all_evenimente()), 0, "Evenimentul nu a fost sters")

    def test_update_eveniment(self):
        self.setUp()
        evenimente_repo = EvenimenteFileRepository("test_evenimente.txt")
        eveniment = Evenimente(1, "12.12.2020", "12:00", "Concert")
        evenimente_repo.add_eveniment(eveniment)
        self.assertEqual(len(evenimente_repo.get_all_evenimente()), 1, "Evenimentul nu a fost adaugat")
        evenimente_repo.update_eveniment(1, "13.12.2020", "13:00", "Festival")
        self.assertEqual(evenimente_repo.get_all_evenimente()[0].get_data(), "13.12.2020", "Data evenimentului nu a fost actualizata")
        self.assertEqual(evenimente_repo.get_all_evenimente()[0].get_timp(), "13:00", "Timpul evenimentului nu a fost actualizat")
        self.assertEqual(evenimente_repo.get_all_evenimente()[0].get_descriere(), "Festival", "Descrierea evenimentului nu a fost actualizata")

    def runTests(self):
        print("Ruleaza testele pentru TestEvenimenteRepository...")
        self.test_add_eveniment()
        print("test_add_eveniment a trecut cu succes!")
        self.test_delete_eveniment()
        print("test_delete_eveniment a trecut cu succes!")
        self.test_update_eveniment()
        print("test_update_eveniment a trecut cu succes!")
        print("Toate testele pentru TestEvenimenteRepository au trecut cu succes!")

class TestEvenimenteController(unittest.TestCase):
    def setUp(self):
        self._persoane_repo = PersoaneFileRepository("test_persoane.txt")
        self._evenimente_repo = EvenimenteFileRepository("test_evenimente.txt")
        self._inscriere_repo = InscriereFileRepository("test_inscriere.txt")
        self._controller = EvenimenteController(self._persoane_repo, self._evenimente_repo, self._inscriere_repo)
        self._persoane_validator = PersoaneValidator()
        self._evenimente_validator = EvenimenteValidator()
        self._inscriere_validator = InscriereValidator()
        with open("test_persoane.txt", "w") as file:
            file.write("")
        with open("test_evenimente.txt", "w") as file:
            file.write("")
        with open("test_inscriere.txt", "w") as file:
            file.write("")

    def test_search_persoane(self):
        self.setUp()
        persoana = Persoane(1, "Vasile", "Arad")
        self._persoane_repo.add_persoana(persoana)
        self.assertEqual(self._controller.search_persoana_by_id(1), persoana, "Persoana nu a fost gasita dupa ID")
        self.assertEqual(self._controller.search_persoane_by_name("Vasile"), [persoana], "Persoana nu a fost gasita dupa nume")
        self.assertEqual(self._controller.search_persoane_by_adresa("Arad"), [persoana], "Persoana nu a fost gasita dupa adresa")

    def test_search_evenimente(self):
        self.setUp()
        eveniment = Evenimente(1, "12.12.2021", "17:00", "Colinde")
        self._evenimente_repo.add_eveniment(eveniment)
        self.assertEqual(self._controller.search_eveniment_by_id(1), eveniment, "Evenimentul nu a fost gasit dupa ID")
        self.assertEqual(self._controller.search_evenimente_by_data("12.12.2021"), [eveniment], "Evenimentul nu a fost gasit dupa data")
        self.assertEqual(self._controller.search_evenimente_by_timp("17:00"), [eveniment], "Evenimentul nu a fost gasit dupa timp")
        self.assertEqual(self._controller.search_evenimente_by_descriere("Colinde"), [eveniment], "Evenimentul nu a fost gasit dupa descriere")

    def test_valideaza_persoana(self):
        self.setUp()
        persoana_valida = Persoane(1, "Ionel", "Vaslui")
        self.assertTrue(self._persoane_validator.valideaza_persoana(persoana_valida), "Persoana nu este valida")
        with self.assertRaises(ValueError):
            persoana_invalida = Persoane(-1, "", "")
            self._persoane_validator.valideaza_persoana(persoana_invalida)

    def test_valideaza_eveniment(self):
        self.setUp()
        eveniment_valid = Evenimente(1, "12.12.2022", "14:00", "Concert")
        self.assertTrue(self._evenimente_validator.valideaza_eveniment(eveniment_valid), "Evenimentul nu este valid")
        with self.assertRaises(ValueError):
            eveniment_invalid = Evenimente(-1, "", "", "")
            self._evenimente_validator.valideaza_eveniment(eveniment_invalid)

    def test_inscrie_persoana(self):
        self.setUp()
        persoana = Persoane(1, "Paul", "Bacau")
        eveniment = Evenimente(1, "12.12.2023", "15:00", "Concert")
        self._persoane_repo.add_persoana(persoana)
        self._evenimente_repo.add_eveniment(eveniment)

        self._controller.inscrie_persoana(1, 1, 1)
        self.assertEqual(len(self._inscriere_repo.get_all_inscrieri()), 1, "Inscrierea nu a fost adaugata")

    def test_evenimente_la_care_participa_o_persoana_descriere(self):
        self.setUp()
        persoana = Persoane(1, "Maria", "Iasi")
        eveniment1 = Evenimente(1, "10.10.2023", "10:00", "Workshop")
        eveniment2 = Evenimente(2, "11.11.2023", "11:00", "Seminar")
        eveniment3 = Evenimente(3, "12.12.2023", "12:00", "Conference")

        self._persoane_repo.add_persoana(persoana)
        self._evenimente_repo.add_eveniment(eveniment1)
        self._evenimente_repo.add_eveniment(eveniment2)
        self._evenimente_repo.add_eveniment(eveniment3)

        self._controller.inscrie_persoana(1, 1, 1)
        self._controller.inscrie_persoana(2, 1, 2)
        self._controller.inscrie_persoana(3, 1, 3)

        evenimente = self._controller.evenimente_la_care_participa_o_persoana(1, sortBy="descriere")
        self.assertEqual(evenimente[0].get_descriere(), "Conference",
                         "Evenimentele nu sunt sortate corect dupa descriere")
        self.assertEqual(evenimente[1].get_descriere(), "Seminar", "Evenimentele nu sunt sortate corect dupa descriere")
        self.assertEqual(evenimente[2].get_descriere(), "Workshop",
                         "Evenimentele nu sunt sortate corect dupa descriere")

    def test_evenimente_la_care_participa_o_persoana_data(self):
        self.setUp()
        persoana = Persoane(1, "Maria", "Iasi")
        eveniment1 = Evenimente(1, "10.10.2023", "10:00", "Workshop")
        eveniment2 = Evenimente(2, "11.11.2023", "11:00", "Seminar")
        eveniment3 = Evenimente(3, "12.12.2023", "12:00", "Conference")

        self._persoane_repo.add_persoana(persoana)
        self._evenimente_repo.add_eveniment(eveniment1)
        self._evenimente_repo.add_eveniment(eveniment2)
        self._evenimente_repo.add_eveniment(eveniment3)

        self._controller.inscrie_persoana(1, 1, 1)
        self._controller.inscrie_persoana(2, 1, 2)
        self._controller.inscrie_persoana(3, 1, 3)

        evenimente = self._controller.evenimente_la_care_participa_o_persoana(1, sortBy="data")
        self.assertEqual(evenimente[0].get_data(), "10.10.2023", "Evenimentele nu sunt sortate corect dupa data")
        self.assertEqual(evenimente[1].get_data(), "11.11.2023", "Evenimentele nu sunt sortate corect dupa data")
        self.assertEqual(evenimente[2].get_data(), "12.12.2023", "Evenimentele nu sunt sortate corect dupa data")

    def test_persoane_participante_la_cele_mai_multe_evenimente(self):
        self.setUp()
        persoana1 = Persoane(1, "Ana", "Timisoara")
        persoana2 = Persoane(2, "Mihai", "Oradea")
        eveniment1 = Evenimente(1, "01.01.2024", "10:00", "Expozitie")
        eveniment2 = Evenimente(2, "02.02.2024", "11:00", "Conferinta")
        eveniment3 = Evenimente(3, "03.03.2024", "12:00", "Workshop")

        self._persoane_repo.add_persoana(persoana1)
        self._persoane_repo.add_persoana(persoana2)
        self._evenimente_repo.add_eveniment(eveniment1)
        self._evenimente_repo.add_eveniment(eveniment2)
        self._evenimente_repo.add_eveniment(eveniment3)

        self._controller.inscrie_persoana(1, 1, 1)
        self._controller.inscrie_persoana(2, 1, 2)
        self._controller.inscrie_persoana(3, 2, 1)
        self._controller.inscrie_persoana(4, 2, 2)
        self._controller.inscrie_persoana(5, 2, 3)
        self._controller.inscrie_persoana(6, 2, 3)

        persoane = self._controller.persoane_participante_la_cele_mai_multe_evenimente()
        self.assertEqual(persoane[0][0].get_personID(), 2, "Persoana cu cele mai multe evenimente nu este corecta")
        self.assertEqual(persoane[1][0].get_personID(), 1, "Persoana cu al doilea numar de evenimente nu este corecta")

    def test_top_20_procent_evenimente_cu_cei_mai_multi_participanti(self):
        self.setUp()
        eveniment1 = Evenimente(1, "01.01.2024", "10:00", "Expozitie")
        eveniment2 = Evenimente(2, "02.02.2024", "11:00", "Conferinta")
        eveniment3 = Evenimente(3, "03.03.2024", "12:00", "Workshop")
        eveniment4 = Evenimente(4, "04.04.2024", "13:00", "Seminar")

        self._evenimente_repo.add_eveniment(eveniment1)
        self._evenimente_repo.add_eveniment(eveniment2)
        self._evenimente_repo.add_eveniment(eveniment3)
        self._evenimente_repo.add_eveniment(eveniment4)

        persoana1 = Persoane(1, "Ana", "Timisoara")
        persoana2 = Persoane(2, "Mihai", "Oradea")

        self._persoane_repo.add_persoana(persoana1)
        self._persoane_repo.add_persoana(persoana2)

        self._controller.inscrie_persoana(1, 1, 1)
        self._controller.inscrie_persoana(2, 1, 2)
        self._controller.inscrie_persoana(3, 2, 1)
        self._controller.inscrie_persoana(4, 2, 2)
        self._controller.inscrie_persoana(5, 2, 3)
        self._controller.inscrie_persoana(6, 2, 4)

        top_evenimente = self._controller.top_20_procent_evenimente_cu_cei_mai_multi_participanti()
        self.assertEqual(len(top_evenimente), 1, "Numarul de evenimente din top nu este corect")
        self.assertEqual(top_evenimente[0][0].get_eventID(), 1, "Primul eveniment din top nu este corect")

    def runTests(self):
        print("Ruleaza testele pentru TestEvenimenteController...")
        self.test_search_persoane()
        print("test_search_persoane a trecut cu succes!")
        self.test_search_evenimente()
        print("test_search_evenimente a trecut cu succes!")
        self.test_valideaza_persoana()
        print("test_valideaza_persoana a trecut cu succes!")
        self.test_valideaza_eveniment()
        print("test_valideaza_eveniment a trecut cu succes!")
        self.test_inscrie_persoana()
        print("test_inscrie_persoana a trecut cu succes!")
        self.test_evenimente_la_care_participa_o_persoana_descriere()
        print("test_evenimente_la_care_participa_o_persoana_descriere a trecut cu succes!")
        self.test_evenimente_la_care_participa_o_persoana_data()
        print("test_evenimente_la_care_participa_o_persoana_data a trecut cu succes!")
        self.test_persoane_participante_la_cele_mai_multe_evenimente()
        print("test_persoane_participante_la_cele_mai_multe_evenimente a trecut cu succes!")
        self.test_top_20_procent_evenimente_cu_cei_mai_multi_participanti()
        print("test_top_20_procent_evenimente_cu_cei_mai_multi_participanti a trecut cu succes!")
        print("Toate testele pentru TestEvenimenteController au trecut cu succes!")