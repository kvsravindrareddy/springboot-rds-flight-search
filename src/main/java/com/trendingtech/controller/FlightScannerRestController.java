package com.trendingtech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trendingtech.data.FlightDetails;
import com.trendingtech.service.FlightScannerService;

@RestController
public class FlightScannerRestController {

	@Autowired
	private FlightScannerService flightScannerService;

//	@GetMapping(value="/flightdetails/{origin}/{destination}/{departureDate}")
	@GetMapping(value = "/flightdetails")
//  http://localhost:port/flightdetails?origin=BLR&destination=DEL&departureDate=2019-10-20
	public List<FlightDetails> getFlightData(@RequestParam("origin") String origin,
			@RequestParam("destination") String destination, @RequestParam("departureDate") String departureDate) {
		return flightScannerService.getFlightData(origin, destination, departureDate);
	}
	
	@PostMapping(value="/flightdetails")
	public void saveFlightsSearchData(@RequestParam("origin") String origin,
			@RequestParam("destination") String destination, @RequestParam("departureDate") String departureDate)
	{
		flightScannerService.saveFlightsSearchData(origin, destination, departureDate);
	}
}