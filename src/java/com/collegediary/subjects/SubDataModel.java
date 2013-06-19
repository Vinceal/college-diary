package com.collegediary.subjects;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author VinceAL
 */
public class SubDataModel extends ListDataModel<Subjects> implements SelectableDataModel<Subjects>, Serializable {

    SubDataModel() {
    }

    SubDataModel(List<Subjects> sub) {
        super(sub);
    }

    @Override
    public Object getRowKey(Subjects sub) {
        return sub.getSubjects();
    }

    @Override
    public Subjects getRowData(String rowKey) {
        List<Subjects> subs = (List<Subjects>) getWrappedData();

        for (Subjects Subjectsx : subs) {
            if (Subjectsx.getSubjects().equals(rowKey)) {
                return Subjectsx;
            }
        }

        return null;
    }
}
