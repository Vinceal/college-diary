package com.collegediary.teachers;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Vinceal
 */
public class TeacherDataModel extends ListDataModel<Teacher> implements SelectableDataModel<Teacher>, Serializable{

    public TeacherDataModel() {
    }

    public TeacherDataModel(List<Teacher> list) {
        super(list);
    }
    
    @Override
    public Object getRowKey(Teacher teacher) {
        return teacher.getId();
    }

    @Override
    public Teacher getRowData(String rowKey) {
        List<Teacher> tch = (List<Teacher>) getWrappedData();
        
        for (Teacher teacher : tch){
            if (teacher.getId().equals(rowKey)){
                return teacher;
            }
        }
        
        return null;
    }
    
}
