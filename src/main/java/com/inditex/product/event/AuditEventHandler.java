package com.inditex.product.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inditex.product.entity.LogData;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AuditEventHandler {

  @SneakyThrows
  @EventListener
  @Async
  public void handleEvent(AuditEvent<LogData> auditEvent) {

    // API CALL
    // Instead of call Security API I print the message

    System.out.println(
        new ObjectMapper()
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(auditEvent.getData()));
  }
}
