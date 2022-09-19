CREATE DEFINER = `root` @`localhost` FUNCTION `getVehicleNumByID`(
  vehID varchar(100)
) RETURNS varchar(100) CHARSET utf8mb4 DETERMINISTIC begin declare VehNum varchar(100);
set 
  VehNum = if(
    isnull(vehID), 
    'Not Found', 
    (
      select 
        v.registration_number 
      from 
        vehicle v 
      where 
        v.id = vehID
    )
  );
return VehNum;
end
