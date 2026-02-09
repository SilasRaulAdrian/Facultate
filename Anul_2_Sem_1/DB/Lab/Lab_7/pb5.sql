USE FitLifeAppDB;
GO

-- Functii
CREATE OR ALTER FUNCTION dbo.fn_FormatName(@n NVARCHAR(200))
RETURNS NVARCHAR(200)
AS
BEGIN
    IF @n IS NULL RETURN NULL;
    RETURN UPPER(LEFT(LTRIM(RTRIM(@n)),1)) + LOWER(SUBSTRING(LTRIM(RTRIM(@n)),2,199));
END;
GO

CREATE OR ALTER FUNCTION dbo.fn_IsPositiveInt(@v INT)
RETURNS BIT
AS
BEGIN
    IF @v IS NULL RETURN 0;
    IF @v > 0 RETURN 1;
    RETURN 0;
END;
GO

-- Constraints Suplimentare
-- DurataSaptamani > 0
-- NumarSeturi, NumarRepetari > 0
IF NOT EXISTS (
    SELECT 1 FROM sys.check_constraints WHERE name = 'CK_Planuri_Durata_Pos'
)
BEGIN
    ALTER TABLE PlanuriAntrenament
    ADD CONSTRAINT CK_Planuri_Durata_Pos CHECK (DurataSaptamani > 0);
END

IF NOT EXISTS (
    SELECT 1 FROM sys.check_constraints WHERE name = 'CK_PlanEx_NumarSeturi_Pos'
)
BEGIN
    ALTER TABLE PlanExercitii
    ADD CONSTRAINT CK_PlanEx_NumarSeturi_Pos CHECK (NumarSeturi >= 0);
END

IF NOT EXISTS (
    SELECT 1 FROM sys.check_constraints WHERE name = 'CK_PlanEx_NumarRep_Pos'
)
BEGIN
    ALTER TABLE PlanExercitii
    ADD CONSTRAINT CK_PlanEx_NumarRep_Pos CHECK (NumarRepetari >= 0);
END
GO

-- Tabele de logare
CREATE TABLE Planuri_Log (
    LogID INT IDENTITY PRIMARY KEY,
    PlanID INT NULL,
    NumePlan NVARCHAR(100) NULL,
    DurataSaptamani INT NULL,
    Nivel NVARCHAR(20) NULL,
    AntrenorID INT NULL,
    Operatie NVARCHAR(10) NOT NULL,
    DataOperatie DATETIME NOT NULL DEFAULT GETDATE(),
    LoginUser NVARCHAR(128) NOT NULL
);

CREATE TABLE Exercitii_Log (
    LogID INT IDENTITY PRIMARY KEY,
    ExercitiuID INT NULL,
    NumeExercitiu NVARCHAR(100) NULL,
    Tip NVARCHAR(50) NULL,
    Descriere NVARCHAR(200) NULL,
    Operatie NVARCHAR(10) NOT NULL,
    DataOperatie DATETIME NOT NULL DEFAULT GETDATE(),
    LoginUser NVARCHAR(128) NOT NULL
);

CREATE TABLE PlanExercitii_Log (
    LogID INT IDENTITY PRIMARY KEY,
    PlanID INT NULL,
    ExercitiuID INT NULL,
    NumarSeturi INT NULL,
    NumarRepetari INT NULL,
    Operatie NVARCHAR(10) NOT NULL,
    DataOperatie DATETIME NOT NULL DEFAULT GETDATE(),
    LoginUser NVARCHAR(128) NOT NULL
);
GO

-- Proceduri CRUD pt PlanuriAntrenament
CREATE OR ALTER PROCEDURE Planuri_Create
    @NumePlan NVARCHAR(100),
    @DurataSaptamani INT,
    @Nivel NVARCHAR(20),
    @AntrenorID INT
AS
BEGIN
    IF dbo.fn_IsPositiveInt(@DurataSaptamani) = 0
    BEGIN
        RAISERROR('DurataSaptamani trebuie > 0',16,1);
    END

    SET @NumePlan = dbo.fn_FormatName(@NumePlan);

    INSERT INTO PlanuriAntrenament(NumePlan, DurataSaptamani, Nivel, AntrenorID)
    VALUES(@NumePlan, @DurataSaptamani, @Nivel, @AntrenorID);

    SELECT SCOPE_IDENTITY() AS NewPlanID;
