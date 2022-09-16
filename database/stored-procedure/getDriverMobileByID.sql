CREATE DEFINER = `root` @`localhost` FUNCTION `getDriverMobileByID`(
  dID varchar(100)
) RETURNS varchar(100) CHARSET utf8mb4 DETERMINISTIC begin declare DriverMob varchar(100);
set 
  DriverMob = if(
    isnull(dID), 
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
            driver d 
          where 
            d.id = dID
        )
    )
  );
return DriverMob;
end
