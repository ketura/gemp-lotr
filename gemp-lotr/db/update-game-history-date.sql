


SELECT *
FROM game_history gh 
ORDER BY ID DESC

ALTER TABLE gemp_db.game_history  
ADD COLUMN start_time DATETIME NOT NULL DEFAULT now() AFTER start_date;

ALTER TABLE gemp_db.game_history  
ADD COLUMN end_time DATETIME NOT NULL DEFAULT now() AFTER end_date;

UPDATE game_history 
SET start_time = from_unixtime(floor(start_date/1000)), end_time = from_unixtime(floor(end_date/1000));

ALTER TABLE gemp_db.game_history 
DROP COLUMN start_date;

ALTER TABLE gemp_db.game_history 
DROP COLUMN end_date;

ALTER TABLE gemp_db.game_history 
RENAME COLUMN start_time TO start_date;

ALTER TABLE gemp_db.game_history 
RENAME COLUMN end_time TO end_date;

ALTER TABLE gemp_db.game_history  ADD INDEX game_history_win_id_index (win_recording_id);
ALTER TABLE gemp_db.game_history  ADD INDEX game_history_lose_id_index (lose_recording_id);



ALTER TABLE gemp_db.game_history  
ADD COLUMN winnerId INT(11) NOT NULL DEFAULT 0 AFTER winner; 

ALTER TABLE gemp_db.game_history  
ADD COLUMN loserId INT(11) NOT NULL DEFAULT 0 AFTER loser; 

UPDATE game_history 
INNER JOIN player P1
	ON P1.name = game_history.winner 
INNER JOIN player P2
	ON P2.name = game_history.loser
SET winnerId = P1.id, loserId = P2.id;

ALTER TABLE gemp_db.game_history 
ADD CONSTRAINT fk_winnerId FOREIGN KEY (winnerId) REFERENCES player(id);

ALTER TABLE gemp_db.game_history 
ADD CONSTRAINT fk_loserId FOREIGN KEY (loserId) REFERENCES player(id);












