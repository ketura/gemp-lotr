
SELECT *
FROM league l 
ORDER BY ID DESC;

SELECT *
FROM league_match lm 
ORDER BY ID DESC;

SELECT *
FROM league_participation lp 
ORDER BY ID DESC;

# Constructed - PC Weekend League Fellowship PC
# 545
# 1658030107404

# players by game count in the league
SELECT 
	lp.player_name
	,COUNT(*)
	#*
FROM league l 
INNER JOIN league_participation lp 
	ON lp.league_type = l.`type` 
INNER JOIN league_match lm 
	ON lm.league_type = l.`type` 
WHERE (lm.winner = lp.player_name OR lm.loser = lp.player_name )
	AND l.id = 554
GROUP BY lp.player_name 
ORDER BY Count(*) DESC;

# players who signed up but didn't play any games
SELECT 
	*
FROM league l 
INNER JOIN league_participation lp 
	ON lp.league_type = l.`type` 
LEFT JOIN league_match lm 
	ON lm.league_type = l.`type` 
	AND (lm.winner = lp.player_name OR lm.loser = lp.player_name )
WHERE l.id = 545
	AND lm.id IS NULL;









