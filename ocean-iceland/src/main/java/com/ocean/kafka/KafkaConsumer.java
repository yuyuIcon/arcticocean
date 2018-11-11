package com.ocean.kafka;

import com.ocean.service.AiHuiShouService;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Kafka Consumer
 */
public class KafkaConsumer implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    //消费者连接
    private final ConsumerConnector consumer;
    //要消费的话题
    private final String topic;
    //zookeeper连接
    private final String zookeeperConnect;
    //组
    private final String groupId;

    private AiHuiShouService aiHuiShouService;

    public KafkaConsumer(String topic, String zookeeperConnect, String groupId, AiHuiShouService aiHuiShouService) {
        this.topic = topic;
        this.zookeeperConnect = zookeeperConnect;
        this.groupId = groupId;
        this.aiHuiShouService = aiHuiShouService;

        consumer = Consumer
                .createJavaConsumerConnector(createConsumerConfig(zookeeperConnect, groupId));

    }

    //配置相关信息
    private static ConsumerConfig createConsumerConfig(String zookeeperConnect, String groupId) {
        Properties props = new Properties();

        props.put("zookeeper.connect", zookeeperConnect);

        props.put("group.id", groupId);

        props.put("zookeeper.session.timeout.ms", "10000");

        props.put("zookeeper.sync.time.ms", "200");

        props.put("auto.commit.interval.ms", "1000");
        return new ConsumerConfig(props);
    }

    public void run() {

        Map<String, Integer> topicMap = new HashMap<String, Integer>();
        topicMap.put(topic, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> streamMap = consumer.createMessageStreams(topicMap);

        KafkaStream<byte[], byte[]> stream = streamMap.get(topic).get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        String msg = null;
        while (true) {
            if (it.hasNext()) {
                try {
                    msg = new String(it.next().message());
                    logger.info("kafka接收Message={}", msg);
                    if (!StringUtils.isEmpty(msg)) {
                        try {

                            System.out.println("kafka接收Message=" + msg);
                            aiHuiShouService.applyCoupon(msg);
                        } catch (Exception e) {
                            logger.error("处理kafka订单信息异常={}", e);

                        }
                    }

                } catch (Exception e) {
                    logger.error("kafka接收订单信息异常={}", e);

                }
            }
        }
    }
}
