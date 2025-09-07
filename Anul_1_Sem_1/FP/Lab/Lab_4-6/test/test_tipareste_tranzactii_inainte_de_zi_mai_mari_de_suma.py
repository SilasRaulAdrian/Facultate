from iteratia1 import ContBancar
from tipareste_tranzactii_inainte_de_zi_mai_mari_de_suma import tranzactii_inainte_de_zi_mai_mari_de_suma

def test_tipareste_tranzactii_inainte_de_zi_mai_mari_de_suma():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 500, 'intrare')
    cont.adauga_tranzactie(2, 300, 'ieșire')
    cont.adauga_tranzactie(3, 1000, 'intrare')

    tranzactii = tranzactii_inainte_de_zi_mai_mari_de_suma(cont, 3, 400)
    assert len(tranzactii) == 1
    assert tranzactii[0].suma == 500
    print("test_tranzactii_inainte_de_zi_si_mai_mari passed")

test_tipareste_tranzactii_inainte_de_zi_mai_mari_de_suma()