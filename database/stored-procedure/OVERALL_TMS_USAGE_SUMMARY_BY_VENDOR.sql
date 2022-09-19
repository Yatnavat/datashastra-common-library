CREATE DEFINER = `root` @`localhost` PROCEDURE `OVERALL_TMS_USAGE_SUMMARY_BY_VENDOR`(IN ISTDIFF time) begin declare VehCnt integer default 0;
select 
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
  dc.city 'DC City', 
  dc.delivery_center_name 'Delivery Center', 
  t.creation_source 'Source', 
  getVendorByDriver(t.driver_id) 'Vendor Name', 
  tv.vehicle_allocation_type, 
  getVehicleTypeByTripID(t.id) 'Vehicle Type', 
  v.capecity 'Veh. Capacity', 
  count(distinct t.id) 'Vehicles', 
  sum(v.capecity) 
from 
  trip t, 
  trip_vehicle tv, 
  delivery_center dc, 
  vehicle v 
where 
  t.delivery_center_id = dc.id 
  and t.id = tv.trip_id 
  and v.id = t.vehicle_id 
  and if (
    t.creation_source in('OFFLINE_TMS'), 
    t.flag in(1), 
    t.flag in(null, 1)
  ) 
group by 
  1, 
  2, 
  3, 
  4, 
  5, 
  6, 
  7, 
  8;
end
