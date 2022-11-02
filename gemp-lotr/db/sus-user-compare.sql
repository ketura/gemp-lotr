

SELECT *
FROM player 
WHERE name = 'Viggo';


SELECT *
FROM ip_ban ib 
WHERE ip LIKE '172%' OR ip LIKE '76.%'
ORDER BY ip;


DELETE FROM ip_ban 
WHERE ip = 'xxx.xxx.xxx.xxx'


SELECT 
	 p.name AS player_name
	,d.name AS deck_name
	,CONCAT('https://play.lotrtcgpc.net/share/deck?id=', TO_BASE64(CONCAT(p.name, '|', d.name))) AS deck_URL
FROM gemp_db.deck d 
INNER JOIN player p 
	ON p.id = d.player_id 
WHERE p.name = 'MrBoggins';



SELECT *
FROM league l 
LEFT JOIN league_match lm
	ON lm.league_type = l.`type` 
WHERE l.`type` IN ('1407765652259', '1450340395752', '1474559752805', '1479913315776', '1479913760854', '1484058440005', '1512930625768', '1521643737740', '1522860387352', '1524027845252', '1525785033266', '1545794756893', '1545794824826', '1545794920548', '1548253142211', '1548253197688', '1548253239085', '1548253273346', '1551139766129', '1551139874324', '1553030121012', '1553030165378', '1553030189148', '1555424301667', '1555424338300', '1556502558084', '1556502586307', '1558725758698', '1558725784637', '1560625708962', '1560625725719', '1562037952193', '1562985253716', '1562985290903', '1562985355701', '1562985397956', '1562985455194', '1566003828198', '1566355746531', '1566740416843', '1567648687300', '1569715101618', '1569715181068', '1571709904990', '1571828144112', '1572727905649', '1574974319801', '1576079028430', '1577194655985')
	AND (
		(lm.winner IN ('Viggo', 'McLovin', 'JeFsPiCoLi', 'EricStpner', 'EricStoner', 'TaZz_LoCC', 'OG_EXODIA', 'clayton', 'MrBoggins', 'al0hal0ha7', 'JCPotato', 'Nimbus2001', 'OrcSwarm', 'TAZZ')
		AND lm.loser NOT IN ('Viggo', 'McLovin', 'JeFsPiCoLi', 'EricStpner', 'EricStoner', 'TaZz_LoCC', 'OG_EXODIA', 'clayton', 'MrBoggins', 'al0hal0ha7', 'JCPotato', 'Nimbus2001', 'OrcSwarm', 'TAZZ'))
		
		OR 
		
		(lm.winner NOT IN ('Viggo', 'McLovin', 'JeFsPiCoLi', 'EricStpner', 'EricStoner', 'TaZz_LoCC', 'OG_EXODIA', 'clayton', 'MrBoggins', 'al0hal0ha7', 'JCPotato', 'Nimbus2001', 'OrcSwarm', 'TAZZ')
		AND lm.loser IN ('Viggo', 'McLovin', 'JeFsPiCoLi', 'EricStpner', 'EricStoner', 'TaZz_LoCC', 'OG_EXODIA', 'clayton', 'MrBoggins', 'al0hal0ha7', 'JCPotato', 'Nimbus2001', 'OrcSwarm', 'TAZZ'))
	)
	
	
SELECT *
FROM game_history gh 
WHERE winner = 'Viggo'
	OR loser = 'Viggo'
