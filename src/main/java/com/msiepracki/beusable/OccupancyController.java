package com.msiepracki.beusable;

import com.msiepracki.beusable.occupancy.dto.OccupancyRequestDto;
import com.msiepracki.beusable.occupancy.dto.OccupancyResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/occupancy")
public class OccupancyController {

    private final OccupancyApiService occupancyApiService;

    @PostMapping
    public OccupancyResponseDto calculateOccupancy(@RequestBody @Valid OccupancyRequestDto request) {
        return occupancyApiService.calculateOccupancy(request);
    }

}
