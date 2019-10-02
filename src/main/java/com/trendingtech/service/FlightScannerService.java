package com.trendingtech.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.trendingtech.data.FlightDetails;
import com.trendingtech.repo.FlightScannerRepo;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class FlightScannerService {

	@Value("${flight.scanner.url}")
	private String flightScannerUrl;

	@Value("${flight.country}")
	private String country;

	@Value("${flight.currency}")
	private String currency;

	@Value("${light.lang}")
	private String lang;
	
	@Autowired
	private FlightScannerRepo flightScannerRepo;

	public List<FlightDetails> getFlightData(String origin, String destination, String departureDate) {
		Response response = requestBuilder(origin, destination, departureDate);
		List<FlightDetails> flightsDetails = new ArrayList<>();
		try {
			String flightScanApiResponse = response.body().string();
			JSONObject mainObject = new JSONObject(flightScanApiResponse);
			JSONArray quotesArr = mainObject.getJSONArray("Quotes");
			quotesArr.forEach(quotesJsonObj -> {
				FlightDetails flightDetail = new FlightDetails();
				JSONObject outboundLegObj = (JSONObject) quotesJsonObj;
				flightDetail.setPrice(outboundLegObj.getDouble("MinPrice"));
				JSONObject outboundLegObjval = outboundLegObj.getJSONObject("OutboundLeg");
//				JSONArray carrierIdsArr = outboundLegObjval.getJSONArray("CarrierIds");
//				flightDetail.setCarrierId(carrierIdsArr.getJSONObject(0).toString());
				flightDetail.setOriginId(outboundLegObjval.getLong("OriginId"));
				flightDetail.setCountry(country);
				flightDetail.setOrigin(origin);
				flightDetail.setDestination(destination);
				flightDetail.setDestinationId(outboundLegObjval.getLong("DestinationId"));
				flightDetail.setDepartureDate(outboundLegObjval.getString("DepartureDate"));
				flightsDetails.add(flightDetail);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flightsDetails;
	}

	public Response requestBuilder(String origin, String destination, String departureDate) {
		OkHttpClient okHttpClient = new OkHttpClient();
		String serviceBaseUrl = UriComponentsBuilder.fromUriString(flightScannerUrl).path("/").path(country).path("/")
				.path(currency).path("/").path(lang).path("/").path(origin).path("-sky/").path(destination)
				.path("-sky/").path(departureDate).toUriString();
		System.out.println("serviceBaseUrl : " + serviceBaseUrl);
		Request request = new Request.Builder().url(serviceBaseUrl).get()
				.addHeader("x-rapidapi-key", "DaBJNUOhYNmshA8fq5vCvdJy32pmp1j1IJqjsnW9GSfWaglesS")
				.addHeader("x-rapidapi-host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
				.addHeader("cache-control", "no-cache").addHeader("content-type", MediaType.APPLICATION_JSON_VALUE)
				.addHeader("connection", "keep-alive").build();
		Response response = null;
		try {
			response = okHttpClient.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	public void saveFlightsSearchData(String origin, String destination, String departureDate) {
		flightScannerRepo.saveAll(getFlightData(origin, destination, departureDate));
	}

}