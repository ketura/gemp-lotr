ALTER TABLE gemp_db.player  MODIFY COLUMN name VARCHAR(30);
-- ALTER TABLE gemp_db.ProcessedReplays  MODIFY COLUMN Player1 VARCHAR(30);
-- ALTER TABLE gemp_db.ProcessedReplays  MODIFY COLUMN Player2 VARCHAR(30);
ALTER TABLE gemp_db.ignores  MODIFY COLUMN playerName VARCHAR(30);
ALTER TABLE gemp_db.ignores  MODIFY COLUMN ignoredName VARCHAR(30);
ALTER TABLE gemp_db.tournament_player  MODIFY COLUMN player VARCHAR(30);


