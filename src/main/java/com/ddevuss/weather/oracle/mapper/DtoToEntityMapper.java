package com.ddevuss.weather.oracle.mapper;

public interface DtoToEntityMapper<D, E> {

    E dtoToEntity(D dto);

}
