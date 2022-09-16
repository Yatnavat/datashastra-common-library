CREATE DEFINER = `root` @`localhost` PROCEDURE `RELEASE_NOT_ACCEPTED_VEHICLES_FROM_TRIP`() BEGIN 
UPDATE 
  trip 
SET 
  bckpTripStatus = trip_status, 
  vehicle_id = NULL 
WHERE 
  vehicle_id IS NOT NULL 
  AND driver_id IS NULL 
  AND timestamp (
    if(
      updated_date IS NULL, created_date, 
      updated_date
    )
  ) < timestamp(NOW() - interval 4 hour);
END
