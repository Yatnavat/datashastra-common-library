CREATE DEFINER = `root` @`%` PROCEDURE `TRIPS_SUMMARY_OF_STATUS_BY_DATE`(
  IN TRIP_DATE date, 
  IN DCID varchar(60), 
  IN IST_Diff varchar(20)
) BEGIN 
SELECT 
  count(distinct t.id) 'Trips', 
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
  ) as 'Not Assigned', 
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
  ) as 'Assigned', 
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
  ) as 'Cancelled' 
FROM 
  trip t 
where 
  t.delivery_center_id = DCID 
  AND date(
    addtime(trip_start_date, IST_DIFF)
  ) = TRIP_DATE;
END
