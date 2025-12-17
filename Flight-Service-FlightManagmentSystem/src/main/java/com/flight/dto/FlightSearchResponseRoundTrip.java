package com.flight.dto;

import java.util.List;

import com.flight.entity.FlightWrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchResponseRoundTrip {
	
	private List<FlightWrapper> going;
    private List<FlightWrapper> coming;
}
