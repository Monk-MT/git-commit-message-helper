package com.dcar.constant;

public class GitCommitConstants {

    public static final String ACTION_PREFIX = "$APP_CONFIG$/GitCommitMessageHelperSettings";

    public static final String DEFAULT_TEMPLATE =
            "#if($type)${type}#end\n" +
            "#if($platform || $taskId) (#end\n" +
            "#if($platform)${platform}#end\n" +
            "#if($platform && $taskId) #end\n" +
            "#if($taskId)${taskId}#end\n" +
            "#if($platform || $taskId))#end\n" +
            "#if($business) [${business}]#end\n" +
            "#if($body) ${body}#end\n";
}