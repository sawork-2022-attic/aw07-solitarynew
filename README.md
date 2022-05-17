# aw07

## pos-counter

pos-cart调用，返回当前购物车内的总价格

## pos-order

pos-cart调用：结算购物车的商品，自动在数据库中创建订单

user调用：查看当前的所有订单，查看特定ip的订单

## pos-delivery

pos-order调用：order Service生成订单时，调用生成运单，并存储在数据库中

user调用：根据运单号查询运单，根据订单号查询运单

Order Service与Delivery Service之间使用Spring Cloud Stream提供的接口，借助RabbitMq中间件通讯

------

Please extend your MicroPOS system by adding a delivery service shown as the following figure.

![](10-pos.svg)

When an order is placed by a user, the order serivce sends out an event into some AMQP MOM (such as RabbitMQ). The delivery service will be notified and a new delivery entry will be generated automatically. User can query the delivery status for his orders.

Use [Spring Cloud Stream](https://spring.io/projects/spring-cloud-stream) to make the scenerio happen. Of coz you can refer to the [demo](https://github.com/sa-spring/stream-loan) for technical details.