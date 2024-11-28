package dgarcia.jsonapi;

import com.fasterxml.jackson.databind.node.ObjectNode;
import dgarcia.jsonapi.dto.RoleDto;
import dgarcia.jsonapi.dto.SaleDTO;
import dgarcia.jsonapi.dto.UserDto;
import dgarcia.jsonapi.error.JsonApiErrorResponse;
import dgarcia.jsonapi.mapper.JsonApiMapper;
import dgarcia.jsonapi.mapper.MaxDepth;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {


        //TODO: Implementar el número de profundidad de la relación (relashionship) en el JsonApiMapper

        UserDto user = new UserDto();
        user.setId("1");
        user.setName("John Doe");

        SaleDTO sale = new SaleDTO();
        sale.setId("1");
        sale.setPrice(100.0);

        List<SaleDTO> sales = new ArrayList<>();
        sales.add(sale);

        RoleDto role = new RoleDto();
        role.setId("admin");
        role.setRoleName("Administrator");
        role.setSales(sales);

        RoleDto role2 = new RoleDto();
        role2.setId("user");
        role2.setRoleName("User");
        role2.setSales(sales);

        List<RoleDto> roleList = new ArrayList<>();
        roleList.add(role);
        roleList.add(role2);
        role.setSales(sales);

        user.setRole(roleList);

        JsonApiMapper mapper = new JsonApiMapper();
        ObjectNode jsonApiOutput = mapper.toJsonApi(user, MaxDepth.TWO);

        System.out.println(jsonApiOutput.toPrettyString());


        JsonApiErrorResponse errorResponse = new JsonApiErrorResponse(
                "400",
                "ERR001",
                "Bad Request",
                "Error in the request parameters",
                "Please check the request parameters"
        );


        System.err.println(errorResponse.toJson(mapper).toPrettyString());

    }
}