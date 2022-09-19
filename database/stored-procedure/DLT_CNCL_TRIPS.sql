CREATE DEFINER = `root` @`%` PROCEDURE `DLT_CNCL_TRIPS`() BEGIN 
delete from 
  trip_driver_tracking 
where 
  trip_id in (
    select 
      id 
    from 
      trip 
    where 
      trip_status = 'CANCELTRIP'
  );
delete from 
  trip_item 
where 
  trip_id in (
    select 
      id 
    from 
      trip 
    where 
      id in (
        select 
          id 
        from 
          trip 
        where 
          trip_status = 'CANCELTRIP'
      )
  );
delete from 
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
          id 
        from 
          trip 
        where 
          id in (
            select 
              id 
            from 
              trip 
            where 
              trip_status = 'CANCELTRIP'
          )
      )
  );
delete from 
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
              id 
            from 
              trip 
            where 
              trip_status = 'CANCELTRIP'
          )
      )
  );
delete from 
  trip_vehicle 
where 
  trip_id in (
    select 
      id 
    from 
      trip 
    where 
      trip_status = 'CANCELTRIP'
  );
delete from 
  trip_orders 
where 
  trip_id in (
    select 
      id 
    from 
      trip 
    where 
      trip_status = 'CANCELTRIP'
  );
delete FROM 
  tripdp_files 
WHERE 
  tripdp_id IN (
    select 
      ID 
    from 
      tripdp 
    where 
      trip_id in (
        select 
          id 
        from 
          trip 
        where 
          trip_status = 'CANCELTRIP'
      )
  );
delete FROM 
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
          trip_id in (
            select 
              id 
            from 
              trip 
            where 
              trip_status = 'CANCELTRIP'
          )
      )
  );
delete from 
  tripdp 
where 
  trip_id in (
    select 
      id 
    from 
      trip 
    where 
      trip_status = 'CANCELTRIP'
  );
delete from 
  trip 
where 
  trip_status = 'CANCELTRIP';
END
