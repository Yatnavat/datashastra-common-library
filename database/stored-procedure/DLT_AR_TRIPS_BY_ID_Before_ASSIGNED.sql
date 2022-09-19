CREATE DEFINER = `root` @`%` PROCEDURE `DLT_AR_TRIPS_BY_ID_Before_ASSIGNED`(
  IN TRIPID_Array varchar(322)
) BEGIN 
DELETE from 
  trip_item 
where 
  trip_id in (
    select 
      trip_id 
    from 
      auto_routing_output 
    where 
      find_in_set(trip_id, TRIPID_Array)
  );
DELETE from 
  order_item 
where 
  id in (
    select 
      order_item_id 
    from 
      trip_item 
    where 
      trip_id in (
        select 
          trip_id 
        from 
          auto_routing_output 
        where 
          find_in_set(trip_id, TRIPID_Array)
      )
  );
DELETE from 
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
          trip_id in (
            select 
              trip_id 
            from 
              auto_routing_output 
            where 
              find_in_set(trip_id, TRIPID_Array)
          )
      )
  );
DELETE from 
  trip_orders 
where 
  trip_id in (
    select 
      trip_id 
    from 
      auto_routing_output 
    where 
      find_in_set(trip_id, TRIPID_Array)
  );
DELETE FROM 
  tripdp 
where 
  trip_id in (
    select 
      trip_id 
    from 
      auto_routing_output 
    where 
      find_in_set(trip_id, TRIPID_Array)
  );
DELETE from 
  trip 
where 
  id in (
    select 
      trip_id 
    from 
      auto_routing_output 
    where 
      find_in_set(trip_id, TRIPID_Array)
  );
DELETE from 
  auto_routing_output 
where 
  find_in_set(trip_id, TRIPID_Array);
END
