CREATE DEFINER = `root` @`localhost` PROCEDURE `REVERT_CANCEL_TRIPS_BY_DC_DATE`(
  IN DCID int, 
  Trip_Date date, 
  IST_DIFF VARCHAR(30)
) BEGIN 
UPDATE 
  vehicle 
SET 
  status = 'CHECKINTODC' 
WHERE 
  id IN(
    SELECT 
      vehicle_id 
    FROM 
      trip 
    WHERE 
      bckpTripStatus IN(
        'DCCHECKIN', 'LOADINGSTART', 'LOADING'
      ) 
      AND trip_status IN ('CANCELTRIP') 
      AND delivery_center_id = DCID 
      AND date(
        addtime(trip_start_date, IST_DIFF)
      )= Trip_Date
  );
UPDATE 
  vehicle 
SET 
  status = 'ALLOCATEDVEHICLE' 
WHERE 
  id IN(
    SELECT 
      vehicle_id 
    FROM 
      trip 
    WHERE 
      bckpTripStatus IN(
        'COLLECTDOCUMENT', 'TRIPREADING', 
        'TRIPSTART'
      ) 
      AND trip_status IN ('CANCELTRIP') 
      AND delivery_center_id = DCID 
      AND date(
        addtime(trip_start_date, IST_DIFF)
      )= Trip_Date
  );
UPDATE 
  vehicle 
SET 
  status = 'CHECKINTODP' 
WHERE 
  id IN(
    SELECT 
      vehicle_id 
    FROM 
      trip 
    WHERE 
      bckpTripStatus IN(
        'DPCHECKIN', 'UNLOADINGCOMPLETE', 
        'COLLECTENDTRIPDETAILS', 'TRIPSUMMARY'
      ) 
      AND trip_status IN ('CANCELTRIP') 
      AND delivery_center_id = DCID 
      AND date(
        addtime(trip_start_date, IST_DIFF)
      )= Trip_Date
  );
UPDATE 
  trip_vehicle 
SET 
  status = 'ACTIVE' 
WHERE 
  trip_id IN (
    SELECT 
      ID 
    FROM 
      trip 
    where 
      trip_status IN ('CANCELTRIP') 
      AND delivery_center_id = DCID 
      AND date(
        addtime(trip_start_date, IST_DIFF)
      )= Trip_Date
  );
UPDATE 
  trip 
SET 
  trip_status = bckpTripStatus 
where 
  trip_status IN ('CANCELTRIP') 
  AND delivery_center_id = DCID 
  AND date(
    addtime(trip_start_date, IST_DIFF)
  )= Trip_Date;
END
