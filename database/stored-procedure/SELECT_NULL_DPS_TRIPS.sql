CREATE DEFINER = `root` @`localhost` PROCEDURE `SELECT_NULL_DPS_TRIPS`() BEGIN 
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
    where 
      trip_id is not null
  ) 
  and creation_source not in('OFFLINE_TMS');
END
