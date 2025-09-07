from iteratia1 import ContBancar
from undo_manager import undo

def test_undo_adauga_tranzactie():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 200, 'intrare')
    cont.adauga_tranzactie(2, 150, 'intrare')
    assert len(cont.tranzactii) == 2
    cont.undo()
    assert len(cont.tranzactii) == 1
    print("test_undo_adauga_tranzactie passed")

def test_undo_actualizare_tranzactie():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 200, 'intrare')
    cont.actualizeaza_tranzactie(1, 300, 'intrare')
    assert cont.tranzactii[0].suma == 300
    cont.undo()
    assert cont.tranzactii[0].suma == 200
    print("test_undo_actualizare_tranzactie passed")

def test_undo_sterge_tranzactie_zi():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 200, 'intrare')
    cont.adauga_tranzactie(2, 150, 'intrare')
    cont.sterge_tranzactii_zi(1)
    assert len(cont.tranzactii) == 1
    cont.undo()
    assert len(cont.tranzactii) == 2
    print("test_undo_sterge_tranzactie_zi passed")

def test_undo_sterge_tranzactie_perioada():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 200, 'intrare')
    cont.adauga_tranzactie(2, 150, 'intrare')
    cont.adauga_tranzactie(3, 300, 'ieșire')
    cont.sterge_tranzactii_perioada(1, 2)
    assert len(cont.tranzactii) == 1
    cont.undo()
    assert len(cont.tranzactii) == 3
    print("test_undo_sterge_tranzactie_perioada passed")

def test_undo_sterge_tranzactie_tip():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 200, 'intrare')
    cont.adauga_tranzactie(2, 150, 'intrare')
    cont.adauga_tranzactie(3, 300, 'ieșire')
    cont.sterge_tranzactii_tip('ieșire')
    assert len(cont.tranzactii) == 2
    cont.undo()
    assert len(cont.tranzactii) == 3
    print("test_undo_sterge_tranzactie_tip passed")

def test_undo_elimina_tranzactii_tip():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 200, 'intrare')
    cont.adauga_tranzactie(2, 150, 'intrare')
    cont.adauga_tranzactie(3, 300, 'ieșire')
    cont.elimina_tranzactii_tip('ieșire')
    assert len(cont.tranzactii) == 2
    cont.undo()
    assert len(cont.tranzactii) == 3
    print("test_undo_elimina_tranzactii_tip passed")

def test_undo_elimina_tranzactii_mai_mici_de_suma_tip_specificat():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 200, 'intrare')
    cont.adauga_tranzactie(2, 150, 'intrare')
    cont.adauga_tranzactie(3, 300, 'ieșire')
    cont.elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat(250, 'intrare')
    assert len(cont.tranzactii) == 1
    cont.undo()
    assert len(cont.tranzactii) == 3
    print("test_undo_elimina_tranzactii_mai_mici_de_suma_tip_specificat passed")

def test_undo():
    test_undo_adauga_tranzactie()
    test_undo_actualizare_tranzactie()
    test_undo_sterge_tranzactie_zi()
    test_undo_sterge_tranzactie_perioada()
    test_undo_sterge_tranzactie_tip()
    test_undo_elimina_tranzactii_tip()
    test_undo_elimina_tranzactii_mai_mici_de_suma_tip_specificat()

test_undo()