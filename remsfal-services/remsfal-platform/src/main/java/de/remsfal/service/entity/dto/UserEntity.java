package de.remsfal.service.entity.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

import de.remsfal.core.model.CustomerModel;
import de.remsfal.service.entity.dto.superclass.AbstractEntity;

/**
 * @author Alexander Stanik [alexander.stanik@htw-berlin.de]
 */
@NamedQuery(name = "UserEntity.updateAuthenticatedAt",
    query = "update UserEntity user set user.authenticatedAt = :timestamp where user.tokenId = :tokenId")
@NamedQuery(name = "UserEntity.deleteById",
    query = "delete from UserEntity user where user.id = :id")
@Entity
@Table(name = "USER")
public class UserEntity extends AbstractEntity implements CustomerModel {

    @Column(name = "TOKEN_ID", unique = true)
    private String tokenId;

    @Email
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "AUTHENTICATED_AT")
    private LocalDateTime authenticatedAt;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "id")
    private AddressEntity address;

    @Column(name = "MOBILE_PHONE_NUMBER")
    private String mobilePhoneNumber;

    @Column(name = "BUSINESS_PHONE_NUMBER")
    private String businessPhoneNumber;

    @Column(name = "PRIVATE_PHONE_NUMBER")
    private String privatePhoneNumber;

    @OneToMany(mappedBy = "user")
    private Set<ProjectMembershipEntity> memberships;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(final String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public Boolean isActive() {
        return tokenId != null;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public LocalDateTime getAuthenticatedAt() {
        return authenticatedAt;
    }

    public void setAuthenticatedAt(final LocalDateTime authenticatedAt) {
        this.authenticatedAt = authenticatedAt;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    @Override
    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    @Override
    public String getBusinessPhoneNumber() {
        return businessPhoneNumber;
    }

    public void setBusinessPhoneNumber(String businessPhoneNumber) {
        this.businessPhoneNumber = businessPhoneNumber;
    }

    @Override
    public String getPrivatePhoneNumber() {
        return privatePhoneNumber;
    }

    public void setPrivatePhoneNumber(String privatePhoneNumber) {
        this.privatePhoneNumber = privatePhoneNumber;
    }

    public Set<ProjectMembershipEntity> getMemberships() {
        return memberships;
    }

    public void setMemberships(final Set<ProjectMembershipEntity> memberships) {
        this.memberships = memberships;
    }

    @Override
    public LocalDate getRegisteredDate() {
        return this.getCreatedAt().toLocalDate();
    }

    @Override
    public LocalDateTime getLastLoginDate() {
        return authenticatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof UserEntity e) {
            return super.equals(e)
                && Objects.equals(tokenId, e.tokenId)
                && Objects.equals(email, e.email)
                && Objects.equals(firstName, e.firstName)
                && Objects.equals(lastName, e.lastName)
                && Objects.equals(address, e.address)
                && Objects.equals(mobilePhoneNumber, e.mobilePhoneNumber)
                && Objects.equals(businessPhoneNumber, e.businessPhoneNumber)
                && Objects.equals(privatePhoneNumber, e.privatePhoneNumber);
        }
        return false;
    }

}
