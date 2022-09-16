
CREATE DEFINER= 'sp_user'@'%' PROCEDURE `DAILY_TMS_REPORT`(IN TRIP_DATE date, IN DCID int, IN ClientID int,
                                                        IN AllowDelayMin int, IN IST_Diff varchar(20),
                                                        IN DeliveryWindowEndTime time)
BEGIN
    select sum(if(t.delivery_center_id not in (58), (select count(*)
                                                     from tripdp tdp1,
                                                          delivery_point dp1
                                                     where tdp1.trip_id = t.id
                                                       and tdp1.delivery_point_id = dp1.id
                                                       and dp1.client_id not in (7)
                                                       and time(addtime(tdp1.check_in_time, IST_Diff)) <=
                                                           TIME(date_add(dp1.end_allowed_entry_time, interval AllowDelayMin minute))),
                  (select count(DISTINCT tdp1.id)
                   from tripdp tdp1,
                        delivery_point dp1
                   where t.id = tdp1.trip_id
                     and tdp1.delivery_point_id = dp1.id
                     and dp1.client_id in (7)
                     and time(ADDTIME(tdp1.check_in_time, IST_Diff)) <=
                         TIME(date_add(time(DeliveryWindowEndTime), interval AllowDelayMin MINUTE)))
        ))        AS                                                                                       `Ontime`,

           sum(if(t.delivery_center_id not in (58), (select count(*)
                                                     from tripdp tdp1,
                                                          delivery_point dp1
                                                     where tdp1.trip_id = t.id
                                                       and tdp1.delivery_point_id = dp1.id
                                                       and dp1.client_id not in (7)
                                                       and time(addtime(tdp1.check_in_time, IST_Diff)) >
                                                           TIME(date_add(dp1.end_allowed_entry_time, interval AllowDelayMin minute))),
                  (select count(tdp1.id)
                   from tripdp tdp1,
                        delivery_point dp1
                   where t.id = tdp1.trip_id
                     and tdp1.delivery_point_id = dp1.id
                     and dp1.client_id in (7)
                     and time(ADDTIME(tdp1.check_in_time, IST_Diff)) >
                         TIME(date_add(time(DeliveryWindowEndTime), interval AllowDelayMin MINUTE)))
               ))                                                                                          'Delay',

           sum(if(t.delivery_center_id not in (58), (select count(distinct tdp1.id)
                                                     from tripdp tdp1,
                                                          delivery_point dp1
                                                     where tdp1.trip_id = t.id
                                                       and tdp1.delivery_point_id = dp1.id
                                                       and dp1.client_id NOT in (7)
                                                       and tdp1.check_in_time is null),
                  (select count(distinct tdp1.id)
                   from tripdp tdp1,
                        delivery_point dp1
                   where t.id = tdp1.trip_id
                     and tdp1.delivery_point_id = dp1.id
                     and dp1.client_id in (7)
                     and tdp1.check_in_time is null)
               )) AS                                                                                       `Ongoing`,
           t.id                                                                                            'tripID',
           date(addtime(t.trip_start_date, IST_Diff))                                                      'tripDate',
           if(t.vehicle_id is not null, (select v.registration_number from vehicle v where v.id = t.vehicle_id),
              false)                                                                                       'vehicleNo',
           if(t.vehicle_id is not null, (select v.contract_type from vehicle v where v.id = t.vehicle_id),
              false)                                                                                       'contractType',
           if(t.vehicle_id is not null, (select v.model_name from vehicle v where v.id = t.vehicle_id),
              false)                                                                                       'model_Name',
           time(addtime(t.trip_start_time, IST_DIFF))                                                      'InTime',
           time(addtime(t.trip_end_time, IST_DIFF))                                                        'ReturnTime',
           TIMEDIFF(addtime(t.trip_end_time, IST_DIFF), addtime(t.trip_start_time, IST_DIFF))              'DutyHrs',
           if(t.id is not null, (select count(tdp1.id) from tripdp tdp1 where t.id = tdp1.trip_id), false) 'NoOfDP',
           r.lengh_inkm                                                                                    'SystemKms',
           t.trip_end_reading - t.trip_start_reading                                                       'OdometerKms',
           t.tripkm                                                                                        'GoogleKms',
           if(t.driver_id is not null, (select concat_ws(' ', first_name, last_name)
                                        from user u
                                        where u.id in (select v.user_id
                                                       from vendor v
                                                       where v.id in (select d.vendor_id from driver d where d.id = t.driver_id))),
              false)                                                                                       'vendorName',
           if(t.driver_assistant_id is not null, (select concat_ws(' ', first_name, last_name)
                                                  from user u
                                                  where u.id in (select da.user_id
                                                                 from driver_assistant da
                                                                 where da.id = t.driver_assistant_id)),
              null)                                                                                        'driverAssistantName',
           if(t.driver_id is not null, (select concat_ws(' ', first_name, last_name)
                                        from user u
                                        where u.id in (select d.user_id from driver d where d.id = t.driver_id)),
              false)                                                                                       'driverName',
           if(t.driver_id is not null,
              (select phone_number from user u where u.id in (select d.user_id from driver d where d.id = t.driver_id)),
              false)                                                                                       'driverPhoneNo',
           t.trip_start_shipperbox_loading                                                                 'NoOfShipperboxesGiven',
           t.trip_start_trolly_loading                                                                     'NoOfTrollyGiven',
           t.trip_end_shipperbox_loading                                                                   'NoOfShipperboxesReturned',
           t.trip_end_trolly_loading                                                                       'NoOfTrollyReturned'

    from trip t,
         route r
    where t.route_id = r.id
      and t.delivery_center_id = DCID
      AND date(addtime(t.trip_start_date, IST_Diff)) = TRIP_DATE
    group by 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22;
END
