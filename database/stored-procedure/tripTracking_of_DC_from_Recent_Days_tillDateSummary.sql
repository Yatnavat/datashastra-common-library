CREATE DEFINER = `root` @`localhost` PROCEDURE `tripTracking_of_DC_from_Recent_Days_tillDateSummary`(in dcID int, Days int) begin 
SELECT 
  u.id, 
  concat_ws(' ', u.first_name, u.last_name) 'Driver Name', 
  u.phone_number, 
  count(*) LatLong, 
  count(
    distinct date(
      addtime(trip_start_time, '5:30')
    )
  ) Days, 
  round(
    count(*) / count(
      distinct date(
        addtime(trip_start_time, '5:30')
      )
    ), 
    0
  ) AVG, 
  dd.* 
FROM 
  device_detail dd, 
  user u, 
  trip_driver_tracking tdt, 
  trip t 
WHERE 
  dd.ID = u.device_detail_id 
  and u.id = tdt.user_id 
  and t.id = tdt.trip_id 
  and delivery_center_id = dcID 
  and date(
    addtime(trip_start_time, '5:30')
  )>= curdate()- Days 
group by 
  1, 
  2, 
  3;
end
