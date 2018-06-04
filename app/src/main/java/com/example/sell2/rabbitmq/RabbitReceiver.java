//package com.example.sell2.rabbitmq;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class RabbitReceiver {
//
//    @Autowired
//    private static Logger logger= LoggerFactory.getLogger(RabbitReceiver.class);
//    @RabbitListener(queues = RabbitmpConfig.MYQUEUE)
//    public void receiver()
//    {
//        logger.info("receiver");
//    }
//}
