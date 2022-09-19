CREATE DEFINER = `root` @`localhost` PROCEDURE `TRIP_DATA_VALIDATION`(
  IN FROM_DATE DATE, TO_DATE DATE, IST_DIFF TIME
) BEGIN 
SELECT 
  concat_ws(' ', u.first_name, u.last_name) 'Created By', 
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
  dc.city 'Location', 
  if(
    isnull(t.client_id), 
    
    /*  true condition*/
    (
      select 
        client_name 
      from 
        client 
      where 
        id in(
          select 
            client_id 
          from 
            delivery_point 
          where 
            id in(
              select 
                delivery_point_id 
              from 
                tripdp 
              where 
                trip_id in(t.id)
            )
        )
    ), 
    
    /* False condition */
    (
      select 
        client_name 
      from 
        client 
      where 
        id in (t.client_id)
    )
  ) 'Client_Name', 
  -- end of If
  dc.delivery_center_name 'DC Name', 
  t.creation_source 'Source', 
  getVehicleTypeByVehID(t.id, t.vehicle_id) VehType, 
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
  ) 'GAP VARY OF COD AMT', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS') 
      AND t.trip_status in('COMPLETETRIP') 
      and t.trip_start_shipperbox_loading - t.trip_end_shipperbox_loading > 50, 
      1, 
      IF(
        t.creation_source IN('OFFLINE_TMS') 
        AND t.trip_status in('COMPLETETRIP') 
        and t.trip_end_shipperbox_loading - t.trip_start_shipperbox_loading > 50, 
        1, 
        0
      )
    )
  ) 'GAP VARY OF Shipper Boxes', 
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
  ) 'GAP VARY OF Trolly', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS') 
      AND t.trip_status in('COMPLETETRIP') 
      and t.trip_start_gelpad - t.trip_end_gelpad > 10, 
      1, 
      IF(
        t.creation_source IN('OFFLINE_TMS') 
        AND t.trip_status in('COMPLETETRIP') 
        and t.trip_end_gelpad - t.trip_start_gelpad > 10, 
        1, 
        0
      )
    )
  ) 'GAP VARY OF Gel Pad', 
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
  ) 'GAP VARY OF BAGS', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS') 
      AND t.trip_status in('COMPLETETRIP') 
      and t.trip_start_icebox - t.trip_end_icebox > 10, 
      1, 
      IF(
        t.creation_source IN('OFFLINE_TMS') 
        AND t.trip_status in('COMPLETETRIP') 
        and t.trip_end_icebox - t.trip_start_icebox > 10, 
        1, 
        0
      )
    )
  ) 'GAP VARY OF ICE BOXES', 
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
  ) 'Null DPs', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS'), 
      t.no_of_delivery_points, 
      getTripDPCount(t.id)
    )
  ) 'DPs', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS'), 
      t.no_ofdps_delivered, 
      getTripDPCount(t.id)
    )
  ) 'DPs Delivered', 
  SUM(
    IF(
      t.creation_source IN('OFFLINE_TMS'), 
      t.no_of_delivery_points - t.no_ofdps_delivered, 
      getTripDPCount(t.id)
    )
  ) 'DPs Returned', 
  SUM(
    t.codamt_received - t.expectedcodamt
  ) 'Total Of COD AMT', 
  SUM(
    t.trip_end_shipperbox_loading - t.trip_start_shipperbox_loading
  ) 'Total Of Shipper Boxes', 
  SUM(
    t.trip_end_trolly_loading - t.trip_start_trolly_loading
  ) 'Total Of Trolley', 
  SUM(
    t.trip_end_gelpad - t.trip_start_gelpad
  ) 'Gap Of Gel Pad', 
  SUM(
    t.trip_end_bags - t.trip_start_bags
  ) 'Total Of BAGS', 
  SUM(
    t.trip_end_icebox - t.trip_start_icebox
  ) 'Total Of ICE BOXES', 
  SUM(t.invoice_number_count) 'Total Invoices' 
FROM 
  trip t, 
  delivery_center dc, 
  user u 
WHERE 
  t.created_by = u.id 
  and t.delivery_center_id = dc.id 
  AND IF(
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
  ) BETWEEN FROM_DATE 
  AND TO_DATE 
  and t.trip_status not in('CANCELTRIP') 
  and IF(
    creation_source IN('OFFLINE_TMS'), 
    t.flag in (1), 
    creation_source not in ('OFFLINE_TMS')
  ) 
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
