CREATE DEFINER = `root` @`localhost` PROCEDURE `VNF_OTD`(IN expOTD_TIME TIME) BEGIN 
select 
  date(
    addtime(t.trip_start_time, '5:30')
  ) TripDate, 
  getDC(t.delivery_center_id) DC, 
  trip_status Status, 
  count(distinct t.id) Trips, 
  count(tdp.delivery_point_id) TDPs, 
  sum(
    if(
      time(
        addtime(tdp.check_in_time, expOTD_TIME)
      )<= time('5:00:00'), 
      1, 
      0
    )
  ) 'Before 5AM', 
  sum(
    if(
      time(
        addtime(tdp.check_in_time, expOTD_TIME)
      )> time('5:00:00'), 
      1, 
      0
    )
  ) 'After 5AM', 
  sum(
    if(
      isnull(tdp.check_in_time), 
      1, 
      0
    )
  ) 'PENDING' 
from 
  tripdp tdp, 
  trip t 
where 
  t.id = tdp.trip_id 
  and t.client_id in(7, 33) 
  and trip_status NOT in('CANCELTRIP') 
group by 
  1, 
  2, 
  3;
END
