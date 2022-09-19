CREATE DEFINER= 'sp_user'@'%' PROCEDURE `CREATE_TMS_ADMIN_TO_ACCESS_SHARED_TRANSPORT`()
BEGIN
        DELETE  FROM lkart.user WHERE auth_token IN('eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGFyZWRtb2JpbGl0eS5hZG1pbiIsInJvbGUiOiJbXSIsImlhdCI6MTY1NDg1ODk2Mn0.yi77vqOyqvwkzj-cRVJbvgX_B_9ziRFDDJxs3-XCIxI');
        INSERT INTO lkart.user (created_by,created_date,auth_token,first_name,last_name,username,password,user_type) values (341,now(),'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzaGFyZWRtb2JpbGl0eS5hZG1pbiIsInJvbGUiOiJbXSIsImlhdCI6MTY1NDg1ODk2Mn0.yi77vqOyqvwkzj-cRVJbvgX_B_9ziRFDDJxs3-XCIxI','SharedMob','Admin','sharedmobility.admin','$2a$10$a4uJ/F0jzOeXij.jF4HTuOdvXJ7FmBtNFAG5MduIC30k4.6T2dJPS','LKARTADMIN');
    end
