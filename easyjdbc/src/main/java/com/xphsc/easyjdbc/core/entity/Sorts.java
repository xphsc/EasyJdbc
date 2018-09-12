package com.xphsc.easyjdbc.core.entity;


import com.xphsc.easyjdbc.core.exception.EasyJdbcException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ${huipei.x}
 */
public class Sorts {
    private final List<Order> orders;
    public static final Direction DEFAULT_DIRECTION = Direction.ASC;
    public Sorts(List<Order> orders) {

        if (null == orders || orders.isEmpty()) {
            throw new EasyJdbcException("You have to provide at least one sort property to sort by!");
        }

        this.orders = orders;
    }
    public Sorts(Order... orders) {
        this(Arrays.asList(orders));
    }

    public Sorts(Direction direction, String property) {
      Order order= new Order(direction,property);
        orders=new ArrayList<>();
        orders.add(order);
    }
    public enum Direction {
        ASC, DESC;
    }
    public static class Order  implements Serializable {
        private final Direction direction;
        private final String property;

        public Order(Direction direction, String property) {
            this.direction = direction;
            this.property = property;
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
