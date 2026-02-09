USE FitLifeAppDB
GO

CREATE TABLE Versiuni
(
    cov_v INT PRIMARY KEY IDENTITY,
    versiune_curenta INT
);

INSERT INTO Versiuni (versiune_curenta) VALUES (0);

SELECT TOP(1) versiune_curenta FROM Versiuni;

CREATE OR ALTER PROCEDURE Versiune_1_Upgrade
AS
BEGIN
    PRINT('--- Upgrade la versiunea 1: Modificare tip coloana ---');
    ALTER TABLE ProfiluriUtilizatori
    ALTER COLUMN Greutate FLOAT;

    UPDATE Versiuni SET versiune_curenta = 1;
END;

EXEC Versiune_1_Upgrade;
SELECT TOP(1) versiune_curenta FROM Versiuni;
SELECT * FROM ProfiluriUtilizatori;

CREATE OR ALTER PROCEDURE Versiune_1_Downgrade
AS
BEGIN
    PRINT('--- Revenire la versiunea 0: Tip coloana initial ---');
    ALTER TABLE ProfiluriUtilizatori
    ALTER COLUMN Greutate DECIMAL(5,2);

    UPDATE Versiuni SET versiune_curenta = 0;
END;

EXEC Versiune_1_Downgrade;
SELECT TOP(1) versiune_curenta FROM Versiuni;
SELECT * FROM ProfiluriUtilizatori;

CREATE OR ALTER PROCEDURE Versiune_2_Upgrade
AS
BEGIN
    PRINT('--- Upgrade la versiunea 2: Adaugare default constraint ---');
    ALTER TABLE Produse
    ADD CONSTRAINT DF_Produse_Stoc DEFAULT 0 FOR Stoc;

    UPDATE Versiuni SET versiune_curenta = 2;
END;

EXEC Versiune_2_Upgrade;
SELECT TOP(1) versiune_curenta FROM Versiuni;
INSERT INTO Produse (NumeProdus, Pret, Categorie)
VALUES ('Protein Bar', 12.5, 'Supliment');
SELECT * FROM Produse;

CREATE OR ALTER PROCEDURE Versiune_2_Downgrade
AS
BEGIN
    PRINT('--- Downgrade de la versiunea 2: Stergere default constraint ---');
    ALTER TABLE Produse
    DROP CONSTRAINT DF_Produse_Stoc;

    UPDATE Versiuni SET versiune_curenta = 1;
END;

EXEC Versiune_2_Downgrade;
SELECT TOP(1) versiune_curenta FROM Versiuni;
INSERT INTO Produse (NumeProdus, Pret, Categorie)
VALUES ('Energy Drink', 5.0, 'Bautura');
SELECT * FROM Produse;

DELETE FROM Produse WHERE ProdusID IN (11, 12);

CREATE OR ALTER PROCEDURE Versiune_3_Upgrade
AS
BEGIN
    PRINT('--- Upgrade la versiunea 3: Creare tabela LogModificari ---');
    CREATE TABLE LogModificari (
        LogID INT IDENTITY PRIMARY KEY,
        DataModificare DATETIME DEFAULT GETDATE(),
        Descriere NVARCHAR(255)
    );

    UPDATE Versiuni SET versiune_curenta = 3;
END;

EXEC Versiune_3_Upgrade;
SELECT TOP(1) versiune_curenta FROM Versiuni;
INSERT INTO LogModificari (Descriere)
VALUES ('Tabela LogModificari creata cu succes.');
SELECT * FROM LogModificari;

CREATE OR ALTER PROCEDURE Versiune_3_Downgrade
AS
BEGIN
    PRINT('--- Downgrade de la versiunea 3: Stergere tabela LogModificari ---');
    DROP TABLE LogModificari;

    UPDATE Versiuni SET versiune_curenta = 2;
END;

EXEC Versiune_3_Downgrade;
SELECT TOP(1) versiune_curenta FROM Versiuni;
SELECT * FROM LogModificari;

CREATE OR ALTER PROCEDURE Versiune_4_Upgrade
AS
BEGIN
    PRINT('--- Upgrade la versiunea 4: Adaugare coloana "Telefon" in Utilizatori ---');
    ALTER TABLE Utilizatori
    ADD Telefon NVARCHAR(20) NULL;

    UPDATE Versiuni SET versiune_curenta = 4;
END;

EXEC Versiune_4_Upgrade;
SELECT TOP(1) versiune_curenta FROM Versiuni;
SELECT * FROM Utilizatori;

CREATE OR ALTER PROCEDURE Versiune_4_Downgrade
AS
BEGIN
    PRINT('--- Downgrade de la versiunea 4: Stergere coloana Telefon ---');
    ALTER TABLE Utilizatori
    DROP COLUMN Telefon;

    UPDATE Versiuni SET versiune_curenta = 3;
END;

EXEC Versiune_4_Downgrade;
SELECT TOP(1) versiune_curenta FROM Versiuni;
SELECT * FROM Utilizatori;

CREATE OR ALTER PROCEDURE Versiune_5_Upgrade
AS
BEGIN
    PRINT('--- Upgrade la versiunea 5: Adaugare cheie straina intre Comenzi si Utilizatori ---');

    ALTER TABLE Comenzi
    ADD CONSTRAINT FK_Comenzi_Utilizatori
    FOREIGN KEY (UtilizatorID) REFERENCES Utilizatori(UtilizatorID);

    UPDATE Versiuni SET versiune_curenta = 5;
END;

EXEC Versiune_5_Upgrade;
SELECT TOP(1) versiune_curenta FROM Versiuni;
SELECT *
FROM sys.foreign_keys
WHERE name = 'FK_Comenzi_Utilizatori';

CREATE OR ALTER PROCEDURE Versiune_5_Downgrade
AS
BEGIN
    PRINT('--- Revenire la versiunea 4: Stergere cheie straina intre Comenzi si Utilizatori ---');

    ALTER TABLE Comenzi
    DROP CONSTRAINT FK_Comenzi_Utilizatori;

    UPDATE Versiuni SET versiune_curenta = 4;
END;

EXEC Versiune_5_Downgrade;
SELECT TOP(1) versiune_curenta FROM Versiuni;
SELECT *
FROM sys.foreign_keys
WHERE name = 'FK_Comenzi_Utilizatori';