package us.thirdbase.rspi.parts.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import us.thirdbase.rspi.parts.model.Part;
import us.thirdbase.rspi.parts.model.PartDto;

import java.time.LocalDate;

@Mapper(imports = {LocalDate.class}, componentModel = "spring")
public interface PartEntityDtoMapper {
    @Mapping(source = "orderDate", target = "orderDate", defaultExpression = "java(LocalDate.of(2017,12,31))")
    PartDto partEntityToDto(Part part);

    Part partDtoToEntity(PartDto partDto);
}
