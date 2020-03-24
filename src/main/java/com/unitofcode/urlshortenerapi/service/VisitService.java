package com.unitofcode.urlshortenerapi.service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unitofcode.urlshortenerapi.dto.VisitDTO;
import com.unitofcode.urlshortenerapi.dto.VisitResponse;
import com.unitofcode.urlshortenerapi.model.User;
import com.unitofcode.urlshortenerapi.repository.UrlRepository;
import com.unitofcode.urlshortenerapi.repository.VisitRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VisitService {
	
	@Autowired
	UrlRepository urlRepository;
	
	@Autowired
	VisitRepository visitRepository;
	
	@Autowired
	UserService userService;

	public Optional<List<VisitResponse>> getLast30DaysHistory(HttpServletRequest httpServletRequest) {
		User user = userService.getCurrentUser(httpServletRequest);
		
		LocalDate oldLocalDate = LocalDate.now(ZoneOffset.UTC).minusMonths(1).plusDays(1);
		Date oldDate = Date.from(oldLocalDate.atStartOfDay(ZoneId.of("GMT")).toInstant());
		
		List<VisitDTO> visitCountList = visitRepository.getLast30DaysHistory(user.getId(), oldDate);
		log.info("{}",visitCountList);
		
		List<VisitResponse> visitResponseList = new ArrayList<VisitResponse>(40);
		
		LocalDate currentDate = LocalDate.now(ZoneOffset.UTC);
		LocalDate oneMonthOldDate = LocalDate.now(ZoneOffset.UTC).minusMonths(1).plusDays(1);

		mergeOneMonth(visitResponseList, oneMonthOldDate, currentDate, visitCountList);
				
		return Optional.of(visitResponseList);
	}

	private void mergeOneMonth(List<VisitResponse> visitResponseList, LocalDate oldDate, LocalDate currentDate,
			List<VisitDTO> visitCountList) {
		DateTimeFormatter formatterDMY = DateTimeFormatter.ofPattern("dd LLL yy");
		int pos = 0;
		
		while(oldDate.isBefore(currentDate) || oldDate.isEqual(currentDate)) {
			VisitResponse visitResponse = new VisitResponse();
			visitResponse.setName(oldDate.format(formatterDMY));
			visitResponse.setValue(0l);
			
			if(pos < visitCountList.size() && datesAreEqualDMY(visitCountList.get(pos).getDate(), oldDate )) {
				visitResponse.setValue(visitCountList.get(pos).getVisit());
				pos++;
			}
			visitResponseList.add(visitResponse);
			oldDate = oldDate.plusDays(1);
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private boolean datesAreEqualDMY(Date first, LocalDate second) {
		if(first == null && second == null) return true;
		if(first.getDate() == second.getDayOfMonth() && first.getMonth() == second.getMonthValue()-1 && first.getYear() == second.getYear()-1900) {
			return true;
		}
		return false;
	}
	
	
	
//	##############################################################################3
	
	

	public Optional<List<VisitResponse>> getlastOneYearResponse(HttpServletRequest httpServletRequest) {
		User user = userService.getCurrentUser(httpServletRequest);
		List<VisitDTO> visitCountList = visitRepository.getOneYearPerMonthHistory(user.getId());
		log.info("{}",visitCountList);

		Collections.sort(visitCountList, new Comparator<VisitDTO>(){

			@Override
			public int compare(VisitDTO o1, VisitDTO o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
			
		});
		
		log.info("{}",visitCountList);

		List<VisitResponse> visitResponseList = new ArrayList<VisitResponse>(15);
		

		
		
		LocalDate currentDate = LocalDate.now(ZoneOffset.UTC);
		LocalDate oneYearOldDate = LocalDate.now(ZoneOffset.UTC).minusYears(1).plusMonths(1);
		log.info("old date starting point: {}", oneYearOldDate);
		log.info("current stating point: {}", currentDate);
		mergeOneYear(visitResponseList, oneYearOldDate, currentDate, visitCountList);
				
		return Optional.of(visitResponseList);
	}
	
	private void mergeOneYear(List<VisitResponse> visitResponseList, LocalDate oldDate, LocalDate currentDate,
			List<VisitDTO> visitCountList) {
		DateTimeFormatter formatterMY = DateTimeFormatter.ofPattern("LLL yy");
		int pos = 0;
		
		while( ( oldDate.getYear() == currentDate.getYear() && oldDate.getMonthValue() <= currentDate.getMonthValue() ) || ( oldDate.getYear() == currentDate.getYear()-1 && oldDate.getMonthValue()>currentDate.getMonthValue() ) ) {
			VisitResponse visitResponse = new VisitResponse();
			visitResponse.setName(oldDate.format(formatterMY));
			visitResponse.setValue(0l);
			
			if(pos < visitCountList.size() && datesAreEqualMY(visitCountList.get(pos).getDate(), oldDate )) {
				visitResponse.setValue(visitCountList.get(pos).getVisit());
				pos++;
			}
			visitResponseList.add(visitResponse);
			oldDate = oldDate.plusMonths(1);
//			log.info("{}", oldDate);
		}
	}
		
	@SuppressWarnings("deprecation")
	private boolean datesAreEqualMY(Date first, LocalDate second) {
		if(first == null && second == null) return true;
		if(first.getMonth() == second.getMonthValue()-1 && first.getYear() == second.getYear()-1900) {
			return true;
		}
		return false;
	}
	

}
