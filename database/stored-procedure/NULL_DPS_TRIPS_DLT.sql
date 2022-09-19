CREATE DEFINER = `root` @`localhost` PROCEDURE `NULL_DPS_TRIPS_DLT`() begin 
DELETE from 
  trip_driver_tracking 
where 
  trip_id in (
    select 
      id 
    from 
      trip 
    where 
      creation_source not in('OFFLINE_TMS') 
      and id not in (
        select 
          trip_id 
        from 
          tripdp 
        where 
          tripdp.trip_id is not null
      )
  );
DELETE from 
  trip_item 
where 
  trip_id in (
    select 
      id 
    from 
      trip 
    where 
      creation_source not in('OFFLINE_TMS') 
      and id not in (
        select 
          trip_id 
        from 
          tripdp 
        where 
          tripdp.trip_id is not null
      )
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
          id 
        from 
          trip 
        where 
          creation_source not in('OFFLINE_TMS') 
          and id not in (
            select 
              trip_id 
            from 
              tripdp 
            where 
              tripdp.trip_id is not null
          )
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
              id 
            from 
              trip 
            where 
              creation_source not in('OFFLINE_TMS') 
              and id not in (
                select 
                  trip_id 
                from 
                  tripdp 
                where 
                  tripdp.trip_id is not null
              )
          )
      )
  );
DELETE from 
  trip_vehicle 
where 
  trip_id in (
    select 
      id 
    from 
      trip 
    where 
      creation_source not in('OFFLINE_TMS') 
      and id not in (
        select 
          trip_id 
        from 
          tripdp 
        where 
          tripdp.trip_id is not null
      )
  );
DELETE from 
  trip_orders 
where 
  trip_id in (
    select 
      id 
    from 
      trip 
    where 
      creation_source not in('OFFLINE_TMS') 
      and id not in (
        select 
          trip_id 
        from 
          tripdp 
        where 
          tripdp.trip_id is not null
      )
  );
DELETE FROM 
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
          creation_source not in('OFFLINE_TMS') 
          and id not in (
            select 
              trip_id 
            from 
              tripdp 
            where 
              tripdp.trip_id is not null
          )
      )
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
          trip_id in (
            select 
              id 
            from 
              trip 
            where 
              id not in (
                select 
                  trip_id 
                from 
                  tripdp 
                where 
                  tripdp.trip_id is not null
              )
          )
      )
  );
DELETE from 
  trip 
where 
  creation_source not in('OFFLINE_TMS') 
  and id not in (
    select 
      trip_id 
    from 
      tripdp 
    where 
      tripdp.trip_id is not null
  );
end
