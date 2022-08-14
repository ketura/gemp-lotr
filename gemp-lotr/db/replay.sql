DROP TABLE IF EXISTS gemp_db.ReplayCards;
DROP TABLE IF EXISTS gemp_db.ProcessedReplays;
CREATE TABLE gemp_db.ProcessedReplays (
	ID INT auto_increment NOT NULL,
	FormatName varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
	Player1 varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
	Player2 varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
	Player1DeckSize INT NOT NULL,
	Player2DeckSize INT NOT NULL,	
	StartingGameplayCardID INT NOT NULL,
	Player1Bid INT NOT NULL,
	Player2Bid INT NOT NULL,
	WentFirst varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
	WinnerReplayID varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
 	LoserReplayID varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
	StartTime DATETIME NOT NULL,
	EndTime DATETIME NOT NULL,
	Duration varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
	Conceded BIT NOT NULL,
	Winner varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
	WinReason varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
	LoseReason varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,

	PRIMARY KEY (ID)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_bin;




CREATE TABLE gemp_db.ReplayCards (
	ID INT auto_increment NOT NULL,
	ProcessedReplayID INT NOT NULL,
	Zone varchar(45) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
	BlueprintID varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
	
	PRIMARY KEY (ID),
	FOREIGN KEY (ProcessedReplayID) REFERENCES ProcessedReplays(ID)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
COLLATE=utf8_bin;



SELECT COUNT(*) FROM gemp_db.ProcessedReplays pr ;

SELECT COUNT(*) FROM gemp_db.ReplayCards rc ;











SELECT * FROM gemp_db.ProcessedReplays pr 

SELECT * FROM gemp_db.ReplayCards rc ;

(SELECT 
	COUNT(*) AS Count
	, FormatName
FROM ProcessedReplays PR
GROUP BY FormatName
ORDER BY FormatName)

UNION ALL

(SELECT 
	COUNT(*) AS Count
	, 'Total' AS FormatName
FROM ProcessedReplays PR)

UNION ALL

(SELECT 
	COUNT(*) AS Count
	, 'Conceded' AS FormatName
FROM ProcessedReplays PR
WHERE Conceded = 1)





;



SELECT 
	RC.BlueprintID 
	, PR.FormatName 
	, COUNT(BlueprintID)
FROM ReplayCards RC
INNER JOIN ProcessedReplays PR
	ON PR.ID = RC.ProcessedReplayID 
WHERE Zone IN ('Deck1', 'Deck2')
GROUP BY BlueprintID, FormatName