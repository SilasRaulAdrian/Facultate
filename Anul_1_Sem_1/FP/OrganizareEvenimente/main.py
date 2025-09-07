from repository.persoane_file_repo import PersoaneFileRepository
from repository.evenimente_file_repo import EvenimenteFileRepository
from controller.evenimente_controller import EvenimenteController
from repository.inscriere_file_repo import InscriereFileRepository
from ui.console_ui import ConsoleUI
from tests.tests import *

def main():
    testPersoaneRepo = TestPersoaneRepository()
    testPersoaneRepo.runTests()
    testEvenimenteRepo = TestEvenimenteRepository()
    testEvenimenteRepo.runTests()
    testEvenimenteController = TestEvenimenteController()
    testEvenimenteController.runTests()

    persoane_repo = PersoaneFileRepository("persoane.txt")
    evenimente_repo = EvenimenteFileRepository("evenimente.txt")
    inscriere_repo = InscriereFileRepository("inscriere.txt")

    controller = EvenimenteController(persoane_repo, evenimente_repo, inscriere_repo)
    ui = ConsoleUI(controller)
    ui.run()

if __name__ == "__main__":
    main()