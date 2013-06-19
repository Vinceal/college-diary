package com.collegediary.stud;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Vinceal
 */
public class StudDataModelAll extends ListDataModel<StudentsAll> implements SelectableDataModel<StudentsAll>, Serializable{

    public StudDataModelAll() {
    }

    public StudDataModelAll(List<StudentsAll> list) {
        super(list);
    }    
    
    @Override
    public Object getRowKey(StudentsAll stud) {
       return stud.idStudent;
    }

    @Override
    public StudentsAll getRowData(String rowKey) {
       List<StudentsAll> wrapDataStud = (List<StudentsAll>) getWrappedData();
        for (StudentsAll stud : wrapDataStud) {
            if (stud.getIdStudent().equals(rowKey)) {
                return stud;
            }
        }

        return null;
    }
    
}
