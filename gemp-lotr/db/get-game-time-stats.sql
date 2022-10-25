SELECT 
	format_name 
	,YEAR(from_unixtime(end_date/1000)) AS Year
	,MONTH(from_unixtime(end_date/1000)) AS Month
	,from_unixtime(end_date/1000) AS PlayDate
	,COUNT(*)
	#*
FROM game_history gh 
GROUP BY YEAR(from_unixtime(end_date/1000)), MONTH(from_unixtime(end_date/1000)), format_name