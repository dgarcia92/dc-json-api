package dgarcia.jsonapi.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dgarcia.jsonapi.anotation.JsonApiAttribute;
import dgarcia.jsonapi.anotation.JsonApiId;
import dgarcia.jsonapi.anotation.JsonApiRelationship;
import dgarcia.jsonapi.anotation.JsonApiResource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class JsonApiMapper {
    public final ObjectMapper objectMapper;

    public JsonApiMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public <T> ObjectNode toJsonApi(T resource, MaxDepth maxDepth) throws IllegalAccessException {
        return toJsonApi(resource, maxDepth.getDepth(), 0);
    }

    private <T> ObjectNode toJsonApi(T resource, int maxDepth, int currentDepth) throws IllegalAccessException {
        if (currentDepth > maxDepth) {
            return null;
        }

        validateResource(resource);

        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectNode dataNode = objectMapper.createObjectNode();
        ObjectNode attributesNode = objectMapper.createObjectNode();
        ObjectNode relationshipsNode = objectMapper.createObjectNode();
        ArrayNode includedNode = objectMapper.createArrayNode();

        JsonApiResource resourceAnnotation = resource.getClass().getAnnotation(JsonApiResource.class);
        dataNode.put("type", resourceAnnotation.type());

        for (Field field : resource.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(JsonApiId.class)) {
                Object idValue = field.get(resource);
                if (idValue == null) {
                    throw new IllegalArgumentException("El campo ID no puede ser nulo.");
                }
                dataNode.put("id", idValue.toString());
            }

            if (field.isAnnotationPresent(JsonApiAttribute.class)) {
                Object attributeValue = field.get(resource);
                if (attributeValue != null) {
                    attributesNode.put(field.getName(), attributeValue.toString());
                }
            }

            if (field.isAnnotationPresent(JsonApiRelationship.class)) {
                JsonApiRelationship relationshipAnnotation = field.getAnnotation(JsonApiRelationship.class);
                Object relatedObject = field.get(resource);

                if (relatedObject == null && relationshipAnnotation.included()) {
                    throw new IllegalArgumentException("Las relaciones incluidas no pueden ser nulas.");
                }

                ObjectNode relationshipNode = buildRelationshipNode(relatedObject, relationshipAnnotation);
                relationshipsNode.set(field.getName(), relationshipNode);

                if (relationshipAnnotation.included() && relatedObject != null) {
                    addIncludedNode(includedNode, relatedObject, maxDepth, currentDepth + 1);
                }
            }
        }

        dataNode.set("attributes", attributesNode);
        if (relationshipsNode.size() > 0) {
            dataNode.set("relationships", relationshipsNode);
        }
        rootNode.set("data", dataNode);

        if (includedNode.size() > 0) {
            rootNode.set("included", includedNode);
        }

        return rootNode;
    }

    private void addIncludedNode(ArrayNode includedNode, Object relatedObject, int maxDepth, int currentDepth) throws IllegalAccessException {
        if (relatedObject instanceof List<?>) {
            for (Object item : (List<?>) relatedObject) {
                if (!isAlreadyIncluded(includedNode, item) && currentDepth <= maxDepth) {
                    includedNode.add(toJsonApi(item, maxDepth, currentDepth).get("data"));
                }
            }
        } else if (!isAlreadyIncluded(includedNode, relatedObject) && currentDepth <= maxDepth) {
            includedNode.add(toJsonApi(relatedObject, maxDepth, currentDepth).get("data"));
        }
    }

    private boolean isAlreadyIncluded(ArrayNode includedNode, Object resource) throws IllegalAccessException {
        String resourceId = getResourceId(resource);
        String resourceType = getResourceType(resource);

        for (int i = 0; i < includedNode.size(); i++) {
            ObjectNode includedItem = (ObjectNode) includedNode.get(i);
            if (includedItem.get("id").asText().equals(resourceId) &&
                    includedItem.get("type").asText().equals(resourceType)) {
                return true;
            }
        }
        return false;
    }

    private String getResourceId(Object resource) throws IllegalAccessException {
        Field idField = findAnnotatedField(resource.getClass(), JsonApiId.class);
        if (idField != null) {
            idField.setAccessible(true);
            Object idValue = idField.get(resource);
            return idValue != null ? idValue.toString() : null;
        }
        return null;
    }

    private String getResourceType(Object resource) {
        JsonApiResource resourceAnnotation = resource.getClass().getAnnotation(JsonApiResource.class);
        return resourceAnnotation != null ? resourceAnnotation.type() : null;
    }

    private Field findAnnotatedField(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                return field;
            }
        }
        throw new IllegalArgumentException("No se encontró un campo con la anotación " + annotationClass.getSimpleName());
    }

    private void validateResource(Object resource) {
        if (resource.getClass().getAnnotation(JsonApiResource.class) == null) {
            throw new IllegalArgumentException("La clase " + resource.getClass().getName() + " debe estar anotada con @JsonApiResource");
        }
    }

    private ObjectNode buildRelationshipNode(Object relatedObject, JsonApiRelationship relationshipAnnotation) throws IllegalAccessException {
        ObjectNode relationshipNode = objectMapper.createObjectNode();

        if (relatedObject instanceof List<?>) {
            ArrayNode dataArray = objectMapper.createArrayNode();
            for (Object item : (List<?>) relatedObject) {
                ObjectNode dataNode = objectMapper.createObjectNode();
                dataNode.put("id", getResourceId(item));
                dataNode.put("type", getResourceType(item));
                dataArray.add(dataNode);
            }
            relationshipNode.set("data", dataArray);
        } else if (relatedObject != null) {
            ObjectNode dataNode = objectMapper.createObjectNode();
            dataNode.put("id", getResourceId(relatedObject));
            dataNode.put("type", getResourceType(relatedObject));
            relationshipNode.set("data", dataNode);
        } else {
            relationshipNode.putNull("data");
        }

        return relationshipNode;
    }
}
