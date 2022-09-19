CREATE DEFINER = `root` @`%` PROCEDURE `SELECT_TRIPS_BY_DC_DATE`(IN DCID int, IN TRIP_DATE date) BEGIN 
select 
  * 
from 
  trip_driver_tracking 
where 
  trip_id in (
    select 
      id 
    from 
      trip 
    where 
      delivery_center_id = DCID 
      and date(created_date) = TRIP_DATE
  );
select 
  * 
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
          delivery_center_id = DCID 
          and date(created_date) = TRIP_DATE
      )
  );
select 
  * 
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
          id in (
            select 
              id 
            from 
              trip 
            where 
              delivery_center_id = DCID 
              and date(created_date) = TRIP_DATE
          )
      )
  );
select 
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
          trip_id in (
            select 
              id 
            from 
              trip 
            where 
              delivery_center_id = DCID 
              and date(created_date) = TRIP_DATE
          )
      )
  );
select 
  * 
from 
  trip_vehicle 
where 
  trip_id in (
    select 
      id 
    from 
      trip 
    where 
      delivery_center_id = DCID 
      and date(created_date) = TRIP_DATE
  );
select 
  * 
from 
  trip_orders 
where 
  trip_id in (
    select 
      id 
    from 
      trip 
    where 
      delivery_center_id = DCID 
      and date(created_date) = TRIP_DATE
  );
select 
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
      trip_id in (
        select 
          id 
        from 
          trip 
        where 
          delivery_center_id = DCID 
          and date(created_date) = TRIP_DATE
      )
  );
select 
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
          trip_id in (
            select 
              id 
            from 
              trip 
            where 
              delivery_center_id = DCID 
              and date(created_date) = TRIP_DATE
          )
      )
  );
select 
  * 
from 
  tripdp 
where 
  trip_id in (
    select 
      id 
    from 
      trip 
    where 
      delivery_center_id = DCID 
      and date(created_date) = TRIP_DATE
  );
select 
  * 
from 
  trip 
where 
  delivery_center_id = DCID 
  and date(created_date) = TRIP_DATE;
END
