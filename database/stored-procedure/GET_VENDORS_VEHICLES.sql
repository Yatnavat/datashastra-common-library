CREATE DEFINER = `root` @`%` PROCEDURE `GET_VENDORS_VEHICLES`(in DCID int) BEGIN 
select 
  v.id ID, 
  v.registration_number 'Vehicle No', 
  round(container_length) 'Length', 
  v.model_name 'Model Name', 
  v.capecity 'Capacity', 
  v.status 'Vehicle Status', 
  ven.id 'Vendor ID', 
  ven.name_of_company 'Vendor Name', 
  ven.owner_name 'Owner Name' 
from 
  vehicle v, 
  vendor ven, 
  vendor_delivery_centers vdc 
where 
  v.vendor_id = ven.id 
  and vdc.vendor_id = ven.id 
  and vdc.delivery_centers_id = DCID;
END
