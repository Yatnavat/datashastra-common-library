CREATE DEFINER= 'sp_user'@'%' PROCEDURE `CANCEL_TRIP_BY_ID`(IN tripID INT)
BEGIN
    UPDATE trip SET bckpTripStatus=trip_status, trip_status='CANCELTRIP'    where trip_status not in('COMPLETETRIP') and id in(tripID);
    UPDATE trip_vehicle SET status='CANCELTRIP'     where trip_id IN (tripID);
    UPDATE vehicle SET status='ACTIVE' WHERE status != 'ACTIVE' AND ID IN (SELECT vehicle_id FROM trip WHERE  trip_status not in('COMPLETETRIP') and trip.id in(tripID));
END