CREATE DEFINER = `root` @`localhost` PROCEDURE `GET_DRIVER_TRACKING_DATA_TILL_DATA_BY_DC`(IN DCID int) BEGIN 
select 
  tdt.*, 
  t.id, 
  t.trip_name, 
  t.trip_status 
from 
  trip_driver_tracking tdt, 
  trip t 
where 
  t.id = tdt.trip_id 
  and t.delivery_center_id <= DCID;
end