END;
GO

CREATE OR ALTER PROCEDURE Planuri_Read
    @PlanID INT
AS
BEGIN
    SELECT p.*, a.Nume AS AntrenorName
    FROM PlanuriAntrenament p
    LEFT JOIN Antrenori a ON p.AntrenorID = a.AntrenorID
    WHERE p.PlanID = @PlanID;
END;
GO

CREATE OR ALTER PROCEDURE Planuri_Update
    @PlanID INT,
    @NumePlan NVARCHAR(100),
    @DurataSaptamani INT,
    @Nivel NVARCHAR(20),
    @AntrenorID INT
AS
BEGIN
    IF NOT EXISTS (SELECT 1 FROM PlanuriAntrenament WHERE PlanID = @PlanID)
    BEGIN
        RAISERROR('Plan nu exista',16,1); RETURN;
    END

    IF dbo.fn_IsPositiveInt(@DurataSaptamani) = 0
    BEGIN
        RAISERROR('DurataSaptamani trebuie > 0',16,1); RETURN;
    END

    SET @NumePlan = dbo.fn_FormatName(@NumePlan);

    UPDATE PlanuriAntrenament
    SET NumePlan = @NumePlan,
        DurataSaptamani = @DurataSaptamani,
        Nivel = @Nivel,
        AntrenorID = @AntrenorID
    WHERE PlanID = @PlanID;
END;
GO

CREATE OR ALTER PROCEDURE Planuri_Delete
    @PlanID INT
AS
BEGIN
    DELETE FROM PlanExercitii WHERE PlanID = @PlanID;
    DELETE FROM PlanuriAntrenament WHERE PlanID = @PlanID;
END;
GO

-- Proceduri CRUD pt Exercitii
CREATE OR ALTER PROCEDURE Exercitii_Create
    @NumeExercitiu NVARCHAR(100),
    @Tip NVARCHAR(50),
    @Descriere NVARCHAR(200)
AS
BEGIN
    SET @NumeExercitiu = dbo.fn_FormatName(@NumeExercitiu);

    INSERT INTO Exercitii(NumeExercitiu, Tip, Descriere)
    VALUES(@NumeExercitiu, @Tip, @Descriere);

    SELECT SCOPE_IDENTITY() AS NewExercitiuID;
END;
GO

CREATE OR ALTER PROCEDURE Exercitii_Read
    @ExercitiuID INT
AS
BEGIN
    SELECT * FROM Exercitii WHERE ExercitiuID = @ExercitiuID;
END;
GO

CREATE OR ALTER PROCEDURE Exercitii_Update
    @ExercitiuID INT,
    @NumeExercitiu NVARCHAR(100),
    @Tip NVARCHAR(50),
    @Descriere NVARCHAR(200)
AS
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Exercitii WHERE ExercitiuID = @ExercitiuID)
    BEGIN
        RAISERROR('Exercitiu nu exista',16,1); RETURN;
    END

    SET @NumeExercitiu = dbo.fn_FormatName(@NumeExercitiu);

    UPDATE Exercitii
    SET NumeExercitiu = @NumeExercitiu,
        Tip = @Tip,
        Descriere = @Descriere
    WHERE ExercitiuID = @ExercitiuID;
END;
GO

CREATE OR ALTER PROCEDURE Exercitii_Delete
    @ExercitiuID INT
AS
BEGIN
    DELETE FROM PlanExercitii WHERE ExercitiuID = @ExercitiuID;
    DELETE FROM Exercitii WHERE ExercitiuID = @ExercitiuID;
END;
GO

-- Proceduri CRUD pt PlanExercitii (M:N)
CREATE OR ALTER PROCEDURE PlanExercitii_Create
    @PlanID INT,
    @ExercitiuID INT,
    @NumarSeturi INT,
    @NumarRepetari INT
