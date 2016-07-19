package com.nozturn.mbg.freemarker;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.cache.MruCacheStorage;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.Version;

public class FMConfiguration {

    private static Logger logger = LoggerFactory.getLogger(FMConfiguration.class);

    // 模板文件夹
    private static final String TEMPLATES_DIRECTORY = "templates";
    // 最大的强引用对象数
    private static final int MAX_STRONG_APPOINT_NUM = 20;
    // 最大的弱引用对象数
    private static final int MAX_WEAK_APPOINT_NUM = 250;

    private static Configuration cfg = null;

    public static Configuration getInstance() {
        if (cfg != null)
            return cfg;
        synchronized (FMConfiguration.class) {
            if (cfg != null)
                return cfg;
            try {
                String templatesPath = FMTemplateFactory.class.getClassLoader().getResource(TEMPLATES_DIRECTORY).getPath();

                cfg = new Configuration(Configuration.VERSION_2_3_0);
                cfg.setDirectoryForTemplateLoading(new File(templatesPath));
                cfg.setDefaultEncoding("UTF-8");
                cfg.setObjectWrapper(new BeansWrapperBuilder(new Version("2.3.22")).build());
                // 最近最多用策略缓存(第一个参数是最大的强引用对象数，第二个为最大的弱引用对象数)
                cfg.setCacheStorage(new MruCacheStorage(MAX_STRONG_APPOINT_NUM, MAX_WEAK_APPOINT_NUM));
            } catch (IOException ioe) {
                logger.error("FreeMarker Init Configuration Error!");
                ioe.printStackTrace();
            }
        }
        return cfg;
    }

}
