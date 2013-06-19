package com.collegediary.buffer;

import com.collegediary.groups.Groups;
import java.io.Serializable;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;

/**
 *
 * @author Vinceal
 */
public class BufferDataModel extends ListDataModel<Buffer> implements SelectableDataModel<Buffer>, Serializable {

    BufferDataModel() {
    }

    BufferDataModel(List<Buffer> buffer) {
        super(buffer);
    }

    @Override
    public Object getRowKey(Buffer t) {
        return t.operation;
    }

    @Override
    public Buffer getRowData(String rowKey) {
        List<Buffer> buffer = (List<Buffer>) getWrappedData();
        for (Buffer obj : buffer) {
            if (obj.operation.equals(rowKey)) {
                return obj;
            }
        }
        return null;
    }
}
