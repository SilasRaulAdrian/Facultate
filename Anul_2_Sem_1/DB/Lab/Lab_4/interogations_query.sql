USE FitLifeAppDB
GO

-- 1. Afiseaza utilizatorii care au obiectivul "Slabire" si ultimele date de progres
SELECT u.Nume, p.Obiectiv, pr.Greutate AS GreutateCurenta, pr.RepetariMaxime, pr.DataInregistrare
FROM Utilizatori u
JOIN ProfiluriUtilizatori p ON u.UtilizatorID = p.UtilizatorID
JOIN ProgresUtilizatori pr ON u.UtilizatorID = pr.UtilizatorID
WHERE p.Obiectiv = 'Slabire';

-- 2. Afiseaza produsele care costa mai mult de 80 de lei si au stoc mai mare de 20 de bucati
SELECT NumeProdus, Pret, Stoc, Categorie
FROM Produse
WHERE Pret > 80 AND Stoc > 20;

-- 3. Afiseaza comenzile "Livrate" impreuna cu produsele comandate
SELECT c.ComandaID, u.Nume AS Client, p.NumeProdus, dc.Cantitate, c.DataComanda
FROM Comenzi c
JOIN Utilizatori u ON c.UtilizatorID = u.UtilizatorID
JOIN DetaliiComenzi dc ON c.ComandaID = dc.ComandaID
JOIN Produse p ON dc.ProdusID = p.ProdusID
WHERE c.Status = 'Livrat';

-- 4. Afiseaza utilizatorii cu greutate < 70kg, obiectiv "Slabire" si antrenorul planului
SELECT DISTINCT u.Nume AS Utilizator, p.Greutate, p.Obiectiv, a.Nume AS Antrenor, pa.NumePlan
FROM Utilizatori u
JOIN ProfiluriUtilizatori p ON u.UtilizatorID = p.UtilizatorID
JOIN PlanuriAntrenament pa ON pa.AntrenorID IS NOT NULL
JOIN Antrenori a ON a.AntrenorID = pa.AntrenorID
WHERE p.Greutate < 70 AND p.Obiectiv = 'Slabire';

-- 5. Afiseaza toate recenziile cu rating sub 4 pentru produse
SELECT r.RecenzieProdusID, p.NumeProdus, u.Nume, r.Rating, r.Comentariu
FROM RecenziiProduse r
JOIN Produse p ON r.ProdusID = p.ProdusID
JOIN Utilizatori u ON r.UtilizatorID = u.UtilizatorID
WHERE r.Rating < 4;

-- 6. Afiseaza totalul produselor vandute per categorie doar pentru comenzile "Livrate"
SELECT p.Categorie, SUM(dc.Cantitate) AS TotalVandute
FROM DetaliiComenzi dc
JOIN Produse p ON dc.ProdusID = p.ProdusID
JOIN Comenzi c ON dc.ComandaID = c.ComandaID
WHERE c.Status = 'Livrat'
GROUP BY p.Categorie;

-- 7. Afiseaza antrenorii care au rating mediu >= 4 si mai mult de 1 plan creat
SELECT a.Nume AS Antrenor, AVG(r.Rating) AS RatingMediu, COUNT(p.PlanID) AS NrPlanuri
FROM Antrenori a
JOIN RecenziiAntrenori r ON a.AntrenorID = r.AntrenorID
JOIN PlanuriAntrenament p ON a.AntrenorID = p.AntrenorID
GROUP BY a.Nume
HAVING AVG(r.Rating) >= 4 AND COUNT(p.PlanID) > 0;

-- 8. Afiseaza produsele care au un rating mediu mai mic decat 4
SELECT p.NumeProdus, AVG(r.Rating) AS RatingMediu
FROM Produse p
JOIN RecenziiProduse r ON p.ProdusID = r.ProdusID
GROUP BY p.NumeProdus
HAVING AVG(r.Rating) < 4;

-- 9. Afiseaza toate exercitiile diferite folosite in planurile de antrenament
SELECT DISTINCT e.NumeExercitiu, e.Tip
FROM PlanExercitii pe
JOIN Exercitii e ON pe.ExercitiuID = e.ExercitiuID;

-- 10. Afiseaza utilizatorii care au comandat cel putin un produs din categoria "Supliment"
SELECT DISTINCT u.Nume AS Client, p.NumeProdus, p.Categorie
FROM Utilizatori u
JOIN Comenzi c ON u.UtilizatorID = c.UtilizatorID
JOIN DetaliiComenzi dc ON c.ComandaID = dc.ComandaID
JOIN Produse p ON dc.ProdusID = p.ProdusID
WHERE p.Categorie = 'Supliment';
