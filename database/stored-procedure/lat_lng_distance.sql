CREATE DEFINER = `root` @`%` FUNCTION `lat_lng_distance`(
  lat1 float, lng1 float, lat2 float, lng2 float
) RETURNS float DETERMINISTIC BEGIN RETURN 6371 * 2 * ASIN(
  SQRT(
    POWER(
      SIN(
        (
          lat1 - abs(lat2)
        ) * pi() / 180 / 2
      ), 
      2
    ) + COS(
      lat1 * pi() / 180
    ) * COS(
      abs(lat2) * pi() / 180
    ) * POWER(
      SIN(
        (lng1 - lng2) * pi() / 180 / 2
      ), 
      2
    )
  )
);
END
