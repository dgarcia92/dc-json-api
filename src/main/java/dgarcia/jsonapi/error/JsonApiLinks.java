package dgarcia.jsonapi.error;

import com.fasterxml.jackson.databind.node.ObjectNode;
import dgarcia.jsonapi.mapper.JsonApiMapper;

public class JsonApiLinks {
    private String about;

    // Constructor
    public JsonApiLinks(String about) {
        this.about = about;
    }

    // Getters y Setters
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    // Convertir a JSON
    public ObjectNode toJson(JsonApiMapper mapper) {
        ObjectNode linksNode = mapper.objectMapper.createObjectNode();
        linksNode.put("about", this.about);
        return linksNode;
    }
}
