package com.collegediary.lessons;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Vinceal
 */
public class LessonsDataModel extends ListDataModel<Lessons> implements SelectableDataModel<Lessons>, Serializable {

    public LessonsDataModel() {
    }

    public LessonsDataModel(List<Lessons> list) {
        super(list);
    }
    
    @Override
    public Object getRowKey(Lessons lesson) {
        return lesson.getId();
    }

    @Override
    public Lessons getRowData(String rowKey) {
        List<Lessons> lessonsList = (List<Lessons>) getWrappedData();
        
        for (Lessons lessons : lessonsList){
            if(lessons.getId().equals(rowKey)){
                return lessons;
            }
        }
        return null;
    }
    
}
