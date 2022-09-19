CREATE DEFINER = `root` @`localhost` PROCEDURE `tripTracking_of_DC_from_Days_to_tillDate`(in dcID int, Days int) begin 
select 
  date(
    addtime(trip_start_time, '5:30')
  ) 'TripDate', 
  tdt.id, 
  trip_id, 
  t.trip_name, 
  latitude, 
  longtitude, 
  getDCs_City(t.delivery_center_id) 'City', 
  getDC(t.delivery_center_id) 'Delivery Center', 
  getTripClient(t.id) 'Client', 
  getVehicleNumByID(t.vehicle_id) 'Vehicle', 
  getDriverByID(t.driver_id) 'Driver', 
  getVendorByDriver(t.driver_id) 
from 
  trip_driver_tracking tdt, 
  trip t 
where 
  t.id = tdt.trip_id 
  and delivery_center_id = dcID 
  and date(
    addtime(trip_start_time, '5:30')
  )>= curdate()- Days;
end
