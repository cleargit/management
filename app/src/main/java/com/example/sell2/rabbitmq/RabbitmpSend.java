//package com.example.sell2.rabbitmq;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RabbitmpSend {
//    private static Logger logger= LoggerFactory.getLogger(RabbitmpSend.class);
//    @Autowired
//    AmqpTemplate amqpTemplate;
//    public void send(Object msg)
//    {
//        logger.info("send msg");
//        amqpTemplate.convertAndSend(msg);
//    }
//}
