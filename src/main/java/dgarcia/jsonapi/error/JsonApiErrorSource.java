package dgarcia.jsonapi.error;

import com.fasterxml.jackson.databind.node.ObjectNode;
import dgarcia.jsonapi.mapper.JsonApiMapper;

public class JsonApiErrorSource {
    private String pointer;
    private String parameter;

    // Constructor
    public JsonApiErrorSource(String pointer, String parameter) {
        this.pointer = pointer;
        this.parameter = parameter;
    }

    // Getters y Setters
    public String getPointer() {
        return pointer;
    }

    public void setPointer(String pointer) {
        this.pointer = pointer;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    // Convertir a JSON
    public ObjectNode toJson(JsonApiMapper mapper) {
        ObjectNode sourceNode = mapper.objectMapper.createObjectNode();
        if (pointer != null) {
            sourceNode.put("pointer", this.pointer);
        }
        if (parameter != null) {
            sourceNode.put("parameter", this.parameter);
        }
        return sourceNode;
    }
}
