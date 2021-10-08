package guides.hazelcast.spring;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.multimap.MultiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class CommandController {

    @Autowired
    HazelcastInstance hazelcastInstance;

    private MultiMap<String,String> retrieveMap() {
        return hazelcastInstance.getMultiMap("propeller");
    }

    @RequestMapping("/put")
    public String put(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value) {
        //"book-hotel:8080:/bank-transaction?quantity=-3"
        String[] split = value.split(":");
        String path = split[2].substring(0, split[2].indexOf("?"));
        //"outbound|80||springboot-service.default.svc.cluster.local"
        retrieveMap().put(key, "outbound|" + split[1] + "||" + split[0] + ".default.svc.cluster.local:" + path);
        return value;
    }

    @RequestMapping("/get")
    public String get(@RequestParam(value = "key") String key) {
        return retrieveMap().get(key).stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","));
    }

}
