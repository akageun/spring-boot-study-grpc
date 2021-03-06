package kr.geun.study.grpc.client;

import io.grpc.Deadline;
import kr.geun.study.grpc.EventRequest;
import kr.geun.study.grpc.EventResponse;
import kr.geun.study.grpc.EventServiceGrpc;
import kr.geun.study.grpc.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * BlockingStub
 *
 * @author akageun
 * @since 2021-12-01
 */
@Slf4j
@RestController
@RequestMapping("/blocking")
public class BlockingStub extends AbstractStub {

    private EventServiceGrpc.EventServiceBlockingStub stub() {
        return EventServiceGrpc.newBlockingStub(channel())
            .withDeadline(Deadline.after(3000, TimeUnit.MILLISECONDS));
    }

    @PostMapping("/sendEvent")
    public String sendEventTest() {
        EventServiceGrpc.EventServiceBlockingStub stub = stub();

        EventRequest request = EventRequest.newBuilder()
            .setId(1)
            .setType(Type.NORMAL)
            .build();

        EventResponse response = stub.sendEvent(request);

        log.info("response : {}", response.toString());

        return "OK";
    }

    @PostMapping("/sendEventServerStream")
    public String sendEventServerStream() {
        EventServiceGrpc.EventServiceBlockingStub stub = stub();

        EventRequest request = EventRequest.newBuilder()
            .setId(1)
            .setType(Type.NORMAL)
            .build();

        Iterator<EventResponse> responseItr = stub.sendEventServerStream(request);
        while (responseItr.hasNext()) {
            EventResponse response = responseItr.next();

            log.info("response : {}", response.toString());
        }

        return "OK";
    }
}
