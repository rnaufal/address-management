package com.rnaufal.address.management.domain;

import com.rnaufal.address.management.domain.validator.CEP;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by rnaufal on 15/11/16.
 */
@Entity
@Table(name = "CEP")
public class Cep implements Serializable {

    private static final long serialVersionUID = -2779160109414671660L;

    private Long id;

    private String value;

    private String street;

    private String district;

    private String city;

    private String estate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CEP_ID", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "CEP", nullable = false)
    @CEP
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "STREET", nullable = false)
    @NotNull(message = "{error.address.street.invalid}")
    @Size(min = 1, message = "{error.address.street.invalid}")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Column(name = "DISTRICT")
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Column(name = "CITY", nullable = false)
    @NotNull(message = "{error.address.city.invalid}")
    @Size(min = 1, message = "{error.address.city.invalid}")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "ESTATE", nullable = false)
    @NotNull(message = "{error.address.estate.invalid}")
    @Size(min = 1, message = "{error.address.estate.invalid}")
    public String getEstate() {
        return estate;
    }

    public void setEstate(String estate) {
        this.estate = estate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cep address = (Cep) o;
        return Objects.equals(id, address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("value", value)
                .append("street", street)
                .append("city", city)
                .append("estate", estate)
                .toString();
    }
}
