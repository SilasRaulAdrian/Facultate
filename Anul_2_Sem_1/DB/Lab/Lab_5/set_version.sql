CREATE PROCEDURE SeteazaVersiune
    @versiune INT
AS
BEGIN
    DECLARE @versiune_curenta INT;
    SELECT @versiune_curenta = versiune_curenta FROM Versiuni;

    PRINT('Versiunea curenta: ' + CAST(@versiune_curenta AS NVARCHAR(10)));
    PRINT('Versiunea dorita: ' + CAST(@versiune AS NVARCHAR(10)));

    IF @versiune_curenta < @versiune
    BEGIN
        WHILE @versiune_curenta < @versiune
        BEGIN
            SET @versiune_curenta = @versiune_curenta + 1;

            IF @versiune_curenta = 2 EXEC Versiune_1_Upgrade;
            ELSE IF @versiune_curenta = 3 EXEC Versiune_2_Upgrade;
            ELSE IF @versiune_curenta = 4 EXEC Versiune_3_Upgrade;
            ELSE IF @versiune_curenta = 5 EXEC Versiune_4_Upgrade;
            ELSE IF @versiune_curenta = 6 EXEC Versiune_5_Upgrade;
        END
    END

    ELSE IF @versiune_curenta > @versiune
    BEGIN
        WHILE @versiune_curenta > @versiune
        BEGIN
            IF @versiune_curenta = 6 EXEC Versiune_5_Downgrade;
            ELSE IF @versiune_curenta = 5 EXEC Versiune_4_Downgrade;
            ELSE IF @versiune_curenta = 4 EXEC Versiune_3_Downgrade;
            ELSE IF @versiune_curenta = 3 EXEC Versiune_2_Downgrade;
            ELSE IF @versiune_curenta = 2 EXEC Versiune_1_Downgrade;

            SET @versiune_curenta = @versiune_curenta - 1;
        END
    END

    PRINT('Baza de date este acum la versiunea: ' + CAST(@versiune_curenta AS NVARCHAR(10)));
END;
GO

EXEC SeteazaVersiune @versiune_target = 4;
SELECT TOP(1) versiune_curenta FROM Versiuni;

EXEC SeteazaVersiune @versiune_target = 2;
SELECT TOP(1) versiune_curenta FROM Versiuni;

EXEC SeteazaVersiune @versiune_target = 5;
SELECT TOP(1) versiune_curenta FROM Versiuni;

EXEC SeteazaVersiune @versiune_target = 1;
SELECT TOP(1) versiune_curenta FROM Versiuni;

EXEC SeteazaVersiune @versiune_target = 3;
SELECT TOP(1) versiune_curenta FROM Versiuni;

EXEC SeteazaVersiune @versiune_target = 6;

EXEC SeteazaVersiune @versiune_target = 100;
SELECT TOP(1) versiune_curenta FROM Versiuni;
