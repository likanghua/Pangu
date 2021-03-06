package com.joindata.inf.common.support.elasticsearch.component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.network.NetworkModule;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.reindex.ReindexPlugin;
import org.elasticsearch.percolator.PercolatorPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.script.mustache.MustachePlugin;
import org.elasticsearch.transport.Netty4Plugin;

import io.netty.util.ThreadDeathWatcher;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * ElasticSearch 客户端<br />
 * <i>重新定义官方版，取消 Netty3 的插件，不然会报错</br>
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2016年12月8日 下午3:39:10
 */
public class ElasticSearchClient extends TransportClient
{

    private static final Collection<Class<? extends Plugin>> PRE_INSTALLED_PLUGINS = Collections.unmodifiableList(Arrays.asList(Netty4Plugin.class, ReindexPlugin.class, PercolatorPlugin.class, MustachePlugin.class));

    /**
     * Creates a new transport client with pre-installed plugins.
     * 
     * @param settings the settings passed to this transport client
     * @param plugins an optional array of additional plugins to run with this client
     */
    @SafeVarargs
    public ElasticSearchClient(Settings settings, Class<? extends Plugin>... plugins)
    {
        this(settings, Arrays.asList(plugins));
    }

    /**
     * Creates a new transport client with pre-installed plugins.
     * 
     * @param settings the settings passed to this transport client
     * @param plugins a collection of additional plugins to run with this client
     */
    public ElasticSearchClient(Settings settings, Collection<Class<? extends Plugin>> plugins)
    {
        this(settings, plugins, null);
    }

    /**
     * Creates a new transport client with pre-installed plugins.
     * 
     * @param settings the settings passed to this transport client
     * @param plugins a collection of additional plugins to run with this client
     * @param hostFailureListener a failure listener that is invoked if a node is disconnected. This can be <code>null</code>
     */
    public ElasticSearchClient(Settings settings, Collection<Class<? extends Plugin>> plugins, HostFailureListener hostFailureListener)
    {
        super(settings, Settings.EMPTY, addPlugins(plugins, PRE_INSTALLED_PLUGINS), hostFailureListener);
    }

    @Override
    public void close()
    {
        super.close();
        if(NetworkModule.TRANSPORT_TYPE_SETTING.exists(settings) == false || NetworkModule.TRANSPORT_TYPE_SETTING.get(settings).equals(Netty4Plugin.NETTY_TRANSPORT_NAME))
        {
            try
            {
                GlobalEventExecutor.INSTANCE.awaitInactivity(5, TimeUnit.SECONDS);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            try
            {
                ThreadDeathWatcher.awaitInactivity(5, TimeUnit.SECONDS);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

}
