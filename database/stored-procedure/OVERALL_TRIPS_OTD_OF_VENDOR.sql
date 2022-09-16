CREATE DEFINER = `root` @`localhost` PROCEDURE `OVERALL_TRIPS_OTD_OF_VENDOR`(
  IN AllowDelayMin int, 
  IN IST_Diff varchar(20), 
  IN DeliveryWindowEndTime time
) BEGIN 
select 
  if(
    t.trip_status in(
      'DCCHECKIN', 'LOADINGSTART', 'LOADING', 
      'COLLECTDOCUMENT', 'TRIPREADING'
    ), 
    date(
      addtime(t.trip_start_date, '5:30')
    ), 
    date(
      addtime(t.trip_start_time, '5:30')
    )
  ) Trip_Date, 
  dc.city 'DC City', 
  dc.delivery_center_name 'Delivery Center', 
  t.creation_source 'Source', 
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
      t.creation_source in('OFFLINE_TMS'), 
      0, 
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
            and dp1.client_id not in (7) 
            and time(
              addtime(tdp1.check_in_time, IST_Diff)
            ) <= TIME(
              date_add(
                if(
                  isnull(dp1.end_allowed_entry_time), 
                  DeliveryWindowEndTime, 
                  dp1.end_allowed_entry_time
                ), 
                interval AllowDelayMin minute
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
            and dp1.client_id in (7) 
            and time(
              ADDTIME(tdp1.check_in_time, IST_Diff)
            ) <= TIME(
              date_add(
                time(DeliveryWindowEndTime), 
                interval AllowDelayMin MINUTE
              )
            )
        )
      )
    )
  ) AS `Ontime`, 
  sum(
    if(
      t.creation_source in('OFFLINE_TMS'), 
      0, 
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
            and dp1.client_id not in (7) 
            and time(
              addtime(tdp1.check_in_time, IST_Diff)
            ) > TIME(
              date_add(
                if(
                  isnull(dp1.end_allowed_entry_time), 
                  DeliveryWindowEndTime, 
                  dp1.end_allowed_entry_time
                ), 
                interval AllowDelayMin minute
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
            and dp1.client_id in (7) 
            and time(
              ADDTIME(tdp1.check_in_time, IST_Diff)
            ) > TIME(
              date_add(
                time(DeliveryWindowEndTime), 
                interval AllowDelayMin MINUTE
              )
            )
        )
      )
    )
  ) 'Delay', 
  sum(
    if(
      t.creation_source in('OFFLINE_TMS'), 
      0, 
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
            and dp1.client_id NOT in (7) 
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
            and dp1.client_id in (7) 
            and tdp1.check_in_time is null
        )
      )
    )
  ) AS `Ongoing` 
from 
  trip t, 
  delivery_center dc 
where 
  t.delivery_center_id = dc.id 
  and if (
    t.creation_source in('OFFLINE_TMS'), 
    t.flag in(1), 
    t.flag in(null, 1)
  ) 
group by 
  1, 
  2, 
  3, 
  4, 
  5;
END
