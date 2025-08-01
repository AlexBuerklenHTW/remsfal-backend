package de.remsfal.test.kafka;

import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;

import de.remsfal.test.AbstractTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.restassured.path.json.JsonPath;
import io.smallrye.reactive.messaging.kafka.companion.ConsumerBuilder;
import io.smallrye.reactive.messaging.kafka.companion.ConsumerTask;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;

@QuarkusTestResource(KafkaCompanionResource.class)
public abstract class AbstractKafkaTest extends AbstractTest {

    @InjectKafkaCompanion
    protected KafkaCompanion companion;

    @BeforeEach
    void clearAllTopics() {
        String topic = "user-notification";
        // delete topic
        companion.topics().delete(topic);
        // create topic with 2 partitions
        companion.topics().create(topic, 2);
    }

    public KafkaTopicSpecification given() {
        return new KafkaTopicSpecification(companion);
    }

    public class KafkaTopicSpecification {

        private final ConsumerBuilder<String, String> builder;
        private ConsumerTask<String, String> task;

        protected KafkaTopicSpecification(final KafkaCompanion companion) {
            builder = companion.consumeStrings();
        }

        public KafkaTopicSpecification topic(final String topic) {
            task = builder.fromTopics(topic, 1);
            return this;
        }

        public KafkaMessageSpecification assertThat() {
            return new KafkaMessageSpecification(task.awaitCompletion().getFirstRecord());
        }
    }

    public class KafkaMessageSpecification {

        private final JsonPath json;

        protected KafkaMessageSpecification(final ConsumerRecord<String, String> recored) {
            String value = recored.value();
            logger.info(value);
            json = JsonPath.from(value);
        }

        public KafkaMessageSpecification json(final String key, final Matcher<String> matcher) {
            assertThat(json.getString(key), matcher);
            return this;
        }
    }

}
