USE Parc_de_Distractii_Corect
GO

CREATE FUNCTION GetCategoryCodeByName
(
    @nume NVARCHAR(100)
)
RETURNS INT
AS
BEGIN
    DECLARE @cod INT;
    SELECT @cod = cod_c
    FROM Categorii
    WHERE nume = @nume;
    RETURN @cod;
END
GO

PRINT dbo.GetCategoryCodeByName('Comedie')

CREATE FUNCTION GetSectiuniByLitera
(
    @litera NVARCHAR(1)
)
RETURNS TABLE
AS
RETURN
(
    SELECT *
    FROM Sectiuni
    WHERE nume LIKE '%' + @litera AND LEN(nume) >= 2
)
GO

INSERT INTO Sectiuni (nume, descriere)
VALUES ('a', 'abc')

SELECT * FROM dbo.GetSectiuniByLitera('a')

CREATE VIEW vw_GetCategoriiByName
AS
    SELECT * FROM Categorii
    WHERE nume = 'pensionari' OR nume = 'copii'
GO

SELECT * FROM vw_GetCategoriiByName

CREATE VIEW vw_GetSectiuniByName
AS
    SELECT * FROM Sectiuni
    WHERE nume LIKE 'c%' OR nume LIKE 'C%'
GO

SELECT * FROM vw_GetSectiuniByName

CREATE VIEW vw_GetVizitatoriAtractii
AS
    SELECT v.nume AS Nume_Vizitator, n.nota AS Nota, a.nume AS Nume_Atractie
    FROM Vizitatori v
    JOIN Note n ON v.cod_v = n.cod_v
    JOIN Atractii a ON n.cod_a = a.cod_a
GO

SELECT * FROM vw_GetVizitatoriAtractii

CREATE TRIGGER StergeCategorie
ON Categorii
INSTEAD OF DELETE
AS
BEGIN
    RAISERROR('Stergerea categoriilor nu este permisa.', 16, 1);
END
GO

DELETE FROM Categorii
WHERE nume = 'studenti'
GO

USE FitLifeAppDB
GO

CREATE TRIGGER trg_PreventDeleteUsers
ON Utilizatori
INSTEAD OF DELETE
AS
BEGIN
    RAISERROR('Deleting users is not allowed.', 16, 1);
END
GO

DELETE FROM Utilizatori
WHERE nume = 'Ana Pop'