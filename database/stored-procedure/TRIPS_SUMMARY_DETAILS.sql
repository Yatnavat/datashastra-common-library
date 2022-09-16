CREATE DEFINER = `root` @`%` PROCEDURE `TRIPS_SUMMARY_DETAILS`(
  IN DCID varchar(60), 
  IN AllowDelayMin int, 
  IN IST_Diff varchar(20), 
  IN DeliveryWindowEndTime time
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
  dp.delivery_point_name 'Delivery_point_Nmae', 
  time(
    addtime(tdp.check_in_time, IST_DIFF)
  ) 'checkInTime', 
  time(
    addtime(tdp.check_out_time, IST_DIFF)
  ) 'checkOutTime', 
  tdp.delivery_process_status 'DP_Status', 
  case when dp.client_id NOT IN (7) then (
    if(
      time(
        addtime(tdp.check_in_time, IST_DIFF)
      ) <= TIME(
        date_add(
          dp.end_allowed_entry_time, interval AllowDelayMin minute
        )
      ), 
      'On time', 
      if(
        tdp.delivery_process_status not in ('CHECKIN', 'DELIVERED'), 
        'Ongoing', 
        'Delay'
      )
    )
  ) when dp.client_id in (7) then (
    if(
      time(
        ADDTIME(tdp.check_in_time, IST_Diff)
      ) <= TIME(
        date_add(
          time(DeliveryWindowEndTime), 
          interval AllowDelayMin MINUTE
        )
      ), 
      'On Time', 
      if(
        tdp.delivery_process_status not in ('CHECKIN', 'DELIVERED'), 
        'Ongoing', 
        'Delay'
      )
    )
  ) else '0' end 'Delivery_Status', 
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
  if(
    t.vehicle_id is not null, 
    (
      select 
        v.contract_type 
      from 
        vehicle v 
      where 
        v.id = t.vehicle_id
    ), 
    '-'
  ) 'contractType', 
  if(
    t.vehicle_id is not null, 
    (
      select 
        v.model_name 
      from 
        vehicle v 
      where 
        v.id = t.vehicle_id
    ), 
    '-'
  ) 'model_Name', 
  t.trip_start_reading 'Start Reading', 
  t.trip_end_reading 'End Reading', 
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
  ) 'driverPhoneNo', 
  t.trip_start_shipperbox_loading 'NoOfShipperboxesGiven', 
  t.trip_start_trolly_loading 'NoOfTrollyGiven', 
  t.trip_end_shipperbox_loading 'NoOfShipperboxesReturned', 
  t.trip_end_trolly_loading 'NoOfTrollyReturned' 
FROM 
  trip t, 
  tripdp tdp, 
  delivery_point dp 
where 
  t.delivery_center_id = DCID 
  and t.id = tdp.trip_id 
  and tdp.delivery_point_id = dp.id;
END
