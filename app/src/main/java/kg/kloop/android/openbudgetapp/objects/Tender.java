package kg.kloop.android.openbudgetapp.objects;

import java.io.Serializable;

import com.google.firebase.Timestamp;
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
    private String procurement_object;
    @SerializedName("procuring_entity")
    @Expose
    private String procuring_entity;
    @SerializedName("procurement_format")
    @Expose
    private String procurement_format;
    @SerializedName("procurement_method")
    @Expose
    private String procurement_method;
    @SerializedName("planned_sum")
    @Expose
    private String planned_sum;
    @SerializedName("publication_date")
    @Expose
    private String publication_date;
    /*@SerializedName("__last_seen")
    @Expose
    private Timestamp __last_seen;
    @SerializedName("__first_seen")
    @Expose
    private Timestamp __first_seen;*/
    @SerializedName("guarantee_provision")
    @Expose
    private String guarantee_provision;
    @SerializedName("actual_address")
    @Expose
    private String actual_address;
    @SerializedName("phone_number")
    @Expose
    private String phone_number;
    @SerializedName("planned_sum_int")
    @Expose
    private Long planned_sum_int;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("due_date")
    @Expose
    private String due_date;
    @SerializedName("finance_source")
    @Expose
    private String finance_source;
    @SerializedName("number_of_ads_for_contract")
    @Expose
    private String number_of_ads_for_contract;
    @SerializedName("cancel_reason")
    @Expose
    private String cancel_reason;
    @SerializedName("eval_pub_date")
    @Expose
    private String eval_pub_date;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ateCode")
    @Expose
    private Long ateCode;
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
    private Long streetAddress;

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

    public String getProcurement_object() {
        return procurement_object;
    }

    public void setProcurement_object(String procurement_object) {
        this.procurement_object = procurement_object;
    }

    public String getProcuring_entity() {
        return procuring_entity;
    }

    public void setProcuring_entity(String procuring_entity) {
        this.procuring_entity = procuring_entity;
    }

    public String getProcurement_format() {
        return procurement_format;
    }

    public void setProcurement_format(String procurement_format) {
        this.procurement_format = procurement_format;
    }

    public String getProcurement_method() {
        return procurement_method;
    }

    public void setProcurement_method(String procurement_method) {
        this.procurement_method = procurement_method;
    }

    public String getPlanned_sum() {
        return planned_sum;
    }

    public void setPlanned_sum(String planned_sum) {
        this.planned_sum = planned_sum;
    }

    public String getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(String publication_date) {
        this.publication_date = publication_date;
    }

    /*public Timestamp get__last_seen() {
        return __last_seen;
    }

    public void set__last_seen(Timestamp __last_seen) {
        this.__last_seen = __last_seen;
    }

    public Timestamp get__first_seen() {
        return __first_seen;
    }

    public void set__first_seen(Timestamp __first_seen) {
        this.__first_seen = __first_seen;
    }*/

    public String getGuarantee_provision() {
        return guarantee_provision;
    }

    public void setGuarantee_provision(String guarantee_provision) {
        this.guarantee_provision = guarantee_provision;
    }

    public String getActual_address() {
        return actual_address;
    }

    public void setActual_address(String actual_address) {
        this.actual_address = actual_address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Long getPlanned_sum_int() {
        return planned_sum_int;
    }

    public void setPlanned_sum_int(Long planned_sum_int) {
        this.planned_sum_int = planned_sum_int;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getFinance_source() {
        return finance_source;
    }

    public void setFinance_source(String finance_source) {
        this.finance_source = finance_source;
    }

    public String getNumber_of_ads_for_contract() {
        return number_of_ads_for_contract;
    }

    public void setNumber_of_ads_for_contract(String number_of_ads_for_contract) {
        this.number_of_ads_for_contract = number_of_ads_for_contract;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public String getEval_pub_date() {
        return eval_pub_date;
    }

    public void setEval_pub_date(String eval_pub_date) {
        this.eval_pub_date = eval_pub_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAteCode() {
        return ateCode;
    }

    public void setAteCode(Long ateCode) {
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

    public Long getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(Long streetAddress) {
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
