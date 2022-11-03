

ALTER TABLE collection ADD CONSTRAINT fk_collection_player_id FOREIGN KEY (player_id) REFERENCES player(id);

ALTER TABLE collection ADD `extra_info` VARCHAR(5000) NULL;

ALTER TABLE collection ADD CONSTRAINT uq_collection_player_type UNIQUE (player_id, type);

ALTER TABLE collection MODIFY COLUMN collection mediumblob NULL;


CREATE TABLE `collection_entries` (
  `collection_id` int(11) NOT NULL,
  `quantity` int(2) DEFAULT 0,
  `product_type` varchar(50) NOT NULL,
  `product_variant` varchar(50) DEFAULT NULL,
  `product` varchar(50) NOT NULL,
  `source` varchar(50) NOT NULL,
  `created_date` datetime DEFAULT current_timestamp(),
  `modified_date` datetime DEFAULT NULL ON UPDATE current_timestamp(),
  `notes` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`collection_id`, `product`),
  CONSTRAINT `collection_entries_ibfk_1` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`)
) ;


SELECT *
FROM collection c 
WHERE player_id = 31040;

SELECT *
FROM collection_entries 
WHERE collection_id = 59510;

SELECT *
FROM collection_entries 
WHERE collection_id = 64643;

SELECT *
FROM player p
LEFT JOIN collection c 
	ON c.player_id = p.id 
WHERE c.id IS NULL
ORDER BY p.name 

SELECT count(*)
FROM collection_entries
GROUP BY collection_id 

SELECT TYPE 
FROM collection c
GROUP BY type

SELECT * 
FROM transfer t 
ORDER BY ID DESC



