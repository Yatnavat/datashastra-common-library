CREATE DEFINER = `root` @`localhost` PROCEDURE `GET_DCs_DPs`() BEGIN 
select 
  dc.city 'City', 
  dc.delivery_center_name 'Delivery center', 
  dp.delivery_point_name 'delivery Point', 
  dp.latitude 'Latitude', 
  dp.longtitude 'Longitude' 
from 
  delivery_point dp, 
  delivery_center dc 
where 
  dp.delivery_center_id = dc.id 
  and dp.id in (
    select 
      distinct delivery_point_id 
    from 
      tripdp
  );
END
