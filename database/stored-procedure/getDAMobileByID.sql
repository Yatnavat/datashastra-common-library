CREATE DEFINER = `root` @`localhost` FUNCTION `getDAMobileByID`(
  daID varchar(100)
) RETURNS varchar(100) CHARSET utf8mb4 DETERMINISTIC begin declare DAMob varchar(100);
set 
  DAMob = if(
    isnull(daID), 
    'Driver Not Found', 
    (
      select 
        phone_number 
      from 
        user 
      where 
        id in(
          select 
            user_id 
          from 
            driver_assistant da 
          where 
            da.id = daID
        )
    )
  );
return DAMob;
end
