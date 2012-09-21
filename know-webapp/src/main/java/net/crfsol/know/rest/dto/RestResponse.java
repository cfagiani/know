package net.crfsol.know.rest.dto;

/**
 * @author Christopher Fagiani
 */
public class RestResponse<T> {
    private int totalCount;
    private String responseType = "success";
    private String errorMessage;
    private T payload;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public String getResponseType() {
        return responseType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.responseType = "error";
    }

}