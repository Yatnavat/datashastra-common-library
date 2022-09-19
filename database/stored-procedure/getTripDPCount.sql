CREATE DEFINER = `root` @`localhost` FUNCTION `getTripDPCount`(
  tripID varchar(10)
) RETURNS varchar(10) CHARSET utf8mb4 DETERMINISTIC begin declare DPs varchar(10);
set 
  DPs = (
    select 
      COUNT(*) 
    FROM 
      trip t 
      left join tripdp t2 on t.id = t2.trip_id 
    where 
      trip_id in(tripID)
  );
return DPs;
end
