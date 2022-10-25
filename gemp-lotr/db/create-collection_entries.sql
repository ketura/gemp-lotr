

ALTER TABLE collection ADD CONSTRAINT fk_collection_player_id FOREIGN KEY (player_id) REFERENCES player(id);


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