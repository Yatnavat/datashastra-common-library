CREATE DEFINER = `root` @`localhost` PROCEDURE `OVERALL_TRIPS_SUMMARY_DETAILS`(
  IN expOTD_TIME time, 
  IST_Diff varchar(20)
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
  dc.city 'DC City', 
  dc.delivery_center_name 'Delivery Center', 
  t.creation_source, 
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
            addtime(tripdp.check_in_time, IST_DIFF)
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
  if(
    t.vehicle_id is not null, 
    getVehicleNumByID(t.vehicle_id), 
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
  ) 'driverPhoneNo', 
  t.trip_start_shipperbox_loading 'ShipperboxesGiven', 
  t.trip_start_trolly_loading 'TrollyGiven', 
  t.trip_end_shipperbox_loading 'ShipperboxesReturned', 
  t.trip_end_trolly_loading 'TrollyReturned', 
  if(
    getTripDPCount(t.id) < 1, 
    1, 
    getTripDPCount(t.id)
  ) DPs, 
  tdp.sequence_id 'DP Sequence', 
  tdp.delivery_point_id DPID, 
  tdp.id tripDPID, 
  addtime(tdp.check_in_time, '5:30') 'Check-in Time', 
  addtime(tdp.check_out_time, '5:30') 'Check-Out Time', 
  if(
    time(
      addtime(tdp.check_in_time, IST_Diff)
    )<= expOTD_TIME, 
    1, 
    0
  ) 'Before 5AM', 
  if(
    time(
      addtime(tdp.check_in_time, IST_Diff)
    )> expOTD_TIME, 
    1, 
    0
  ) 'After 5AM', 
  tdp.delivery_process_status 'DP Status', 
  tdp.total_weight 
FROM 
  trip t, 
  trip_vehicle tv, 
  delivery_center dc, 
  tripdp tdp 
where 
  t.delivery_center_id = dc.id 
  and t.id = tv.trip_id 
  and t.id = tdp.trip_id 
  and if (
    t.creation_source in('OFFLINE_TMS'), 
    t.flag in(1), 
    t.flag in(null, 1)
  ) 
  and t.trip_status in('COMPLETETRIP');
END
