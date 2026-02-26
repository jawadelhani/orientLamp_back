package com.example.orientlamp_back.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityRequestDTO {

    @NotBlank(message = "University name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @Size(max = 100, message = "Type must not exceed 100 characters")
    private String type;

    private String description;

    @Size(max = 255, message = "Website must not exceed 255 characters")
    private String website;

    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String contactEmail;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Size(max = 100, message = "Accreditation status must not exceed 100 characters")
    private String accreditationStatus;

    private String programs;

    @Size(max = 512, message = "Image URL must not exceed 512 characters")
    private String imageUrl;
}