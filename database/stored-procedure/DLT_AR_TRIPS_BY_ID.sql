CREATE DEFINER = `root` @`localhost` PROCEDURE `DLT_AR_TRIPS_BY_ID`(
  IN TRIPID_Array varchar(10000)
) BEGIN 
delete from 
  trip_item 
where 
  find_in_set(trip_id, TRIPID_Array);
delete from 
  trip_orders 
where 
  find_in_set(trip_id, TRIPID_Array);
delete from 
  order_item 
where 
  order_tbl_id in(
    SELECT 
      id 
    from 
      order_tbl 
    where 
      id in(
        SELECT 
          orders_id 
        from 
          trip_orders 
        where 
          find_in_set(trip_id, TRIPID_Array)
      )
  );
delete from 
  order_tbl 
where 
  id not in(
    select 
      orders_id 
    from 
      trip_orders
  ) 
  and id not in (
    SELECT 
      order_tbl_id 
    from 
      order_item
  );
delete FROM 
  tripdp 
where 
  find_in_set(trip_id, TRIPID_Array);
delete from 
  auto_routing_output 
where 
  find_in_set(trip_id, TRIPID_Array);
delete from 
  route_delivery_points 
where 
  route_id in(
    select 
      route_id 
    from 
      trip 
    where 
      find_in_set(id, TRIPID_Array)
  );
delete from 
  route_path 
where 
  route_id in(
    select 
      route_id 
    from 
      trip 
    where 
      find_in_set(id, TRIPID_Array)
  );
DELETE from 
  trip_driver_tracking 
where 
  find_in_set(trip_id, TRIPID_Array);
delete from 
  trip 
where 
  find_in_set(id, TRIPID_Array);
END
