USE FitLifeAppDB;
GO

-- INSERT date necesare

-- 1) adaugam un antrenor
INSERT INTO Antrenori (Nume, Specializare, Email)
VALUES ('Test Trainer', 'Fitness', 'trainer@test.com');

SELECT *
FROM Antrenori
ORDER BY AntrenorID DESC;

-- 2) adaugam exercitii necesare
EXEC Exercitii_Create
    @NumeExercitiu = 'Flotari',
    @Tip = 'Push',
    @Descriere = 'Flotari standard';

EXEC Exercitii_Create
    @NumeExercitiu = 'Genuflexiuni',
    @Tip = 'Legs',
    @Descriere = 'Genuflexiuni clasice';

SELECT *
FROM Exercitii
ORDER BY ExercitiuID DESC;

-- TESTE CRUD -> PlanuriAntrenament

-- CREATE
DECLARE @AntrenorID INT;

SELECT @AntrenorID = MAX(AntrenorID)
FROM Antrenori;

EXEC Planuri_Create
    'Plan Test',
    4,
    'Incepator',
    @AntrenorID;

SELECT *
FROM PlanuriAntrenament
ORDER BY PlanID DESC;

-- READ
DECLARE @PlanID INT;

SELECT @PlanID = MAX(PlanID)
FROM PlanuriAntrenament;

EXEC Planuri_Read
    @PlanID = @PlanID;

-- UPDATE
DECLARE @AntrenorID INT;

SELECT @AntrenorID = MAX(AntrenorID)
FROM Antrenori;

DECLARE @PlanID INT;

SELECT @PlanID = MAX(PlanID)
FROM PlanuriAntrenament;

EXEC Planuri_Update
    @PlanID = @PlanID,
    @NumePlan = 'Plan Test Modificat',
    @DurataSaptamani = 6,
    @Nivel = 'Mediu',
    @AntrenorID = @AntrenorID;

EXEC Planuri_Read
    @PlanID = @PlanID;

-- DELETE
DECLARE @PlanID INT;

SELECT @PlanID = MAX(PlanID)
FROM PlanuriAntrenament;

EXEC Planuri_Delete
    @PlanID = @PlanID;

SELECT * FROM PlanuriAntrenament;

-- TESTE CRUD -> Exercitii

-- READ
DECLARE @ExercitiuID INT;

SELECT @ExercitiuID = MAX(ExercitiuID)
FROM Exercitii;

EXEC Exercitii_Read
    @ExercitiuID = @ExercitiuID;

-- UPDATE
DECLARE @ExercitiuID INT;

SELECT @ExercitiuID = MAX(ExercitiuID)
FROM Exercitii;

EXEC Exercitii_Update
    @ExercitiuID = @ExercitiuID,
    @NumeExercitiu = 'Flotari Modificate',
    @Tip = 'Push',
    @Descriere = 'Flotari executate corect';

EXEC Exercitii_Read
    @ExercitiuID = @ExercitiuID;

-- DELETE
DECLARE @ExercitiuID INT;

SELECT @ExercitiuID = MAX(ExercitiuID)
FROM Exercitii;

EXEC Exercitii_Delete
    @ExercitiuID = @ExercitiuID;

SELECT * FROM Exercitii;

-- TESTE CRUD -> PlanExercitii

-- CREATE legaturi M:N
DECLARE @PlanID INT;

SELECT @PlanID = MAX(PlanID)
FROM PlanuriAntrenament;

DECLARE @ExercitiuIDMax INT;

SELECT @ExercitiuIDMax = MAX(ExercitiuID)
FROM Exercitii;

DECLARE @ExercitiuIDMin INT;
SELECT @ExercitiuIDMin = MIN(ExercitiuID)
FROM Exercitii;

EXEC PlanExercitii_Create
    @PlanID = @PlanID,
    @ExercitiuID = @ExercitiuIDMin,
    @NumarSeturi = 4,
    @NumarRepetari = 12;

EXEC PlanExercitii_Create
    @PlanID = @PlanID,
    @ExercitiuID = @ExercitiuIDMax,
    @NumarSeturi = 3,
    @NumarRepetari = 15;

SELECT * FROM PlanExercitii;

-- READ
DECLARE @PlanID INT;

SELECT @PlanID = MAX(PlanID)
FROM PlanuriAntrenament;

DECLARE @ExercitiuIDMin INT;

SELECT @ExercitiuIDMin = MIN(ExercitiuID)
FROM Exercitii;

EXEC PlanExercitii_Read
    @PlanID = @PlanID,
    @ExercitiuID = @ExercitiuIDMin;

-- UPDATE
DECLARE @PlanID INT;

SELECT @PlanID = MAX(PlanID)
FROM PlanuriAntrenament;

DECLARE @ExercitiuIDMin INT;

SELECT @ExercitiuIDMin = MIN(ExercitiuID)
FROM Exercitii;

EXEC PlanExercitii_Update
    @PlanID = @PlanID,
    @ExercitiuID = @ExercitiuIDMin,
    @NumarSeturi = 5,
    @NumarRepetari = 10;

DECLARE @PlanID INT;

SELECT @PlanID = MAX(PlanID)
FROM PlanuriAntrenament;

DECLARE @ExercitiuIDMin INT;

SELECT @ExercitiuIDMin = MIN(ExercitiuID)
FROM Exercitii;

EXEC PlanExercitii_Read
    @PlanID = @PlanID,
    @ExercitiuID = @ExercitiuIDMin;

-- DELETE
DECLARE @PlanID INT;

SELECT @PlanID = MAX(PlanID)
FROM PlanuriAntrenament;

DECLARE @ExercitiuIDMax INT;

SELECT @ExercitiuIDMax = MAX(ExercitiuID)
FROM Exercitii;

EXEC PlanExercitii_Delete
    @PlanID = @PlanID,
    @ExercitiuID = @ExercitiuIDMax;

EXEC Exercitii_Delete
    @ExercitiuID = @ExercitiuIDMax;

EXEC Planuri_Delete
    @PlanID = @PlanID;

SELECT * FROM PlanuriAntrenament;
SELECT * FROM Exercitii;
SELECT * FROM PlanExercitii;

-- Verificam log-urile
SELECT * FROM Planuri_Log ORDER BY LogID DESC;
SELECT * FROM Exercitii_Log ORDER BY LogID DESC;
SELECT * FROM PlanExercitii_Log ORDER BY LogID DESC;

-- Verificam view-urile
SELECT * FROM vw_Planuri_CuExercitii;
SELECT * FROM vw_Exercitii_CuPlanuri;
