package com.zeprofile.application.base;

import com.zeprofile.application.MainPage;

import java.util.List;

public class UserInfoBean {
    private Integer id;
    private String email;
    private List<UserHobbyBean> hobbies = null;
    private String billingAddress;
    private String postingAddress;
    private Integer budgeaId;
    private String budgeaToken;
    private String firstname;
    private String lastname;
    private Boolean durationYear;   //Durée: true - 1 an; false - 2 ans
    private Boolean receiverOnly;   //Destinataire: true - Destinataire uniquement; false - Destinataire et ses partenaires
    private Boolean communicateAddress; //Localisation: true - Résidence
    private Boolean continuousLocation; //Localisation: true - Géolocalisation
    private Boolean marketingContentAllProducts;    //Contenu Marketing: true - Tous produits; false - Catégorie achetée
    private Boolean marketingFrequencyOncePerMonth; //Fréquence Marketing: true - 1 fois par mois; false - 1 fois par semaine
    private String billingAddressPostalCode;
    private String billingAddressCity;
    private String postingAddressPostalCode;
    private String postingAddressCity;
    private Boolean isBudgeaEnabled;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<UserHobbyBean> getHobbies() {
        return hobbies;
    }
    public void setHobbies(List<UserHobbyBean> hobbies) {
        this.hobbies = hobbies;
    }
    public String getBillingAddress() {
        return billingAddress;
    }
    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }
    public String getPostingAddress() {
        return postingAddress;
    }
    public void setPostingAddress(String postingAddress) {
        this.postingAddress = postingAddress;
    }
    public Object getBudgeaId() {
        return budgeaId;
    }
    public void setBudgeaId(Integer budgeaId) {
        this.budgeaId = budgeaId;
    }
    public Object getBudgeaToken() {
        return budgeaToken;
    }
    public void setBudgeaToken(String budgeaToken) {
        this.budgeaToken = budgeaToken;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public Boolean getDurationYear() {
        return durationYear;
    }
    public void setDurationYear(Boolean durationYear) {
        this.durationYear = durationYear;
    }
    public Boolean getReceiverOnly() {
        return receiverOnly;
    }
    public void setReceiverOnly(Boolean receiverOnly) {
        this.receiverOnly = receiverOnly;
    }
    public Boolean getCommunicateAddress() {
        return communicateAddress;
    }
    public void setCommunicateAddress(Boolean communicateAddress) {
        this.communicateAddress = communicateAddress;
    }
    public Boolean getContinuousLocation() {
        return continuousLocation;
    }
    public void setContinuousLocation(Boolean continuousLocation) {
        this.continuousLocation = continuousLocation;
    }
    public Boolean getMarketingContentAllProducts() {
        return marketingContentAllProducts;
    }
    public void setMarketingContentAllProducts(Boolean marketingContentAllProducts) {
        this.marketingContentAllProducts = marketingContentAllProducts;
    }
    public Boolean getMarketingFrequencyOncePerMonth() {
        return marketingFrequencyOncePerMonth;
    }
    public void setMarketingFrequencyOncePerMonth(Boolean marketingFrequencyOncePerMonth) {
        this.marketingFrequencyOncePerMonth = marketingFrequencyOncePerMonth;
    }
    public String getBillingAddressPostalCode() {
        return billingAddressPostalCode;
    }
    public void setBillingAddressPostalCode(String billingAddressPostalCode) {
        this.billingAddressPostalCode = billingAddressPostalCode;
    }
    public String getBillingAddressCity() {
        return billingAddressCity;
    }
    public void setBillingAddressCity(String billingAddressCity) {
        this.billingAddressCity = billingAddressCity;
    }
    public String getPostingAddressPostalCode() {
        return postingAddressPostalCode;
    }
    public void setPostingAddressPostalCode(String postingAddressPostalCode) {
        this.postingAddressPostalCode = postingAddressPostalCode;
    }
    public String getPostingAddressCity() {
        return postingAddressCity;
    }
    public void setPostingAddressCity(String postingAddressCity) {
        this.postingAddressCity = postingAddressCity;
    }
    public Boolean getIsBudgeaEnabled() {
        return isBudgeaEnabled;
    }
    public void setIsBudgeaEnabled(Boolean isBudgeaEnabled) {
        this.isBudgeaEnabled = isBudgeaEnabled;
    }
}

