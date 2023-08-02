package com.online.ordering.system.order.service.messaging.listener.kafka;

import com.online.ordering.system.kafka.consumer.KafkaConsumer;
import com.online.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.online.ordering.system.kafka.order.avro.model.ShopApprovalResponseAvroModel;
import com.online.ordering.system.order.service.domain.ports.input.message.listener.shopapproval.ShopApprovalResponseMessageListener;
import com.online.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.online.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Slf4j
@Component
public class ShopApprovalResponseKafkaListener  implements KafkaConsumer<ShopApprovalResponseAvroModel> {

    private final ShopApprovalResponseMessageListener shopApprovalResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public ShopApprovalResponseKafkaListener(ShopApprovalResponseMessageListener shopApprovalResponseMessageListener,
                                             OrderMessagingDataMapper orderMessagingDataMapper) {
        this.shopApprovalResponseMessageListener = shopApprovalResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.shop-approval-consumer-group-id}",
            topics = "${order-service.shop-approval-response-topic-name}")
    public void receive(@Payload  List<ShopApprovalResponseAvroModel> messages,
                        @Header (KafkaHeaders.RECEIVED_KEY) List<String > keys,
                        @Header (KafkaHeaders.RECEIVED_PARTITION)List<Integer> partitions,
                        @Header (KafkaHeaders.OFFSET)List<Long> offsets) {

        log.info("{} number of shop approval responses received with keys: {}, partitions: {} , and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(shopApprovalResponseAvroModel -> {
            if(OrderApprovalStatus.APPROVED == shopApprovalResponseAvroModel.getOrderApprovalStatus()) {
                log.info("Processing approved order for order id: {}",
                        shopApprovalResponseAvroModel.getOrderId());
                shopApprovalResponseMessageListener.orderApproved(orderMessagingDataMapper
                        .approvalResponseAvroModelToApprovalResponse(shopApprovalResponseAvroModel));
            } else if (OrderApprovalStatus.REJECTED == shopApprovalResponseAvroModel.getOrderApprovalStatus()) {
                log.info("Processing rejected order for order id: {}, with failure messages: {}",
                        shopApprovalResponseAvroModel.getOrderId(),
                        String.join(FAILURE_MESSAGE_DELIMITER,
                                shopApprovalResponseAvroModel.getFailureMessages()));
                shopApprovalResponseMessageListener.orderRejected(orderMessagingDataMapper
                        .approvalResponseAvroModelToApprovalResponse(shopApprovalResponseAvroModel));
            }
        });

    }
}
