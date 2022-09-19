CREATE DEFINER = `root` @`localhost` PROCEDURE `GET_VEHICLE_IN_DEADLOCK`() begin 
SELECT 
  * 
FROM 
  vehicle 
WHERE 
  STATUS NOT IN(
    'ACTIVE', 'UNAPPROVED', 'DEACTIVATE'
  ) 
  and id not in(
    SELECT 
      vehicle_ID 
    from 
      trip_vehicle 
    where 
      trip_vehicle.status IN ('ACTIVE')
  );
END
