


SET @player = 'CoS', @rank=0;

SELECT 
	 @player AS player
	,CASE WHEN winner = @player THEN winner_deck_name  ELSE loser_deck_name  END AS deck_name
	,CONCAT('https://play.lotrtcgpc.net/share/deck?id=',
		CASE 
			WHEN winner = @player THEN TO_BASE64(CONCAT(REPLACE(winner, '_', '%5F'), '|', winner_deck_name))
			ELSE TO_BASE64(CONCAT(REPLACE(loser, '_', '%5F'), '|', loser_deck_name)) 
		END) AS DeckURL
	,CONCAT('[url=https://play.lotrtcgpc.net/gemp-lotr/game.html?replayId=',
		CASE 
			WHEN winner = @player THEN CONCAT(REPLACE(winner, '_', '%5F'), '$', win_recording_id)  
			ELSE CONCAT(REPLACE(loser, '_', '%5F'), '$', lose_recording_id) 
		END, ']',  @rank:=@rank+1,'[/url] • ') AS URL
FROM game_history gh 
INNER JOIN player p 
	ON p.name = @player
INNER JOIN deck d 
	ON d.player_id = p.id 
	AND (d.name = winner_deck_name OR d.name = loser_deck_name) 
WHERE tournament = 'March Weekend Qualifier - PC-Fellowship - Serie 1'
	AND start_date > '2023-03-22'
	AND (winner = @player OR loser = @player)
ORDER BY gh.id;


