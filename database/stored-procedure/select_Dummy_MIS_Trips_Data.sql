CREATE DEFINER = `root` @`localhost` PROCEDURE `select_Dummy_MIS_Trips_Data`() BEGIN -- SQL CODE GOES HERE
select 
  * 
FROM 
  trip_vehicle 
where 
  trip_id in(
    select 
      id 
    from 
      trip 
    where 
      trip.delivery_center_id in(51, 53) 
      and creation_source in ('OFFLINE_TMS')
  );
select 
  * 
from 
  trip 
where 
  delivery_center_id in(51, 53) 
  and creation_source in ('OFFLINE_TMS');
-- END OF SQL
end
