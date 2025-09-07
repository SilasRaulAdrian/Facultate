from controller.emisiune_controller import Emisiune_Controller
from repository.emisiune_repo import EmisiuneRepo
from tests.tests import Teste
from ui.console_ui import ConsoleUI


def main():
    teste = Teste()
    teste.run_tests()
    emisiune_repo = EmisiuneRepo("emisiuni.txt")
    emisiune_controller = Emisiune_Controller(emisiune_repo)
    ui = ConsoleUI(emisiune_controller)
    ui.run()

if __name__ == "__main__":
    main()