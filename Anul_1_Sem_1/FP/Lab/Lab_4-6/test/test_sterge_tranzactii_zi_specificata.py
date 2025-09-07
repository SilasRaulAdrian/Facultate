from iteratia1 import ContBancar
from sterge_tranzactii_zi_specificata import sterge_tranzactii_zi

def test_sterge_tranzactii_zi():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 100, 'intrare')
    cont.adauga_tranzactie(2, 200, 'ieșire')
    cont.adauga_tranzactie(1, 300, 'intrare')

    sterge_tranzactii_zi(cont, 1)
    assert len(cont.tranzactii) == 1
    assert cont.tranzactii[0].zi == 2
    print("test_sterge_tranzactii_zi passed")

test_sterge_tranzactii_zi()