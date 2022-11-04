
SELECT 
	HEX(LEFT(c.collection,1)) AS Version
	,CONV(HEX(SUBSTRING(c.collection,2,3)),16,10) AS currency
	,json_extract(extra_info, '$.currency') AS extra_info_currency
	, CONV(HEX(SUBSTRING(c.collection,2,3)),16,10) + json_extract(extra_info, '$.currency') AS total
	, extra_info
	, CONCAT('{"currency":', CONV(HEX(SUBSTRING(c.collection,2,3)),16,10) + json_extract(extra_info, '$.currency'), '}') AS new_extra_info
	,p.id
FROM collection c 
INNER JOIN player p 
	ON c.player_id = p.id 
WHERE c.type = 'permanent'
	 AND p.name = 'Caram';


UPDATE collection 
SET extra_info = CONCAT('{"currency":', CONV(HEX(SUBSTRING(collection,2,3)),16,10) + json_extract(extra_info, '$.currency'), '}') 
WHERE type = 'permanent'
	AND player_id = 31040


-- 
-- SELECT *
-- FROM transfer t 
-- WHERE player = 'Tunadan'