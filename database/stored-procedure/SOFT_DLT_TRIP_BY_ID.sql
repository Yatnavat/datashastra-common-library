CREATE DEFINER = `root` @`localhost` PROCEDURE `SOFT_DLT_TRIP_BY_ID`(IN tripID int) BEGIN 
UPDATE 
  trip 
SET 
  bckpTripStatus = trip_status, 
  flag = '0' 
where 
  id IN (tripID);
UPDATE 
  trip_vehicle 
SET 
  flag = '0' 
where 
  trip_id IN (tripID);
END
