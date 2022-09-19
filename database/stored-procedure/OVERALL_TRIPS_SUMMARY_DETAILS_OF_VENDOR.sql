CREATE DEFINER = `root` @`localhost` PROCEDURE `OVERALL_TRIPS_SUMMARY_DETAILS_OF_VENDOR`(
  IN IST_Diff varchar(20)
) BEGIN 
SELECT 
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
  dc.delivery_center_name 'Delivery Center', 
  t.id 'TripID', 
  time(
    addtime(tv.dc_chekin_time, '5:30')
  ) 'DC Report Time', 
  time(
    addtime(trip_start_time, IST_DIFF)
  ) 'Start Time', 
  if(
    creation_source in('OFFLINE_TMS'), 
    time(
      addtime(last_delivery_time, IST_DIFF)
    ), 
    (
      SELECT 
        MAX(
          time(
            addtime(check_out_time, IST_DIFF)
          )
        ) 
      FROM 
        tripdp 
      WHERE 
        trip_id = t.id
    )
  ) 'Last Delivery Time', 
  time(
    addtime(trip_end_time, IST_DIFF)
  ) 'End Time', 
  if(
    t.client_id is null, 
    (
      select 
        distinct c.client_name 
      from 
        client c 
      where 
        c.id in (
          select 
            distinct client_id 
          from 
            delivery_point dp 
          where 
            dp.id in(
              select 
                delivery_point_id 
              from 
                tripdp 
              where 
                trip_id = t.id
            )
        )
    ), 
    (
      select 
        distinct client_name 
      from 
        client 
      where 
        id in(t.client_id)
    )
  ) 'ClientName', 
  t.trip_status 'TripStatus', 
  tv.vehicle_allocation_type 'Trip Allocation Type', 
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
    getVehicleNumByID(t.vehicle_id), 
    '-'
  ) 'vehicleNo', 
  TIMEDIFF(
    addtime(t.trip_end_time, IST_DIFF), 
    addtime(t.trip_start_time, IST_DIFF)
  ) 'DutyHrs', 
  t.trip_end_reading - t.trip_start_reading 'OdometerKms', 
  t.tripkm 'GoogleKms', 
  if(
    t.driver_id is not null, 
    getVendorByDriver(t.driver_id), 
    '-'
  ) 'vendorName', 
  if(
    t.driver_assistant_id is not null, 
    getDAByID(t.driver_assistant_id), 
    '-'
  ) 'driverAssistantName', 
  if(
    t.driver_id is not null, 
    getDriverByID(t.driver_id), 
    '-'
  ) 'driverName', 
  if(
    t.driver_id is not null, 
    getDriverMobileByID(t.driver_id), 
    '-'
  ) 'driverPhoneNo' 
FROM 
  trip t, 
  delivery_center dc, 
  trip_vehicle tv 
where 
  t.id = tv.trip_id 
  and t.delivery_center_id = dc.id 
  and if (
    t.creation_source in('OFFLINE_TMS'), 
    t.flag in(1), 
    t.flag in(null, 1)
  );
END
