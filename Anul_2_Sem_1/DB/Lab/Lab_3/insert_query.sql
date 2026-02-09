USE FitLifeAppDB
GO

INSERT INTO Utilizatori (Nume, Email, Parola, DataCreare)
VALUES
('Ana Pop', 'ana.pop@mail.com', 'parola123', GETDATE()),
('Mihai Ionescu', 'mihai.ion@mail.com', 'pass321', GETDATE()),
('Ioana Dinu', 'ioana.dinu@mail.com', '12345', GETDATE()),
('Vlad Petrescu', 'vlad.pet@mail.com', 'qwerty', GETDATE()),
('Maria Stan', 'maria.stan@mail.com', 'fitlife', GETDATE()),
('Andrei Pavel', 'andrei.pavel@mail.com', 'strong1', GETDATE()),
('Radu Enache', 'radu.en@mail.com', 'abc123', GETDATE()),
('Elena Ilie', 'elena.ilie@mail.com', 'testpass', GETDATE()),
('Cristina Georgescu', 'cristina.geo@mail.com', 'mypwd', GETDATE()),
('George Vasilescu', 'george.vas@mail.com', 'secure', GETDATE());

INSERT INTO ProfiluriUtilizatori (UtilizatorID, DataNasterii, Greutate, Inaltime, Obiectiv)
VALUES
(1, '2000-04-12', 60, 165, 'Slabire'),
(2, '1995-09-08', 78, 180, 'Masa musculara'),
(3, '1998-02-15', 62, 170, 'Tonifiere'),
(4, '1989-07-22', 85, 182, 'Rezistenta'),
(5, '1997-11-05', 70, 175, 'Definire'),
(6, '2003-03-19', 68, 178, 'Performanta'),
(7, '1985-05-27', 90, 183, 'Slabire'),
(8, '2006-09-10', 55, 160, 'Tonifiere'),
(9, '1993-01-17', 74, 177, 'Masa musculara'),
(10, '1999-08-03', 64, 168, 'Slabire');


INSERT INTO Antrenori (Nume, Specializare, Email)
VALUES
('Alex Popescu', 'Fitness', 'alex.popescu@fit.com'),
('Bianca Marin', 'Yoga', 'bianca.marin@fit.com'),
('Cristian Dobre', 'Bodybuilding', 'cristian.dobre@fit.com'),
('Diana Mihai', 'Cardio', 'diana.mihai@fit.com'),
('Emanuel Radu', 'Functional', 'emanuel.radu@fit.com'),
('Florina Matei', 'Pilates', 'florina.matei@fit.com'),
('Gabriel Vasile', 'Crossfit', 'gabriel.vasile@fit.com'),
('Horia Ene', 'Stretching', 'horia.ene@fit.com'),
('Irina Stoica', 'Fitness', 'irina.stoica@fit.com'),
('Julian Munteanu', 'Nutritie', 'julian.munteanu@fit.com');

INSERT INTO PlanuriAntrenament (NumePlan, DurataSaptamani, Nivel, AntrenorID)
VALUES
('Incepatori 1', 4, 'Incepator', 1),
('Cardio Max', 6, 'Intermediar', 4),
('Power Strength', 8, 'Avansat', 3),
('Yoga Basics', 4, 'Incepator', 2),
('Functional Power', 6, 'Intermediar', 5),
('Total Body', 8, 'Avansat', 1),
('Cross Power', 5, 'Avansat', 7),
('Pilates Relax', 4, 'Incepator', 6),
('Flex & Stretch', 3, 'Incepator', 8),
('Body Sculpt', 6, 'Intermediar', 9);

INSERT INTO Exercitii (NumeExercitiu, Tip, Descriere)
VALUES
('Genuflexiuni', 'Forta', 'Exercitiu pentru picioare'),
('Flotari', 'Forta', 'Exercitiu pentru piept si brate'),
('Alergare', 'Cardio', 'Alergare usoara'),
('Abdomene', 'Forta', 'Lucreaza zona abdominala'),
('Plank', 'Stabilitate', 'Intarirea trunchiului'),
('Sarit coarda', 'Cardio', 'Coordonare si rezistenta'),
('Fandari', 'Forta', 'Exercitiu pentru picioare'),
('Intinderi spate', 'Stretching', 'Reducere tensiune spate'),
('Bicicleta statica', 'Cardio', 'Incalzire cardio'),
('Ridicari gantere', 'Forta', 'Pentru bicepsi');

