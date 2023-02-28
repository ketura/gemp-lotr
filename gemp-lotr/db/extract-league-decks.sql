


SET @player = 'Sibiatu', @rank=0;

SELECT 
	@player AS player,
	CASE WHEN winner = @player THEN winner_deck_name  ELSE loser_deck_name  END AS deck,
	CONCAT('[url=https://play.lotrtcgpc.net/gemp-lotr/game.html?replayId=', CASE WHEN winner = @player THEN CONCAT(winner, '$', win_recording_id)  ELSE CONCAT(loser, '$', lose_recording_id) END, ']',  @rank:=@rank+1,'[/url] • ') AS URL
FROM game_history gh 
WHERE tournament = 'Constructed - PC Weekend League Movie PC - Serie 1'
	AND start_date > 1677163016000
	AND (winner = @player OR loser = @player)
ORDER BY id;