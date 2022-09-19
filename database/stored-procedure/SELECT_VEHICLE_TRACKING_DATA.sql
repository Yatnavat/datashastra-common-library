CREATE DEFINER = `root` @`%` PROCEDURE `SELECT_VEHICLE_TRACKING_DATA`(IN NoOfRows int) BEGIN 
select 
  * 
FROM 
  vehicle_tracking 
order by 
  vehicle_tracking_id 
limit 
  NoOfRows;
END
