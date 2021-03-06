package com.nozturn.mbg.freemarker;

import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FMTemplateFactory {

    private static Logger logger = LoggerFactory.getLogger(FMTemplateFactory.class);

    public static Template getTemplate(String templateName) {
        try {
            return FMConfiguration.getInstance().getTemplate(templateName);
        } catch (IOException ioe) {
            logger.error("FreeMarker Templates File Path Error!");
            ioe.printStackTrace();
        }
        return null;
    }

}
