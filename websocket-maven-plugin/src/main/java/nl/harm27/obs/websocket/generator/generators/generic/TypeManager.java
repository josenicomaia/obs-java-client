package nl.harm27.obs.websocket.generator.generators.generic;

import com.helger.jcodemodel.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.helger.jcodemodel.AbstractJType.parse;

public class TypeManager extends GenericGenerator {
    private final JCodeModel codeModel;
    private final Map<String, JDefinedClass> apiTypes;
    private final JPackage enumPackage;
    private JDefinedClass timeUtilClass;

    public TypeManager(JCodeModel codeModel, JPackage enumPackage) {
        this.codeModel = codeModel;
        this.enumPackage = enumPackage;
        this.apiTypes = new HashMap<>();
    }

    public AbstractJType getType(JDefinedClass targetClass, String fieldName, String typeName, String description) throws JCodeModelException, UnknownTypeException {
        if (typeName.contains("Array")) {
            AbstractJType arraySubType = getType(targetClass, fieldName, typeName.replace("Array<", "").replace(">", ""), description);
            return codeModel.ref(List.class).narrow(arraySubType);
        }

        JDefinedClass subClassType = getSubClassType(targetClass, typeName);
        if (subClassType != null)
            return subClassType;

        JDefinedClass apiType = apiTypes.get(typeName);
        if (apiType != null)
            return apiType;

        JDefinedClass customType = getCustomType(findParentClass(targetClass).name().toLowerCase(), fieldName.toLowerCase(), description);
        if (customType != null)
            return customType;

        AbstractJType primitiveType = getPrimitiveType(typeName);
        if (primitiveType != null)
            return primitiveType;

        throw new UnknownTypeException(typeName);
    }

    public void addApiType(String typeName, JDefinedClass typeClass) {
        apiTypes.put(typeName, typeClass);
    }

    public AbstractJType getListPrimitiveType(String typeName) {
        return codeModel.ref(List.class).narrow(getPrimitiveType(typeName));
    }

    public AbstractJType getPrimitiveType(String typeName) {
        switch (typeName) {
            case "Number":
                return codeModel.ref(Number.class);
            case "string":
            case "String":
                return codeModel.ref(String.class);
            case "Boolean":
                return codeModel.ref(Boolean.class);
            case "Object":
                return codeModel.ref(Object.class);
            case "Integer":
                return codeModel.ref(Integer.class);
            case "bool":
                return codeModel.BOOLEAN;
            case "boolean":
            case "int":
            case "double":
            case "float":
            case "void":
                return parse(codeModel, typeName);
            default:
                return null;
        }
    }

    private JDefinedClass getCustomType(String className, String fieldName, String description) throws JCodeModelException {
        if (isSourceTypeEnum(className, fieldName))
            return generateEnum(enumPackage, "SourceType", StringConstants.SOURCE_TYPE_VALUES, description);
        else if (isMediaStateEnum(className, fieldName))
            return generateEnum(enumPackage, "MediaState", StringConstants.MEDIA_STATE_FIELD, description);
        else if (isMonitorTypeEnum(className, fieldName))
            return generateEnum(enumPackage, "MonitorType", StringConstants.MONiTOR_TYPE_FIELD, description);
        else if ("GetTextGDIPlusProperties".equalsIgnoreCase(className) && "align".equalsIgnoreCase(fieldName))
            return generateEnum(enumPackage, "TextAlignment", StringConstants.TEXT_ALIGNMENT_FIELD, description);
        else if ("GetTextGDIPlusProperties".equalsIgnoreCase(className) && "valign".equalsIgnoreCase(fieldName))
            return generateEnum(enumPackage, "TextVerticalAlignment", StringConstants.TEXT_VERTICAL_ALIGNMENT_FIELD, description);
        else if ("Bounds".equalsIgnoreCase(className) && "bounds.type".equalsIgnoreCase(fieldName))
            return generateEnum(enumPackage, "BoundingBoxType", StringConstants.BOUNDING_BOX_TYPE_VALUES, description);
        else if ("OpenProjector".equalsIgnoreCase(className) && "type".equalsIgnoreCase(fieldName))
            return generateEnum(enumPackage, "ProjectorType", StringConstants.PROJECTOR_TYPE_FIELD, description);
        else if ("MoveSourceFilter".equalsIgnoreCase(className) && "movementType".equalsIgnoreCase(fieldName))
            return generateEnum(enumPackage, "MovementType", StringConstants.MOVEMENT_TYPE_FIELD, description);
        return null;
    }

