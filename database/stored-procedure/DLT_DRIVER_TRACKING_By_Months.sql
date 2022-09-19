CREATE DEFINER = `root` @`localhost` PROCEDURE `DLT_DRIVER_TRACKING_By_Months`(IN mntVal int) BEGIN 
DELETE from 
  trip_driver_tracking 
where 
  DATE(created_date) < subdate(
    curdate(), 
    interval mntVal month
  );
end
