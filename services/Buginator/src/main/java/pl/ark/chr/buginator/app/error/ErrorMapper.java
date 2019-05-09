package pl.ark.chr.buginator.app.error;

import org.mapstruct.Mapper;
import pl.ark.chr.buginator.commons.util.DtoMapper;
import pl.ark.chr.buginator.domain.core.Error;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
interface ErrorMapper extends DtoMapper<Error, ErrorDTO> {
}
