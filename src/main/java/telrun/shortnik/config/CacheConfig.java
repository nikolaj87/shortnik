package telrun.shortnik.config;

import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public Config hazelcastConfig() {
        return  new Config()
                .setInstanceName("hazelcast-instance")
                .addMapConfig(new MapConfig()
                        .setName("url")
                        .setEvictionConfig(new EvictionConfig()
                                .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                                .setEvictionPolicy(EvictionPolicy.LFU)
                                .setSize(100)
                        )
                        .setTimeToLiveSeconds(180));
    }
}
