from iteratia1 import ContBancar
from sterge_tranzactii_anumit_tip import sterge_tranzactii_tip

def test_sterge_tranzactii_tip():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 100, 'intrare')
    cont.adauga_tranzactie(2, 200, 'ieșire')
    cont.adauga_tranzactie(3, 300, 'intrare')

    sterge_tranzactii_tip(cont, 'intrare')
    assert len(cont.tranzactii) == 1
    assert cont.tranzactii[0].tip == 'ieșire'
    print("test_sterge_tranzactii_tip passed")

test_sterge_tranzactii_tip()