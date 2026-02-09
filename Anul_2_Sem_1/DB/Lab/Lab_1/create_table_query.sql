GO
USE FitLifeApp;
GO

-- 1. Utilizatori
CREATE TABLE Utilizatori (
    UtilizatorID INT IDENTITY PRIMARY KEY,
    Nume NVARCHAR(100),
    Email NVARCHAR(100) UNIQUE,
    Parola NVARCHAR(255),
    DataCreare DATETIME DEFAULT GETDATE()
);

-- 2. ProfiluriUtilizatori (1-1 cu Utilizatori)
CREATE TABLE ProfiluriUtilizatori (
    ProfilID INT IDENTITY PRIMARY KEY,
    UtilizatorID INT UNIQUE FOREIGN KEY REFERENCES Utilizatori(UtilizatorID),
    DataNasterii DATETIME,
    Greutate DECIMAL(5,2),
    Inaltime INT,
    Obiectiv NVARCHAR(50)
);

-- 3. Antrenori
CREATE TABLE Antrenori (
    AntrenorID INT IDENTITY PRIMARY KEY,
    Nume NVARCHAR(100),
    Specializare NVARCHAR(50),
    Email NVARCHAR(100)
);

-- 4. PlanuriAntrenament (1-m cu Antrenori)
CREATE TABLE PlanuriAntrenament (
    PlanID INT IDENTITY PRIMARY KEY,
    NumePlan NVARCHAR(100),
    DurataSaptamani INT,
    Nivel NVARCHAR(20),
    AntrenorID INT FOREIGN KEY REFERENCES Antrenori(AntrenorID)
);

-- 5. Exercitii
CREATE TABLE Exercitii (
    ExercitiuID INT IDENTITY PRIMARY KEY,
    NumeExercitiu NVARCHAR(100),
    Tip NVARCHAR(50),
    Descriere NVARCHAR(200)
);

-- 6. PlanExercitii (m-n intre PlanuriAntrenament si Exercitii)
CREATE TABLE PlanExercitii (
    PlanID INT FOREIGN KEY REFERENCES PlanuriAntrenament(PlanID),
    ExercitiuID INT FOREIGN KEY REFERENCES Exercitii(ExercitiuID),
    NumarSeturi INT,
    NumarRepetari INT,
    PRIMARY KEY (PlanID, ExercitiuID)
);

-- 7. ProgresUtilizatori (1-m cu Utilizatori)
CREATE TABLE ProgresUtilizatori (
    ProgresID INT IDENTITY PRIMARY KEY,
    UtilizatorID INT FOREIGN KEY REFERENCES Utilizatori(UtilizatorID),
    DataInregistrare DATE,
    Greutate DECIMAL(5,2),
    RepetariMaxime INT
);

-- 8. Produse
CREATE TABLE Produse (
    ProdusID INT IDENTITY PRIMARY KEY,
    NumeProdus NVARCHAR(100),
    Pret DECIMAL(10,2),
    Stoc INT,
    Categorie NVARCHAR(50)
);

-- 9. Comenzi (1-m cu Utilizatori)
CREATE TABLE Comenzi (
    ComandaID INT IDENTITY PRIMARY KEY,
    UtilizatorID INT FOREIGN KEY REFERENCES Utilizatori(UtilizatorID),
    DataComanda DATETIME DEFAULT GETDATE(),
    Status NVARCHAR(20)
);

-- 10. DetaliiComenzi (m-n intre Comenzi si Produse)
CREATE TABLE DetaliiComenzi (
    ComandaID INT FOREIGN KEY REFERENCES Comenzi(ComandaID),
    ProdusID INT FOREIGN KEY REFERENCES Produse(ProdusID),
    Cantitate INT,
    PretRecent DECIMAL(10,2),
    PRIMARY KEY (ComandaID, ProdusID)
);

-- 11. RecenziiProduse
CREATE TABLE RecenziiProduse (
    RecenzieProdusID INT IDENTITY PRIMARY KEY,
    ProdusID INT FOREIGN KEY REFERENCES Produse(ProdusID),
    UtilizatorID INT FOREIGN KEY REFERENCES Utilizatori(UtilizatorID),
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comentariu NVARCHAR(500)
);

-- 12. RecenziiAntrenori
CREATE TABLE RecenziiAntrenori (
    RecenzieAntrenorID INT IDENTITY PRIMARY KEY,
    AntrenorID INT FOREIGN KEY REFERENCES Antrenori(AntrenorID),
    UtilizatorID INT FOREIGN KEY REFERENCES Utilizatori(UtilizatorID),
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comentariu NVARCHAR(500)
);