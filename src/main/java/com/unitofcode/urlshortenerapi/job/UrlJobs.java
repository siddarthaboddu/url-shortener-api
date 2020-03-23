package com.unitofcode.urlshortenerapi.job;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.unitofcode.urlshortenerapi.repository.VisitRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UrlJobs {
	
	@Autowired
	private VisitRepository visitRepository;
	
	@Scheduled(cron = "${url.shortener.removeOldVisits}")
	public void removeOldVisitsHistory() {
		Timestamp lastMonthTimeStamp = Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC).minusMonths(1));
		int numberOfVisitsDeleted = visitRepository.deleteByCreateTimeBefore(lastMonthTimeStamp);
		log.info("NUMBER_OF_VISITS_DELETED: {}",numberOfVisitsDeleted);
	}

}
