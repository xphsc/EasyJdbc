package com.xphsc.easyjdbc.core.metadata.type;

import com.xphsc.easyjdbc.core.metadata.FieldElement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description:
 * @since 2.0.5
 */
public class FillDateTypeHandler {

    public static  Object fillDate(FieldElement fieldElement){
        Object value=null;
        if(LocalDate.class.equals(fieldElement.getType())){
            value=LocalDate.now();
        }else if(fieldElement.getType().equals(Date.class)) {
            value=new Date();
        }else if(LocalDateTime.class.equals(fieldElement.getType())) {
            value=LocalDateTime.now();
        }else if(LocalTime.class.equals(fieldElement.getType())){
            value=LocalTime.now();
        }

        return value;
    }
}
