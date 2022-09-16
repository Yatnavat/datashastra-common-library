CREATE DEFINER = `root` @`localhost` PROCEDURE `OVERALL_TRIP_DATA_VALIDATION`(IN IST_DIFF time) BEGIN 
SELECT 
  IF(
    creation_source IN('OFFLINE_TMS'), 
    date(
      addtime(
        addtime (
          timestamp (
            date(trip_start_date), 
            '00:00:00'
          ), 
          TIME(trip_start_time)
        ), 
        IST_DIFF
      )
    ), 
    date(
      addtime(trip_start_date, IST_DIFF)
    )
  ) Trip_Date, 
  if(
    t.created_by is null, 
    'Created BY Not Found', 
    (
      select 
        concat_ws(' ', u.first_name, u.last_name) 
      from 
        user u 
      where 
        t.created_by = u.id
    )
  ) 'Created By', 
  dc.city 'Location', 
  if(
    isnull(t.client_id), 
    '-', 
    getTripClient(t.id)
  ) 'Client_Name', 
  -- end of If
  dc.delivery_center_name 'DC Name', 
  t.creation_source 'Source', 
  getVehicleTypeByVehID(t.vehicle_id) VehType, 
  t.trip_status, 
  count(distinct t.id) Trips, 
  round(
    avg(
      IF(
        t.creation_source IN('OFFLINE_TMS') 
        AND t.otd > 1, 
        t.otd, 
        if(
          time(
            addtime(t.trip_end_time, IST_DIFF)
          )< '12:30:00', 
          '100', 
          0
        )
      )
    ), 
    1
  ) 'OTD', 
  round(
    SUM(t.total_weight / 1000), 
    1
  ) 'Tonnage', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS') 
      AND t.trip_status in('COMPLETETRIP') 
      and t.otd < 1, 
      1, 
      0
    )
  ) 'Blank OTD', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS') 
      AND t.trip_status in('COMPLETETRIP') 
      and t.total_weight < 1, 
      1, 
      0
    )
  ) 'Blank Weight', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS') 
      AND t.trip_status in('COMPLETETRIP') 
      and t.expectedcodamt - t.codamt_received > 1000, 
      1, 
      IF(
        t.creation_source IN('OFFLINE_TMS') 
        AND t.trip_status in('COMPLETETRIP') 
        and t.codamt_received - t.expectedcodamt > 1000, 
        1, 
        0
      )
    )
  ) 'Invalid COD AMT', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS') 
      AND t.trip_status in('COMPLETETRIP') 
      and t.trip_start_shipperbox_loading - t.trip_end_shipperbox_loading > 50, 
      1, 
      IF(
        t.creation_source IN('OFFLINE_TMS') 
        AND t.trip_status in('COMPLETETRIP') 
        and t.trip_end_shipperbox_loading - t.trip_end_shipperbox_loading > 50, 
        1, 
        0
      )
    )
  ) 'Invalid Shipper Boxes', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS') 
      AND t.trip_status in('COMPLETETRIP') 
      and t.trip_start_trolly_loading - t.trip_end_trolly_loading > 30, 
      1, 
      IF(
        t.creation_source IN('OFFLINE_TMS') 
        AND t.trip_status in('COMPLETETRIP') 
        and t.trip_end_trolly_loading - t.trip_start_trolly_loading > 30, 
        1, 
        0
      )
    )
  ) 'Invalid Trolley', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS') 
      AND t.trip_status in('COMPLETETRIP') 
      and t.trip_start_gelpad - t.trip_end_gelpad > 10, 
      1, 
      IF(
        t.creation_source IN('OFFLINE_TMS') 
        AND t.trip_status in('COMPLETETRIP') 
        and t.trip_end_gelpad - t.trip_end_gelpad > 10, 
        1, 
        0
      )
    )
  ) 'Invalid Gel Pad', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS') 
      AND t.trip_status in('COMPLETETRIP') 
      and t.trip_start_bags - t.trip_end_bags > 10, 
      1, 
      IF(
        t.creation_source IN('OFFLINE_TMS') 
        AND t.trip_status in('COMPLETETRIP') 
        and t.trip_end_bags - t.trip_start_bags > 10, 
        1, 
        0
      )
    )
  ) 'Invalid Bags', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS') 
      AND t.trip_status in('COMPLETETRIP') 
      and t.trip_start_icebox - t.trip_end_icebox > 10, 
      1, 
      IF(
        t.creation_source IN('OFFLINE_TMS') 
        AND t.trip_status in('COMPLETETRIP') 
        and t.trip_end_icebox - t.trip_end_icebox > 10, 
        1, 
        0
      )
    )
  ) 'Invalid Ice Boxes', 
  SUM(
    IF(
      t.trip_status in('COMPLETETRIP') 
      and t.trip_start_reading - t.trip_end_reading > 1, 
      1, 
      IF(
        t.trip_status in('COMPLETETRIP') 
        and t.trip_end_reading - t.trip_start_reading > 200, 
        1, 
        0
      )
    )
  ) 'INVALID READING', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS') 
      AND t.trip_status in('COMPLETETRIP') 
      and t.no_of_delivery_points < 1, 
      1, 
      IF(
        t.trip_status in('COMPLETETRIP') 
        and t.creation_source NOT IN('OFFLINE_TMS'), 
        isnull(
          getTripDPCount(t.id)
        ), 
        0
      )
    )
  ) 'Blank DPs' 
FROM 
  trip t, 
  delivery_center dc 
WHERE 
  t.delivery_center_id = dc.id -- AND IF(creation_source IN('OFFLINE_TMS'), date(addtime(addtime ( timestamp (date(trip_start_date),'00:00:00'),TIME(trip_start_time)),IST_DIFF)),
  -- date(addtime(trip_start_date, IST_DIFF))) BETWEEN FROM_DATE AND TO_DATE
group by 
  1, 
  2, 
  3, 
  4, 
  5, 
  6, 
  7, 
  8;
END
