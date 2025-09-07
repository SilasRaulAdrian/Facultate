from iteratia1 import ContBancar
from elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat import elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat

def test_elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 200, 'intrare')
    cont.adauga_tranzactie(2, 150, 'intrare')
    cont.adauga_tranzactie(3, 300, 'ieșire')
    cont.adauga_tranzactie(4, 100, 'intrare')
    cont.adauga_tranzactie(5, 50, 'intrare')

    # Eliminăm tranzacțiile de tip 'intrare' cu suma mai mică de 150
    elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat(cont, 150, 'intrare')

    # Testăm că doar tranzacțiile de tip 'intrare' cu suma >= 150 au rămas
    assert len(cont.tranzactii) == 3
    assert cont.tranzactii[0].suma == 200 and cont.tranzactii[0].tip == 'intrare'
    assert cont.tranzactii[1].suma == 150 and cont.tranzactii[1].tip == 'intrare'
    assert cont.tranzactii[2].suma == 300 and cont.tranzactii[2].tip == 'ieșire'
    print("test_elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat passed")


test_elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat()
