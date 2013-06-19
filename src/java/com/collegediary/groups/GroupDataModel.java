package com.collegediary.groups;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author VinceAL
 */
public class GroupDataModel extends ListDataModel<Groups> implements SelectableDataModel<Groups>, Serializable{

    GroupDataModel(){
    
    }
    
    GroupDataModel(List<Groups> grplist) {
       super(grplist);
    }

    @Override
    public Object getRowKey(Groups object) {
        return object.getGroup();
    }

    @Override
    public Groups getRowData(String rowKey) {
        List<Groups> grp = (List<Groups>) getWrappedData();

        for (Groups groups : grp) {
            if (groups.getGroup().equals(rowKey)){
            return groups;
            }
            
        }

        return null;
    }
    
}
