package kr.geun.study.grpc.client;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.Deadline;
import kr.geun.study.grpc.EventRequest;
import kr.geun.study.grpc.EventResponse;
import kr.geun.study.grpc.EventServiceGrpc;
import kr.geun.study.grpc.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * FutureStub
 *
 * @author akageun
 * @since 2021-12-01
 */
@Slf4j
@RestController
@RequestMapping("/future")
public class FutureStub extends AbstractStub {

    private EventServiceGrpc.EventServiceFutureStub stub() {
        return EventServiceGrpc.newFutureStub(channel())
            .withDeadline(Deadline.after(3000, TimeUnit.MILLISECONDS));
    }

    @PostMapping("/sendEvent")
    public String sendEventTest() {

        EventServiceGrpc.EventServiceFutureStub stub = stub();

        EventRequest request = EventRequest.newBuilder()
            .setId(1)
            .setType(Type.NORMAL)
            .build();

        ListenableFuture<EventResponse> responseFuture = stub.sendEvent(request);

        try {
            EventResponse response = responseFuture.get(5000, TimeUnit.MILLISECONDS); //5 sec
            log.info("response : {}", response.toString());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return "OK";
    }
}
