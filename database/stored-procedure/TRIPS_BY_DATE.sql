CREATE DEFINER = `root` @`%` PROCEDURE `TRIPS_BY_DATE`(
  IN TRIP_DATE date, 
  IN DCID varchar(60), 
  IN IST_DIFF varchar(30)
) BEGIN 
select 
  distinct t.id 'tripId', 
  trip_name 'tripName', 
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
    ''
  ) 'vendorName', 
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
    ''
  ) 'vehicleNo', 
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
    ''
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
    ''
  ) 'driverPhoneNo', 
  date_format(
    addtime(t.trip_start_time, IST_DIFF), 
    '%r'
  ) 'tripStartTime', 
  dc.delivery_center_name 'dcName', 
  c.client_name, 
  date(
    addtime(trip_start_date, IST_DIFF)
  ) 'tripStartDate', 
  t.trip_status 'tripStatus', 
  dp.customer_code 'DPCode', 
  dp.delivery_point_name 'DeliveryPointName', 
  addtime(tdp.check_in_time, IST_DIFF) 'Check-In', 
  addtime(tdp.check_out_time, IST_DIFF) 'Check-Out', 
  tdp.delivery_process_status 'DP_Status' 
from 
  trip t, 
  client c, 
  delivery_point dp, 
  tripdp tdp, 
  delivery_center dc 
where 
  dp.client_id = c.id 
  and tdp.delivery_point_id = dp.id 
  and tdp.trip_id = t.id 
  and dc.id = t.delivery_center_id 
  AND t.delivery_center_id = DCID 
  AND date(
    addtime(trip_start_date, IST_DIFF)
  ) = TRIP_DATE;
END
