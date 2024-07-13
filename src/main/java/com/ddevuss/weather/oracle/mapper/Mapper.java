package com.ddevuss.weather.oracle.mapper;

public interface Mapper <O, T> {

    T map(O object);
}
