CREATE DEFINER = `root` @`localhost` PROCEDURE `DLT_DRIVER_TRACKING_DATA_TILL_DATE`(IN DLT_TILL_DATE DATE) BEGIN 
DELETE from 
  trip_driver_tracking 
where 
  DATE(created_date) <= DLT_TILL_DATE;
end
