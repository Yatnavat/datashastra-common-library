CREATE DEFINER = `root` @`localhost` PROCEDURE `TripVehicle_and_tripTbl_Cancel_issue`() begin 
update 
  trip_vehicle 
set 
  status = 'COMPLETETRIP' 
where 
  status = 'CANCELTRIP' 
  and trip_id in(
    select 
      id 
    from 
      trip 
    where 
      trip.trip_status not in('CANCELTRIP', 'HOLD', 'ABSENT')
  );
end
