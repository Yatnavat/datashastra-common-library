CREATE DEFINER = `root` @`localhost` FUNCTION `getDriverByID`(
  dID varchar(100)
) RETURNS varchar(100) CHARSET utf8mb4 DETERMINISTIC begin declare DriverName varchar(100);
set 
  DriverName = if(
    isnull(dID), 
    'Not Found', 
    (
      select 
        concat_ws(' ', first_name, last_name) 
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
return DriverName;
end
