from controller.evenimente_controller import EvenimenteController
from repository.evenimente_repo import EvenimenteRepo
from tests.tests import Teste
from ui.console_ui import ConsoleUI


def main():
    tests = Teste()
    tests.runTests()
    repo = EvenimenteRepo("evenimente.txt")
    ctrl = EvenimenteController(repo)
    ui = ConsoleUI(ctrl)
    ui.run()

if __name__ == "__main__":
    main()