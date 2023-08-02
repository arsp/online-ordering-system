package com.online.ordering.system.order.service.messaging.publisher.kafka;

import com.online.ordering.system.kafka.order.avro.model.ShopApprovalRequestAvroModel;
import com.online.ordering.system.kafka.producer.service.KafkaProducer;
import com.online.ordering.system.order.service.domain.config.OrderServiceConfigData;
import com.online.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.online.ordering.system.order.service.domain.ports.output.message.publisher.shopapproval.OrderPaidShopRequestMessagePublisher;
import com.online.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayOrderKafkaMessagePublisher implements OrderPaidShopRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String , ShopApprovalRequestAvroModel> kafkaProducer;

    private final OrderKafkaMessageHelper orderKafkaMessageHelper;

    public PayOrderKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                         OrderServiceConfigData orderServiceConfigData,
                                         KafkaProducer<String, ShopApprovalRequestAvroModel> kafkaProducer,
                                         OrderKafkaMessageHelper orderKafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.orderKafkaMessageHelper = orderKafkaMessageHelper;
    }


    @Override
    public void publish(OrderPaidEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();

        try {
            ShopApprovalRequestAvroModel shopApprovalRequestAvroModel = orderMessagingDataMapper
                    .orderPaidEventToShopApprovalRequestAvroModel(domainEvent);

            kafkaProducer.send(orderServiceConfigData.getShopApprovalRequestTopicName(),
                    orderId,
                    shopApprovalRequestAvroModel,
                    orderKafkaMessageHelper.getKafkaCallback(orderServiceConfigData.getPaymentResponseTopicName(),
                            shopApprovalRequestAvroModel,
                            orderId,
                            "ShopApprovalRequestAvroModel"));

            log.info("ShopApprovalRequestAvroModel sent to Kafka for order id: {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending PaymentRequestAvroModel message" +
                    " to Kafka with order id: {}, error: {}", orderId, e.getMessage());
        }


    }
}
