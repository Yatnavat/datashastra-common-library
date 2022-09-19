CREATE DEFINER = `root` @`%` PROCEDURE `TRIPS_SUMMARY_OF_STATUS_BY_VENDOR`(
  IN DCID varchar(60), 
  IN IST_Diff varchar(20)
) BEGIN 
SELECT 
  date(
    addtime(trip_start_date, IST_DIFF)
  ) 'TRIP_DATE', 
  if(
    t.driver_id is not null, 
    (
      select 
        concat_ws(' ', first_name, last_name) 
      from 
        user u 
      where 
        u.id in (
          select 
            v.user_id 
          from 
            vendor v 
          where 
            v.id in (
              select 
                d.vendor_id 
              from 
                driver d 
              where 
                d.id = t.driver_id
            )
        )
    ), 
    'No Vendor'
  ) 'VendorName', 
  count(distinct t.id) 'TripsCount', 
  sum(
    (
      select 
        count(tdp1.id) 
      from 
        tripdp tdp1 
      where 
        t.id = tdp1.trip_id
    )
  ) 'NoOfDP', 
  sum(
    CASE WHEN t.trip_status in (
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
    CASE WHEN t.trip_status in (
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
  ) as 'Cancel' 
FROM 
  trip t 
where 
  t.delivery_center_id = DCID 
GROUP BY 
  1, 
  2;
END
