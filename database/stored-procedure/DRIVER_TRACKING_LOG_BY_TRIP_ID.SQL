
create procedure DRIVER_TRACKING_LOG_BY_TRIP_ID(IN TID INT)
begin
select id, trip_name, DCName, Driver, DriverMob, Vehicle, Vendor, FirstLog, LastLog,timediff(LastLog,FirstLog)TripDuration,
       FirstLatLong, LastLatLong, LastLogDiff, TrackingStatus,LogCount
from (select distinctrow t.id,
                         trip_name,
                         getDC(delivery_center_id)                                                                 DCName,
                         getDriverByID(driver_id)                                                                  Driver,
                         getDriverMobileByID(driver_id)                                                            DriverMob,
                         getVehicleNumByID(vehicle_id)                                                             Vehicle,
                         getVendorByDriver(driver_id)                                                            Vendor,
                         first_value(addtime(tdt.created_date, '5:30'))
                                     over (partition by trip_id order by trip_id)                                  FirstLog,
                         last_value(addtime(tdt.created_date, '5:30'))
                                    over (partition by trip_id order by trip_id)                                   LastLog,
                         first_value(concat_ws(',', latitude, longtitude))
                                     over (partition by trip_id order by trip_id)                                  FirstLatLong,
                         last_value(concat_ws(',', latitude, longtitude))
                                    over (partition by trip_id order by trip_id)                                   LastLatLong,
                         timediff(addtime(now(), '5:30'), last_value(addtime(tdt.created_date, '5:30'))
                                                                     over (partition by trip_id order by trip_id)) LastLogDiff,
                         if(timediff(addtime(now(), '5:30'), last_value(addtime(tdt.created_date, '5:30'))
                                                                        over (partition by trip_id order by trip_id)) >
                            '00:01:00', '0', '1')                                                                  TrackingStatus,
          (select count(*) from trip_driver_tracking tdt2 where tdt2.trip_id=t.id) LogCount
      from trip t
               left join trip_driver_tracking tdt on t.id = tdt.trip_id
      where t.id = tdt.trip_id
        AND t.ID = TID) tt;
end;