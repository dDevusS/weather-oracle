package com.ddevuss.weather.oracle.mapper;

public interface EntityToDtoMapper<D, E> {

    D entityToDto(E entity);

}
