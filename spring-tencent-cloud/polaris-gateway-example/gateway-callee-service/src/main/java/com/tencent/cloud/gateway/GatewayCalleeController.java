package com.tencent.cloud.gateway;

import com.tencent.cloud.metadata.constant.MetadataConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@RestController
@RequestMapping("/gateway/example/callee")
public class GatewayCalleeController {

    private static Logger logger = LoggerFactory.getLogger(GatewayCalleeController.class);

    /**
     * 返回服务信息
     */
    @RequestMapping("/info")
    public String info() {
        return "Gateway Example Callee";
    }

    /**
     * Get metadata in HTTP header
     */
    @RequestMapping("/echo")
    public String echoHeader(@RequestHeader(MetadataConstant.HeaderName.CUSTOM_METADATA) String metadataStr)
        throws UnsupportedEncodingException {
        logger.info(URLDecoder.decode(metadataStr, "UTF-8"));
        return URLDecoder.decode(metadataStr, "UTF-8");
    }
}