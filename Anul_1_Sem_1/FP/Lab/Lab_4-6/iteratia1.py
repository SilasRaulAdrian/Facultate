from sterge_tranzactii_anumit_tip import sterge_tranzactii_tip
from sterge_tranzactii_zi_specificata import sterge_tranzactii_zi
from tipareste_tranzactii_anumit_tip import tranzactii_tip
from tipareste_tranzactii_inainte_de_zi_mai_mari_de_suma import tranzactii_inainte_de_zi_mai_mari_de_suma
from elimina_tranzactii_anumit_tip import elimina_tranzactii_tip
from elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat import elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat
from sterge_tranzactii_perioada_data import sterge_tranzactii_perioada
from tipareste_tranzactii_tip_ordonate import tipareste_tranzactii_tip_ordonate_dupa_suma
from validator import valideaza_tranzactie
from undo_manager import *
#from test.test_elimina_tranzactii_anumit_tip import test_elimina_tranzactii_tip
#from test.test_elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat import test_elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat
#from test.test_sterge_tranzactii_anumit_tip import test_sterge_tranzactii_tip
#from test.test_sterge_tranzactii_zi_specificata import test_sterge_tranzactii_zi
#from test.test_tipareste_tranzactii_inainte_de_zi_mai_mari_de_suma import test_tipareste_tranzactii_inainte_de_zi_mai_mari_de_suma

class Tranzactie:
    def __init__(self, zi, suma, tip):
        self.zi = zi
        self.suma = suma
        self.tip = tip

    def __repr__(self):
        return f"Tranzactie(ziua = {self.zi}, suma = {self.suma}, tipul = {self.tip})"

    def get_suma(self):
        """
            Returneaza suma tranzactiei
        """
        return self.suma

class ContBancar:
    def __init__(self):
        """
            Inițializează un cont bancar cu o listă de tranzacții goală.
        """

        self.tranzactii = []
        self.istoric_tranzactii = [[]]


    def adauga_tranzactie(self, zi, suma, tip):
        """
            Adaugă o nouă tranzacție în cont.

            :param zi: ziua tranzacției (int)
            :param suma: suma tranzacției (float)
            :param tip: tipul tranzacției ('intrare' sau 'ieșire')
            :return: None
        """
        valideaza_tranzactie(zi, suma, tip)
        salveaza_stare(self.tranzactii, self.istoric_tranzactii)

        tranzactie = Tranzactie(zi, suma, tip)
        self.tranzactii.append(tranzactie)

    def actualizeaza_tranzactie(self, zi, suma, tip):
        """
            Actualizează o tranzacție existentă pe baza zilei.

            :param zi: ziua tranzacției care trebuie actualizată (int)
            :param suma: noua sumă a tranzacției (float)
            :param tip: noul tip al tranzacției ('intrare' sau 'ieșire')
            :return: None
        """
        valideaza_tranzactie(zi, suma, tip)
        salveaza_stare(self.tranzactii, self.istoric_tranzactii)

        for tranzactie in self.tranzactii:
            if tranzactie.zi == zi:
                tranzactie.suma = suma
                tranzactie.tip = tip
                break

    def sterge_tranzactii_zi(self, zi):
        """
            Șterge toate tranzacțiile de la ziua specificată.

            :param zi: ziua tranzacțiilor care trebuie șterse (int)
            :return: None
        """

        salveaza_stare(self.tranzactii, self.istoric_tranzactii)
        self.tranzactii = sterge_tranzactii_zi(self, zi)
        if self.tranzactii is None:
            self.tranzactii = []

    def sterge_tranzactii_perioada(self, zi_inceput, zi_sfarsit):
        """
            Șterge toate tranzacțiile dintr-o perioadă dată.

            :param zi_inceput: ziua de început a perioadei (int)
            :param zi_sfarsit: ziua de sfârșit a perioadei (int)
            :return: None
        """

        salveaza_stare(self.tranzactii, self.istoric_tranzactii)
        self.tranzactii = sterge_tranzactii_perioada(self, zi_inceput, zi_sfarsit)
        if self.tranzactii is None:
            self.tranzactii = []

    def sterge_tranzactii_tip(self, tip):
        """
            Șterge toate tranzacțiile de un anumit tip.

            :param tip: tipul tranzacțiilor care trebuie șterse ('intrare' sau 'ieșire')
            :return: None
        """

        salveaza_stare(self.tranzactii, self.istoric_tranzactii)
        self.tranzactii = sterge_tranzactii_tip(self, tip)
        if self.tranzactii is None:
            self.tranzactii = []

    def elimina_tranzactii_tip(self, tip):
        """
            Elimină toate tranzacțiile de un anumit tip.

            :param tip: tipul tranzacțiilor care trebuie eliminate ('intrare' sau 'ieșire')
            :return: None
        """

        salveaza_stare(self.tranzactii, self.istoric_tranzactii)
        elimina_tranzactii_tip(self, tip)
        if self.tranzactii is None:
            self.tranzactii = []

    def elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat(self, suma, tip):
        """
            Elimină toate tranzacțiile mai mici decât o sumă dată care au tipul specificat.

            :param suma: suma prag (float)
            :param tip: tipul tranzacțiilor care trebuie eliminate ('intrare' sau 'ieșire')
            :return: None
        """

        salveaza_stare(self.tranzactii, self.istoric_tranzactii)
        elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat(self, suma, tip)
        if self.tranzactii is None:
            self.tranzactii = []

    def undo(self):
        """
            Reface ultima operație.
        """
        ultima_stare = undo(self.istoric_tranzactii)
        if ultima_stare:
            self.tranzactii = [Tranzactie(tr['zi'], tr['suma'], tr['tip']) for tr in ultima_stare]
        print("Operația de undo a fost efectuată cu succes.")

    def tranzactii_sume_mai_mari_decat_o_suma(self, suma):
        """
            Returnează toate tranzacțiile cu sume mai mari decât suma specificată.

            :param suma: suma prag (float)
            :return: o listă de tranzacții care au suma mai mare decât suma dată
        """

        return [tr for tr in self.tranzactii if tr.suma > suma]

    def suma_totala_tip(self, tip):
        """
            Returnează suma totală a tranzacțiilor de un anumit tip.

            :param tip: tipul tranzacțiilor ('intrare' sau 'ieșire')
            :return: suma totală a tranzacțiilor până la ziua dată
        """

        return sum(tr.suma for tr in self.tranzactii if tr.tip == tip)

    def sold_la_data(self, zi):
        """
            Returnează soldul contului la data specificată.

            :param zi: ziua pentru care se calculează soldul (int)
            :return: soldul contului la ziua dată
        """

        sold = 0
        for tr in self.tranzactii:
            if tr.zi <= zi:
                if tr.tip == "intrare":
                    sold += tr.suma
                elif tr.tip == "iesire":
                    sold -= tr.suma
        return sold

    def afiseaza_tranzactii(self):
        """
            Afișează toate tranzacțiile din cont.
        """

        if not self.tranzactii:
            print("Nu există tranzacții în cont.")
        else:
            for tranzactie in self.tranzactii:
                print(tranzactie)

