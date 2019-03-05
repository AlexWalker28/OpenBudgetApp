package kg.kloop.android.openbudgetapp.objects;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tender implements Serializable {
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("deadline")
    @Expose
    private String deadline;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("isCompleted")
    @Expose
    private Boolean isCompleted;
    @SerializedName("link_id")
    @Expose
    private String link_id;
    @SerializedName("method")
    @Expose
    private String method;
    @SerializedName("orgAddress")
    @Expose
    private String orgAddress;
    @SerializedName("orgName")
    @Expose
    private String orgName;
    @SerializedName("orgPhone")
    @Expose
    private String orgPhone;
    @SerializedName("planSum")
    @Expose
    private long planSum;
    @SerializedName("procuringEntity")
    @Expose
    private String procuringEntity;
    @SerializedName("published")
    @Expose
    private String published;
    @SerializedName("purchase")
    @Expose
    private String purchase;
    @SerializedName("tender_num")
    @Expose
    private String tender_num;
    @SerializedName("validPeriod")
    @Expose
    private String validPeriod;
    @SerializedName("hasTasks")
    @Expose
    private Boolean hasTasks;
    @SerializedName("hasWork")
    @Expose
    private Boolean hasWork;
    @SerializedName("createTime")
    @Expose
    private long createTime;
    @SerializedName("editTime")
    @Expose
    private long editTime;

    public Tender() {
        isCompleted = false;
        hasTasks = false;
        hasWork = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink_id() {
        return link_id;
    }

    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }

    public String getTender_num() {
        return tender_num;
    }

    public void setTender_num(String tender_num) {
        this.tender_num = tender_num;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public String getOrgPhone() {
        return orgPhone;
    }

    public void setOrgPhone(String orgPhone) {
        this.orgPhone = orgPhone;
    }

    public String getPurchase() {
        return purchase;
    }

    public void setPurchase(String purchase) {
        this.purchase = purchase;
    }

    public String getProcuringEntity() {
        return procuringEntity;
    }

    public void setProcuringEntity(String procuringEntity) {
        this.procuringEntity = procuringEntity;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getPlanSum() {
        return planSum;
    }

    public void setPlanSum(long planSum) {
        this.planSum = planSum;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(String validPeriod) {
        this.validPeriod = validPeriod;
    }

    public Boolean getHasTasks() {
        return hasTasks;
    }

    public void setHasTasks(Boolean hasTasks) {
        this.hasTasks = hasTasks;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
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

    public long getEditTime() {
        return editTime;
    }

    public void setEditTime(long editTime) {
        this.editTime = editTime;
    }
}
