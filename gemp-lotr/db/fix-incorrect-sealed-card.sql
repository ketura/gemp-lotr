

	
	
SELECT *
FROM collection c
INNER JOIN collection_entries ce 
	ON ce.collection_id = c.id
INNER JOIN player p 
	ON c.player_id = p.id
LEFT JOIN 
(
	SELECT collection_id, quantity
	FROM collection_entries ce2
	WHERE ce2.product = '101_42'
) X
	ON X.collection_id = c.id
WHERE c.type = '1673497069403'
	AND product = '101_42'
	AND X.collection_id IS NOT NULL
ORDER BY p.name

SELECT *
FROM transfer t 
WHERE player = 'elfwarrior'
ORDER BY id DESC 




SELECT source
FROM collection_entries ce 
GROUP BY source


INSERT INTO gemp_db.collection_entries
(collection_id, quantity, product_type, product_variant, product, source)
VALUES
(131041, 0, 'CARD', NULL, '101_42', 'Manual admin action'),
(131049, 0, 'CARD', NULL, '101_42', 'Manual admin action'),
(131111, 0, 'CARD', NULL, '101_42', 'Manual admin action');



UPDATE collection_entries ce
INNER JOIN collection c
	ON ce.collection_id = c.id
SET ce.quantity = ce.quantity + 1
WHERE c.type = '1673497069403'
	AND c.id = 131408
	AND ce.product = '101_42'





SELECT *
FROM league l 
ORDER BY id DESC