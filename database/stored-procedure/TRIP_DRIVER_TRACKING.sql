CREATE DEFINER = `root` @`localhost` PROCEDURE `TRIP_DRIVER_TRACKING`() begin 
SELECT 
  t.id 'tripId', 
  trip_name 'tripName', 
  concat_ws(' ', uv.first_name, uv.last_name) 'vendorName', 
  v.registration_number 'vehicleNo', 
  concat_ws(' ', ud.first_name, ud.last_name) 'driverName', 
  ud.phone_number 'driverPhoneNo', 
  dc.delivery_center_name 'dcName', 
  c.client_name 'clientName', 
  t.trip_status 'tripStatus', 
  date(
    addtime(trip_start_time, '5:30')
  ) 'tripStartDate', 
  date_format(
    addtime(t.trip_start_time, '5:30'), 
    '%r'
  ) 'tripStartTime', 
  tdt.id, 
  tdt.latitude, 
  tdt.longtitude 
from 
  trip t, 
  tripdp tdp, 
  delivery_center dc, 
  driver d, 
  vehicle v, 
  route r, 
  vendor ven, 
  user uv, 
  user ud, 
  delivery_point dp, 
  route_path rp, 
  client c, 
  order_tbl ot, 
  trip_orders tord, 
  trip_driver_tracking tdt 
where 
  t.delivery_center_id = dc.id 
  and t.driver_id = d.id 
  and t.route_id = r.id 
  and t.vehicle_id = v.id 
  and tdp.trip_id = t.id 
  and tdp.delivery_point_id = dp.id 
  and ven.id = v.vendor_id 
  and d.vendor_id = ven.id 
  and uv.id = ven.user_id 
  and ud.id = d.user_id 
  and r.delivery_center_id = t.delivery_center_id 
  and dp.delivery_center_id = dc.id 
  and rp.route_id = r.id 
  and rp.dp_id = dp.id 
  and dp.client_id = c.id 
  and ot.delivery_point_id = dp.id 
  and ot.delivery_center_id = dc.id 
  and t.id = tord.trip_id 
  and tord.orders_id = ot.id 
  and t.id = tdt.trip_id;
end
