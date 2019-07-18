package kg.kloop.android.openbudgetapp.objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import com.google.firebase.Timestamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tenders_table")
public class Tender implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("url")
    @Expose
    private String url;
    @ColumnInfo(name = "procurement_object")
    @SerializedName("procurement_object")
    @Expose
    private String procurement_object;
    @ColumnInfo(name = "procuring_entity")
    @SerializedName("procuring_entity")
    @Expose
    private String procuring_entity;
    @ColumnInfo(name = "procurement_format")
    @SerializedName("procurement_format")
    @Expose
    private String procurement_format;
    @ColumnInfo(name = "procurement_method")
    @SerializedName("procurement_method")
    @Expose
    private String procurement_method;
    @ColumnInfo(name = "planned_sum")
    @SerializedName("planned_sum")
    @Expose
    private String planned_sum;
    @ColumnInfo(name = "publication_date")
    @SerializedName("publication_date")
    @Expose
    private String publication_date;
    /*@SerializedName("__last_seen")
    @Expose
    private Timestamp __last_seen;
    @SerializedName("__first_seen")
    @Expose
    private Timestamp __first_seen;*/
    @ColumnInfo(name = "guarantee_provision")
    @SerializedName("guarantee_provision")
    @Expose
    private String guarantee_provision;
    @ColumnInfo(name = "actual_address")
    @SerializedName("actual_address")
    @Expose
    private String actual_address;
    @ColumnInfo(name = "phone_number")
    @SerializedName("phone_number")
    @Expose
    private String phone_number;
    @ColumnInfo(name = "planned_sum_int")
    @SerializedName("planned_sum_int")
    @Expose
    private Long planned_sum_int;
    @ColumnInfo(name = "currency")
    @SerializedName("currency")
    @Expose
    private String currency;
    @ColumnInfo(name = "due_date")
    @SerializedName("due_date")
    @Expose
    private String due_date;
    @ColumnInfo(name = "finance_source")
    @SerializedName("finance_source")
    @Expose
    private String finance_source;
    @ColumnInfo(name = "number_of_ads_for_contract")
    @SerializedName("number_of_ads_for_contract")
    @Expose
    private String number_of_ads_for_contract;
    @ColumnInfo(name = "cancel_reason")
    @SerializedName("cancel_reason")
    @Expose
    private String cancel_reason;
    @ColumnInfo(name = "eval_pub_date")
    @SerializedName("eval_pub_date")
    @Expose
    private String eval_pub_date;
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    private String id;
    @ColumnInfo(name = "ateCode")
    @SerializedName("ateCode")
    @Expose
    private Long ateCode;
    @ColumnInfo(name = "countryName")
    @SerializedName("countryName")
    @Expose
    private String countryName;
    @ColumnInfo(name = "region")
    @SerializedName("region")
    @Expose
    private String region;
    @ColumnInfo(name = "subRegion")
    @SerializedName("subRegion")
    @Expose
    private String subRegion;
    @ColumnInfo(name = "district")
    @SerializedName("district")
    @Expose
    private String district;
    @ColumnInfo(name = "subDistrict")
    @SerializedName("subDistrict")
    @Expose
    private String subDistrict;
    @ColumnInfo(name = "subSubDistrict")
    @SerializedName("subSubDistrict")
    @Expose
    private String subSubDistrict;
    @ColumnInfo(name = "locality")
    @SerializedName("locality")
    @Expose
    private String locality;
    @ColumnInfo(name = "streetAddress")
    @SerializedName("streetAddress")
    @Expose
    private Long streetAddress;

    @ColumnInfo(name = "is_completed")
    private Boolean isCompleted;
    @ColumnInfo(name = "has_tasks")
    private Boolean hasTasks;
    @ColumnInfo(name = "has_work")
    private Boolean hasWork;
    @ColumnInfo(name = "create_time_milli")
    private long createTime;
    @ColumnInfo(name = "update_time_milli")
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
