package com.nepxion.discovery.common.nacos.constant;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.alibaba.nacos.api.PropertyKeyConst;

public class NacosConstant extends PropertyKeyConst {
    public static final String TYPE = "Nacos";
    public static final String NACOS_SERVER_ADDR = "nacos.server-addr";
    public static final String NACOS_ACCESS_KEY = "nacos.access-key";
    public static final String NACOS_SECRET_KEY = "nacos.secret-key";
    public static final String NACOS_PLUGIN_NAMESPACE = "nacos.plugin.namespace";
    public static final String NACOS_PLUGIN_CLUSTER_NAME = "nacos.plugin.cluster-name";
    public static final String NACOS_PLUGIN_CONTEXT_PATH = "nacos.plugin.context-path";
    public static final String NACOS_PLUGIN_ENDPOINT = "nacos.plugin.endpoint";
    public static final String NACOS_PLUGIN_ENCODE = "nacos.plugin.encode";
    public static final String NACOS_PLUGIN_NAMING_LOAD_CACHE_AT_START = "nacos.plugin.naming-load-cache-at-start";
    public static final String NACOS_PLUGIN_TIMEOUT = "nacos.plugin.timout";

    public static final long DEFAULT_TIMEOUT = 30000;
}