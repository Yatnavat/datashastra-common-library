CREATE DEFINER = `root` @`localhost` FUNCTION `getTripClientID`(
  tripID varchar(100)
) RETURNS varchar(100) CHARSET utf8mb4 DETERMINISTIC begin declare ClientID varchar(100);
set 
  ClientID = (
    select 
      if(
        isnull(tripID), 
        'Not Found', 
        (
          select 
            distinct client_id 
          from 
            delivery_point dp 
          where 
            dp.id in(
              select 
                delivery_point_id 
              from 
                tripdp 
              where 
                trip_id = tripID
            )
        )
      )
  );
return ClientID;
end
