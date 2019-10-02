package com.trendingtech.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.trendingtech.data.FlightDetails;

@Repository
public interface FlightScannerRepo extends CrudRepository<FlightDetails, Long>{
	
}