USE Parc_de_Distractii_Corect
GO

CREATE INDEX IDX_Vizitatori_CodC ON Vizitatori(cod_c);
SELECT * FROM Vizitatori WHERE cod_c = 2;
CREATE INDEX IDX_Atractii_CodS ON Atractii(cod_s);
SELECT * FROM Atractii WHERE cod_s = 4;
CREATE INDEX IDX_Note_CodV ON Note(cod_v);
SELECT * FROM Note WHERE cod_v = 1;
CREATE INDEX IDX_Note_CodA ON Note(cod_a);
SELECT * FROM Note WHERE cod_a = 2;
CREATE INDEX IDX_Sectiuni_Nume ON Sectiuni(nume);
SELECT * FROM Sectiuni WHERE nume = 'Intergalactic';
CREATE INDEX IDX_Categorii_Nume ON Categorii(nume);
SELECT * FROM Categorii WHERE nume = 'pensionari';
CREATE INDEX IDX_Atractii_Nume ON Atractii(nume);
SELECT * FROM Atractii WHERE nume = 'Carousel';
CREATE INDEX IDX_Note_Nota ON Note(nota);
SELECT * FROM Note WHERE nota >= 8.0;
CREATE INDEX IDX_Vizitatori_Nume ON Vizitatori(nume);
SELECT * FROM Vizitatori WHERE nume LIKE 'D%';
CREATE INDEX IDX_Atractii_VarstaMin ON Atractii(varsta_min);
SELECT * FROM Atractii WHERE varsta_min <= 12;
CREATE INDEX IDX_Note_CodA_CodV ON Note(cod_a, cod_v);
SELECT * FROM Note WHERE cod_a = 2 AND cod_v = 1;
CREATE INDEX IDX_Note_CodV_CodA ON Note(cod_v, cod_a);
SELECT * FROM Note WHERE cod_v = 1 AND cod_a = 2;

USE FitLifeAppDB
GO

CREATE INDEX IDX_ProfiluriUtilizatori_Obiectiv ON ProfiluriUtilizatori(Obiectiv);
SELECT * FROM ProfiluriUtilizatori WHERE Obiectiv = 'Slabire';
CREATE INDEX IDX_Produse_Pret_Stoc ON Produse(Pret, Stoc);
SELECT * FROM Produse WHERE Pret > 80 AND Stoc > 20;
CREATE INDEX IDX_Comenzi_Status ON Comenzi(Status);
SELECT * FROM Comenzi WHERE Status = 'Livrat';
CREATE INDEX IDX_ProgresUtilizatori_Greutate ON ProgresUtilizatori(Greutate);
SELECT * FROM ProgresUtilizatori WHERE Greutate < 70;
CREATE INDEX IDX_RecenziiProduse_Rating ON RecenziiProduse(Rating);
SELECT * FROM RecenziiProduse WHERE Rating < 4;
CREATE INDEX IDX_Produse_Categorie ON Produse(Categorie);
SELECT * FROM Produse WHERE Categorie = 'Supliment';
CREATE INDEX IDX_Antrenori_AntrenorID ON Antrenori(AntrenorID);
SELECT * FROM Antrenori WHERE AntrenorID IS NOT NULL;
CREATE INDEX IDX_RecenziiAntrenori_Rating ON RecenziiAntrenori(Rating);
SELECT * FROM RecenziiAntrenori WHERE Rating >= 4;
CREATE INDEX IDX_PlanExercitii_ExercitiuID ON PlanExercitii(ExercitiuID);
SELECT * FROM PlanExercitii;
CREATE INDEX IDX_Produse_Categorie_NumeProdus ON Produse(Categorie, NumeProdus);
SELECT * FROM Produse WHERE Categorie = 'Supliment' AND NumeProdus LIKE 'P%';