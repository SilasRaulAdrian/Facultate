from iteratia1 import ContBancar
from tipareste_tranzactii_anumit_tip import tranzactii_tip

def test_tranzactii_tip():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 500, "intrare")
    cont.adauga_tranzactie(2, 300, "iesire")
    cont.adauga_tranzactie(3, 1000, "intrare")

    tranzactii = tranzactii_tip(cont, "intrare")
    assert len(tranzactii) == 2
    assert tranzactii[0].tip == "intrare"
    print("test_tranzactii_tip passed")

test_tranzactii_tip()