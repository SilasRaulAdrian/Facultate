from iteratia1 import ContBancar
from elimina_tranzactii_anumit_tip import elimina_tranzactii_tip

def test_elimina_tranzactii_tip():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 150, 'intrare')
    cont.adauga_tranzactie(2, 300, 'ieșire')

    elimina_tranzactii_tip(cont, 'ieșire')
    assert len(cont.tranzactii) == 1
    assert cont.tranzactii[0].tip == 'intrare'
    print("test_elimina_tranzactii_tip passed")

test_elimina_tranzactii_tip()