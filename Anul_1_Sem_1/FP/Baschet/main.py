from controller.baschet_controller import BaschetController
from repository.baschet_repo import BaschetRepo
from tests.tests import Teste
from ui.console_ui import ConsoleUI


def main():
    tests = Teste()
    tests.runTests()
    repo = BaschetRepo("baschet.txt")
    ctrl = BaschetController(repo)
    ui = ConsoleUI(ctrl)
    ui.run()

if __name__ == "__main__":
    main()