CREATE DEFINER = `root` @`localhost` FUNCTION `getTripClient`(
  tripID varchar(100)
) RETURNS varchar(100) CHARSET utf8mb4 DETERMINISTIC begin declare Client varchar(100);
set 
  Client = (
    select 
      if(
        isnull(tripID), 
        '-', 
        (
          select 
            distinct c.client_name 
          from 
            client c 
          where 
            c.id in (
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
      )
  );
return Client;
end
