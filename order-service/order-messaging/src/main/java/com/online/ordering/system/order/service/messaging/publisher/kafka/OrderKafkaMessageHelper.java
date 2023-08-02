package com.online.ordering.system.order.service.messaging.publisher.kafka;

import com.online.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
public class OrderKafkaMessageHelper {

    public <T> BiConsumer<SendResult<String, T>, Throwable>
    getKafkaCallback(String responseTopicName, T requestAvroModel, String orderId, String requestAvroModelName) {

        return new BiConsumer<SendResult<String, T>, Throwable>() {
            @Override
            public void accept(SendResult<String, T> result, Throwable throwable) {

                if(result != null){
                    RecordMetadata metadata = result.getRecordMetadata();
                    log.info("Received successful response from Kafka for order id: {}" +
                                    " , Topic: {} , Partition: {} , Offset: {} , Timestamp: {}",
                            orderId,
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset(),
                            metadata.timestamp());
                } else if (throwable != null) {
                    log.error("Error while sending " + requestAvroModelName +
                            "message {} to topic {}", requestAvroModel.toString(), responseTopicName, throwable);
                }
            }
        };
    }

}
