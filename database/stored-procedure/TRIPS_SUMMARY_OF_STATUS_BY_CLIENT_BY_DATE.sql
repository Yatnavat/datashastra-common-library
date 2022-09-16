CREATE DEFINER = `root` @`%` PROCEDURE `TRIPS_SUMMARY_OF_STATUS_BY_CLIENT_BY_DATE`(
  IN TRIP_DATE date, 
  IN DCID varchar(60), 
  IN IST_Diff varchar(20)
) BEGIN 
SELECT 
  date(
    addtime(trip_start_date, IST_DIFF)
  ) 'TRIP_DATE', 
  (
    select 
      distinct client_name 
    from 
      client 
    where 
      id in (
        select 
          distinct client_id 
        from 
          tripdp tdp1, 
          delivery_point dp1 
        where 
          tdp1.trip_id = t.id 
          and tdp1.delivery_point_id = dp1.id 
          and t.delivery_center_id = DCID
      )
  ) 'ClientName', 
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
  ) as 'Vh. Not Assigned', 
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
  ) as 'Vh. Assigned', 
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
  AND date(
    addtime(trip_start_date, IST_DIFF)
  ) = TRIP_DATE 
GROUP BY 
  1, 
  2;
END
