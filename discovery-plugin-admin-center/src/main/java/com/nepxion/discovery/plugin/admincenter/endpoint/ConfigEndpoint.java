package com.nepxion.discovery.plugin.admincenter.endpoint;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nepxion.discovery.common.constant.DiscoveryConstant;
import com.nepxion.discovery.common.entity.RuleEntity;
import com.nepxion.discovery.plugin.framework.adapter.PluginAdapter;
import com.nepxion.discovery.plugin.framework.context.PluginContextAware;
import com.nepxion.discovery.plugin.framework.event.PluginEventWapper;
import com.nepxion.discovery.plugin.framework.event.RuleClearedEvent;
import com.nepxion.discovery.plugin.framework.event.RuleUpdatedEvent;

@RestController
@RequestMapping(path = "/config")
@Api(tags = { "配置接口" })
@RestControllerEndpoint(id = "config")
@ManagedResource(description = "Config Endpoint")
public class ConfigEndpoint {
    @Autowired
    private PluginContextAware pluginContextAware;

    @Autowired
    private PluginAdapter pluginAdapter;

    @Autowired
    private PluginEventWapper pluginEventWapper;

    @RequestMapping(path = "/update-async", method = RequestMethod.POST)
    @ApiOperation(value = "异步推送更新规则配置信息", notes = "", response = ResponseEntity.class, httpMethod = "POST")
    @ResponseBody
    @ManagedOperation
    public ResponseEntity<?> updateAsync(@RequestBody @ApiParam(value = "规则配置内容，XML格式", required = true) String config) {
        return update(config, true);
    }

    @RequestMapping(path = "/update-sync", method = RequestMethod.POST)
    @ApiOperation(value = "同步推送更新规则配置信息", notes = "", response = ResponseEntity.class, httpMethod = "POST")
    @ResponseBody
    @ManagedOperation
    public ResponseEntity<?> updateSync(@RequestBody @ApiParam(value = "规则配置内容，XML格式", required = true) String config) {
        return update(config, false);
    }

    @RequestMapping(path = "/clear-async", method = RequestMethod.POST)
    @ApiOperation(value = "异步清除更新的规则配置信息", notes = "", response = ResponseEntity.class, httpMethod = "POST")
    @ResponseBody
    @ManagedOperation
    public ResponseEntity<?> clearAsync() {
        return clear(true);
    }

    @RequestMapping(path = "/clear-sync", method = RequestMethod.POST)
    @ApiOperation(value = "同步清除更新的规则配置信息", notes = "", response = ResponseEntity.class, httpMethod = "POST")
    @ResponseBody
    @ManagedOperation
    public ResponseEntity<?> clearSync() {
        return clear(false);
    }

    @RequestMapping(path = "/view", method = RequestMethod.GET)
    @ApiOperation(value = "查看本地和更新的规则配置信息", notes = "", response = ResponseEntity.class, httpMethod = "GET")
    @ResponseBody
    @ManagedOperation
    public ResponseEntity<List<String>> view() {
        return view(false);
    }

    private ResponseEntity<?> update(String config, boolean async) {
        Boolean discoveryControlEnabled = pluginContextAware.isDiscoveryControlEnabled();
        if (!discoveryControlEnabled) {
            // return new ResponseEntity<>(Collections.singletonMap("Message", "Discovery control is disabled"), HttpStatus.NOT_FOUND);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Discovery control is disabled");
        }

        Boolean isConfigRestControlEnabled = pluginContextAware.isConfigRestControlEnabled();
        if (!isConfigRestControlEnabled) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Config rest control is disabled");
        }

        pluginEventWapper.fireRuleUpdated(new RuleUpdatedEvent(config), async);

        // return ResponseEntity.ok().build();

        return ResponseEntity.ok().body(DiscoveryConstant.OK);
    }

    private ResponseEntity<?> clear(boolean async) {
        Boolean discoveryControlEnabled = pluginContextAware.isDiscoveryControlEnabled();
        if (!discoveryControlEnabled) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Discovery control is disabled");
        }

        Boolean isConfigRestControlEnabled = pluginContextAware.isConfigRestControlEnabled();
        if (!isConfigRestControlEnabled) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Config rest control is disabled");
        }

        pluginEventWapper.fireRuleCleared(new RuleClearedEvent(), async);

        return ResponseEntity.ok().body(DiscoveryConstant.OK);
    }

    private ResponseEntity<List<String>> view(boolean async) {
        List<String> ruleList = new ArrayList<String>(2);

        String localRuleContent = StringUtils.EMPTY;
        RuleEntity localRuleEntity = pluginAdapter.getLocalRule();
        if (localRuleEntity != null && StringUtils.isNotEmpty(localRuleEntity.getContent())) {
            localRuleContent = localRuleEntity.getContent();
        }

        String dynamicRuleContent = StringUtils.EMPTY;
        RuleEntity dynamicRuleEntity = pluginAdapter.getDynamicRule();
        if (dynamicRuleEntity != null && StringUtils.isNotEmpty(dynamicRuleEntity.getContent())) {
            dynamicRuleContent = dynamicRuleEntity.getContent();
        }

        ruleList.add(localRuleContent);
        ruleList.add(dynamicRuleContent);

        return ResponseEntity.ok().body(ruleList);
    }

    protected ResponseEntity<String> toExceptionResponseEntity(Exception e, boolean showDetail) {
        String message = null;
        if (showDetail) {
            message = ExceptionUtils.getStackTrace(e);
        } else {
            message = e.getMessage();
        }

        message = "An internal error occurred while processing your request\n" + message;

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }
}