def menu():
    """
        Meniul aplicației.
    """
    cont = ContBancar()

    while True:
        print("\n --- Meniu Cont Bancar --- \n")
        print("1. Adăugare de noi tranzacții")
        print("2. Ștergere")
        print("3. Căutări")
        print("4. Rapoarte")
        print("5. Filtrare")
        print("6. Undo")
        print("7. Afișează tranzacțiile")
        print("0. Ieșire")

        optiune = input("Alege o opțiune: ")

        if optiune == "1":
            print("1. Adaugă tranzacție")
            print("2. Actualizare tranzacție")
            functionalitate = input("Alegeți o funcționalitate: ")
            if functionalitate == "1":
                zi = int(input("Ziua tranzacției: "))
                suma = float(input("Suma tranzacției: "))
                tip = input("Tipul tranzacției ('intrare' sau 'ieșire'): ")
                cont.adauga_tranzactie(zi, suma, tip)
                print("Tranzacție adăugată cu succes.")
            elif functionalitate == "2":
                zi = int(input("Ziua tranzacției care trebuie actualizată: "))
                suma = float(input("Noua sumă a tranzacției: "))
                tip = input("Noul tip al tranzacției ('intrare' sau 'ieșire'): ")
                cont.actualizeaza_tranzactie(zi, suma, tip)
                print("Tranzacție actualizată cu succes.")
        elif optiune == "2":
            print("1. Șterge toate tranzacțiile de la ziua specificată")
            print("2. Șterge toate tranzacțiile dintr-o perioadă dată")
            print("3. Șterge toate tranzacțiile de un anumit tip")
            functionalitate = input("Alegeți o funcționalitate: ")
            if functionalitate == "1":
                zi = int(input("Introduceți ziua: "))
                sterge_tranzactii_zi(cont, zi)
                print("Tranzacțiile de la ziua specificată au fost șterse.")
            elif functionalitate == "2":
                zi_inceput = int(input("Introduceți ziua de început: "))
                zi_sfarsit = int(input("Introduceți ziua de sfârșit: "))
                sterge_tranzactii_perioada(cont, zi_inceput, zi_sfarsit)
            elif functionalitate == "3":
                tip = input("Introduceți tipul tranzacției ('intrare' sau 'ieșire'): ")
                sterge_tranzactii_tip(cont, tip)
                print("Tranzacțiile de tipul specificat au fost șterse.")
        elif optiune == "3":
            print("1. Tipărește tranzacțiile cu sume mai mari decât o sumă dată")
            print("2. Tipărește toate tranzacțiile efectuate înainte de o zi și cu sume mai mari decât o sumă")
            print("3. Tipărește tranzacțiile de un anumit tip")
            functionalitate = input("Alegeți o funcționalitate: ")
            if functionalitate == "1":
                suma = float(input("Introduceți suma: "))
                tranzactii = cont.tranzactii_sume_mai_mari_decat_o_suma(suma)
                if tranzactii:
                    print("Transacțiile cu sume mai mari decât suma dată sunt:")
                    for tranzactie in tranzactii:
                        print(tranzactie)
                else:
                    print("Nu există tranzacții cu sume mai mari decât suma dată.")
            elif functionalitate == "2":
                zi = int(input("Introduceți ziua: "))
                suma = float(input("Introduceți suma: "))
                tranzactii = tranzactii_inainte_de_zi_mai_mari_de_suma(cont, zi, suma)
                if tranzactii:
                    print("Tranzacțiile efectuate înainte de ziua specificată și cu sume mai mari decât suma dată sunt:")
                    for tranzactie in tranzactii:
                        print(tranzactie)
                else:
                    print("Nu există tranzacții care să satisfacă condițiile.")
            elif functionalitate == "3":
                tip = input("Introduceți tipul tranzacției ('intrare' sau 'ieșire'): ")
                tranzactii = tranzactii_tip(cont, tip)
                if tranzactii:
                    print("Tranzacțiile efectuate de un anumit tip sunt:")
                    for tranzactie in tranzactii:
                        print(tranzactie)
                else:
                    print("Nu există tranzacții care să satisfacă condițiile.")
        elif optiune == "4":
            print("1. Suma totală a tranzacțiilor de un anumit tip")
            print("2. Soldul contului la o dată specificată")
            print("3. Tipărește toate tranzacțiile de un anumit tip ordonat după sumă")
            functionalitate = input("Alegeți o funcționalitate: ")
            if functionalitate == "1":
                tip = input("Introduceți tipul tranzacției ('intrare' sau 'ieșire'): ")
                suma = cont.suma_totala_tip(tip)
                print(f"Suma totală a tranzacțiilor de tipul {tip} este: {suma}")
            elif functionalitate == "2":
                zi = int(input("Introduceți ziua: "))
                sold = cont.sold_la_data(zi)
                print(f"Soldul contului la data de {zi} este: {sold}")
            elif functionalitate == "3":
                tip = input("Introduceți tipul tranzacției ('intrare' sau 'ieșire'): ")
                crescator = input("Dacă doriți să ordonați crescător, introduceți 'True'; altfel, introduceți 'False': ")
                tranzactii = tipareste_tranzactii_tip_ordonate_dupa_suma(cont, tip, crescator)
                if tranzactii:
                    print("Tranzacțiile de un anumit tip ordonate după sumă sunt:")
                    for tranzactie in tranzactii:
                        print(tranzactie)
                else:
                    print("Nu există tranzacții care să satisfacă condițiile.")
        elif optiune == "5":
            print("1. Elimină toate tranzacțiile de un anumit tip")
            print("2. Elimină toate tranzacțiile mai mici decât o sumă dată care au tipul specificat")
            functionalitate = input("Alegeți o funcționalitate: ")
            if functionalitate == "1":
                tip = input("Introduceți tipul tranzacției ('intrare' sau 'ieșire'): ")
                elimina_tranzactii_tip(cont, tip)
                print("Tranzacțiile de tipul specificat au fost eliminate.")
            elif functionalitate == "2":
                suma = float(input("Introduceți suma: "))
                tip = input("Introduceți tipul tranzacției ('intrare' sau 'ieșire'): ")
                elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat(cont, suma, tip)
                print("Tranzacțiile mai mici decât suma dată și de tipul specificat au fost eliminate.")
        elif optiune == "6":
            cont.undo()
        elif optiune == "7":
            cont.afiseaza_tranzactii()
        elif optiune == "0":
            print("Iesire din aplicatie.")
            break
        else:
            print("Opțiune invalidă! Alegeti o optiune valida.")

# Testăm funcționalitățile

cont = ContBancar()
cont.adauga_tranzactie(1, 1000, "intrare")
cont.adauga_tranzactie(2, 500, "iesire")
cont.adauga_tranzactie(3, 300, "intrare")
assert len(cont.tranzactii) == 3

cont.actualizeaza_tranzactie(1, 1200, "intrare")
assert cont.tranzactii[0].suma == 1200

tranzactii_mari = cont.tranzactii_sume_mai_mari_decat_o_suma(600)
assert len(tranzactii_mari) == 1

suma_intrari = cont.suma_totala_tip("intrare")
assert suma_intrari == 1500

sold = cont.sold_la_data(2)
assert sold == 700

#test_elimina_tranzactii_tip()
#test_elimina_tranzactii_mai_mici_de_suma_cu_tip_specificat()
#test_sterge_tranzactii_tip()
#test_sterge_tranzactii_zi()
#test_tipareste_tranzactii_inainte_de_zi_mai_mari_de_suma()

if __name__ == "__main__":
    menu()