AS
BEGIN
    IF NOT EXISTS (SELECT 1 FROM PlanuriAntrenament WHERE PlanID = @PlanID)
    BEGIN
        RAISERROR('Plan invalid',16,1); RETURN;
    END
    IF NOT EXISTS (SELECT 1 FROM Exercitii WHERE ExercitiuID = @ExercitiuID)
    BEGIN
        RAISERROR('Exercitiu invalid',16,1); RETURN;
    END
    IF dbo.fn_IsPositiveInt(@NumarSeturi)=0 AND @NumarSeturi IS NOT NULL
    BEGIN
        RAISERROR('NumarSeturi invalid',16,1); RETURN;
    END
    IF dbo.fn_IsPositiveInt(@NumarRepetari)=0 AND @NumarRepetari IS NOT NULL
    BEGIN
        RAISERROR('NumarRepetari invalid',16,1); RETURN;
    END

    INSERT INTO PlanExercitii(PlanID, ExercitiuID, NumarSeturi, NumarRepetari)
    VALUES(@PlanID, @ExercitiuID, @NumarSeturi, @NumarRepetari);
END;
GO

CREATE OR ALTER PROCEDURE PlanExercitii_Read
    @PlanID INT,
    @ExercitiuID INT
AS
BEGIN
    SELECT pe.*, p.NumePlan, e.NumeExercitiu
    FROM PlanExercitii pe
    JOIN PlanuriAntrenament p ON pe.PlanID = p.PlanID
    JOIN Exercitii e ON pe.ExercitiuID = e.ExercitiuID
    WHERE pe.PlanID = @PlanID AND pe.ExercitiuID = @ExercitiuID;
END;
GO

CREATE OR ALTER PROCEDURE PlanExercitii_Update
    @PlanID INT,
    @ExercitiuID INT,
    @NumarSeturi INT,
    @NumarRepetari INT
AS
BEGIN
    IF NOT EXISTS (SELECT 1 FROM PlanExercitii WHERE PlanID=@PlanID AND ExercitiuID=@ExercitiuID)
    BEGIN
        RAISERROR('Intrare PlanExercitii inexistenta',16,1); RETURN;
    END

    UPDATE PlanExercitii
    SET NumarSeturi = @NumarSeturi,
        NumarRepetari = @NumarRepetari
    WHERE PlanID=@PlanID AND ExercitiuID=@ExercitiuID;
END;
GO

CREATE OR ALTER PROCEDURE PlanExercitii_Delete
    @PlanID INT,
    @ExercitiuID INT
AS
BEGIN
    DELETE FROM PlanExercitii WHERE PlanID=@PlanID AND ExercitiuID=@ExercitiuID;
END;
GO

-- Triggere + Logare: PlanuriAntrenament
CREATE OR ALTER TRIGGER trg_Planuri_Update
ON PlanuriAntrenament
AFTER UPDATE
AS
BEGIN
    INSERT INTO Planuri_Log (PlanID, NumePlan, DurataSaptamani, Nivel, AntrenorID, Operatie, DataOperatie, LoginUser)
    SELECT d.PlanID, d.NumePlan, d.DurataSaptamani, d.Nivel, d.AntrenorID, 'UPDATE', GETDATE(), ORIGINAL_LOGIN()
    FROM deleted d;
END;
GO

CREATE OR ALTER TRIGGER trg_Planuri_Delete
ON PlanuriAntrenament
AFTER DELETE
AS
BEGIN
    INSERT INTO Planuri_Log (PlanID, NumePlan, DurataSaptamani, Nivel, AntrenorID, Operatie, DataOperatie, LoginUser)
    SELECT d.PlanID, d.NumePlan, d.DurataSaptamani, d.Nivel, d.AntrenorID, 'DELETE', GETDATE(), ORIGINAL_LOGIN()
    FROM deleted d;
END;
GO

-- Triggere + Logare: Exercitii
CREATE OR ALTER TRIGGER trg_Exercitii_Update
ON Exercitii
AFTER UPDATE
AS
BEGIN
    INSERT INTO Exercitii_Log (ExercitiuID, NumeExercitiu, Tip, Descriere, Operatie, DataOperatie, LoginUser)
    SELECT d.ExercitiuID, d.NumeExercitiu, d.Tip, d.Descriere, 'UPDATE', GETDATE(), ORIGINAL_LOGIN()
    FROM deleted d;
