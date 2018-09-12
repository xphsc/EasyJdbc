package com.xphsc.easyjdbc.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ${huipei.x}
 */
public class ListUtil {
    public static <E> List<E> removeAll(Collection<E> collection, Collection<?> remove) {
        ArrayList list = new ArrayList();
        Iterator i = collection.iterator();

        while(i.hasNext()) {
            Object obj = i.next();
            if(!remove.contains(obj)) {
                list.add(obj);
            }
        }

        return list;
    }


}
