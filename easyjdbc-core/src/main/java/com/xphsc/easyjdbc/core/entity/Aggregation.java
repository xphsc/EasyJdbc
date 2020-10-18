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

import com.xphsc.easyjdbc.core.lambda.LambdaFunction;
import com.xphsc.easyjdbc.core.lambda.Reflections;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ${huipei.x}
 */
public class Aggregation {

    private  List<Aggregate> aggregates;
    public Aggregation(List<Aggregate> aggregates) {
        this.aggregates = aggregates;
    }

    public Aggregation(Aggregate... aggregates) {
        this(Arrays.asList(aggregates));
    }

   public Aggregation(AggregateType aggregateType, String property,String asProperty) {
       Aggregate aggregate=  new Aggregate(aggregateType,property,asProperty);
       aggregates=new ArrayList<>();
       aggregates.add(aggregate);
    }

    public <T> Aggregation aggregation(AggregateType aggregateType,  LambdaFunction<T, Object>  property,String asProperty) {
        Aggregate aggregate=  new Aggregate().aggregate(aggregateType,property,asProperty);
        aggregates=new ArrayList<>();
        aggregates.add(aggregate);
        return this;
    }

    public <T,S> Aggregation aggregation(AggregateType aggregateType,  LambdaFunction<T, Object>  property,LambdaFunction<S, Object> asProperty) {
        Aggregate aggregate=  new Aggregate().aggregate(aggregateType,property,asProperty);
        aggregates=new ArrayList<>();
        aggregates.add(aggregate);
        return this;
    }

    public static class Aggregate  implements Serializable {
        private AggregateType aggregateType;
        private  String property;
        private String asProperty;
        public Aggregate(AggregateType type, String property,String asProperty) {
            this.aggregateType=type;
            this.property=property;
            this.asProperty=asProperty;
        }

        public Aggregate(AggregateType type, String property) {
            this.aggregateType=type;
            this.property=property;
        }

        public Aggregate() {

        }


        public <T> Aggregate aggregate(AggregateType type, LambdaFunction<T, Object> property ,String asProperty) {
            this.aggregateType = type;
            this.property = Reflections.fieldNameForLambdaFunction(property);;
            this.asProperty = asProperty;
            return this;
        }
        public <T,S> Aggregate aggregate(AggregateType type, LambdaFunction<T, Object> property ,LambdaFunction<S, Object> asProperty) {
            this.aggregateType = type;
            this.property = Reflections.fieldNameForLambdaFunction(property);
            this.asProperty = Reflections.fieldNameForLambdaFunction(asProperty);
            return this;
        }


        public AggregateType getAggregateType() {
            return aggregateType;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public void setAggregateType(AggregateType aggregateType) {
            this.aggregateType = aggregateType;
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
