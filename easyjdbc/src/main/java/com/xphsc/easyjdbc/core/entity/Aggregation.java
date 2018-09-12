package com.xphsc.easyjdbc.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ${huipei.x}
 */
public class Aggregation {
    private final List<Aggregate> aggregates;
    public Aggregation(List<Aggregate> aggregates) {
        this.aggregates = aggregates;
    }

    public Aggregation(Aggregate... aggregates) {
        this(Arrays.asList(aggregates));
    }

   public Aggregation(String type, String property,String asProperty) {
       Aggregate aggregate=  new Aggregate(type,property,asProperty);
       aggregates=new ArrayList<>();
       aggregates.add(aggregate);
    }

    public static class Aggregate  implements Serializable {
        private  String type;
        private  String property;
        private String asProperty;
        public Aggregate(String type, String property,String asProperty) {
            this.type=type;
            this.property=property;
            this.asProperty=asProperty;
        }
        public Aggregate(String type, String property) {
            this.type=type;
            this.property=property;
        }
        public Aggregate() {

        }
        public String getType() {
            return type;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }


        public void setType(String type) {
            this.type = type;
        }

        public String getAsProperty() {
            return asProperty;
        }

        public void setAsProperty(String asProperty) {
            this.asProperty = asProperty;
        }
    }

    public List<Aggregate> getAggregates() {
        return aggregates;
    }


}
