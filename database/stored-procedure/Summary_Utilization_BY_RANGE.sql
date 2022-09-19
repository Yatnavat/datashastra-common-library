CREATE DEFINER = `root` @`localhost` PROCEDURE `Summary_Utilization_BY_RANGE`(
  IN frmDate date, toDate DATE, istDiff time
) BEGIN 
SELECT 
  if(
    t.trip_status in(
      'DCCHECKIN', 'LOADINGSTART', 'LOADING', 
      'COLLECTDOCUMENT', 'TRIPREADING'
    ), 
    date(
      addtime(t.trip_start_date, istDiff)
    ), 
    date(
      addtime(t.trip_start_time, istDiff)
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
      addtime(t.trip_start_date, istDiff)
    ), 
    date(
      addtime(t.trip_start_time, istDiff)
    )
  ) BETWEEN frmDate 
  AND toDate 
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
