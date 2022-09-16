CREATE DEFINER = `root` @`localhost` FUNCTION `getVendorByVehicle`(
  vehID varchar(100)
) RETURNS varchar(100) CHARSET utf8mb4 DETERMINISTIC begin declare vendorName varchar(100);
set 
  vendorName = if(
    isnull(vehID), 
    'No Vendor', 
    (
      select 
        name_of_company 
      from 
        vendor v 
      where 
        v.id in (
          select 
            vendor_id 
          from 
            vehicle v 
          where 
            v.id = vehID
        )
    )
  );
return vendorName;
end
