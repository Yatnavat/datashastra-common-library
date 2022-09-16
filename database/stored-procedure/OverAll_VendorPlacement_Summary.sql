CREATE DEFINER = `root` @`localhost` PROCEDURE `OverAll_VendorPlacement_Summary`(in ISTDIFF time) begin 
select 
  date(
    addtime(t.trip_start_date, ISTDIFF)
  ) 'Trip Date', 
  dc.delivery_center_name 'Delivery Center', 
  ven.name_of_company 'Vendor Name', 
  case when t.vehicle_type in(1) then 'TATA ACE' when t.vehicle_type in(2) then 'BOLERO' when t.vehicle_type in(3, 4) then 'T407' when t.vehicle_type is null then 'Veh. Not Assigned' else 'Other Veh.' end 'Vehicle Type', 
  tv.vehicle_allocation_type 'Vehicle Allocation Type', 
  count(*) 'No. of Used Vehicles', 
  count(*) 'Trips', 
  sum(
    getTripDPCount(t.id)
  ) 'No. of DPs' 
from 
  trip t, 
  trip_vehicle tv, 
  delivery_center dc, 
  vehicle v, 
  vendor ven 
where 
  t.delivery_center_id = dc.id 
  and t.id = tv.trip_id 
  and v.id = t.vehicle_id 
  and v.vendor_id = ven.id 
group by 
  1, 
  2, 
  3, 
  4, 
  5;
end
