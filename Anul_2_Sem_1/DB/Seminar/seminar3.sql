USE Parc_de_Distractii_Corect
GO

CREATE PROCEDURE InsertSectiune
    @NumeSectiune NVARCHAR(100),
    @DescriereSectiune NVARCHAR(255)
AS
BEGIN
    INSERT INTO Sectiuni (nume, descriere)
    VALUES (@NumeSectiune, @DescriereSectiune)
END
GO
EXEC InsertSectiune 'Aventura', 'Sectiune cu atractii de aventura'

-- SELECT * FROM Sectiuni

CREATE PROCEDURE UpdateVizitator
    @CodVizitator INT,
    @AdresaEmail NVARCHAR(100)
AS
BEGIN
    UPDATE Vizitatori
    SET email = @AdresaEmail
    WHERE cod_v = @CodVizitator
END
GO
EXEC UpdateVizitator 1, 'stan@gmail.com'

-- SELECT * FROM Vizitatori

CREATE PROCEDURE GetVizitatoriCuNote
AS
BEGIN
    SELECT v.nume, v.email, COUNT(n.cod_a) AS numar_note
    FROM Vizitatori v
    JOIN Note n ON v.cod_v = n.cod_v
    GROUP BY v.nume, v.email, v.nume
    HAVING COUNT(n.cod_a) > 0
END
GO
EXEC GetVizitatoriCuNote

CREATE PROCEDURE InsertCategorie
    @NumeCategorie NVARCHAR(100)
AS
BEGIN
    IF EXISTS (SELECT 1 FROM Categorii WHERE nume = @NumeCategorie)
    BEGIN
        RAISERROR('Categoria "%s" exista deja.', 16, 1, @NumeCategorie)
    END
    ELSE
    BEGIN
        INSERT INTO Categorii (nume)
        VALUES (@NumeCategorie)
    END
END
GO
EXEC InsertCategorie 'Thriller'

-- SELECT * FROM Categorii

CREATE PROCEDURE InsertAtractie
    @NumeAtractie NVARCHAR(100),
    @DescriereAtractie NVARCHAR(255),
    @VarstaMinima INT,
    @NumeSectiune NVARCHAR(100)
AS
BEGIN
    DECLARE @CodSectiune INT

    SELECT @CodSectiune = cod_s FROM Sectiuni WHERE nume = @NumeSectiune

    -- Daca sectiunea nu exista, o inseram
    IF @CodSectiune IS NULL
    BEGIN
        INSERT INTO Sectiuni (nume, descriere)
        VALUES (@NumeSectiune, 'Descriere implicita')

        SET @CodSectiune = SCOPE_IDENTITY()
    END

    -- Inseram atractia in tabelul Atractii
    INSERT INTO Atractii (nume, descriere, varsta_min, cod_s)
    VALUES (@NumeAtractie, @DescriereAtractie, @VarstaMinima, @CodSectiune)
END
GO
EXEC InsertAtractie 'Roller Coaster', 'Atractie palpitanta cu viteza mare', 12, 'Aventura'
EXEC InsertAtractie 'Carousel', 'Atractie clasica pentru copii', 3, 'aaaa'

-- SELECT * FROM Atractii
-- SELECT * FROM Sectiuni

CREATE PROCEDURE GetVizitatorByEmail
    @AdresaEmail NVARCHAR(100)
AS
BEGIN
    DECLARE @CodVizitator INT

    SELECT @CodVizitator = cod_v FROM Vizitatori WHERE email = @AdresaEmail

    IF @CodVizitator IS NOT NULL
    BEGIN
        SELECT @CodVizitator AS CodVizitator
    END
    ELSE
    BEGIN
        RAISERROR('Nu exista vizitator cu adresa de email "%s".', 16, 1, @AdresaEmail)
    END
END
GO
EXEC GetVizitatorByEmail 'raul@gmail.com'

CREATE PROCEDURE DeleteAtractie
    @NumeAtractie NVARCHAR(100)
AS
BEGIN
    DECLARE @CodAtractie INT
    SELECT @CodAtractie = cod_a FROM Atractii WHERE nume = @NumeAtractie
    IF @CodAtractie IS NULL
    BEGIN
        RAISERROR('Atractia "%s" nu exista.', 16, 1, @NumeAtractie)
    END
    IF EXISTS (SELECT 1 FROM Note WHERE cod_a = @CodAtractie)
    BEGIN
        RAISERROR('Atractia "%s" are note asociate si nu poate fi stearsa.', 16, 1, @NumeAtractie)
    END
    ELSE
    BEGIN
        DELETE FROM Atractii WHERE cod_a = @CodAtractie
    END
    END
GO
EXEC DeleteAtractie 'Carousel'
EXEC DeleteAtractie 'NonExistentAtractie'
EXEC DeleteAtractie 'Space Ship'
SELECT * FROM Atractii

CREATE PROCEDURE InsertNota
    @CodAtractie INT,
    @CodVizitator INT,
    @ValoareNota INT
AS
BEGIN
    IF NOT EXISTS (SELECT 1 FROM Atractii WHERE cod_a = @CodAtractie)
    BEGIN
        RAISERROR('Atractia cu codul %d nu exista.', 16, 1, @CodAtractie)
    END
    IF NOT EXISTS (SELECT 1 FROM Vizitatori WHERE cod_v = @CodVizitator)
    BEGIN
        RAISERROR('Vizitatorul cu codul %d nu exista.', 16, 1, @CodVizitator)
    END
    IF @ValoareNota < 1 OR @ValoareNota > 10
    BEGIN
        RAISERROR('Valoarea notei %d nu este valida. Trebuie sa fie intre 1 si 10.', 16, 1, @ValoareNota)
    END
    INSERT INTO Note (cod_a, cod_v, nota)
    VALUES (@CodAtractie, @CodVizitator, @ValoareNota)
END
GO
EXEC InsertNota 1, 6, 9
EXEC InsertNota 999, 1, 8
EXEC InsertNota 1, 999, 7
EXEC InsertNota 1, 1, 15
SELECT * FROM Note

CREATE PROCEDURE InsertMultipleAtractii
AS
BEGIN
    DECLARE @i INT = 1
    WHILE @i <= 50
    BEGIN
        DECLARE @NumeAtractie NVARCHAR(100) = 'Atractie_' + CAST(@i AS NVARCHAR(10))
        DECLARE @DescriereAtractie NVARCHAR(255) = 'Descriere pentru ' + @NumeAtractie
        DECLARE @VarstaMinima INT = CAST(RAND() * 15 + 1 AS INT)  -- Varsta minima intre 1 si 15
        DECLARE @NumeSectiune NVARCHAR(100) = 'Sectiune_' + CAST((@i % 5 + 1) AS NVARCHAR(10))  -- 5 sectiuni diferite
        EXEC InsertAtractie @NumeAtractie, @DescriereAtractie, @VarstaMinima, @NumeSectiune
        SET @i = @i + 1
    END
END
GO
EXEC InsertMultipleAtractii
SELECT * FROM Atractii