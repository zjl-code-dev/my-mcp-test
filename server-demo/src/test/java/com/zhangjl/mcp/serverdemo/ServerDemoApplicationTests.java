package com.zhangjl.mcp.serverdemo;

import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ServerDemoApplicationTests {

    @Test
    void contextLoads() {
        var transport = HttpClientSseClientTransport.builder("http://localhost:10080").build();
        new SampleClient(transport).run();
    }

}
