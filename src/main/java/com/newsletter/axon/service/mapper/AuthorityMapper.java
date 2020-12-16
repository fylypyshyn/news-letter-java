package com.newsletter.axon.service.mapper;

import com.newsletter.axon.domain.Authority;
import com.newsletter.axon.domain.User;
import com.newsletter.axon.service.dto.AuthorityDTO;
import com.newsletter.axon.service.dto.UserDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link User} and its DTO called {@link UserDTO}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Mapper(componentModel = "spring")
public interface AuthorityMapper extends MapperHelper<Authority, AuthorityDTO> {

}
