CREATE DEFINER = `root` @`%` PROCEDURE `DLT_TRIPS_BY_ID`(
  IN TRIPID_Array varchar(10000)
) BEGIN 
DELETE from 
  trip_driver_tracking 
where 
  trip_id in (TRIPID_Array);
DELETE from 
  trip_item 
where 
  trip_id in (TRIPID_Array);
DELETE from 
  order_tbl 
where 
  id not in (
    SELECT 
      order_tbl_id 
    from 
      order_item
  );
DELETE from 
  trip_vehicle 
where 
  trip_id in (TRIPID_Array);
DELETE from 
  trip_orders 
where 
  trip_id in (TRIPID_Array);
DELETE FROM 
  tripdp_files 
WHERE 
  tripdp_id IN (
    select 
      ID 
    from 
      tripdp 
    where 
      trip_id in (TRIPID_Array)
  );
DELETE FROM 
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
          trip_id in (TRIPID_Array)
      )
  );
DELETE FROM 
  tripdp 
where 
  trip_id in (TRIPID_Array);
DELETE from 
  route_delivery_points 
where 
  route_id in(
    select 
      route_id 
    from 
      trip 
    where 
      id in(TRIPID_Array)
  );
DELETE from 
  route_path 
where 
  route_id in(
    select 
      route_id 
    from 
      trip 
    where 
      id in(TRIPID_Array)
  );
DELETE from 
  route 
where 
  id not in(
    select 
      route_id 
    from 
      route_delivery_points
  );
DELETE FROM 
  trip 
where 
  id in (TRIPID_Array);
END
