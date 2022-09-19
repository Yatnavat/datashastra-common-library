CREATE DEFINER = `root` @`%` PROCEDURE `TRIPS_SUMMARY_DETAILS_OF_VENDOR`(
  IN DCID varchar(60), 
  IN IST_Diff varchar(20)
) BEGIN 
SELECT 
  t.id 'TripID', 
  date(
    addtime(trip_start_date, IST_DIFF)
  ) 'TRIP_DATE', 
  time(
    addtime(trip_start_time, IST_DIFF)
  ) 'TRIP_Start_TIME', 
  time(
    addtime(trip_end_time, IST_DIFF)
  ) 'TRIP_END_TIME', 
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
  t.trip_status 'TripStatus', 
  if(
    t.id is not null, 
    (
      select 
        count(tdp1.id) 
      from 
        tripdp tdp1 
      where 
        t.id = tdp1.trip_id
    ), 
    false
  ) 'NoOfDP', 
  if(
    t.vehicle_id is not null, 
    (
      select 
        v.registration_number 
      from 
        vehicle v 
      where 
        v.id = t.vehicle_id
    ), 
    '-'
  ) 'vehicleNo', 
  time(
    addtime(t.trip_start_time, IST_DIFF)
  ) 'InTime', 
  time(
    addtime(t.trip_end_time, IST_DIFF)
  ) 'ReturnTime', 
  TIMEDIFF(
    addtime(t.trip_end_time, IST_DIFF), 
    addtime(t.trip_start_time, IST_DIFF)
  ) 'DutyHrs', 
  t.trip_end_reading - t.trip_start_reading 'OdometerKms', 
  t.tripkm 'GoogleKms', 
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
  ) 'vendorName', 
  if(
    t.driver_assistant_id is not null, 
    (
      select 
        concat_ws(' ', first_name, last_name) 
      from 
        user u 
      where 
        u.id in (
          select 
            da.user_id 
          from 
            driver_assistant da 
          where 
            da.id = t.driver_assistant_id
        )
    ), 
    '-'
  ) 'driverAssistantName', 
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
            d.user_id 
          from 
            driver d 
          where 
            d.id = t.driver_id
        )
    ), 
    '-'
  ) 'driverName', 
  if(
    t.driver_id is not null, 
    (
      select 
        phone_number 
      from 
        user u 
      where 
        u.id in (
          select 
            d.user_id 
          from 
            driver d 
          where 
            d.id = t.driver_id
        )
    ), 
    '-'
  ) 'driverPhoneNo' 
FROM 
  trip t 
where 
  t.delivery_center_id = DCID 
group by 
  1, 
  2, 
  3, 
  4, 
  5, 
  6, 
  7, 
  8, 
  9, 
  10, 
  11, 
  12, 
  13, 
  14, 
  15, 
  16, 
  17;
END
