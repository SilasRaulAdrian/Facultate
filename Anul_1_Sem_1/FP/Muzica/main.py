from controller.muzica_controller import MuzicaController
from repository.muzica_repo import MuzicaRepo
from tests.tests import Teste
from ui.console_ui import ConsoleUI
from validator.muzica_valid import MuzicaValidator


def main():
    teste = Teste()
    teste.runTests()
    muzica_repo = MuzicaRepo("muzica.txt")
    controller = MuzicaController(muzica_repo)
    ui = ConsoleUI(controller)
    ui.run()

if __name__ == "__main__":
    main()