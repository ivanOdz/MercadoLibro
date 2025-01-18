package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.utils.BookStateWrapper;

import javax.ws.rs.core.UriInfo;

public class BookConditionDTO {
    private String condition;
    private Integer amount;

    public BookConditionDTO() {
    }

    static public BookConditionDTO fromBookState(UriInfo uriInfo, BookStateWrapper bookStateWrapper) {
        BookConditionDTO dto = new BookConditionDTO();
        dto.condition = bookStateWrapper.getBookState().getValue();
        dto.amount = bookStateWrapper.getResultByState();
        return dto;
    }

    public String getCondition() {
        return condition;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
