
SELECT * FROM gemp_db.deck d 
INNER JOIN gemp_db.player p 
	ON p.id = d.player_id 
WHERE p.name = 'asdf'

ALTER TABLE gemp_db.deck 
ADD COLUMN notes VARCHAR(5000) NOT NULL DEFAULT '' AFTER contents;
