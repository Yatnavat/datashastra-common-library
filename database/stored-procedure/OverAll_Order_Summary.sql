CREATE DEFINER = `root` @`localhost` PROCEDURE `OverAll_Order_Summary`(in ISTDIFF time) begin 
select 
  date(
    addtime(t.trip_start_date, ISTDIFF)
  ) 'Trip Date', 
  dc.delivery_center_name 'Delivery Center', 
  count(*) 'Trips', 
  sum(
    getTripDPCount(t.id)
  ) 'No. of Orders', 
  round(
    sum(t.total_weight)/ 1000, 
    2
  ) 'Total tonnage Dispatched', 
  case when t.vehicle_type in(1) then 'TATA ACE' when t.vehicle_type in(2) then 'BOLERO' when t.vehicle_type in(3, 4) then 'T407' else 'Veh. Not Assigned' end 'No. of Used Vehicle', 
  count(*) 'Vehicles', 
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
  vehicle v 
where 
  t.delivery_center_id = dc.id 
  and t.id = tv.trip_id 
  and v.id = t.vehicle_id -- and t.delivery_center_id=DCID
  -- and date(addtime(t.trip_start_date,ISTDIFF))=TripDate
group by 
  1, 
  2, 
  6;
end
