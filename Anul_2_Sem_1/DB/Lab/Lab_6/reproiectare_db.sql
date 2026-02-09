USE FitLifeAppDB
GO

-- Departamente (1) -> Angajati (N)

CREATE TABLE Departamente (
    DepartamentID INT PRIMARY KEY IDENTITY(1,1),
    Nume NVARCHAR(50)
);

CREATE TABLE Angajati (
    AngajatID INT PRIMARY KEY IDENTITY(1,1),
    Nume NVARCHAR(100),
    DepartamentID INT FOREIGN KEY REFERENCES Departamente(DepartamentID)
);

-- Categorii (1) -> ProduseTema (N)

CREATE TABLE Categorii (
    CategorieID INT PRIMARY KEY IDENTITY(1,1),
    NumeCategorie NVARCHAR(50)
);

CREATE TABLE ProduseTema(
    ProdusTemaID INT PRIMARY KEY IDENTITY(1,1),
    Nume NVARCHAR(100),
    CategorieID INT FOREIGN KEY REFERENCES Categorii(CategorieID)
);

-- Sali (1) -> ClaseFitness (N)

CREATE TABLE Sali(
    SalaID INT PRIMARY KEY IDENTITY(1,1),
    NumeSala NVARCHAR(50)
);

CREATE TABLE ClaseFitness(
    ClasaID INT PRIMARY KEY IDENTITY(1,1),
    NumeClasa NVARCHAR(100),
    SalaID INT FOREIGN KEY REFERENCES Sali(SalaID)
);

-- Membri (M) <-> ClaseFitness (N) prin tabelul Inscrieri(M:N)

CREATE TABLE Membri(
    MembruID INT PRIMARY KEY IDENTITY(1,1),
    Nume NVARCHAR(100)
);

CREATE TABLE Inscrieri(
    MembruID INT FOREIGN KEY REFERENCES Membri(MembruID),
    ClasaID INT FOREIGN KEY REFERENCES ClaseFitness(ClasaID),
    PRIMARY KEY (MembruID, ClasaID)
);

INSERT INTO Departamente VALUES ('HR'), ('IT'), ('Vanzari'), ('Marketing'),
('Financiar'), ('Sport'), ('Nutritie'), ('Logistica'), ('Curatenie'), ('Administrativ');

INSERT INTO Angajati (Nume, DepartamentID)
VALUES ('Ion',1),('Maria',1),('Alex',2),('Radu',2),('Diana',3),('Cristi',4),
('Elena',5),('Andrei',6),('Paula',7),('George',8);

INSERT INTO Categorii VALUES ('Suplimente'), ('Echipamente'), ('Imbracaminte'), ('Accesorii'),
('Bauturi'), ('Alimente'), ('Gadget'), ('Fitness'), ('Cardio'), ('Fortă');

INSERT INTO ProduseTema (Nume, CategorieID)
VALUES ('Produs1',1),('Produs2',1),('Produs3',2),('Produs4',3),('Produs5',4),
('Produs6',5),('Produs7',5),('Produs8',6),('Produs9',7),('Produs10',8);

INSERT INTO Sali VALUES ('Sala A'), ('Sala B'), ('Sala C'), ('Sala D'),
('Sala E'),('Sala F'),('Sala G'),('Sala H'),('Sala I'),('Sala J');

INSERT INTO ClaseFitness (NumeClasa, SalaID)
VALUES ('Yoga',1),('Pilates',1),('Cardio',2),('TRX',3),('Box',4),
('Stretching',5),('HIIT',5),('Functional',6),('Pump',7),('Dance',8);

INSERT INTO Membri VALUES ('M1'),('M2'),('M3'),('M4'),('M5'),('M6'),('M7'),('M8'),('M9'),('M10');

INSERT INTO Inscrieri VALUES
(1,1),(1,2),(2,2),(3,3),(3,4),(4,5),(5,6),(6,7),(7,8),(8,9);

CREATE TABLE Legaturi_Eliminate(
    ID INT PRIMARY KEY IDENTITY(1,1),
    NumeTabelSt NVARCHAR(50),
    IdSt INT,
    NumeTabelDr NVARCHAR(50),
    IdDr INT
);

-- Transformarea 1: 1:N -> N:1
-- Departamente (1) -> Angajati (N) devine Angajati (1) -> Departamente (N)
ALTER TABLE Departamente ADD AngajatID INT NULL;

UPDATE Departamente
SET AngajatID = (
    SELECT MAX(AngajatID)
    FROM Angajati
    WHERE Angajati.DepartamentID = Departamente.DepartamentID
);

INSERT INTO Legaturi_Eliminate
SELECT 'Departamente', D.DepartamentID, 'Angajati', A.AngajatID
FROM Departamente D
JOIN Angajati A ON A.DepartamentID = D.DepartamentID
WHERE A.AngajatID <> (
    SELECT MAX(AngajatID)
    FROM Angajati X
    WHERE X.DepartamentID = D.DepartamentID
);

SELECT * FROM Legaturi_Eliminate;

ALTER TABLE Angajati DROP CONSTRAINT FK__Angajati__Depart__7B264821;
ALTER TABLE Angajati DROP COLUMN DepartamentID;

ALTER TABLE Departamente
ADD CONSTRAINT FK_Dep_Ang FOREIGN KEY (AngajatID) REFERENCES Angajati(AngajatID);

-- Transformarea 2: 1:N -> M:N
-- Categorii - ProduseTema devine relatie M:N
CREATE TABLE CategoriiProduse (
    CategorieID INT FOREIGN KEY REFERENCES Categorii(CategorieID),
    ProdusTemaID INT FOREIGN KEY REFERENCES ProduseTema(ProdusTemaID),
    PRIMARY KEY (CategorieID, ProdusTemaID)
);

INSERT INTO CategoriiProduse
SELECT CategorieID, ProdusTemaID FROM ProduseTema;

ALTER TABLE ProduseTema DROP CONSTRAINT FK__ProduseTe__Categ__7FEAFD3E;
ALTER TABLE ProduseTema DROP COLUMN CategorieID;

-- Transformarea 3: M:N -> 1:N
-- Membri – ClaseFitness (prin Inscrieri) devine ClaseFitness (1) – Membri (N)
ALTER TABLE Membri ADD ClasaID INT NULL;

UPDATE Membri
SET ClasaID = (
    SELECT MAX(ClasaID)
    FROM Inscrieri
    WHERE Inscrieri.MembruID = Membri.MembruID
);

INSERT INTO Legaturi_Eliminate
SELECT 'Membri', M.MembruID, 'ClaseFitness', I.ClasaID
FROM Membri M
JOIN Inscrieri I ON I.MembruID = M.MembruID
WHERE I.ClasaID <> (
     SELECT MAX(ClasaID)
     FROM Inscrieri X
     WHERE X.MembruID = M.MembruID
);

SELECT * FROM Legaturi_Eliminate;

DROP TABLE Inscrieri;

ALTER TABLE Membri
ADD CONSTRAINT FK_Membri_Clase FOREIGN KEY (ClasaID) REFERENCES ClaseFitness(ClasaID);

-- Transformarea 4: 1:N -> 1:1
-- Sali – ClaseFitness devine 1:1
ALTER TABLE ClaseFitness
ADD CONSTRAINT UQ_Clase_SalaID UNIQUE (SalaID);

DELETE FROM ClaseFitness
WHERE ClasaID NOT IN (
    SELECT MAX(ClasaID)
    FROM ClaseFitness
    GROUP BY SalaID
);

SELECT * FROM Legaturi_Eliminate;
