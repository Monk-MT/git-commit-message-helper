package com.dcar.utils;

import com.dcar.exception.TemplateConvertException;
import com.dcar.localization.PluginBundle;
import com.dcar.model.CommitTemplate;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import java.io.StringWriter;
import java.util.Properties;


public class VelocityUtils {

    private static VelocityEngine engine;


    static {
        engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.PARSER_POOL_SIZE, 20);
        engine.setProperty(RuntimeConstants.INPUT_ENCODING, "UTF-8");
        engine.setProperty(RuntimeConstants.OUTPUT_ENCODING, "UTF-8");

        Properties props = new Properties();
        props.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
        props.put("runtime.log.logsystem.log4j.category", "velocity");
        props.put("runtime.log.logsystem.log4j.logger", "velocity");

        engine.init(props);
    }

    public static String convert(String template, CommitTemplate commitTemplate) throws TemplateConvertException {
        StringWriter writer = new StringWriter();
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("type", commitTemplate.getType());
        velocityContext.put("platform", commitTemplate.getPlatform());
        velocityContext.put("taskId", commitTemplate.getTaskId());
        velocityContext.put("business", commitTemplate.getBusiness());
        velocityContext.put("body", commitTemplate.getBody());
        velocityContext.put("newline", "\n");
        velocityContext.put("velocityTool", new VelocityTool());
        String VM_LOG_TAG = "GitCommitMessage VelocityUtils";
        try {
            engine.evaluate(velocityContext, writer, VM_LOG_TAG, template);
            return writer.toString();
        } catch (Exception e) {
            throw new TemplateConvertException(PluginBundle.get("setting.template.description.error"));
        }
    }


    public static String convertDescription(String html) throws TemplateConvertException {
        StringWriter writer = new StringWriter();
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("setting.template.description.tip", PluginBundle.get("setting.template.description.tip"));
        velocityContext.put("setting.template.description.predefined.tip", PluginBundle.get("setting.template.description.predefined.tip"));
        velocityContext.put("setting.template.description.type", PluginBundle.get("setting.template.description.type"));
        velocityContext.put("setting.template.description.platform", PluginBundle.get("setting.template.description.platform"));
        velocityContext.put("setting.template.description.id", PluginBundle.get("setting.template.description.id"));
        velocityContext.put("setting.template.description.business", PluginBundle.get("setting.template.description.business"));
        velocityContext.put("setting.template.description.body", PluginBundle.get("setting.template.description.body"));
        velocityContext.put("setting.template.description.newLine", PluginBundle.get("setting.template.description.newLine"));
        velocityContext.put("setting.template.description.used", PluginBundle.get("setting.template.description.used"));
        velocityContext.put("globals", velocityContext);
        String VM_LOG_TAG = "GitCommitMessage Description VelocityUtils";
        try {
            engine.evaluate(velocityContext, writer, VM_LOG_TAG, html);
            return writer.toString();
        } catch (Exception e) {
            throw new TemplateConvertException(PluginBundle.get("setting.template.description.error"));
        }
    }
}
