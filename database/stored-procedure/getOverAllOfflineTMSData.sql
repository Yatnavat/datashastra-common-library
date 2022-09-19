CREATE DEFINER = `root` @`localhost` PROCEDURE `getOverAllOfflineTMSData`(IN ISTDIFF time) BEGIN 
select 
  t.arranged_by arrangedBy, 
  addtime(tv.dc_chekin_time, ISTDIFF) 'checkInTime', 
  dc.city, 
  getTripClient(t.client_id) client_name, 
  t.cod_settelment 'codSettelment', 
  t.codamt_received 'codamountReceived', 
  dc.delivery_center_name 'deliveryCenter', 
  t.delivery_done_by 'deliveryDoneBy', 
  getDriverByID(t.driver_id) 'driverName', 
  t.trip_end_reading 'endKM', 
  t.expectedcodamt 'expectedCODAmount', 
  t.fareye_closure 'fareyeClosure', 
  t.first_delivery_point 'firstDeliveryPoint', 
  t.first_order_handover 'firstOrderHandover', 
  t.fuel_type 'fuelType', 
  t.googlekm 'googleKms', 
  t.last_delivery_point 'lastDeliveryPoint', 
  t.last_delivery_time 'lastDeliveryTime', 
  t.last_order_handover 'lastOrderHandover', 
  t.load_end_time 'loadEndTime', 
  t.load_start_time 'loadStartTime', 
  getVendorByDriver(t.driver_id) 'VendorName', 
  if(
    creation_source IN('OFFLINE_TMS'), 
    no_of_delivery_points, 
    getTripDPCount(t.id)
  ) 'noOfDP', 
  t.no_ofdps_delivered 'noOfDPsDelivered', 
  t.invoice_number_count 'noOfInvoices', 
  t.no_ofpod 'noOfPOD', 
  t.trip_start_shipperbox_loading 'noOfShipperBoxesGiven', 
  t.trip_end_shipperbox_loading 'noOfShipperBoxesReturned', 
  t.trip_start_trolly_loading 'noOfTrollyGiven', 
  t.trip_end_trolly_loading 'noOfTrollyReturned', 
  t.otd 'otd', 
  t.penalties 'penalties', 
  t.remark 'remark', 
  t.trip_name 'routeId', 
  t.trip_start_reading 'startKM', 
  t.trip_end_reading - t.trip_start_reading 'totalKM', 
  if(
    t.trip_status in(
      'DCCHECKIN', 'LOADINGSTART', 'LOADING', 
      'COLLECTDOCUMENT', 'TRIPREADING'
    ), 
    date(
      addtime(t.trip_start_date, ISTDIFF)
    ), 
    date(
      addtime(t.trip_start_time, ISTDIFF)
    )
  ) Date, 
  t.trip_end_bags 'tripEndBags', 
  t.trip_end_charge_reading 'tripEndChargeReading', 
  t.trip_end_gelpad 'tripEndGelpad', 
  addtime(t.trip_end_time, ISTDIFF) 'tripEndTime', 
  t.trip_start_bags 'tripStartBags', 
  t.trip_start_charge_reading 'tripStartChargeReading', 
  t.trip_start_gelpad 'tripStartGelpad', 
  addtime(t.trip_start_time, ISTDIFF) 'tripStartTime', 
  t.trip_status 'tripStatus', 
  t.id 'trip_ID', 
  tv.vehicle_allocation_type 'vehicleAllocationType', 
  getVehicleNumByID(t.vehicle_id) 'vehicleNo', 
  t.total_weight 'weight', 
  t.vehicle_id 'vehicleID', 
  t.driver_id 'driverID', 
  (
    select 
      vendor_id 
    from 
      driver 
    where 
      id = t.driver_id
  ) 'vendorID', 
  dc.id 'deliveryCenterID', 
  t.client_id 'clientID', 
  t.trip_end_shipperbox_loading - t.trip_start_shipperbox_loading 'gapShipperboxes', 
  t.trip_end_trolly_loading - t.trip_start_trolly_loading 'gapNoOfCrates', 
  t.codamt_received - t.expectedcodamt 'gapCODAMT', 
  t.trip_end_gelpad - t.trip_start_gelpad 'gapGelpad', 
  t.trip_end_bags - t.trip_start_bags 'gapBags' 
from 
  trip t, 
  trip_vehicle tv, 
  delivery_center dc 
where 
  t.delivery_center_id = dc.id 
  and t.id = tv.trip_id 
  and if (
    t.creation_source in('OFFLINE_TMS'), 
    t.flag in(1), 
    t.flag in(null, 1)
  ) 
  and t.trip_status in('COMPLETETRIP', 'HOLD', 'ABSENT') 
order by 
  if(
    t.trip_status in(
      'DCCHECKIN', 'LOADINGSTART', 'LOADING', 
      'COLLECTDOCUMENT', 'TRIPREADING'
    ), 
    date(
      addtime(t.trip_start_date, ISTDIFF)
    ), 
    date(
      addtime(t.trip_start_time, ISTDIFF)
    )
  ), 
  t.id;
END
