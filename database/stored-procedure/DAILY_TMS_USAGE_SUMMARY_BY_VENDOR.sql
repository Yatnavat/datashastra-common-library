CREATE DEFINER= 'sp_user'@'%' PROCEDURE `DAILY_TMS_USAGE_SUMMARY_BY_VENDOR`(in DCID int, ISTDIFF time, TripDate date)
begin
select date(addtime(t.trip_start_date,ISTDIFF)) TripDate, dc.delivery_center_name 'Delivery Center' ,
       getVendorByVehicle(t.vehicle_id) 'Vendor Name',getVehicleTypeByTripID(t.id) 'Vehicle Type', tv.vehicle_allocation_type,
       count(*) 'Vehicles', v.capecity 'Veh. Capacity', sum(v.capecity) 'Total Veh. Capacity'
from trip t, trip_vehicle tv, delivery_center dc, vehicle v
where t.delivery_center_id=dc.id and t.id=tv.trip_id  and v.id=t.vehicle_id and t.delivery_center_id=DCID
  and date(addtime(t.trip_start_date,ISTDIFF))=TripDate
group by 1,2,3,4,5,7;
end
