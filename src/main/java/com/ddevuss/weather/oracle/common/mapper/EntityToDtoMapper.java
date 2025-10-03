package com.ddevuss.weather.oracle.common.mapper;

public interface EntityToDtoMapper<E, D> {

    D entityToDto(E entity);

}
