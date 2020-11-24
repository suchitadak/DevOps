package io.javabrains.coronavirustracker.services;
import java.io.IOException;
import java.io.StringReader;
import java.net.*;
import java.net.http.*;
import javax.annotation.PostConstruct;
import java.util.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.javabrains.coronavirustracker.models.LocationStats;

@Service
public class CoronaVirusDataService {
	
	public static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
	
	private List<LocationStats> allStats = new ArrayList<>(); 
	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetchVirusData() throws IOException, InterruptedException
	{
		List<LocationStats> newStats = new ArrayList<>(); 
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();
		  HttpResponse<String> httpResponse   = client.send(request, HttpResponse.BodyHandlers.ofString());
		  
		  StringReader csvBodyReader =  new StringReader(httpResponse.body());
		  Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
		  for (CSVRecord record : records) {
		      LocationStats locationStat = new LocationStats();
		      locationStat.setState(record.get("Province/State"));
		      locationStat.setCountry(record.get("Country/Region"));
		      locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
		      newStats.add(locationStat);
		  }
		  this.setAllStats(newStats);
	}
	public List<LocationStats> getAllStats() {
		return allStats;
	}
	public void setAllStats(List<LocationStats> allStats) {
		this.allStats = allStats;
	}

}
