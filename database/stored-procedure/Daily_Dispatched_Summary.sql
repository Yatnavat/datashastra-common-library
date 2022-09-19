CREATE DEFINER = `root` @`localhost` PROCEDURE `Daily_Dispatched_Summary`(
  in DCID int, ISTDIFF time, TripDate date
) begin 
select 
  date(
    addtime(t.trip_start_date, ISTDIFF)
  ) 'Trip Date', 
  dc.delivery_center_name 'Delivery Center', 
  count(*) 'Trips', 
  sum(
    getTripDPCount(t.id)
  ) 'No. of DPs', 
  min(
    addtime(t.trip_start_time, ISTDIFF)
  ) 'First Vehicle Dispatched', 
  max(
    addtime(t.trip_start_time, ISTDIFF)
  ) 'Last Vehicle Dispatched' 
from 
  trip t, 
  trip_vehicle tv, 
  delivery_center dc, 
  vehicle v 
where 
  t.delivery_center_id = dc.id 
  and t.id = tv.trip_id 
  and v.id = t.vehicle_id 
  and t.delivery_center_id = DCID 
  and date(
    addtime(t.trip_start_date, ISTDIFF)
  )= TripDate 
group by 
  1, 
  2;
end
