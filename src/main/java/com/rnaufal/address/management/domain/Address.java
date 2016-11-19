package com.rnaufal.address.management.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by rnaufal on 14/11/16.
 */
@Entity
@Table(name = "ADDRESS")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address implements Serializable {

    private static final long serialVersionUID = 8080474380031498752L;

    private Long id;

    private Cep cep;

    private String complement;

    private String number;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ADDRESS_ID", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CEP_ID", referencedColumnName = "CEP_ID", nullable = false)
    @NotNull(message = "{error.address.cep.invalid}")
    @Valid
    public Cep getCep() {
        return cep;
    }

    public void setCep(Cep cep) {
        this.cep = cep;
    }

    @Column(name = "COMPLEMENT")
    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    @Column(name = "NUMBER", nullable = false)
    @NotNull(message = "{error.address.number.invalid}")
    @Pattern(regexp = "\\d+", message = "{error.address.number.invalid}")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
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
                .append("complement", complement)
                .append("number", number)
                .toString();
    }
}
