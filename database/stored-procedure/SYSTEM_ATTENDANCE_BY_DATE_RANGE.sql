CREATE DEFINER = `root` @`localhost` PROCEDURE `SYSTEM_ATTENDANCE_BY_DATE_RANGE`(
  IN FROM_DATE DATE, TO_DATE DATE, IST_DIFF TIME
) BEGIN 
select 
  concat_ws(' ', u.first_name, u.last_name) 'Created By', 
  dc.city 'DC City', 
  dc.delivery_center_name 'Delivery Center', 
  if(
    isnull(t.client_id), 
    
    /*  true condition*/
    (
      select 
        client_name 
      from 
        client 
      where 
        id in(
          select 
            client_id 
          from 
            delivery_point 
          where 
            id in(
              select 
                delivery_point_id 
              from 
                tripdp 
              where 
                trip_id in(t.id)
            )
        )
    ), 
    
    /* False condition */
    (
      select 
        client_name 
      from 
        client 
      where 
        id in (t.client_id)
    )
  ) 'Client_Name', 
  -- end of If
  max(
    IF(
      creation_source IN('OFFLINE_TMS'), 
      date(
        addtime(
          addtime (
            timestamp (
              date(trip_start_date), 
              '00:00:00'
            ), 
            TIME(trip_start_time)
          ), 
          IST_DIFF
        )
      ), 
      date(
        addtime(trip_start_date, IST_DIFF)
      )
    )
  ) Trip_Date, 
  count(distinct t.id) 'Trips', 
  COUNT(
    DISTINCT IF(
      creation_source IN('OFFLINE_TMS'), 
      date(
        addtime(
          addtime (
            timestamp (
              date(trip_start_date), 
              '00:00:00'
            ), 
            TIME(trip_start_time)
          ), 
          IST_DIFF
        )
      ), 
      date(
        addtime(trip_start_date, IST_DIFF)
      )
    )
  ) 'No of Days' 
from 
  delivery_center dc, 
  trip t, 
  trip_vehicle tv, 
  user u 
where 
  t.delivery_center_id = dc.id 
  and t.id = tv.trip_id 
  and t.created_by = u.id 
  AND IF(
    creation_source IN('OFFLINE_TMS'), 
    date(
      addtime(
        addtime (
          timestamp (
            date(trip_start_date), 
            '00:00:00'
          ), 
          TIME(trip_start_time)
        ), 
        IST_DIFF
      )
    ), 
    date(
      addtime(trip_start_date, IST_DIFF)
    )
  ) BETWEEN FROM_DATE 
  AND TO_DATE 
group by 
  1, 
  2, 
  3, 
  4;
END
