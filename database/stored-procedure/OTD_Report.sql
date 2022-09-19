CREATE DEFINER = `root` @`localhost` PROCEDURE `OTD_Report`() BEGIN -- OTD Report/ Report No. 3
SET 
  @row_number = 0;
SELECT 
  (@row_number := @row_number + 1) AS 'Sr No', 
  dc.dc_code 'DC CODE', 
  dc.delivery_center_name 'Delivery Center Name', 
  dp.customer_code 'DP Code', 
  dp.delivery_point_name 'Delivery Point Name', 
  tdp.delivery_process_status 'Delivery Process Status', 
  tdp.id 'TDP ID', 
  tdp.estimated_time 'Estimated Time', 
  date(
    addtime(check_in_time, '5:30')
  ) 'Check In Date', 
  date_format(
    addtime(check_in_time, '5:30'), 
    '%r'
  ) 'Check In Time', 
  ven.unique_code 'Vendor Code', 
  concat_ws(' ', uv.first_name, uv.last_name) 'Vendor Name', 
  dp.client_id 'Client ID', 
  c.client_name 'Client Name', 
  c.user_status 'Client Status', 
  t.id 'Trip ID', 
  trip_name 'Trip Name', 
  v.registration_number 'Vehicle No', 
  v.status 'Vehicle Status', 
  r.route_name 'Route Name', 
  rp.sequence_id 'Planned Sequence', 
  date(t.created_date) 'Trip Created On', 
  date_format(
    addtime(trip_start_time, '5:30'), 
    '%M-%Y'
  ) 'Month', 
  date(
    addtime(trip_start_time, '5:30')
  ) 'Trip Start Date', 
  date_format(
    addtime(t.trip_start_time, '5:30'), 
    '%r'
  ) 'Trip Start Time', 
  date_format(t.trip_end_time, '%r') 'Trip End Time', 
  t.trip_status 'Trip Status', 
  round(t.trip_start_reading) 'Trip Start Reading', 
  round(t.trip_end_reading) 'Trip End Reading', 
  date(ot.delivery_date) 'Date of Delivery (Planned)', 
  date_format(ot.delivery_date, '%r') 'Time of Delivery (Planned)', 
  date(
    addtime(tdp.check_out_time, '5:30')
  ) 'Date of Delivery (Actual', 
  date_format(
    addtime(tdp.check_out_time, '5:30'), 
    '%r'
  ) 'Time of Delivery (Actual)', 
  timestampdiff(
    minute, 
    addtime(
      date(
        addtime(tdp.check_in_time, '5:30')
      ), 
      '13:00:30'
    ), 
    time(
      addtime(tdp.check_in_time, '5:30')
    )
  ) 'Delay in Minutes', 
  case when dp.client_id = 7 then (
    if(
      time(
        addtime(tdp.check_in_time, '5:30')
      ) <= '13:00:30', 
      true, 
      false
    )
  ) when dp.client_id in (5, 6) then (
    if(
      timestampdiff(
        minute, 
        addtime(tdp.check_in_time, '5:30'), 
        ot.delivery_date
      ) < 0, 
      true, 
      false
    )
  ) else '0' end 'OnTime', 
  case when dp.client_id = 7 then (
    if(
      time(
        addtime(tdp.check_in_time, '5:30')
      ) <= '13:00:30', 
      'On time', 
      if(
        tdp.delivery_process_status not in ('CHECKIN', 'DELIVERED'), 
        'Not Delivered', 
        'Delay'
      )
    )
  ) when dp.client_id in (5, 6) then (
    if(
      timestampdiff(
        minute, 
        addtime(tdp.check_in_time, '5:30'), 
        ot.delivery_date
      ) < 0, 
      'On Time', 
      if(
        tdp.delivery_process_status not in ('CHECKIN', 'DELIVERED'), 
        'Not Delivered', 
        'Delay'
      )
    )
  ) else '0' end 'IsOnTime', 
  case when dp.client_id = 7 then (
    if(
      time(
        addtime(tdp.check_in_time, '5:30')
      ) <= '13:00:30', 
      '100', 
      '0'
    )
  ) when dp.client_id in (5, 6) then (
    if(
      timestampdiff(
        minute, 
        addtime(tdp.check_in_time, '5:30'), 
        ot.delivery_date
      ) < 0, 
      '100', 
      '0'
    )
  ) else '0' end 'OTD %', 
  case when hour(
    addtime(tdp.check_in_time, '5:30')
  ) >= 0 
  and hour(
    addtime(tdp.check_in_time, '5:30')
  ) < 2 then '00-02' when hour(
    addtime(tdp.check_in_time, '5:30')
  ) >= 2 
  and hour(
    addtime(tdp.check_in_time, '5:30')
  ) < 4 then '02-04' when hour(
    addtime(tdp.check_in_time, '5:30')
  ) >= 4 
  and hour(
    addtime(tdp.check_in_time, '5:30')
  ) < 6 then '04-06' when hour(
    addtime(tdp.check_in_time, '5:30')
  ) >= 6 
  and hour(
    addtime(tdp.check_in_time, '5:30')
  ) < 8 then '06-08' when hour(
    addtime(tdp.check_in_time, '5:30')
  ) >= 8 
  and hour(
    addtime(tdp.check_in_time, '5:30')
  ) < 10 then '08-10' when hour(
    addtime(tdp.check_in_time, '5:30')
  ) >= 10 
  and hour(
    addtime(tdp.check_in_time, '5:30')
  ) < 12 then '10-12' when hour(
    addtime(tdp.check_in_time, '5:30')
  ) >= 12 
  and hour(
    addtime(tdp.check_in_time, '5:30')
  ) < 14 then '12-14' when hour(
    addtime(tdp.check_in_time, '5:30')
  ) >= 14 
  and hour(
    addtime(tdp.check_in_time, '5:30')
  ) < 16 then '14-16' when hour(
    addtime(tdp.check_in_time, '5:30')
  ) >= 16 
  and hour(
    addtime(tdp.check_in_time, '5:30')
  ) < 18 then '16-18' when hour(
    addtime(tdp.check_in_time, '5:30')
  ) >= 18 
  and hour(
    addtime(tdp.check_in_time, '5:30')
  ) < 20 then '18-20' when hour(
    addtime(tdp.check_in_time, '5:30')
  ) >= 20 
  and hour(
    addtime(tdp.check_in_time, '5:30')
  ) < 22 then '20-22' when hour(
    addtime(tdp.check_in_time, '5:30')
  ) >= 22 
  and hour(
    addtime(tdp.check_in_time, '5:30')
  ) < 24 then '22-24' else 'Something went wrong' end 'CheckIn Time Span' 
from 
  trip t, 
  tripdp tdp, 
  delivery_center dc, 
  driver d, 
  vehicle v, 
  route r, 
  vendor ven, 
  user uv, 
  delivery_point dp, 
  route_path rp, 
  client c, 
  order_tbl ot, 
  trip_orders tord 
where 
  t.delivery_center_id = dc.id 
  and t.driver_id = d.id 
  and t.route_id = r.id 
  and t.vehicle_id = v.id 
  and tdp.trip_id = t.id 
  and tdp.delivery_point_id = dp.id 
  and ven.id = v.vendor_id 
  and d.vendor_id = ven.id 
  and uv.id = ven.user_id 
  and r.delivery_center_id = t.delivery_center_id 
  and dp.delivery_center_id = dc.id 
  and rp.route_id = r.id 
  and rp.dp_id = dp.id 
  and dp.client_id = c.id 
  and ot.delivery_point_id = dp.id 
  and ot.delivery_center_id = dc.id 
  and t.id = tord.trip_id 
  and tord.orders_id = ot.id 
  and t.trip_status not in ('CANCELTRIP');
END
