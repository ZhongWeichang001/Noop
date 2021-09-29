package org.apache.flink.runtime.microbatch;

import org.apache.flink.runtime.microbatch.kafka.Subber;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.flink.runtime.microbatch.ProcessConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchWatcher {
    Subber subber;
    boolean[] completed;
    protected static final Logger log = LoggerFactory.getLogger(BatchWatcher.class);

    public BatchWatcher(int subindex, int nSink) {
        completed = new boolean[nSink];
        String groupid = "micro_batch_sub" + subindex + System.nanoTime();
        String topic = ProcessConf.getProperty(ProcessConf.SYNC_TOPIC);
        String host = ProcessConf.getProperty(ProcessConf.KAFKA_HOST);
        String timeout = ProcessConf.getProperty(ProcessConf.KAFKA_TIMEOUT);
        subber = new Subber(groupid, host, topic, timeout);
    }

    public void watch(String prefix) {
        clean();
        while (true) {
            ConsumerRecords<String, String> records = subber.get();
            for (ConsumerRecord<String, String> record : records) {
                String value = record.value();
                String[] msg = value.split(":");
                String _prefix = msg[0];
                if (!prefix.equals(_prefix))
                    continue;
                Integer subindex = Integer.parseInt(msg[2]);
                completed[subindex] = true;
            }
            for (boolean c : completed) {
                if (!c) {
                    continue;
                }
            }
            return;
        }
    }

    void clean() {
        for (int i = 0; i < completed.length; i++) {
            completed[i] = false;
        }
    }
}
