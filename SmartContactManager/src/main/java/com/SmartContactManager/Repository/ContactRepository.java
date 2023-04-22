package com.SmartContactManager.Repository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.SmartContactManager.Model.Contect;
@Repository
public interface ContactRepository extends JpaRepository<Contect, Integer>{

	@Query("from Contect as c where c.user.id =:userid")
	public List<Contect> findByUser(@Param("userid")int userid);
}
