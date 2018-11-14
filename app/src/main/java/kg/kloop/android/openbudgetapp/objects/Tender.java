package kg.kloop.android.openbudgetapp.objects;

import java.io.Serializable;

public class Tender implements Serializable {
    private String id;
    private String link_id;
    private String tender_num;
    private String orgName;
    private String orgAddress;
    private String orgPhone;
    private String purchase;
    private String procuringEntity;
    private String format;
    private String method;
    private long planSum;
    private String published;
    private String currency;
    private String deadline;
    private String validPeriod;
    private Boolean hasTasks;
    private boolean isCompleted;

    public Tender() {

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
}
