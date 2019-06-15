package kg.kloop.android.openbudgetapp.objects;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tender implements Serializable {
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("procurement_object")
    @Expose
    private String procurementObject;
    @SerializedName("procuring_entity")
    @Expose
    private String procuringEntity;
    @SerializedName("procurement_format")
    @Expose
    private String procurementFormat;
    @SerializedName("procurement_method")
    @Expose
    private String procurementMethod;
    @SerializedName("planned_sum")
    @Expose
    private String plannedSum;
    @SerializedName("publication_date")
    @Expose
    private String publicationDate;
    @SerializedName("__last_seen")
    @Expose
    private String lastSeen;
    @SerializedName("__first_seen")
    @Expose
    private long firstSeen;
    @SerializedName("guarantee_provision")
    @Expose
    private String guaranteeProvision;
    @SerializedName("actual_address")
    @Expose
    private String actualAddress;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("planned_sum_int")
    @Expose
    private long plannedSumLong;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("due_date")
    @Expose
    private String dueDate;
    @SerializedName("finance_source")
    @Expose
    private String financeSource;
    @SerializedName("number_of_ads_for_contract")
    @Expose
    private String numberOfAdsForContract;
    @SerializedName("cancel_reason")
    @Expose
    private String cancelReason;
    @SerializedName("eval_pub_date")
    @Expose
    private String evalPubDate;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ateCode")
    @Expose
    private String ateCode;
    @SerializedName("countryName")
    @Expose
    private String countryName;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("subRegion")
    @Expose
    private String subRegion;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("subDistrict")
    @Expose
    private String subDistrict;
    @SerializedName("subSubDistrict")
    @Expose
    private String subSubDistrict;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("streetAddress")
    @Expose
    private String streetAddress;

    private Boolean isCompleted;
    private Boolean hasTasks;
    private Boolean hasWork;
    private long createTime;
    private long updateTime;

    public Tender() {
        isCompleted = false;
        hasTasks = false;
        hasWork = false;
        createTime = 0;
        updateTime = 0;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProcurementObject() {
        return procurementObject;
    }

    public void setProcurementObject(String procurementObject) {
        this.procurementObject = procurementObject;
    }

    public String getProcuringEntity() {
        return procuringEntity;
    }

    public void setProcuringEntity(String procuringEntity) {
        this.procuringEntity = procuringEntity;
    }

    public String getProcurementFormat() {
        return procurementFormat;
    }

    public void setProcurementFormat(String procurementFormat) {
        this.procurementFormat = procurementFormat;
    }

    public String getProcurementMethod() {
        return procurementMethod;
    }

    public void setProcurementMethod(String procurementMethod) {
        this.procurementMethod = procurementMethod;
    }

    public String getPlannedSum() {
        return plannedSum;
    }

    public void setPlannedSum(String plannedSum) {
        this.plannedSum = plannedSum;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public long getFirstSeen() {
        return firstSeen;
    }

    public void setFirstSeen(long firstSeen) {
        this.firstSeen = firstSeen;
    }

    public String getGuaranteeProvision() {
        return guaranteeProvision;
    }

    public void setGuaranteeProvision(String guaranteeProvision) {
        this.guaranteeProvision = guaranteeProvision;
    }

    public String getActualAddress() {
        return actualAddress;
    }

    public void setActualAddress(String actualAddress) {
        this.actualAddress = actualAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getPlannedSumLong() {
        return plannedSumLong;
    }

    public void setPlannedSumLong(long plannedSumLong) {
        this.plannedSumLong = plannedSumLong;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getFinanceSource() {
        return financeSource;
    }

    public void setFinanceSource(String financeSource) {
        this.financeSource = financeSource;
    }

    public String getNumberOfAdsForContract() {
        return numberOfAdsForContract;
    }

    public void setNumberOfAdsForContract(String numberOfAdsForContract) {
        this.numberOfAdsForContract = numberOfAdsForContract;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getEvalPubDate() {
        return evalPubDate;
    }

    public void setEvalPubDate(String evalPubDate) {
        this.evalPubDate = evalPubDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAteCode() {
        return ateCode;
    }

    public void setAteCode(String ateCode) {
        this.ateCode = ateCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubRegion() {
        return subRegion;
    }

    public void setSubRegion(String subRegion) {
        this.subRegion = subRegion;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(String subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getSubSubDistrict() {
        return subSubDistrict;
    }

    public void setSubSubDistrict(String subSubDistrict) {
        this.subSubDistrict = subSubDistrict;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    public Boolean getHasTasks() {
        return hasTasks;
    }

    public void setHasTasks(Boolean hasTasks) {
        this.hasTasks = hasTasks;
    }

    public Boolean getHasWork() {
        return hasWork;
    }

    public void setHasWork(Boolean hasWork) {
        this.hasWork = hasWork;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
