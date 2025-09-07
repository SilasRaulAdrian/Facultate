from iteratia1 import ContBancar
from sterge_tranzactii_perioada_data import sterge_tranzactii_perioada

def test_sterge_tranzactii_perioada():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 200, 'intrare')
    cont.adauga_tranzactie(3, 150, 'intrare')
    cont.adauga_tranzactie(5, 300, 'ieșire')
    cont.adauga_tranzactie(7, 100, 'intrare')
    cont.adauga_tranzactie(10, 50, 'intrare')

    sterge_tranzactii_perioada(cont, 3, 7)

    assert len(cont.tranzactii) == 2
    assert cont.tranzactii[0].zi == 1 and cont.tranzactii[0].suma == 200 and cont.tranzactii[0].tip == 'intrare'
    assert cont.tranzactii[1].zi == 10 and cont.tranzactii[1].suma == 50 and cont.tranzactii[1].tip == 'intrare'
    print("test_sterge_tranzactii_perioada passed")

test_sterge_tranzactii_perioada()