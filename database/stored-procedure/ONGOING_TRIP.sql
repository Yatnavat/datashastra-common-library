CREATE DEFINER = `root` @`localhost` PROCEDURE `ONGOING_TRIP`(
  IN DC_ID int, IN tripDate date, IN IST_DIFF time
) BEGIN 
SELECT 
  t.id 'tripID', 
  trip_name 'tripName', 
  if(
    t.trip_status in(
      'DCCHECKIN', 'LOADINGSTART', 'LOADING', 
      'COLLECTDOCUMENT', 'TRIPREADING'
    ), 
    date(
      addtime(t.trip_start_date, IST_DIFF)
    ), 
    date(
      addtime(t.trip_start_time, IST_DIFF)
    )
  ) 'tripDate', 
  getTripClient(t.id) clientName, 
  if(
    t.vehicle_id is not null, 
    getVehicleNumByID(t.vehicle_id), 
    '-'
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
    '-'
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
    '-'
  ) 'model_Name', 
  if(
    (
      select 
        count(*) 
      from 
        trip_vehicle tv 
      where 
        trip_id = t.id
    ) > 0, 
    (
      select 
        time_format(
          addtime(dc_chekin_time, IST_DIFF), 
          "%r"
        ) 
      from 
        trip_vehicle tv 
      where 
        trip_id = t.id
    ), 
    '-'
  ) 'dcCheckInTime', 
  time_format(
    time(
      addtime(t.trip_start_time, IST_DIFF)
    ), 
    "%r"
  ) 'InTime', 
  time_format(
    time(
      addtime(t.trip_end_time, IST_DIFF)
    ), 
    "%r"
  ) 'ReturnTime', 
  timestampdiff(
    hour, 
    addtime(t.trip_end_time, IST_DIFF), 
    addtime(t.trip_start_time, IST_DIFF)
  ) 'DutyHrs', 
  dc.delivery_center_name 'deliveryCenter', 
  getTripDPCount(t.id) 'NoOfDP', 
  t.trip_end_reading - t.trip_start_reading 'OdometerKms', 
  t.tripkm 'GoogleKms', 
  if(
    t.vehicle_id is not null, 
    getVendorByVehicle(t.vehicle_id), 
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
  ) 'driverPhoneNo', 
  t.trip_status 'tripStatus' 
from 
  trip t, 
  delivery_center dc 
where 
  t.delivery_center_id = dc.id 
  and t.trip_status not in (
    'CANCELTRIP', 'COMPLETETRIP', 'HOLD', 
    'ABSENT'
  ) 
  and creation_source not in ('OFFLINE_TMS') 
  and (
    city is null 
    or dc.city = city
  ) 
  and (
    DC_ID is null 
    or delivery_center_id = DC_ID
  ) 
  and if(
    t.trip_status in(
      'DCCHECKIN', 'LOADINGSTART', 'LOADING', 
      'COLLECTDOCUMENT', 'TRIPREADING'
    ), 
    (
      tripDate is null 
      or date(
        addtime(t.trip_start_date, IST_DIFF)
      )= tripDate
    ), 
    (
      tripDate is null 
      or date(
        addtime(t.trip_start_time, IST_DIFF)
      )= tripDate
    )
  ) 
order by 
  t.id desc;
end
