CREATE DEFINER= 'sp_user'@'%' PROCEDURE `DC_Details`(in uCITY varchar(50))
begin
    select ID DCID, delivery_center_name DeliveryCenter, dc_code, City, Latitude , Longitude, status ,address1,zip_code, dc_head_name,dc_head_email,dc_head_phone_number
    from delivery_center
    where (uCITY is null  or city=uCITY);
END;