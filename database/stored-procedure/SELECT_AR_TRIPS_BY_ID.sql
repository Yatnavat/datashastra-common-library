CREATE DEFINER = `root` @`%` PROCEDURE `SELECT_AR_TRIPS_BY_ID`(
  IN TRIPID_Array varchar(322)
) BEGIN 
SELECT 
  * 
from 
  trip_item 
where 
  find_in_set(trip_id, TRIPID_Array);
SELECT 
  * 
from 
  order_item 
where 
  order_tbl_id in (
    SELECT 
      order_tbl.id 
    from 
      order_tbl 
    where 
      order_tbl.id in (
        SELECT 
          orders_id 
        from 
          trip_orders 
        where 
          find_in_set(trip_id, TRIPID_Array)
      )
  );
SELECT 
  * 
from 
  order_tbl 
where 
  id in (
    SELECT 
      orders_id 
    from 
      trip_orders 
    where 
      find_in_set(trip_id, TRIPID_Array)
  );
SELECT 
  * 
from 
  trip_orders 
where 
  find_in_set(trip_id, TRIPID_Array);
SELECT 
  * 
FROM 
  tripdp 
where 
  find_in_set(trip_id, TRIPID_Array);
SELECT 
  * 
from 
  trip 
where 
  find_in_set(id, TRIPID_Array);
SELECT 
  * 
from 
  auto_routing_output 
where 
  find_in_set(trip_id, TRIPID_Array);
END
