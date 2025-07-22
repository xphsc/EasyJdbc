/*
 * Copyright (c) 2018-2019  huipei.x
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


import com.xphsc.easyjdbc.core.exception.EasyJdbcException;
import com.xphsc.easyjdbc.core.lambda.LambdaFunction;
import com.xphsc.easyjdbc.core.lambda.Reflections;
import com.xphsc.easyjdbc.util.Collects;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: Sorts类
 */
public class Sorts {
    private List<Order> orders;
    public static final Direction DEFAULT_DIRECTION = Direction.ASC;

    public Sorts(List<Order> orders) {
        if (Collects.isEmpty(orders)) {
            throw new EasyJdbcException("You have to provide at least one sort property to sort by!");
        }

        this.orders = orders;
    }

    public Sorts(Order... orders) {
        this(Arrays.asList(orders));
    }

    public Sorts(Direction direction, String property) {
        Order order = new Order(direction, property);
        orders = new ArrayList<Order>();
        orders.add(order);
    }


    public <S> Sorts sort(Sorts.Direction direction, LambdaFunction<S, Object> property) {
        Sorts.Order order = new Sorts.Order(direction, Reflections.fieldNameForLambdaFunction(property));
        this.orders = new ArrayList();
        this.orders.add(order);
        return this;
    }

    public Sorts() {
    }

    public enum Direction {
        ASC, DESC;
    }

    public static class Order implements Serializable {
        private Direction direction;
        private String property;

        public Order(Direction direction, String property) {
            this.direction = direction;
            this.property = property;
        }

        public <S> Order order(Sorts.Direction direction, LambdaFunction<S, Object> property) {
            this.direction = direction;
            this.property = Reflections.fieldNameForLambdaFunction(property);
            return this;
        }

        public Direction getDirection() {
            return direction;
        }

        public String getProperty() {
            return property;
        }

    }

    public List<Order> getOrders() {
        return orders;
    }

    public static Direction getDefaultDirection() {
        return DEFAULT_DIRECTION;
    }


}
