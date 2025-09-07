from iteratia1 import ContBancar
from tipareste_tranzactii_tip_ordonate import tipareste_tranzactii_tip_ordonate_dupa_suma

def test_tipareste_tranzactii_tip_ordonate_dupa_suma():
    cont = ContBancar()
    cont.adauga_tranzactie(1, 200, 'intrare')
    cont.adauga_tranzactie(2, 150, 'intrare')
    cont.adauga_tranzactie(3, 300, 'iesire')
    cont.adauga_tranzactie(4, 100, 'intrare')
    cont.adauga_tranzactie(5, 50, 'intrare')
    cont.adauga_tranzactie(6, 200, 'iesire')

    # Tipărește tranzacțiile de tip 'intrare' ordonate în ordine crescătoare
    tranzactii_cresc = tipareste_tranzactii_tip_ordonate_dupa_suma(cont, tip='intrare', crescator=True)
    assert tranzactii_cresc[0].zi == 5 and tranzactii_cresc[0].suma == 50 and tranzactii_cresc[0].tip == 'intrare'
    assert tranzactii_cresc[1].zi == 4 and tranzactii_cresc[1].suma == 100 and tranzactii_cresc[1].tip == 'intrare'
    assert tranzactii_cresc[2].zi == 2 and tranzactii_cresc[2].suma == 150 and tranzactii_cresc[2].tip == 'intrare'
    assert tranzactii_cresc[3].zi == 1 and tranzactii_cresc[3].suma == 200 and tranzactii_cresc[3].tip == 'intrare'

    # Tipărește tranzacțiile de tip 'ieșire' ordonate în ordine descrescătoare
    tranzactii_desc = tipareste_tranzactii_tip_ordonate_dupa_suma(cont, tip='iesire', crescator=False)
    assert tranzactii_desc[0].zi == 3 and tranzactii_desc[0].suma == 300 and tranzactii_desc[0].tip == 'iesire'
    assert tranzactii_desc[1].zi == 6 and tranzactii_desc[1].suma == 200 and tranzactii_desc[1].tip == 'iesire'

    print("test_tipareste_tranzactii_tip_ordonate_dupa_suma passed")

test_tipareste_tranzactii_tip_ordonate_dupa_suma()