package com.msiepracki.beusable.occupancy;

import com.msiepracki.beusable.occupancy.dto.OccupancyRequestDto;
import com.msiepracki.beusable.occupancy.dto.OccupancyResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OccupancyListConverter {

    public OccupancyList toOccupancyList(OccupancyRequestDto occupancyRequestDto) {
        return OccupancyList.builder()
                .occupancy(Occupancy.builder()
                        .bookingType(BookingType.PREMIUM)
                        .count(occupancyRequestDto.premiumRooms())
                        .build()
                )
                .occupancy(Occupancy.builder()
                        .bookingType(BookingType.ECONOMY)
                        .count(occupancyRequestDto.economyRooms())
                        .build()
                )
                .build();
    }

    public OccupancyResponseDto toOccupancyResponseDto(OccupancyResultList occupancyResultList) {
        List<OccupancyResult> availabilityResultList = occupancyResultList.getOccupancyResults();
        var occupancyResponseDtoBuilder = OccupancyResponseDto.builder();
        availabilityResultList.forEach(occupancyResult -> {
            if(occupancyResult.getBookingType() == BookingType.PREMIUM) {
                occupancyResponseDtoBuilder.usagePremium(occupancyResult.getUsage())
                        .revenuePremium(occupancyResult.getRevenue().getNumberStripped());
            }
            if(occupancyResult.getBookingType() == BookingType.ECONOMY) {
                occupancyResponseDtoBuilder.usageEconomy(occupancyResult.getUsage())
                        .revenueEconomy(occupancyResult.getRevenue().getNumberStripped());
            }
        });
        return occupancyResponseDtoBuilder.build();
    }

}
