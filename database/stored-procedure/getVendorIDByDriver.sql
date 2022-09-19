CREATE DEFINER = `root` @`localhost` FUNCTION `getVendorIDByDriver`(
  DriverID varchar(100)
) RETURNS varchar(100) CHARSET utf8mb4 DETERMINISTIC begin declare vendorID varchar(100);
set 
  vendorID = if(
    isnull(DriverID), 
    'No Vendor', 
    (
      select 
        d.vendor_id 
      from 
        driver d 
      where 
        d.id = DriverID
    )
  );
return vendorID;
end
