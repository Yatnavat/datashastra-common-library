CREATE DEFINER = `root` @`localhost` PROCEDURE `Kilometer_Report`() BEGIN -- KM Report/ Report No. 4
SET 
  @row_number2 = 0;
SELECT 
  (@row_number2 := @row_number2 + 1) AS 'SN2', 
  dc.dc_code 'DC CODE', 
  dc.delivery_center_name 'Delivery Center Name', 
  ven.unique_code 'Vendor Code', 
  concat_ws(' ', uv.first_name, uv.last_name) 'Vendor Name', 
  dp.client_id 'Client ID', 
  c.client_name 'Client Name', 
  c.user_status 'Client Status', 
  concat_ws(' ', ud.first_name, ud.last_name) 'Driver Name', 
  v.registration_number 'Vehicle No.', 
  v.container_length 'Container Length (Feet)', 
  v.capecity 'Carrying Capacity (Weight in Kg)', 
  v.container_height 'Container Height', 
  v.container_type 'Container Type', 
  t.id 'Trip ID', 
  t.trip_name 'Trip Name', 
  DATE(
    addtime(trip_start_time, '5:30')
  ) 'Trip Start Date', 
  DATE_FORMAT(
    addtime(trip_start_time, '5:30'), 
    '%r'
  ) 'Trip Start Time', 
  round(t.trip_start_reading) 'Trip Start Reading', 
  round(t.trip_end_reading) 'Trip End Reading', 
  round(t.tripkm, 2) 'Planned Kms', 
  if(
    t.trip_end_reading = '', '', t.trip_end_reading - t.trip_start_reading
  ) as 'Odometer Kms', 
  round(
    tripkm - if(
      t.trip_end_reading = '', 0, t.trip_end_reading - t.trip_start_reading
    ), 
    1
  ) 'Variance  (Odometer vs Planned)' 
from 
  trip t, 
  delivery_center dc, 
  driver d, 
  vehicle v, 
  route r, 
  vendor ven, 
  user uv, 
  delivery_point dp, 
  user ud, 
  route_path rp, 
  client c 
where 
  t.delivery_center_id = dc.id 
  and t.driver_id = d.id 
  and t.route_id = r.id 
  and t.vehicle_id = v.id 
  and ven.id = v.vendor_id 
  and d.vendor_id = ven.id 
  and ud.id = d.user_id 
  and uv.id = ven.user_id 
  and r.delivery_center_id = t.delivery_center_id 
  and dp.delivery_center_id = dc.id 
  and rp.route_id = r.id 
  and rp.dp_id = dp.id 
  and dp.client_id = c.id 
  and t.trip_status not in ('CANCELTRIP');
END
