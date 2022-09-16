CREATE DEFINER = `root` @`localhost` PROCEDURE `OverAll_TRIPS_KMs_OF_VENDOR`(
  IN IST_Diff varchar(20)
) BEGIN 
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
  dc.delivery_center_name 'Delivery Center', 
  if(
    t.driver_id is not null, 
    getVendorByDriver(t.driver_id), 
    ''
  ) 'vendorName', 
  round(
    sum(t.tripkm)
  ) 'Google KMs', 
  round(
    sum(
      t.trip_end_reading - t.trip_start_reading
    )
  ) 'Odoemeter KMs' 
FROM 
  trip t, 
  delivery_center dc 
where 
  t.delivery_center_id = dc.id 
  and t.trip_status in ('COMPLETETRIP', 'HOLD', 'ABSENT') 
  and if (
    t.creation_source in('OFFLINE_TMS'), 
    t.flag in(1), 
    t.flag in(null, 1)
  ) 
group by 
  1, 
  2, 
  3, 
  4;
END
