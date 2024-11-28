package dgarcia.jsonapi.dto;

import dgarcia.jsonapi.anotation.JsonApiAttribute;
import dgarcia.jsonapi.anotation.JsonApiId;
import dgarcia.jsonapi.anotation.JsonApiResource;

@JsonApiResource(type = "sales")
public class SaleDTO {
    @JsonApiId
    private String id;
    @JsonApiAttribute
    private Double price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
