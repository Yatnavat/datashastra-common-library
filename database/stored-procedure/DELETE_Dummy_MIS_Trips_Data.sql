CREATE DEFINER = `root` @`localhost` PROCEDURE `DELETE_Dummy_MIS_Trips_Data`() BEGIN -- SQL CODE GOES HERE
DELETE FROM 
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
DELETE FROM 
  trip 
where 
  delivery_center_id in(51, 53) 
  and creation_source in ('OFFLINE_TMS');
-- END OF SQL
end
