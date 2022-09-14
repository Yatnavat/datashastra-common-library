drop procedure if exists TRIP_HISTORY_DETAILS;
create procedure TRIP_HISTORY_DETAILS(IN CITY varchar(30), IN DC_ID int, IN CLIENT_ID int,
                                                             frmDate date ,IN toDate date, IN IST_DIFF time)
    BEGIN
        SELECT t.id                                                                                          'tripID',
               trip_name                                                                                     'tripName',
               date_format (addtime(t.trip_start_time, IST_DIFF),'%d-%M-%y %h:%i %p')                                                  'tripDate',
               getTripClient(t.id)                                                                           clientName,
               if(t.vehicle_id is not null, (select v.registration_number from vehicle v where v.id = t.vehicle_id),
                  '-')                                                                                       'vehicleNo',
               if(t.vehicle_id is not null, (select v.container_length from vehicle v where v.id = t.vehicle_id),
                  '-')                                                                                       'containerLength',
               if(t.vehicle_id is not null, (select v.model_name from vehicle v where v.id = t.vehicle_id),
                  '-')                                                                                       'model_Name',
               if((select count(*) from trip_vehicle tv where trip_id = t.id) > 0,
                  (select date_format(addtime(dc_chekin_time, IST_DIFF), '%d-%m-%y %h:%i %p') from trip_vehicle tv where trip_id = t.id),
                  '-')                                                                                       'dcCheckInTime',
               time_format(time(addtime(t.trip_start_time, IST_DIFF)), '%d-%m-%y %h:%i %p')                                 'InTime',
               time_format(time(addtime(t.trip_end_time, IST_DIFF)), '')                                   'ReturnTime',
               timestampdiff(hour, addtime(t.trip_end_time, IST_DIFF), addtime(t.trip_start_time, IST_DIFF)) 'DutyHrs',
               dc.delivery_center_name                                                                       'deliveryCenter',
               getTripDPCount(t.id)                                                                          'NoOfDP',
               t.trip_end_reading - t.trip_start_reading                                                     'OdometerKms',
               t.tripkm                                                                                      'GoogleKms',
               if(t.driver_id is not null,getVendorByDriver(t.driver_id) ,'-')   'vendorName',

               if(t.driver_assistant_id is not null, getDAByID(t.driver_assistant_id),  '-')     'DA_Name',
               if(t.driver_assistant_id is not null, getDAMobileByID(t.driver_assistant_id),  '-')     'driverAssistantPhoneNo',
               if(t.driver_id is not null, getDriverByID(t.driver_id),'-')       'driverName',
               if(t.driver_id is not null,getDriverMobileByID(t.driver_id),'-')               'driverPhoneNo',
               t.trip_status   'tripStatus',t.total_weight 'total_weight',t.total_weight_delivered 'total_weight_delivered',
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
 true as 'TrackingStatus'


        from trip t,
             delivery_center dc
        where t.delivery_center_id = dc.id
        and t.trip_status in ('CANCELTRIP', 'COMPLETETRIP')
        and creation_source not in ('OFFLINE_TMS')
        and (city is null or dc.city=city)
        and (DC_ID is null or delivery_center_id=DC_ID)
        and (CLIENT_ID is null or if(CLIENT_ID in(7),t.client_id in(7,33),t.client_id in(CLIENT_ID)))
        and date(addtime(t.trip_start_time, IST_DIFF)) between if(frmDate is null ,toDate,frmDate) and toDate
        and t.id not in ( SELECT id from trip where id not in (select trip_id from tripdp where trip_id is not null ) and creation_source not in('OFFLINE_TMS'))
        order by t.id desc;
    end;
DELIMITER $$