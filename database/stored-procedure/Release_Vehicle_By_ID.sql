CREATE DEFINER = `root` @`localhost` PROCEDURE `Release_Vehicle_By_ID`(IN VEH_ID int) BEGIN declare flag int;
UPDATE 
  vehicle 
SET 
  status = 'ACTIVE' 
WHERE 
  status not in ('ACTIVE') 
  and id = VEH_ID 
  and id in (
    select 
      vehicle_id 
    from 
      trip_vehicle 
    where 
      vehicle_id = VEH_ID 
      and trip_id is null 
      AND trip_vehicle.status IN ('ACTIVE')
  );
update 
  trip 
set 
  vehicle_id = null 
where 
  driver_id is null 
  and trip_status not in('COMPLETETRIP') 
  AND vehicle_id = VEH_ID;
delete from 
  trip_vehicle 
where 
  status IN ('ACTIVE') 
  AND trip_id IS NULL 
  AND vehicle_id = VEH_ID;
DELETE FROM 
  trip_vehicle 
WHERE 
  status IN ('ACTIVE') 
  AND trip_id IS NULL 
  AND vehicle_id IN(
    SELECT 
      ID 
    FROM 
      vehicle 
    WHERE 
      STATUS NOT IN(
        'ALLOCATEDVEHICLE', 'CHECKINTODC'
      ) 
      AND ID = VEH_ID
  );
UPDATE 
  vehicle 
SET 
  status = 'ACTIVE' 
WHERE 
  STATUS IN(
    'ALLOCATEDVEHICLE', 'CHECKINTODC'
  ) 
  AND ID = VEH_ID 
  AND ID IN(
    SELECT 
      vehicle_id 
    FROM 
      trip_vehicle 
    WHERE 
      status NOT IN ('ACTIVE') 
      AND trip_id IS NULL
  );
END
