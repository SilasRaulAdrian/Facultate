from controller.tractoare_controller import TractoareController
from repository.tractoare_repo import TractorRepo
from tests.tests import Teste
from ui.console_ui import ConsoleUI


def main():
    tests = Teste()
    tests.runTests()
    tractor_repo = TractorRepo("tractoare.txt")
    tractor_controller = TractoareController(tractor_repo)
    ui = ConsoleUI(tractor_controller)
    ui.run()

if __name__ == "__main__":
    main()