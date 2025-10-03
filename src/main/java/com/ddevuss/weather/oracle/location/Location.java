package com.ddevuss.weather.oracle.location;

import com.ddevuss.weather.oracle.common.entity.BaseEntity;
import com.ddevuss.weather.oracle.auth.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "locations", indexes = @Index(name = "idx_base_target", columnList = "user_id, latitude, longitude"))
public class Location extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "state", nullable = false)
    private String state;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;
}