END;
GO

CREATE OR ALTER TRIGGER trg_Exercitii_Delete
ON Exercitii
AFTER DELETE
AS
BEGIN
    INSERT INTO Exercitii_Log (ExercitiuID, NumeExercitiu, Tip, Descriere, Operatie, DataOperatie, LoginUser)
    SELECT d.ExercitiuID, d.NumeExercitiu, d.Tip, d.Descriere, 'DELETE', GETDATE(), ORIGINAL_LOGIN()
    FROM deleted d;
END;
GO

-- Triggere + Logare: PlanExercitii
CREATE OR ALTER TRIGGER trg_PlanExercitii_Update
ON PlanExercitii
AFTER UPDATE
AS
BEGIN
    INSERT INTO PlanExercitii_Log (PlanID, ExercitiuID, NumarSeturi, NumarRepetari, Operatie, DataOperatie, LoginUser)
    SELECT d.PlanID, d.ExercitiuID, d.NumarSeturi, d.NumarRepetari, 'UPDATE', GETDATE(), ORIGINAL_LOGIN()
    FROM deleted d;
END;
GO

CREATE OR ALTER TRIGGER trg_PlanExercitii_Delete
ON PlanExercitii
AFTER DELETE
AS
BEGIN
    INSERT INTO PlanExercitii_Log (PlanID, ExercitiuID, NumarSeturi, NumarRepetari, Operatie, DataOperatie, LoginUser)
    SELECT d.PlanID, d.ExercitiuID, d.NumarSeturi, d.NumarRepetari, 'DELETE', GETDATE(), ORIGINAL_LOGIN()
    FROM deleted d;
END;
GO

-- Views
CREATE OR ALTER VIEW vw_Planuri_CuExercitii
AS
SELECT p.PlanID, p.NumePlan, p.DurataSaptamani, p.Nivel, p.AntrenorID,
       pe.ExercitiuID, e.NumeExercitiu, pe.NumarSeturi, pe.NumarRepetari
FROM PlanuriAntrenament p
LEFT JOIN PlanExercitii pe ON p.PlanID = pe.PlanID
LEFT JOIN Exercitii e ON pe.ExercitiuID = e.ExercitiuID;
GO

CREATE OR ALTER VIEW vw_Exercitii_CuPlanuri
AS
SELECT e.ExercitiuID, e.NumeExercitiu, e.Tip, COUNT(pe.PlanID) AS NrPlanuri
FROM Exercitii e
LEFT JOIN PlanExercitii pe ON e.ExercitiuID = pe.ExercitiuID
GROUP BY e.ExercitiuID, e.NumeExercitiu, e.Tip;
GO

-- Indecsi nonclustered
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Planuri_AntrenorID' AND object_id = OBJECT_ID('PlanuriAntrenament'))
    CREATE NONCLUSTERED INDEX IX_Planuri_AntrenorID ON PlanuriAntrenament(AntrenorID);

SELECT * FROM PlanuriAntrenament WHERE AntrenorID = 1;

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_PlanEx_ExercitiuID' AND object_id = OBJECT_ID('PlanExercitii'))
    CREATE NONCLUSTERED INDEX IX_PlanEx_ExercitiuID ON PlanExercitii(ExercitiuID);

SELECT * FROM PlanExercitii WHERE ExercitiuID = 1;

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Exercitii_Tip' AND object_id = OBJECT_ID('Exercitii'))
    CREATE NONCLUSTERED INDEX IX_Exercitii_Tip ON Exercitii(Tip);

SELECT * FROM Exercitii WHERE Tip = 'Cardio';

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'IX_Planuri_Nume' AND object_id = OBJECT_ID('PlanuriAntrenament'))
    CREATE NONCLUSTERED INDEX IX_Planuri_Nume ON PlanuriAntrenament(NumePlan);

SELECT * FROM PlanuriAntrenament WHERE NumePlan LIKE 'P%';
