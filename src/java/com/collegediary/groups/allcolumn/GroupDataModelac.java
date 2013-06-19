package com.collegediary.groups.allcolumn;

import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Vinceal
 */
public class GroupDataModelac extends ListDataModel<GroupAllColumn>
        implements SelectableDataModel<GroupAllColumn>, Serializable {

    public GroupDataModelac() {
    }

    public GroupDataModelac(List<GroupAllColumn> list) {
        super(list);
    }   
    
    @Override
    public Object getRowKey(GroupAllColumn object) {
        return object.idGroup;
    }

    @Override
    public GroupAllColumn getRowData(String rowKey) {
        List<GroupAllColumn> grp = (List<GroupAllColumn>) getWrappedData();

        for (GroupAllColumn groups : grp) {
            if (groups.getIdGroup().equals(rowKey)) {
                return groups;
            }

        }

        return null;
    }
}
