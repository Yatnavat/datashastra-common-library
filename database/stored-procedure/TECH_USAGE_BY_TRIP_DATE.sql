CREATE DEFINER = `root` @`localhost` PROCEDURE `TECH_USAGE_BY_TRIP_DATE`(IN uDATE date) BEGIN 
select 
  IF(
    creation_source IN('OFFLINE_TMS'), 
    date(
      addtime(trip_start_time, '5:30')
    ), 
    date(
      addtime(trip_start_date, '5:30')
    )
  ) Trip_Date, 
  concat_ws(' ', u.first_name, u.last_name) 'Created By', 
  dc.city 'DC City', 
  dc.delivery_center_name 'Delivery Center', 
  if(
    isnull(t.client_id), 
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
    (
      select 
        client_name 
      from 
        client 
      where 
        id in (t.client_id)
    )
  ) 'Client Name', 
  case when creation_source in('OFFLINE_TMS') then 'TRIP MIS' when creation_source in('WEB_PORTAL') then 'ONLINE' when creation_source in('AUTO_ROUTING') then 'IR' when creation_source in('') then 'EMPTY' else 'Other' end 'Source', 
  count(distinct t.id) 'Trips' 
from 
  trip t, 
  user u, 
  delivery_center dc 
where 
  dc.id = t.delivery_center_id 
  and t.created_by = u.id 
  and trip_status not in ('CANCELTRIP') 
  AND date(
    addtime(t.created_date, '5:30')
  )= uDATE 
  AND t.id not in(
    SELECT 
      id 
    from 
      trip 
    where 
      id not in (
        select 
          trip_id 
        from 
          tripdp
      ) 
      and creation_source not in('OFFLINE_TMS')
  ) -- find Null DPs trip
  AND if(
    creation_source not in('OFFLINE_TMS'), 
    t.flag in(1), 
    flag in(1, null)
  ) 
group by 
  1, 
  2, 
  3, 
  4, 
  5, 
  6 
order by 
  2, 
  3, 
  5;
END
