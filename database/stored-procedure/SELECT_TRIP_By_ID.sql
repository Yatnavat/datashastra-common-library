CREATE DEFINER = `root` @`localhost` PROCEDURE `SELECT_TRIP_By_ID`(
  IN TRIPID varchar(100)
) begin 
SELECT 
  * 
from 
  trip_driver_tracking 
where 
  trip_id in (TRIPID);
SELECT 
  * 
from 
  trip_item 
where 
  trip_id in (TRIPID);
SELECT 
  * 
from 
  order_tbl 
where 
  id in (
    select 
      order_tbl_id 
    from 
      order_item 
    where 
      id in (
        select 
          order_item_id 
        from 
          trip_item 
        where 
          trip_id in (TRIPID)
      )
  );
SELECT 
  * 
from 
  trip_vehicle 
where 
  trip_id in (TRIPID);
SELECT 
  * 
from 
  trip_orders 
where 
  trip_id in (TRIPID);
SELECT 
  * 
FROM 
  tripdp_files 
WHERE 
  tripdp_id IN (
    select 
      ID 
    from 
      tripdp 
    where 
      trip_id in (TRIPID)
  );
SELECT 
  * 
FROM 
  file_url 
WHERE 
  id IN (
    SELECT 
      files_id 
    FROM 
      tripdp_files 
    WHERE 
      tripdp_id IN (
        select 
          ID 
        from 
          tripdp 
        where 
          trip_id in (TRIPID)
      )
  );
SELECT 
  * 
FROM 
  tripdp 
where 
  trip_id in (TRIPID);
SELECT 
  * 
FROM 
  trip 
where 
  id in (TRIPID);
end
