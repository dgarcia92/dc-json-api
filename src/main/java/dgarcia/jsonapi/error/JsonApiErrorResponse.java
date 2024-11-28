package dgarcia.jsonapi.error;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import dgarcia.jsonapi.mapper.JsonApiMapper;

import java.util.List;

public class JsonApiErrorResponse {
    private String status;
    private String code;
    private String title;
    private String detail;
    private String suggestion;
    private List<JsonApiErrorSource> source;
    private JsonApiLinks links;

    // Constructor
    public JsonApiErrorResponse(String status, String code, String title, String detail, String suggestion) {
        this.status = status;
        this.code = code;
        this.title = title;
        this.detail = detail;
        this.suggestion = suggestion;
        this.source = null;
        this.links = new JsonApiLinks("https://example.com/errors/" + code);
    }

    // Getters y Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public List<JsonApiErrorSource> getSource() {
        return source;
    }

    public void setSource(List<JsonApiErrorSource> source) {
        this.source = source;
    }

    public JsonApiLinks getLinks() {
        return links;
    }

    public void setLinks(JsonApiLinks links) {
        this.links = links;
    }

    // Convertir a JSON
    public ObjectNode toJson(JsonApiMapper mapper) throws IllegalAccessException {
        ObjectNode errorNode = mapper.objectMapper.createObjectNode();
        errorNode.put("status", this.status);
        errorNode.put("code", this.code);
        errorNode.put("title", this.title);
        errorNode.put("detail", this.detail);
        errorNode.put("suggestion", this.suggestion);

        if (this.source != null) {
            ArrayNode sourceArray = mapper.objectMapper.createArrayNode();
            for (JsonApiErrorSource errorSource : source) {
                sourceArray.add(errorSource.toJson(mapper));
            }
            errorNode.set("source", sourceArray);
        }

        errorNode.set("links", this.links.toJson(mapper));

        return errorNode;
    }
}

