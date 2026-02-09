CREATE DATABASE Parc_de_Distractii_Corect
GO

USE Parc_de_Distractii_Corect

CREATE TABLE Categorii
(cod_c INT PRIMARY KEY IDENTITY(1,1),
nume NVARCHAR(100),
);

CREATE TABLE Vizitatori
(cod_v INT PRIMARY KEY IDENTITY(1,1),
nume NVARCHAR(100),
email NVARCHAR(100),
cod_c INT FOREIGN KEY REFERENCES Categorii(cod_c),
);

CREATE TABLE Sectiuni
(cod_s INT PRIMARY KEY IDENTITY(1,1),
nume NVARCHAR(100),
descriere NVARCHAR(100),
);

CREATE TABLE Atractii
(cod_a INT PRIMARY KEY IDENTITY(1,1),
nume NVARCHAR(100),
descriere NVARCHAR(100),
varsta_min INT,
cod_s INT FOREIGN KEY REFERENCES Sectiuni(cod_s),
);

CREATE TABLE Note
(cod_a INT FOREIGN KEY REFERENCES Atractii(cod_a),
cod_v INT FOREIGN KEY REFERENCES Vizitatori(cod_v),
nota FLOAT CHECK (nota BETWEEN 1 AND 10),
PRIMARY KEY (cod_a, cod_v)
);

INSERT INTO Categorii (nume) VALUES
('categoria 1'),
('categoria 2'),
('categoria 3'),
('categoria 4'),
('categoria 5'),
('categoria 6'),
('categoria 7');

INSERT INTO Vizitatori (nume, email, cod_c) VALUES
('Denisa', 'denisa@gmail.com', 1),
('Raul', 'raul@gmail.com', 2),
('Alexia', 'alexia@gmail.com', 3),
('David', 'david@gmail.com', 4),
('Paul', 'paul@gmail.com', 1),
('Tatiana', 'tatiana@gmail.com', 7),
('Vasile', 'vasile@gmail.com', 5);

insert into Sectiuni (nume, descriere)
values ('Sectiune de groaza','te sperii de te c*ci pe tine'),
       ('Sectiune de copii','potrivit pentru copii sub 12 ani'),
       ('Sectiune3','descriere sectiune3'),
       ('Sectiune4','descriere sectiune4'),
       ('Sectiune5','descriere sectiune5'),
       ('Sectiune6','descriere sectiune6'),
       ('Sectiune7','descriere?sectiune7');

INSERT INTO Atractii(nume, descriere, varsta_min, cod_s)
VALUES
('Carousel','descriere 1', 8, 2),
('Casa Groazei', 'descriere 2', 12, 1),
('Roaler Coaster', 'descriere 3', 16,3),
('Space Ship', 'descriere 4', 12, 4),
('Pirate Ship', 'descriere 5', 12, 5),
('OZN', 'descriere 6', 16, 4),
('The Drop', 'descriere?7',?16,?7);

INSERT INTO Note(cod_a,cod_v,nota)
VALUES
(1,1,8.0),
(2,1,8.8),
(3,2,9.2),
(4,5,8.6),
(5,7,6.7),
(6,3,7.8),
(7,4,8.9);

UPDATE Sectiuni SET nume='Intergalactic'
WHERE cod_s='4';

UPDATE Categorii SET nume='Categoria 1'
WHERE cod_c='1';

UPDATE Vizitatori SET nume='Dennis'
WHERE cod_v='7';

UPDATE Atractii SET descriere='Descriere 3'
WHERE cod_a='3';

UPDATE Note SET nota=7.7
WHERE cod_a='5';

DELETE FROM Note WHERE cod_a='6';

DELETE FROM Atractii WHERE cod_a='7'

UPDATE Categorii SET nume='pensionari'
WHERE cod_c='1';

UPDATE Categorii SET nume='copii'
WHERE cod_c='2';

UPDATE Categorii SET nume='adulti'
WHERE cod_c='3';

UPDATE Categorii SET nume='pensionari'
WHERE cod_c='4';

UPDATE Categorii SET nume='copii'
WHERE cod_c='5';

UPDATE Categorii SET nume='pensionari'
WHERE cod_c='6';

UPDATE Categorii SET nume='adulti'
WHERE cod_c='7';

SELECT * FROM Categorii WHERE nume='pensionari' OR nume='copii';

UPDATE Sectiuni SET nume='Caca'
WHERE cod_s='6';

UPDATE Sectiuni SET nume='Copil'
WHERE cod_s='7';

SELECT * FROM Sectiuni WHERE nume LIKE 'C%'; 

UPDATE Sectiuni SET nume='Telefon'
WHERE cod_s='3';

UPDATE Sectiuni SET nume='Copiln'
WHERE cod_s='5';

SELECT * FROM Sectiuni WHERE nume LIKE '%_n'

SELECT * FROM Vizitatori v
WHERE NOT EXISTS (SELECT 1 FROM Note n WHERE n.cod_v = v.cod_v);

SELECT v.nume, n.nota, a.nume AS nume_atractie
FROM Vizitatori v
JOIN Note n ON v.cod_v = n.cod_v
JOIN Atractii a ON n.cod_a = a.cod_a

SELECT v.nume AS nume_vizitator, COUNT(n.nota) AS numar_note
FROM Vizitatori v
LEFT JOIN Note n ON v.cod_v = n.cod_v
GROUP BY v.nume

UPDATE Note SET nota=8.8
WHERE cod_a='5'

SELECT DISTINCT n.nota AS note_distincte
FROM Note n

SELECT s.nume AS nume_sectiune, a.nume AS nume_atractie, a.descriere AS descriere_atractie
FROM Sectiuni s
LEFT JOIN Atractii a ON s.cod_s = a.cod_s
WHERE a.cod_a IS NOT NULL;

UPDATE Note SET cod_a='2'
WHERE cod_a='3'

SELECT a.nume AS nume_atractie, a.varsta_min AS varsta_minima, COUNT(n.nota) AS numar_note
FROM Atractii a
JOIN Note n ON a.cod_a = n.cod_a
GROUP BY a.nume, a.varsta_min
HAVING COUNT(n.nota) >= 2;

SELECT c.nume AS nume_categorie, v.nume AS nume_vizitator, n.nota AS nota, a.nume AS nume_atractie, a.descriere AS descriere_atractie
FROM Categorii c
JOIN Vizitatori v ON c.cod_c = v.cod_c
JOIN Note n ON v.cod_v = n.cod_v
JOIN Atractii a ON n.cod_a = a.cod_a
WHERE c.nume != 'adulti'
AND v.cod_v IN (
    SELECT DISTINCT n.cod_v
    FROM Note n
);

SELECT a.nume AS nume_atractie, MAX(n.nota) AS nota_maxima
FROM Atractii a
JOIN Note n ON a.cod_a = n.cod_a
GROUP BY a.nume;

SELECT a.nume AS nume_atractie, MIN(n.nota) AS nota_minima
FROM Atractii a
JOIN Note n ON a.cod_a = n.cod_a
GROUP BY a.nume;

UPDATE Vizitatori SET cod_c=NULL
WHERE cod_v='6';

SELECT v.nume, v.email
FROM Vizitatori v
LEFT JOIN Categorii c ON v.cod_c = c.cod_c
WHERE c.cod_c IS NULL;

SELECT a.nume AS nume_atractie, a.descriere AS descriere_atractie
FROM Atractii a
JOIN Sectiuni s ON a.cod_s = s.cod_s
JOIN Vizitatori v ON v.cod_c = s.cod_s
JOIN Categorii c ON v.cod_c = c.cod_c
WHERE c.cod_c IS NOT NULL;
