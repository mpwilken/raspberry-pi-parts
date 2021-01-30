package us.thirdbase.rspi.parts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Part {
    @NotNull
    short quantity;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "part_generator")
    @SequenceGenerator(name = "part_generator", sequenceName = "part_id_seq", allocationSize = 50)
    private Long id;
    @NotNull
    @Size(max = 50)
    private String name;
    @Column(nullable = false, precision = 7, scale = 2)
    @NotNull
    private BigDecimal cost;
    @Size(max = 255)
    private String shortDescription;
    @Size(max = 4000)
    private String description;
    private String url;

    @Convert(converter = LocalDateToDateConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Builder.Default
    private LocalDate orderDate = LocalDate.of(2017, 12, 31);
    private String orderId;
}
