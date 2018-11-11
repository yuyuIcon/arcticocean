package com.ocean.kafka;

import com.ocean.service.AiHuiShouService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author: Qigaowei
 * Date: 2018/7/23 10:41
 * Description:
 */
@Component
public class KafkaConsumerService  implements CommandLineRunner {

    @Value("${GROUPID}")
    public  String GROUPID;
    @Value("${zookeeper.connect}")
    public  String ZOOKEEPER;
    @Value("${kafka.topic}")
    public  String KAFKA_TOPIC;
    @Autowired
    private AiHuiShouService aiHuiShouService;

    @Override
    public void run(String... strings) throws Exception {
        //KafkaConsumer kafkaConsumer = new KafkaConsumer(KAFKA_TOPIC, ZOOKEEPER, GROUPID, aiHuiShouService);
        //kafkaConsumer.run();
    }
}
