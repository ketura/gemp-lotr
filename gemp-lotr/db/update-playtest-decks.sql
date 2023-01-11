



SELECT *
FROM deck d 
WHERE contents RLIKE '(7|8)\\d_'
	
	
SELECT REGEXP_REPLACE(REGEXP_REPLACE('7_94,7_94,87_13,81_30,72_32,51_80,1_83,71_240,71_240,71_240,71_240,72_32', '7(\\d)_', '5\\1_'), '8(\\d)_', '6\\1_')



-- UPDATE deck 
-- SET contents = REGEXP_REPLACE(REGEXP_REPLACE(contents, '7(\\d)_', '5\\1_'), '8(\\d)_', '6\\1_')
-- WHERE contents RLIKE '(7|8)_'
