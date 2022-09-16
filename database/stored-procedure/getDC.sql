CREATE DEFINER = `root` @`localhost` FUNCTION `getDC`(
  dcID varchar(100)
) RETURNS varchar(100) CHARSET utf8mb4 DETERMINISTIC begin declare DC varchar(100);
set 
  DC = (
    select 
      if(
        isnull(dcID), 
        '-', 
        (
          select 
            delivery_center_name 
          from 
            delivery_center 
          where 
            id = dcID
        )
      )
  );
return DC;
end
