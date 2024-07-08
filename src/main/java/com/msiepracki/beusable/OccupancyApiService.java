package com.msiepracki.beusable;

import com.msiepracki.beusable.occupancy.OccupancyRequestDto;
import com.msiepracki.beusable.occupancy.OccupancyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class OccupancyApiService {

    public OccupancyResponseDto calculateOccupancy(OccupancyRequestDto request) {
        // TODO: Service + algorithm implementation
        return OccupancyResponseDto.builder()
                .usagePremium(0)
                .revenuePremium(BigDecimal.ZERO)
                .usageEconomy(0)
                .revenueEconomy(BigDecimal.ZERO)
                .build();
    }
}