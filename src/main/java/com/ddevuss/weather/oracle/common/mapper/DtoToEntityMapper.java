package com.ddevuss.weather.oracle.common.mapper;

public interface DtoToEntityMapper<D, E> {

    E dtoToEntity(D dto);

}
