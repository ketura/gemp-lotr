SELECT *
FROM gemp_db.deck d 
ORDER BY ID DESC 


ALTER TABLE gemp_db.deck 
ADD COLUMN target_format VARCHAR(50) NOT NULL DEFAULT 'Anything Goes' AFTER name;

ALTER TABLE gemp_db.deck 
MODIFY COLUMN name VARCHAR(100) NOT NULL;

ALTER TABLE gemp_db.deck ADD FOREIGN KEY (player_id) REFERENCES gemp_db.player(id);



-- DELETE
-- SELECT *
FROM gemp_db.deck 
-- LEFT JOIN gemp_db.player p 
-- 	ON P.id = d.player_id 
WHERE player_id  IN (1, 2, 7)
ORDER BY D.ID DESC 


SELECT *
FROM gemp_db.player p 
WHERE p.id IN (1, 2, 7)