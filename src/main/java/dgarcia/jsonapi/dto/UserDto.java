package dgarcia.jsonapi.dto;

import dgarcia.jsonapi.anotation.JsonApiAttribute;
import dgarcia.jsonapi.anotation.JsonApiId;
import dgarcia.jsonapi.anotation.JsonApiRelationship;
import dgarcia.jsonapi.anotation.JsonApiResource;
import java.util.List;

@JsonApiResource(type = "user")
public class UserDto {
    @JsonApiId
    private String id;
    @JsonApiAttribute
    private String name;
    @JsonApiRelationship(included = true)
    private List<RoleDto> role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RoleDto> getRole() {
        return role;
    }

    public void setRole(List<RoleDto> role) {
        this.role = role;
    }
}
