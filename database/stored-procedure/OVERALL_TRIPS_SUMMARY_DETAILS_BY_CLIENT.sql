CREATE DEFINER = `root` @`localhost` PROCEDURE `OVERALL_TRIPS_SUMMARY_DETAILS_BY_CLIENT`(
  IN CID INT, 
  S1_StartTime time, 
  S1_EndTime time, 
  S2_EndTime time, 
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
  dc.city 'DC City', 
  dc.delivery_center_name 'Delivery Center', 
  t.creation_source, 
  t.id 'TripID', 
  addtime(tv.dc_chekin_time, IST_Diff) 'DC Report Time', 
  addtime(trip_start_time, IST_DIFF) 'StartDateTime', 
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
  addtime(trip_end_time, IST_DIFF) 'TripEndTime', 
  if(
    t.client_id is null, 
    '-', 
    getTripClient(t.id)
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
  addtime(tdp.check_in_time, IST_Diff) 'Check-in Time', 
  addtime(tdp.check_out_time, IST_Diff) 'Check-Out Time', 
  tdp.delivery_process_status 'DP Status', 
  tdp.total_weight, 
  if(
    t.delivery_center_id not in(126), 
    '-', 
    if(
      timestamp (
        addtime(trip_start_time, IST_Diff)
      ) between timestamp (
        date(
          addtime(trip_start_time, IST_Diff)
        ), 
        S1_StartTime
      ) 
      and timestamp (
        date(
          addtime(trip_start_time, IST_Diff)
        ), 
        S1_EndTime
      ), 
      '1st', 
      '2nd'
    )
  ) as `Shift`, 
  timestamp (
    adddate(
      date(
        addtime(trip_start_time, IST_Diff)
      ), 
      interval 1 day
    ), 
    S2_EndTime
  ) End_Time 
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
  and t.trip_status in('COMPLETETRIP') 
  AND t.client_id = CID;
END
