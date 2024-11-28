package dgarcia.jsonapi.dto;

import dgarcia.jsonapi.anotation.JsonApiAttribute;
import dgarcia.jsonapi.anotation.JsonApiId;
import dgarcia.jsonapi.anotation.JsonApiRelationship;
import dgarcia.jsonapi.anotation.JsonApiResource;
import java.util.List;

@JsonApiResource(type = "role")
public class RoleDto {
    @JsonApiId
    private String id;
    @JsonApiAttribute
    private String roleName;
    @JsonApiRelationship(included = true)
    private List<SaleDTO> sales;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SaleDTO> getSales() {
        return sales;
    }

    public void setSales(List<SaleDTO> sales) {
        this.sales = sales;
    }
}

