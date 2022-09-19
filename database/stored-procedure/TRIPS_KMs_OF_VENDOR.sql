CREATE DEFINER = `root` @`%` PROCEDURE `TRIPS_KMs_OF_VENDOR`(
  IN DCID varchar(60), 
  IN IST_Diff varchar(20)
) BEGIN 
SELECT 
  date(
    addtime(t.trip_start_date, IST_DIFF)
  ) 'TRIP_DATE', 
  if(
    t.driver_id is not null, 
    (
      select 
        concat_ws(' ', first_name, last_name) 
      from 
        user u 
      where 
        u.id in (
          select 
            v.user_id 
          from 
            vendor v 
          where 
            v.id in (
              select 
                d.vendor_id 
              from 
                driver d 
              where 
                d.id = t.driver_id
            )
        )
    ), 
    'No Vendor'
  ) 'Vendor Name', 
  round(
    sum(t.tripkm)
  ) 'Google KMs', 
  round(
    sum(
      t.trip_end_reading - t.trip_start_reading
    )
  ) 'Odometer KMs', 
  round(
    sum(
      t.trip_end_reading - t.trip_start_reading
    )
  ) - round(
    sum(t.tripkm)
  ) 'KMs Variance' 
FROM 
  trip t 
where 
  t.delivery_center_id = DCID 
  and t.trip_status in ('COMPLETETRIP') 
group by 
  1, 
  2;
END
