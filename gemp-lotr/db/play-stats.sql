SELECT 
	 G.DateOnly AS Date
	,G.game_count
	,P2.player_count
FROM 
(
	SELECT 
		 COUNT(*) AS game_count
		,DATE(start_date) AS DateOnly
	FROM game_history gh 
	GROUP BY DateOnly
) G

INNER JOIN 
(
	SELECT 
		 COUNT(*) AS player_count
		,P.DateOnly
	FROM (
		SELECT 
			 winner AS player
			 ,DATE(start_date) AS DateOnly
		FROM game_history gh 
		
		UNION
		
		SELECT 
			 loser AS player
			 ,DATE(start_date) AS DateOnly
		FROM game_history gh 
	) P
	GROUP BY DateOnly
) P2
ON P2.DateOnly = G.DateOnly
ORDER BY G.DateOnly
