
CREATE DEFINER= 'sp_user'@'%' PROCEDURE `CLEAR_ALL_ORDER_ITEMS_DATA`(IN DCID varchar(60), IN CLIENTID varchar(60))
BEGIN
delete from order_item
    where id not in (Select Order_item_id from trip_item)
    and order_tbl_id in (SELECT id from order_tbl where status not IN('ALLOCATEDTRIP') and delivery_center_id = DCID
    and delivery_point_id in (select id from delivery_point where client_id = CLIENTID));

    DELETE from order_tbl where id not in (Select Order_tbl_id from order_item) AND status not IN('ALLOCATEDTRIP') and delivery_center_id = DCID
      and delivery_point_id in (select id from delivery_point where client_id = CLIENTID);
END
