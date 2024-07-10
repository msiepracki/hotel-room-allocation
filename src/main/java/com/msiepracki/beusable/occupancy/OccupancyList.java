package com.msiepracki.beusable.occupancy;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Set;

@Data
@Builder
public class OccupancyList {
    @Singular
    private Set<Occupancy> occupancies;
}
