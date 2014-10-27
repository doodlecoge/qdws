package com.cisex.util;

import com.cisex.model.Defect;
import com.cisex.model.DefectField;
import com.cisex.model.TimeField;

import javax.persistence.Column;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hch
 * Date: 13-6-30
 * Time: 下午9:41
 * To change this template use File | Settings | File Templates.
 */
public class Mapping {
    public static List<String> ColumnNames = getDefectColumns();
    private static Map<String, Field> ColumnFieldMapping = getDefectColumnFieldMapping();

    public static Field GetColumnField(String columnName) {
        return ColumnFieldMapping.get(columnName);
    }

    private static List<String> getDefectColumns() {
        List<String> cols = new ArrayList<String>();

        for (DefectField field : DefectField.values()) {
            cols.add(field.dbColName);
        }

        for (TimeField field : TimeField.values()) {
            cols.add(field.dbColName);
        }

        return cols;
    }

    private static Map<String, Field> getDefectColumnFieldMapping() {
        Field[] fields = Defect.class.getDeclaredFields();

        Map<String, Field> mapping = new HashMap<String, Field>();

        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (!(annotation instanceof Column)) continue;
                mapping.put(((Column) annotation).name(), field);
                break;
            }
        }

        return mapping;
    }
}
