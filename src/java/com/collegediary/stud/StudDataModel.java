package com.collegediary.stud;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Vinceal
 */
public class StudDataModel extends ListDataModel<Students> implements SelectableDataModel<Students>, Serializable{
    
    StudDataModel(){ }
    
    StudDataModel(List<Students> stud) {
        super(stud);
    }

    @Override
    public Object getRowKey(Students stud) {
        return stud.getFio();
    }

    @Override
    public Students getRowData(String rowKey) {
        List<Students> wrapDataStud = (List<Students>) getWrappedData();
        for (Students stud : wrapDataStud) {
            if (stud.getFio().equals(rowKey)) {
                return stud;
            }
        }

        return null;
    }
    
}
