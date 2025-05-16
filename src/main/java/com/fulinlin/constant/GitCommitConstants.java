package com.fulinlin.constant;

/**
 * @program: git-commit-message-helper
 * @author: fulin
 * @create: 2019-12-08 11:37
 **/
public class GitCommitConstants {

    public static final String ACTION_PREFIX = "$APP_CONFIG$/GitCommitMessageHelperSettings";

    public static final String DEFAULT_TEMPLATE = "#if($type)${type}#end\n"+
            "#if($platform)(${platform}#end\n" +
            "#if($changeId) ${changeId})#end\n" +
            "#if($business) [${business}]#end\n" +
            "#if($body) ${body}#end\n";
}