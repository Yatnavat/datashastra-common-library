CREATE DEFINER = `root` @`localhost` PROCEDURE `Vehicles_Type_Summary_of_Vendor`(IN IST_DIFF time) BEGIN 
SELECT 
  if(
    t.trip_status in(
      'DCCHECKIN', 'LOADINGSTART', 'LOADING', 
      'COLLECTDOCUMENT', 'TRIPREADING'
    ), 
    date(
      addtime(t.trip_start_date, '5:30')
    ), 
    date(
      addtime(t.trip_start_time, '5:30')
    )
  ) Trip_Date, 
  dc.city 'DC City', 
  dc.delivery_center_name DC_Name, 
  t.creation_source 'Source', 
  if(
    t.driver_id is not null, 
    getVendorByDriver(t.driver_id), 
    '-'
  ) 'VendorName', 
  case when vehicle_type in(1) then ('TATA ACE') when vehicle_type in(2) then ('BOLERO') when vehicle_type in(3, 4) then ('T407') else 'Not Provided' end VehicleType, 
  count(*) Vehicles, 
  round(
    sum(t.tripkm), 
    1
  ) 'TotalKMs' 
FROM 
  trip t, 
  delivery_center dc 
where 
  dc.id = t.delivery_center_id 
  and if (
    t.creation_source in('OFFLINE_TMS'), 
    t.flag in(1), 
    t.flag in(null, 1)
  ) 
GROUP BY 
  1, 
  2, 
  3, 
  4, 
  5, 
  6 
ORDER BY 
  1;
end
