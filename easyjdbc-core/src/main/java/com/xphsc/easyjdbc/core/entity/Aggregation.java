/*
 * Copyright (c) 2018-2019 huipei.x
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
