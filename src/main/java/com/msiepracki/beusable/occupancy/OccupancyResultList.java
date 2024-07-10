package com.msiepracki.beusable.occupancy;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class OccupancyResultList {
    @Singular
    private List<OccupancyResult> occupancyResults;
}
