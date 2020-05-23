package acs.dal;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import acs.data.UserEntity;

public interface UserDao extends PagingAndSortingRepository<UserEntity, String> {

 public  UserEntity findOneByEmail(@Param("email")String email);
	
}