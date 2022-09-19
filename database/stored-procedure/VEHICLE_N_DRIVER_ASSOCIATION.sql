CREATE DEFINER = `root` @`localhost` PROCEDURE `VEHICLE_N_DRIVER_ASSOCIATION`(
  in uCity varchar(60), 
  dcID int, 
  cID int, 
  venID int
) BEGIN 
select 
  IF(
    dc_chekin_time IS NULL, 
    ADDTIME(tv.created_date, '5:30'), 
    ADDTIME(dc_chekin_time, '5:30')
  ) Date, 
  tv.vehicle_id VehID, 
  getVehicleNumByID(tv.vehicle_id) 'VehicleNo', 
  round(container_length) lengthOfContainer, 
  getVehicleTypeByVehID(tv.vehicle_id) vehicleType, 
  tv.driver_id driverID, 
  getDriverByID(tv.driver_id) 'DriverName', 
  getDriverMobileByID(tv.driver_id) driverMobile, 
  if(
    isnull(tv.delivery_center_id), 
    '-', 
    tv.delivery_center_id
  ) 'dcID', 
  if(
    isnull(tv.delivery_center_id), 
    '-', 
    getdc(tv.delivery_center_id)
  ) 'DeliveryCentre', 
  getVendorIDByDriver(tv.driver_id) 'VendorID', 
  getVendorByDriver(tv.driver_id) 'VendorName', 
  v.status 'Status' 
from 
  trip_vehicle tv, 
  vehicle v 
where 
  tv.vehicle_id = v.id 
  and tv.status in('ACTIVE') 
  and trip_id is null 
  and (
    uCity is null 
    or tv.vehicle_id in (
      select 
        id 
      from 
        vehicle 
      where 
        vendor_id in(
          select 
            vendor_id 
          from 
            vendor_delivery_centers 
          where 
            delivery_centers_id in(
              select 
                id 
              from 
                delivery_center 
              where 
                city = uCity
            )
        )
    )
  ) 
  and (
    dcID is null 
    or tv.vehicle_id in (
      select 
        id 
      from 
        vehicle 
      where 
        vendor_id in(
          select 
            vendor_id 
          from 
            vendor_delivery_centers 
          where 
            delivery_centers_id in(dcID)
        )
    )
  ) 
  and (
    cID is null 
    or tv.vehicle_id in (
      select 
        id 
      from 
        vehicle 
      where 
        vendor_id in(
          select 
            vendor_id 
          from 
            vendor_delivery_centers 
          where 
            delivery_centers_id in (
              select 
                dcc.delivery_center_id 
              from 
                delivery_center_clients dcc 
              where 
                dcc.clients_id in(cID)
            )
        )
    )
  ) 
  and (
    venID is null 
    or tv.vehicle_id in (
      select 
        id 
      from 
        vehicle 
      where 
        vendor_id in(venID)
    )
  );
END
