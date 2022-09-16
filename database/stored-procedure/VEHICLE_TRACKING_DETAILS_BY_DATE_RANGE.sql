CREATE DEFINER = `root` @`localhost` PROCEDURE `VEHICLE_TRACKING_DETAILS_BY_DATE_RANGE`(
  IN StartDateRange DATE, EndDateRange DATE
) BEGIN 
select 
  vehicle_no, 
  min(time) StartTime, 
  round(
    max(time), 
    2
  ) EndTime, 
  min(speed) minSpeed, 
  round(
    max(speed), 
    2
  ) maxSpeed, 
  round(
    avg(speed), 
    2
  ) avgSpeed, 
  min(battery_current) minBattery_current, 
  max(battery_current) maxBattery_current, 
  round(
    avg(battery_current), 
    2
  ) avgBattery_current, 
  min(battery_temp) minBattery_temp, 
  max(battery_temp) maxBattery_temp, 
  round(
    avg(battery_temp), 
    2
  ) avgBattery_temp, 
  min(battery_voltage) minBattery_voltage, 
  max(battery_voltage) maxBattery_voltage, 
  round(
    avg(battery_voltage), 
    2
  ) avgBattery_voltage, 
  (
    select 
      concat_ws(',', latitude, longitude) 
    from 
      vehicle_tracking v2 
    where 
      v2.vehicle_no = v1.vehicle_no 
    order by 
      v2.vehicle_tracking_id 
    limit 
      1
  ) StartLatLong, 
  (
    select 
      concat_ws(',', latitude, longitude) 
    from 
      vehicle_tracking v2 
    where 
      v2.vehicle_no = v1.vehicle_no 
    order by 
      v2.vehicle_tracking_id desc 
    limit 
      1
  ) EndLatLong 
from 
  vehicle_tracking v1 
where 
  v1.time between StartDateRange 
  and EndDateRange 
group by 
  1;
END
