CREATE DEFINER = `root` @`localhost` PROCEDURE `GET_ACTIVE_IN_VEH_AND_TRIP_VEHICLE`() begin 
SELECT 
  * 
from 
  trip_vehicle 
where 
  trip_vehicle.status IN ('ACTIVE') 
  AND vehicle_id IN(
    SELECT 
      ID 
    FROM 
      vehicle 
    WHERE 
      STATUS IN('ACTIVE')
  );
END
