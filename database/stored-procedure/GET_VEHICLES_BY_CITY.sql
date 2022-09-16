CREATE DEFINER = `root` @`localhost` PROCEDURE `GET_VEHICLES_BY_CITY`() BEGIN 
select 
  v.id Vehicle_ID, 
  v.registration_number 'Vehicle No', 
  round(container_length) 'Length', 
  v.model_name 'Model Name', 
  v.capecity 'Capacity', 
  v.status 'Vehicle Status', 
  ven.id 'Vendor ID', 
  ven.name_of_company 'Vendor Name', 
  ven.owner_name 'Owner Name', 
  ven.city 'Vendor City', 
  v.typeof_vehicle 'Vehicle Type ID', 
  ven.registered_under, 
  ven.committed_vehicles 'Committed Vehicles', 
  dc.delivery_center_name 'DC Name', 
  dc.dc_code 'DC Code', 
  dc.city 'DC City' 
from 
  vehicle v, 
  vendor ven, 
  vendor_delivery_centers vdc, 
  delivery_center dc 
where 
  v.vendor_id = ven.id 
  and vdc.vendor_id = ven.id 
  and delivery_centers_id = dc.id;
END
