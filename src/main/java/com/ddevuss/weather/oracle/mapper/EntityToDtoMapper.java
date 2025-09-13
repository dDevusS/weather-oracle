package com.ddevuss.weather.oracle.mapper;

public interface EntityToDtoMapper<E, D> {

    D entityToDto(E entity);

}
