CREATE DEFINER = `root` @`localhost` PROCEDURE `getOfflineTMSData`(
  IN uFromDate date, 
  IN uToDate date, 
  IN uCity varchar(60), 
  IN dcID int, 
  IN cID int, 
  IN dID int, 
  IN venID int, 
  IN vID int, 
  IN uDuty varchar(30), 
  IN OTDSPAN time, 
  IN IST_Diff time
) begin 
select 
  t.arranged_by arrangedBy, 
  tv.dc_chekin_time 'checkInTime', 
  dc.city, 
  getTripClient(t.id) client_name, 
  t.cod_settelment 'codSettelment', 
  t.codamt_received 'codamountReceived', 
  dc.delivery_center_name 'deliveryCenter', 
  t.delivery_done_by 'deliveryDoneBy', 
  if(
    t.driver_id is null, 
    '-', 
    getDriverByID(t.driver_id)
  ) 'driverName', 
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
  if(
    t.driver_id is null, 
    '-', 
    getVendorByDriver(t.driver_id)
  ) 'nameOfCompany', 
  t.no_of_delivery_points 'noOfDP', 
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
  t.tripkm tripKM, 
  date(
    addtime(t.trip_start_time, IST_Diff)
  ) tripDate, 
  t.trip_end_bags 'tripEndBags', 
  t.trip_end_charge_reading 'tripEndChargeReading', 
  t.trip_end_gelpad 'tripEndGelpad', 
  t.trip_end_time 'tripEndTime', 
  t.trip_start_bags 'tripStartBags', 
  t.trip_start_charge_reading 'tripStartChargeReading', 
  t.trip_start_gelpad 'tripStartGelpad', 
  t.trip_start_time 'tripStartTime', 
  t.trip_status 'tripStatus', 
  t.id 'trip_ID', 
  tv.vehicle_allocation_type 'vehicleAllocationType', 
  getVehicleNumByID(t.vehicle_id) 'vehicleNo', 
  t.total_weight 'weight', 
  t.vehicle_id 'vehicleID', 
  t.driver_id 'driverID', 
  dc.id 'deliveryCenterID', 
  t.client_id 'clientID', 
  getVendorIDByDriver(t.driver_id) vendorID, 
  t.trip_end_shipperbox_loading - t.trip_start_shipperbox_loading 'gapShipperboxes', 
  t.trip_end_trolly_loading - t.trip_start_trolly_loading 'gapNoOfCrates', 
  t.codamt_received - t.expectedcodamt 'gapCODAMT', 
  t.trip_end_gelpad - t.trip_start_gelpad 'gapGelpad', 
  t.trip_end_bags - t.trip_start_bags 'gapBags', 
  t.toll 'toll', 
  if(
    t.vehicle_id is null, 
    '-', 
    getVehicleTypeByVehID(t.vehicle_id)
  ) 'vehicleType' 
from 
  trip t, 
  trip_vehicle tv, 
  delivery_center dc 
where 
  t.delivery_center_id = dc.id 
  and t.id = tv.trip_id 
  and date(
    addtime(t.trip_start_time, IST_Diff)
  ) between uFromDate 
  and uToDate 
  and dc.city = uCity 
  and t.delivery_center_id = dcID 
  and (
    cID is null 
    or t.client_id = cID
  ) 
  and (
    dID is null 
    or t.driver_id = dID
  ) 
  and (
    vID is null 
    or t.vehicle_id = vID
  ) 
  and (
    venID is null 
    or getVendorIDByDriver(t.driver_id)= venID
  ) 
  and (
    uDuty is null 
    or tv.vehicle_allocation_type = uDuty
  ) 
  and t.trip_status in('HOLD', 'ABSENT', 'COMPLETETRIP') 
  and t.flag in(1) 
order by 
  date(
    addtime(t.trip_start_time, IST_Diff)
  ), 
  t.id;
end
