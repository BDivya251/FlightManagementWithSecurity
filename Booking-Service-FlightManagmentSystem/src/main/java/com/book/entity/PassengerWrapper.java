package com.book.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerWrapper {
	@NotNull(message="Name is required")
	private String name;
	@NotNull(message="age is required")
    private Integer age;
	@NotNull(message="age is required")
    private Integer seatNumber;
}
