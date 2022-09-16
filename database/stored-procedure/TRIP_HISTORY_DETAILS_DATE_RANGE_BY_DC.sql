CREATE DEFINER = `root` @`%` PROCEDURE `TRIP_HISTORY_DETAILS_DATE_RANGE_BY_DC`(
  IN DC_ID int, FROM_TRIP_DATE date, IN TO_TRIP_DATE date, 
  IN IST_Diff time
) BEGIN 
SELECT 
  t.id 'tripID', 
  trip_name 'tripName', 
  date(
    addtime(t.trip_start_date, IST_Diff)
  ) 'tripDate', 
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
      )
  ) 'clientName', 
  t.vehicle_id 'VehicleID', 
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
    false
  ) 'vehicleNo', 
  if(
    t.vehicle_id is not null, 
    (
      select 
        v.container_length 
      from 
        vehicle v 
      where 
        v.id = t.vehicle_id
    ), 
    false
  ) 'containerLength', 
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
    false
  ) 'model_Name', 
  addtime(tv.dc_chekin_time, IST_DIFF) 'DC CheckIn Time', 
  time(
    addtime(t.trip_start_time, IST_DIFF)
  ) 'InTime', 
  time(
    addtime(t.trip_end_time, IST_DIFF)
  ) 'ReturnTime', 
  timestampdiff(
    hour, 
    addtime(tv.dc_chekin_time, IST_DIFF), 
    addtime(t.trip_end_time, IST_DIFF)
  ) 'DutyHrs', 
  dc.delivery_center_name 'deliveryCenter', 
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
    false
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
    null
  ) 'driverAssistantName', 
  t.driver_id DriverID, 
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
    false
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
    false
  ) 'driverPhoneNo', 
  t.trip_status 'tripStatus', 
  t.trip_start_reading 'Start Reading', 
  t.trip_end_reading 'End Reading', 
  t.creation_source 'Source', 
  t.total_weight 'Weight', 
  tv.vehicle_allocation_type 'Allocation Type', 
  t.googlekm, 
  t.tripkm, 
  t.total_weight, 
  t.vehicle_type 
FROM 
  trip t, 
  trip_vehicle tv, 
  delivery_center dc 
WHERE 
  dc.id = t.delivery_center_id 
  and t.id = tv.trip_id 
  and DATE(
    ADDTIME(trip_start_date, IST_Diff)
  ) BETWEEN FROM_TRIP_DATE 
  AND TO_TRIP_DATE 
  and t.delivery_center_id = DC_ID;
end
