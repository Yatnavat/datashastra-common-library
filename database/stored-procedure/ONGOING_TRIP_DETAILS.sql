DROP PROCEDURE IF EXISTS ONGOING_TRIP_DETAILS;
CREATE PROCEDURE ONGOING_TRIP_DETAILS(in city      varchar(30),
                                      IN dc_id     int,
                                      IN client_id int,
                                      IN ist_diff  time,
                                      IN frmDate  date, toDate date)
BEGIN
  SELECT distinctrow t.id 'tripID',
         trip_name 'tripName',
  IF(t.trip_status IN('DCCHECKIN',
                      'LOADINGSTART',
                      'LOADING',
                      'COLLECTDOCUMENT',
                      'TRIPREADING'), date_format (addtime(t.trip_start_date, ist_diff),'%d-%M-%y %h:%i %p'),date_format (addtime(t.trip_start_time, ist_diff),'%d-%M-%y %h:%i %p')) 'tripDate', gettripclient(t.id) clientname,
  IF(t.vehicle_id IS NOT NULL, getvehiclenumbyid(t.vehicle_id),'-') 'vehicleNo',
  IF(t.vehicle_id IS NOT NULL,
  (
         SELECT v.container_length
         FROM   vehicle v
         WHERE  v.id = t.vehicle_id),
  (
         SELECT length_of_container
         FROM   vehicle_type_master
         WHERE  id=t.vehicle_type)) 'containerLength',
  IF(t.vehicle_id IS NOT NULL,
  (
         SELECT v.model_name
         FROM   vehicle v
         WHERE  v.id = t.vehicle_id), '-') 'model_Name',
  IF(
      (
      SELECT count(*)
      FROM   trip_vehicle tv
      WHERE  trip_id = t.id) > 0,
     (
            SELECT date_format(addtime(dc_chekin_time, ist_diff), '%d-%M-%y %h:%i %p')
            FROM   trip_vehicle tv
            WHERE  trip_id = t.id), '-') 'dcCheckInTime',
  time_format(time(addtime(t.trip_start_time, ist_diff)), '%r') 'InTime',
  time_format(time(addtime(t.trip_end_time, ist_diff)), '%r') 'ReturnTime',
  timediff( addtime(t.trip_end_time, ist_diff), addtime(t.trip_start_time, ist_diff)) 'DutyHrs',
  dc.delivery_center_name 'deliveryCenter',
  gettripdpcount(t.id) 'NoOfDP',
  t.trip_end_reading - t.trip_start_reading 'OdometerKms',
  t.tripkm 'GoogleKms',
  IF(t.driver_id IS NOT NULL, getvendorbydriver(t.driver_id),'-') 'vendorName',
  IF(t.driver_assistant_id IS NOT NULL,getdabyid(t.driver_assistant_id) , '-') 'driverAssistantName',
  IF(t.driver_id IS NOT NULL, getdriverbyid(t.driver_id), '-') 'driverName',
  IF(t.driver_id IS NOT NULL,getdrivermobilebyid(t.driver_id), '-') 'driverPhoneNo',
  t.trip_status 'tripStatus',
  t.toll 'toll',
  t.vehicle_type 'vehicleType' ,
  t.total_weight 'total_weight',
  t.total_weight_delivered 'total_weight_delivered',
  IF(t.driver_id IS NULL ,'-',getdrivermobilebyid(t.driver_id) ) 'driver_Mobile',
  IF(t.driver_assistant_id IS NULL ,'-',getdabyid(t.driver_assistant_id)) 'DA_Name',
  IF(t.driver_assistant_id IS NOT NULL,getdamobilebyid(t.driver_assistant_id) ,'-') driverassistantphoneno,
  IF(t.created_by IS NULL,'-',
  (
         SELECT concat_ws(' ',first_name,last_name)
         FROM   user
         WHERE  id=t.created_by))tripcreatedby,
  IF(t.updated_by IS NULL,'-',
  (
         SELECT concat_ws(' ',first_name,last_name)
         FROM   user
         WHERE  id=t.updated_by))tripupdatedby,
  IF(t.updated_by IS NULL,'-',
  (
         SELECT user_type
         FROM   user
         WHERE  id=t.updated_by))completedby,
       'FirstLogOn',
 'LastLogOn',
 'FirstLatLong',
 'LastLatLong',
 true 'TrackingStatus'

  FROM trip t left join trip_vehicle tv on t.id = tv.trip_id
  left join tripdp tdp on t.id = tdp.trip_id
  left join delivery_center dc on t.delivery_center_id = dc.id
  WHERE
  t.trip_status NOT IN ('CANCELTRIP',
                        'COMPLETETRIP',
                        'HOLD',
                        'ABSENT')
  AND
  creation_source NOT IN ('OFFLINE_TMS')
  AND
  (
    city IS NULL
    OR
    dc.city=city
  )
  AND
  (
    dc_id IS NULL
    OR
    t.delivery_center_id=dc_id
  )
  AND
  (
    client_id IS NULL
    OR
    IF(client_id                                      IN(7),t.client_id IN(7,
                                      33),t.client_id IN(client_id))
  )
  AND
  IF(t.trip_status IN('DCCHECKIN',
                      'LOADINGSTART',
                      'LOADING',
                      'COLLECTDOCUMENT',
                      'TRIPREADING'), (if(frmDate is null ,toDate,frmDate) IS NULL
  OR
  date(addtime(t.trip_start_date, ist_diff))=if(frmDate is null ,toDate,frmDate) ),(if(frmDate is null ,toDate,frmDate) IS NULL
  OR
  date(addtime(t.trip_start_time, ist_diff)) between if(frmDate is null ,toDate,frmDate) and toDate))
 ORDER BY t.id DESC;
end;