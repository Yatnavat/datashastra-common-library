CREATE DEFINER = `root` @`localhost` PROCEDURE `RELEASE_WRONG_DCCHECKIN_ALLOCATED_VEHICLES`() BEGIN 
UPDATE 
  vehicle 
SET 
  status = 'ACTIVE' 
WHERE 
  status IN (
    'ALLOCATEDVEHICLE', 'CHECKINTODC'
  ) 
  AND ID IN(
    SELECT 
      vehicle_id 
    FROM 
      trip_vehicle 
    WHERE 
      status IN('ACTIVE') 
      AND trip_id IS NULL 
      AND timestamp (
        if(
          updated_date IS NULL, created_date, 
          updated_date
        )
      ) < timestamp(NOW() - interval 4 hour)
  );
DELETE FROM 
  trip_vehicle 
WHERE 
  status IN('ACTIVE') 
  AND trip_id IS NULL 
  AND timestamp (
    if(
      updated_date IS NULL, created_date, 
      updated_date
    )
  ) < timestamp(NOW() - interval 4 hour);
END
