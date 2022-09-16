CREATE DEFINER = `root` @`%` PROCEDURE `TRIPS_OTD_OF_CLIENT`(
  IN DCID int, 
  IN AllowDelayMin int, 
  IN IST_Diff varchar(20), 
  IN DeliveryWindowEndTime time
) BEGIN 
select 
  date(
    addtime(t.trip_start_date, IST_Diff)
  ) 'TRIP_DATE', 
  (
    select 
      distinct client_name 
    from 
      client 
    where 
      id in (
        select 
          distinct client_id 
        from 
          tripdp tdp1, 
          delivery_point dp1 
        where 
          tdp1.trip_id = t.id 
          and tdp1.delivery_point_id = dp1.id 
          and t.delivery_center_id = DCID
      )
  ) 'ClientName', 
  COUNT(DISTINCT t.ID) 'Trips', 
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
  ) AS `Ongoing` 
from 
  trip t 
where 
  t.delivery_center_id = DCID 
group by 
  1, 
  2;
END
