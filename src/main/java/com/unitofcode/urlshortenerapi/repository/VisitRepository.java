package com.unitofcode.urlshortenerapi.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unitofcode.urlshortenerapi.dto.VisitDTO;
import com.unitofcode.urlshortenerapi.model.Visit;

@Repository
public interface VisitRepository extends CrudRepository<Visit, Long>{
	
	@Modifying
	public int deleteByCreateTimeBefore(Timestamp createTime);

	@Query("select new com.unitofcode.urlshortenerapi.dto.VisitDTO(v.userId, count(v), DATE(v.createTime)) from Visit v where (v.createTime) > :oldDate and v.userId = :userId group by DATE(v.createTime)")
	public List<VisitDTO> getLast30DaysHistory(@Param("userId") Long userId, @Param("oldDate") Date oldDate);
	
	@Query("select new com.unitofcode.urlshortenerapi.dto.VisitDTO(v.userId, count(v), DATE(v.createTime)) from Visit v where ( (YEAR(v.createTime) = YEAR(CURRENT_DATE) and MONTH(v.createTime) <= MONTH(CURRENT_DATE) ) or (YEAR(v.createTime) = YEAR(CURRENT_DATE)-1 and MONTH(v.createTime) > MONTH(CURRENT_DATE) ) )  and v.userId = :userId group by EXTRACT(MONTH from v.createTime)")
	public List<VisitDTO> getOneYearPerMonthHistory(@Param("userId") Long userId);
	
}
