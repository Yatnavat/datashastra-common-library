CREATE DEFINER = `root` @`localhost` PROCEDURE `OVERALL_TRIPS_SUMMARY_OF_STATUS_BY_RANGE`(
  IN frmDate date, toDate date, IST_Diff time
) BEGIN 
SELECT 
  if(
    t.driver_id IS NOT NULL, 
    date(
      addtime(t.trip_start_date, IST_Diff)
    ), 
    date(
      addtime(t.trip_start_time, IST_Diff)
    )
  ) Trip_Date, 
  dc.city City, 
  dc.delivery_center_name 'Delivery Center', 
  count(distinct t.id) 'Trips', 
  sum(
    if (
      t.creation_source in('OFFLINE_TMS'), 
      t.no_of_delivery_points, 
      getTripDPCount(t.id)
    )
  ) 'NoOfDP', 
  sum(
    CASE WHEN t.trip_status in(
      'TRIPSTART', 'DPCHECKIN', 'UNLOADINGCOMPLETE', 
      'COLLECTENDTRIPDETAILS', 'TRIPSUMMARY'
    ) then (
      select 
        count(*) 
      FROM 
        trip t2 
      where 
        t.id = t2.id
    ) end
  ) as 'Veh. Assigned', 
  sum(
    CASE WHEN t.trip_status in(
      'DCCHECKIN', 'LOADINGSTART', 'LOADING', 
      'COLLECTDOCUMENT', 'TRIPREADING'
    ) then (
      select 
        count(*) 
      FROM 
        trip t2 
      where 
        t.id = t2.id
    ) end
  ) as 'Veh. Not Assigned', 
  sum(
    CASE WHEN t.trip_status = 'COMPLETETRIP' then (
      select 
        count(*) 
      FROM 
        trip t2 
      where 
        t.id = t2.id
    ) end
  ) as 'Complete', 
  sum(
    CASE WHEN t.trip_status = 'CANCELTRIP' then (
      select 
        count(*) 
      FROM 
        trip t2 
      where 
        t.id = t2.id
    ) end
  ) as 'Cancel', 
  sum(
    CASE WHEN t.trip_status = 'HOLD' then (
      select 
        count(*) 
      FROM 
        trip t2 
      where 
        t.id = t2.id
    ) end
  ) as 'Hold', 
  sum(
    CASE WHEN t.trip_status = 'ABSENT' then (
      select 
        count(*) 
      FROM 
        trip t2 
      where 
        t.id = t2.id
    ) end
  ) as 'Absent' 
FROM 
  trip t, 
  delivery_center dc 
where 
  t.delivery_center_id = dc.id 
  and if (
    t.creation_source in('OFFLINE_TMS'), 
    flag in(1), 
    flag in(null, 1)
  ) 
  AND if(
    t.driver_id IS NOT NULL, 
    date(
      addtime(t.trip_start_date, IST_Diff)
    ), 
    date(
      addtime(t.trip_start_time, IST_Diff)
    )
  ) BETWEEN frmDate 
  and toDate 
group by 
  1, 
  2, 
  3;
END
