CREATE DEFINER = `root` @`localhost` PROCEDURE `FIND_NOT_EXISTS_DPs_IN_VNF_VASHI`() BEGIN 
select 
  * 
from 
  delivery_point 
where 
  id in (
    select 
      delivery_point_id 
    from 
      tripdp 
    where 
      trip_id in (
        select 
          id 
        from 
          trip t 
        where 
          t.delivery_center_id = 58 
          and date(
            addtime(trip_start_date, '5:30')
          ) >= subdate(
            date(
              addtime(trip_start_date, '5:30')
            ), 
            interval 3 month
          )
      )
  ) 
  and customer_code not in(
    select 
      customer_code 
    from 
      delivery_point 
    where 
      delivery_center_id = 64
  );
END
