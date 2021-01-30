package us.thirdbase.rspi.parts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartDto {
    private Long id;
    private String name;
    private BigDecimal cost;
    private short quantity;
    private String shortDescription;
    private String description;
    private String url;
    private LocalDate orderDate;
    private String orderId;
}
