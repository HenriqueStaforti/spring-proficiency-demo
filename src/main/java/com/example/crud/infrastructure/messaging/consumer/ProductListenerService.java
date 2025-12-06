package com.example.crud.infrastructure.messaging.consumer;

import com.example.crud.entity.ProductEntity;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class ProductListenerService {

    @RabbitListener(queues = "${app.rabbit.product.queue}")
    public void listenProductCreated(ProductEntity product) {
        System.out.println("Received product created message: " + product);
    }
}
