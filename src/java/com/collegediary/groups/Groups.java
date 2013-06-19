package com.collegediary.groups;
import java.io.Serializable;

/**
 *
 * @author VinceAL
 */
public class Groups implements Serializable {
    
    String group, idGroup;

    public String getGroup() {
        return group;
    }

    public String getIdGroup() {
        return idGroup;
    }

    Groups(String mask, String course, String idGroup) {
        
        String replace = mask.replace('$', course.charAt(0));
        this.group=replace;
        this.idGroup = idGroup;
    
    }

}
