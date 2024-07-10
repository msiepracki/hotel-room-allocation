package com.msiepracki.beusable;

import com.msiepracki.beusable.booking.BookingList;
import com.msiepracki.beusable.booking.BookingListConverter;
import com.msiepracki.beusable.occupancy.OccupancyList;
import com.msiepracki.beusable.occupancy.OccupancyListConverter;
import com.msiepracki.beusable.occupancy.OccupancyResultList;
import com.msiepracki.beusable.occupancy.dto.OccupancyRequestDto;
import com.msiepracki.beusable.occupancy.dto.OccupancyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OccupancyApiService {

    private final OccupancyService occupancyService;
    private final BookingListConverter bookingListConverter;
    private final OccupancyListConverter occupancyListConverter;

    public OccupancyResponseDto calculateOccupancy(OccupancyRequestDto request) {
        BookingList bookingList = bookingListConverter.toBookingList(request.potentialGuests());
        OccupancyList occupancyList = occupancyListConverter.toOccupancyList(request);

        OccupancyResultList occupancyResultList = occupancyService.calculateOccupancy(
                bookingList,
                occupancyList
        );

        return occupancyListConverter.toOccupancyResponseDto(occupancyResultList);
    }
}