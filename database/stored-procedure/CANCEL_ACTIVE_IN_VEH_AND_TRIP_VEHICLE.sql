CREATE DEFINER = `root` @`localhost` PROCEDURE `CANCEL_ACTIVE_IN_VEH_AND_TRIP_VEHICLE`() begin 
UPDATE 
  trip_vehicle 
SET 
  status = 'CANCELTRIP' 
where 
  status IN ('ACTIVE') 
  AND vehicle_id IN(
    SELECT 
      ID 
    FROM 
      vehicle 
    WHERE 
      STATUS IN('ACTIVE')
  );
END
