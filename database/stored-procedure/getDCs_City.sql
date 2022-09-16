CREATE DEFINER = `root` @`localhost` FUNCTION `getDCs_City`(
  dcID varchar(100)
) RETURNS varchar(100) CHARSET utf8mb4 DETERMINISTIC begin declare DCity varchar(100);
set 
  DCity = (
    select 
      if(
        isnull(dcID), 
        '-', 
        (
          select 
            city 
          from 
            delivery_center 
          where 
            id = dcID
        )
      )
  );
return DCity;
end
