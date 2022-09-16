CREATE DEFINER = `root` @`localhost` PROCEDURE `GET_NULL_DPS_TRIPS`() BEGIN 
SELECT 
  * 
from 
  trip 
where 
  id not in (
    select 
      trip_id 
    from 
      tripdp
  ) 
  and creation_source not in('OFFLINE_TMS');
END
