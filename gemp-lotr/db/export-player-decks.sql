SELECT 
	 p.name AS player_name
	,d.name AS deck_name
	,CONCAT('https://play.lotrtcgpc.net/share/deck?id=',TO_BASE64(CONCAT(REPLACE(p.name, '_', '%5F'), '|', d.name))) AS DeckURL
FROM gemp_db.deck d 
INNER JOIN player p 
	ON p.id = d.player_id 
WHERE p.name = 'met'


