package com.newsletter.axon.service.mapper;

import java.util.List;

public interface MapperHelper<E, D> {

    E toEntity(final D dto);

    List<E> toEntities(final List<D> dto);

    D toDto(final E entity);

    List<D> toDtos(final List<E> entity);
}