INSERT INTO PlanExercitii (PlanID, ExercitiuID, NumarSeturi, NumarRepetari)
VALUES
(1, 1, 3, 15),
(1, 2, 3, 10),
(2, 3, 1, 30),
(3, 10, 4, 8),
(4, 5, 3, 60),
(5, 6, 3, 20),
(6, 7, 4, 12),
(7, 4, 3, 15),
(8, 8, 2, 45),
(9, 9, 1, 20);

INSERT INTO ProgresUtilizatori (UtilizatorID, DataInregistrare, Greutate, RepetariMaxime)
VALUES
(1, '2025-01-10', 60, 20),
(2, '2025-01-11', 77, 25),
(3, '2025-01-12', 61, 30),
(4, '2025-01-13', 84, 22),
(5, '2025-01-14', 69, 18),
(6, '2025-01-15', 67, 35),
(7, '2025-01-16', 89, 10),
(8, '2025-01-17', 54, 28),
(9, '2025-01-18', 73, 24),
(10, '2025-01-19', 63, 26);

INSERT INTO Produse (NumeProdus, Pret, Stoc, Categorie)
VALUES
('Shake proteic', 99.99, 50, 'Supliment'),
('Gantere 5kg', 120.00, 20, 'Echipament'),
('Saltea yoga', 75.50, 40, 'Accesorii'),
('Creatina monohidrat', 85.00, 30, 'Supliment'),
('Banda elastica', 40.00, 60, 'Accesorii'),
('Tricou sport', 55.00, 80, 'Imbracaminte'),
('Baton proteic', 12.00, 200, 'Supliment'),
('Rucsac fitness', 110.00, 25, 'Accesorii'),
('Minge medicinala', 95.00, 15, 'Echipament'),
('Bidon apa 1L', 30.00, 100, 'Accesorii');

INSERT INTO Comenzi (UtilizatorID, DataComanda, Status)
VALUES
(1, GETDATE(), 'Livrat'),
(2, GETDATE(), 'In procesare'),
(3, GETDATE(), 'Livrat'),
(4, GETDATE(), 'Anulat'),
(5, GETDATE(), 'Livrat'),
(6, GETDATE(), 'Livrat'),
(7, GETDATE(), 'In procesare'),
(8, GETDATE(), 'Livrat'),
(9, GETDATE(), 'Livrat'),
(10, GETDATE(), 'In procesare');

INSERT INTO DetaliiComenzi (ComandaID, ProdusID, Cantitate, PretRecent)
VALUES
(1, 1, 2, 99.99),
(1, 2, 1, 120.00),
(2, 3, 1, 75.50),
(3, 4, 2, 85.00),
(4, 5, 1, 40.00),
(5, 6, 2, 55.00),
(6, 7, 5, 12.00),
(7, 8, 1, 110.00),
(8, 9, 1, 95.00),
(9, 10, 3, 30.00);

INSERT INTO RecenziiProduse (ProdusID, UtilizatorID, Rating, Comentariu)
VALUES
(1, 1, 5, 'Excelent shake proteic!'),
(2, 2, 4, 'Gantere de calitate.'),
(3, 3, 5, 'Saltea foarte buna.'),
(4, 4, 4, 'Creatina eficienta.'),
(5, 5, 3, 'Banda elastica e ok.'),
(6, 6, 5, 'Tricou comod.'),
(7, 7, 4, 'Batoanele sunt gustoase.'),
(8, 8, 5, 'Rucsac practic.'),
(9, 9, 4, 'Minge solida.'),
(10, 10, 5, 'Bidon excelent.');

INSERT INTO RecenziiAntrenori (AntrenorID, UtilizatorID, Rating, Comentariu)
VALUES
(1, 1, 5, 'Planuri foarte eficiente!'),
(2, 2, 4, 'Yoga foarte relaxanta.'),
(3, 3, 5, 'Rezultate excelente.'),
(4, 4, 3, 'Un pic cam intens.'),
(5, 5, 4, 'Antrenamente variate.'),
(6, 6, 5, 'Pilates excelent.'),
(7, 7, 5, 'Crossfit intens dar util.'),
(8, 8, 4, 'Stretching bun pentru spate.'),
(9, 9, 5, 'Foarte profesionist.'),
(10, 10, 4, 'Sfaturi bune de nutritie.');
