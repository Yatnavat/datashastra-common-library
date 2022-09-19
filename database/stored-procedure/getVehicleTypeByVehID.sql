CREATE DEFINER = `root` @`localhost` FUNCTION `getVehicleTypeByVehID`(
  vehID varchar(100)
) RETURNS varchar(100) CHARSET utf8mb4 DETERMINISTIC begin declare vehicleType varchar(100);
set 
  vehicleType = (
    select 
      case when v.vehicle_type_id in(1) then 'TATA ACE' when v.vehicle_type_id in(2) then 'BOLERO' when v.vehicle_type_id in(3) then 'T407' when v.vehicle_type_id in(4) then '14ft' when v.vehicle_type_id in(5) then '17ft' when v.vehicle_type_id in(6) then '19ft' when v.vehicle_type_id in(7) then '22ft' when v.vehicle_type_id in(8) then '32ft' else 'Veh. Not Found' end 
    from 
      vehicle v 
    where 
      v.id = vehID
  );
return vehicleType;
end
