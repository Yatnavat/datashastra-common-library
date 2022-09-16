CREATE DEFINER = `root` @`localhost` FUNCTION `get_Idle_Allocated_Vehicles`() RETURNS varchar(100) CHARSET utf8mb4 DETERMINISTIC begin declare cnt int default 0;
set 
  cnt =(
    SELECT 
      COUNT(*) 
    FROM 
      trip_vehicle 
    WHERE 
      status IN('ACTIVE') 
      AND trip_id IS NULL 
      AND timestamp (
        if(
          updated_date IS NULL, created_date, 
          updated_date
        )
      ) < timestamp(NOW() - interval 4 hour)
  );
return cnt;
end
