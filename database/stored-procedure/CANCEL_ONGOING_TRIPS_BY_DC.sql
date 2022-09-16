CREATE DEFINER= 'sp_user'@'%' PROCEDURE `CANCEL_ONGOING_TRIPS_BY_DC`(IN DCID int)
BEGIN
    UPDATE trip SET bckpTripStatus=trip_status, trip_status='CANCELTRIP'    where trip_status NOT IN ('COMPLETETRIP','HOLD','ABSENT', 'CANCELTRIP')
      AND delivery_center_id = DCID;

    UPDATE trip_vehicle SET status='CANCELTRIP'     where status NOT IN ('COMPLETETRIP','HOLD','ABSENT', 'CANCELTRIP')
      AND vehicle_id IN (SELECT ID FROM vehicle WHERE vendor_id IN (SELECT vendor_id FROM vendor_delivery_centers WHERE delivery_centers_id = DCID));

    UPDATE vehicle     SET status='ACTIVE'     WHERE status != 'ACTIVE'
      AND vendor_id IN (SELECT vendor_id FROM vendor_delivery_centers WHERE delivery_centers_id = DCID);
END

