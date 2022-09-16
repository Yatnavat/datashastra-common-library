CREATE DEFINER = `root` @`%` PROCEDURE `TRIPS_OTD_BY_VENDOR`(
  IN TRIP_DATE date, 
  IN DCID int, 
  IN ClientID int, 
  IN AllowDelayMin int, 
  IN IST_Diff varchar(20), 
  IN DeliveryWindowEndTime time
) BEGIN 
select 
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
    'Not Assigned Yet'
  ) 'VendorName', 
  count(distinct t.id) 'TripsCount', 
  sum(
    (
      select 
        count(tdp1.id) 
      from 
        tripdp tdp1 
      where 
        t.id = tdp1.trip_id
    )
  ) 'NoOfDP', 
  sum(
    if(
      t.delivery_center_id not in (58), 
      (
        select 
          count(*) 
        from 
          tripdp tdp1, 
          delivery_point dp1 
        where 
          tdp1.trip_id = t.id 
          and tdp1.delivery_point_id = dp1.id 
          and dp1.client_id in (ClientID) 
          and time(
            addtime(tdp1.check_in_time, '5:30')
          ) <= TIME(
            date_add(
              dp1.end_allowed_entry_time, interval 30 minute
            )
          )
      ), 
      (
        select 
          count(DISTINCT tdp1.id) 
        from 
          tripdp tdp1, 
          delivery_point dp1 
        where 
          t.id = tdp1.trip_id 
          and tdp1.delivery_point_id = dp1.id 
          and dp1.client_id in (ClientID) 
          and time(
            ADDTIME(tdp1.check_in_time, '5:30')
          ) <= TIME(
            date_add(
              time('12:00'), 
              interval 60 MINUTE
            )
          )
      )
    )
  ) AS `Ontime`, 
  sum(
    if(
      t.delivery_center_id not in (58), 
      (
        select 
          count(*) 
        from 
          tripdp tdp1, 
          delivery_point dp1 
        where 
          tdp1.trip_id = t.id 
          and tdp1.delivery_point_id = dp1.id 
          and dp1.client_id in (ClientID) 
          and time(
            addtime(tdp1.check_in_time, '5:30')
          ) > TIME(
            date_add(
              dp1.end_allowed_entry_time, interval 30 minute
            )
          )
      ), 
      (
        select 
          count(tdp1.id) 
        from 
          tripdp tdp1, 
          delivery_point dp1 
        where 
          t.id = tdp1.trip_id 
          and tdp1.delivery_point_id = dp1.id 
          and dp1.client_id in (ClientID) 
          and time(
            ADDTIME(tdp1.check_in_time, '5:30')
          ) > TIME(
            date_add(
              time('12:00'), 
              interval 60 MINUTE
            )
          )
      )
    )
  ) 'Delay', 
  sum(
    if(
      t.delivery_center_id not in (58), 
      (
        select 
          count(distinct tdp1.id) 
        from 
          tripdp tdp1, 
          delivery_point dp1 
        where 
          tdp1.trip_id = t.id 
          and tdp1.delivery_point_id = dp1.id 
          and dp1.client_id in (ClientID) 
          and tdp1.check_in_time is null
      ), 
      (
        select 
          count(distinct tdp1.id) 
        from 
          tripdp tdp1, 
          delivery_point dp1 
        where 
          t.id = tdp1.trip_id 
          and tdp1.delivery_point_id = dp1.id 
          and dp1.client_id in (ClientID) 
          and tdp1.check_in_time is null
      )
    )
  ) AS `Ongoing` 
from 
  trip t 
where 
  t.delivery_center_id = DCID 
  AND date(
    addtime(t.trip_start_date, IST_Diff)
  ) = TRIP_DATE 
group by 
  1;
END
