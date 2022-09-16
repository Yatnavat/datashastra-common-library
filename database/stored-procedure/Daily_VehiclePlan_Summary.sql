CREATE DEFINER = `root` @`localhost` PROCEDURE `Daily_VehiclePlan_Summary`(
  in DCID int, ISTDIFF time, TripDate date
) begin DECLARE TACE int;
DECLARE BOLERO INT;
DECLARE T407 INT;
select 
  date(
    addtime(t.trip_start_date, ISTDIFF)
  ) 'Trip Date', 
  dc.delivery_center_name 'Delivery Center', 
  case when t.vehicle_type in(1) then 'TATA ACE' when t.vehicle_type in(2) then 'BOLERO' when t.vehicle_type in(3, 4) then 'T407' else 'Veh. Not Assigned' end 'Vehicle Type', 
  dcv.No_Of_Vehicle 'Fixed Vehicles', 
  count(*) 'Trips', 
  CASE WHEN count(*)- dcv.No_Of_Vehicle > 0 THEN 'ADHOC' WHEN count(*)- dcv.No_Of_Vehicle < 0 THEN 'HOLD' ELSE 'ALL USED' END 'VEH. USAGE', 
  count(*)- dcv.No_Of_Vehicle 'Usage Variance', 
  sum(
    getTripDPCount(t.id)
  ) 'No. of Orders', 
  min(
    addtime(t.trip_start_time, ISTDIFF)
  ) 'First Vehicle Dispatched', 
  max(
    addtime(t.trip_start_time, ISTDIFF)
  ) 'Last Vehicle Dispatched', 
  sum(t.trip_start_trolly_loading) 'Crates Dispatched' 
from 
  trip t, 
  trip_vehicle tv, 
  delivery_center dc, 
  vehicle v, 
  delivery_center_fixed_vehicles dcv 
where 
  t.delivery_center_id = dc.id 
  and t.id = tv.trip_id 
  and v.id = t.vehicle_id 
  and t.delivery_center_id = dcv.delivery_center_id 
  and t.vehicle_type = dcv.vehicle_type_id 
  and t.delivery_center_id = DCID 
  and date(
    addtime(t.trip_start_date, ISTDIFF)
  )= TripDate 
group by 
  1, 
  2, 
  3, 
  4;
end
