CREATE DEFINER = `root` @`localhost` PROCEDURE `Summary_Utilization_BY_DAY`(IN TRIP_DATE DATE) BEGIN 
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
  ) Date, 
  dc.city 'DC City', 
  dc.delivery_center_name 'Delivery Center', 
  COUNT(t.id) 'No. of Trips', 
  sum(
    if(
      creation_source IN('OFFLINE_TMS'), 
      no_of_delivery_points, 
      getTripDPCount(t.id)
    )
  ) 'No. of Orders', 
  count(t.vehicle_id) 'No. of Vehicles', 
  count(distinct t.vehicle_id) 'Unique Vehicles' 
FROM 
  trip t, 
  delivery_center dc 
WHERE 
  t.delivery_center_id = dc.id 
  AND if(
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
  ) = TRIP_DATE 
  and if (
    t.creation_source in('OFFLINE_TMS'), 
    flag in(1), 
    flag in(null, 1)
  ) 
group by 
  1, 
  2, 
  3;
end