    private boolean isMonitorTypeEnum(String className, String fieldName) {
        if ("GetAudioMonitorType".equalsIgnoreCase(className) && "monitorType".equalsIgnoreCase(fieldName))
            return true;
        else
            return "SetAudioMonitorType".equalsIgnoreCase(className) && "monitorType".equalsIgnoreCase(fieldName);
    }

    private boolean isMediaStateEnum(String className, String fieldName) {
        if ("GetMediaState".equalsIgnoreCase(className) && "mediaState".equalsIgnoreCase(fieldName))
            return true;
        else
            return "MediaSources".equalsIgnoreCase(className) && "mediaSources.*.mediaState".equalsIgnoreCase(fieldName);
    }

    private boolean isSourceTypeEnum(String className, String fieldName) {
        if (StringConstants.SOURCE_TYPE_CLASSES.contains(className) && StringConstants.SOURCE_TYPE_FIELD.equalsIgnoreCase(fieldName))
            return true;
        else if ("SceneItem".equalsIgnoreCase(className) && "type".equalsIgnoreCase(fieldName))
            return true;
        else if ("Sources".equalsIgnoreCase(className) && "sources.*.type".equalsIgnoreCase(fieldName))
            return true;
        else
            return "Types".equalsIgnoreCase(className) && "types.*.type".equalsIgnoreCase(fieldName);
    }

    private JDefinedClass getSubClassType(JDefinedClass targetClass, String typeName) {
        return getRootClass(targetClass).classes().stream().filter(sub -> typeName.equalsIgnoreCase(sub.name())).findFirst().orElse(null);
    }

    public AbstractJType getOptionalForType(AbstractJType fieldType) {
        return codeModel.ref(Optional.class).narrow(fieldType);
    }

    public AbstractJType getOptionalForType(Class<?> fieldClass) {
        return getOptionalForType(codeModel.ref(fieldClass));
    }

    public IJExpression getOptionalReturnForField(IJExpression fieldVar) {
        return codeModel.ref(Optional.class).staticInvoke("ofNullable").arg(fieldVar);
    }

    public JInvocation getEmptyOptional() {
        return codeModel.ref(Optional.class).staticInvoke("empty");
    }

    public AbstractJClass getEnumClassMap(AbstractJClass enumClass) {
        return codeModel.ref(Map.class).narrow(enumClass, codeModel.ref(Class.class).narrowAny());
    }

    public IJExpression getEnumMap(JDefinedClass enumClass) {
        return codeModel.ref(EnumMap.class)._new().arg(enumClass.dotclass());
    }

    public AbstractJClass getSupplier(AbstractJType targetClass) {
        return codeModel.ref(Supplier.class).narrow(targetClass);
    }

    public AbstractJClass getConsumer(AbstractJClass targetClass) {
        return codeModel.ref(Consumer.class).narrow(targetClass);
    }

    public AbstractJClass getBiConsumer(AbstractJClass targetClass1, AbstractJClass targetClass2) {
        return codeModel.ref(BiConsumer.class).narrow(targetClass1, targetClass2);
    }

    public JInvocation getParseDuration(JFieldVar fieldVar) {
        return timeUtilClass.staticInvoke("parseDuration").arg(fieldVar);
    }

    public boolean isBoolean(AbstractJType fieldType) {
        return Arrays.asList("boolean", "Boolean").contains(fieldType.name());
    }

    public JPrimitiveType getVoidType() {
        return codeModel.VOID;
    }

    public void registerTimeUtil(JDefinedClass timeUtilClass) {
        this.timeUtilClass = timeUtilClass;
    }

    public IJExpression getException(Class<? extends Exception> exceptionClass, String message) {
        return codeModel.ref(exceptionClass)._new().arg(message);
    }

    public AbstractJType getAnyClassType() {
        return codeModel.ref(Class.class).narrowAny();
    }

    public IJExpression getArraysAsList(JInvocation array) {
        return codeModel.ref(Arrays.class).staticInvoke("asList").arg(array);
    }
}
