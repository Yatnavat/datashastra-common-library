CREATE DEFINER = `root` @`%` PROCEDURE `GET_VENDORS_DRIVERS`(IN DCID int) BEGIN 
select 
  d.id 'Driver ID', 
  concat_ws(' ', u.first_name, u.last_name) 'Driver Name', 
  right(
    trim(u.phone_number), 
    10
  ) 'Mobile No', 
  ven.id 'Vendor ID', 
  ven.name_of_company 'Vendor Name', 
  ven.owner_name 'Owner Name' 
from 
  driver d, 
  vendor ven, 
  vendor_delivery_centers vdc, 
  user u 
where 
  d.user_id = u.id 
  and d.vendor_id = ven.id 
  and vdc.vendor_id = ven.id 
  and vdc.delivery_centers_id = DCID;
END
