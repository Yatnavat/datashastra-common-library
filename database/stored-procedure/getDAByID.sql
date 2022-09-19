CREATE DEFINER = `root` @`localhost` FUNCTION `getDAByID`(
  dID varchar(100)
) RETURNS varchar(100) CHARSET utf8mb4 DETERMINISTIC begin declare DA varchar(100);
set 
  DA = if(
    isnull(dID), 
    'Driver Assistant Not Found', 
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
            driver_assistant da 
          where 
            da.id = dID
        )
    )
  );
return DA;
end
