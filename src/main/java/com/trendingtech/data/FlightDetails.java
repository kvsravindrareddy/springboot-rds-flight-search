package com.trendingtech.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity(name="FLIGHT_DETAILS")
public class FlightDetails {
	@Id
	@GeneratedValue
	private Long id;
	private String carrierId;
	private String carrierName;
	private Long originId;
	private String origin;
	private Long destinationId;
	private String destination;
	private String country;
	private Double price;
	private String departureDate;
}