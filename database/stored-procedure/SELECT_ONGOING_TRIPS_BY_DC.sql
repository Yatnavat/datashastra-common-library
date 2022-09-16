CREATE DEFINER = `root` @`%` PROCEDURE `SELECT_ONGOING_TRIPS_BY_DC`(IN DCID int) BEGIN 
select 
  * 
from 
  trip 
where 
  trip_status NOT IN ('COMPLETETRIP', 'CANCELTRIP') 
  AND delivery_center_id = DCID;
select 
  * 
from 
  trip_vehicle 
where 
  status NOT IN ('COMPLETETRIP', 'CANCELTRIP') 
  AND vehicle_id IN (
    SELECT 
      ID 
    FROM 
      vehicle 
    WHERE 
      vendor_id IN (
        SELECT 
          vendor_id 
        FROM 
          vendor_delivery_centers 
        WHERE 
          delivery_centers_id = DCID
      )
  );
SELECT 
  * 
FROM 
  vehicle 
WHERE 
  status != 'ACTIVE' 
  AND vendor_id IN (
    SELECT 
      vendor_id 
    FROM 
      vendor_delivery_centers 
    WHERE 
      delivery_centers_id = DCID
  );
END
