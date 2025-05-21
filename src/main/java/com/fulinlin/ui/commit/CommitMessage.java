package com.fulinlin.ui.commit;

import com.fulinlin.model.CommitTemplate;
import com.fulinlin.model.Alias;
import com.fulinlin.storage.GitCommitMessageHelperSettings;
import com.fulinlin.utils.VelocityUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * @author fulin
 */
public class CommitMessage {

    private final String content;

    public CommitMessage(GitCommitMessageHelperSettings settings,
                         Alias alias,
                         Alias platformAlias,
                         String taskId,
                         String business,
                         String longDescription
    ) {
        this.content = buildContent(
                settings,
                alias,
                platformAlias,
                taskId,
                business,
                longDescription
        );
    }

    private String buildContent(GitCommitMessageHelperSettings settings,
                                Alias alias,
                                Alias platformAlias,
                                String taskId,
                                String business,
                                String longDescription
    ) {

        CommitTemplate commitTemplate = new CommitTemplate();
        if (alias != null) {
            if (StringUtils.isNotBlank(alias.getTitle())) {
                commitTemplate.setType(alias.getTitle());
            }
        }
        if (platformAlias!= null) {
            if (StringUtils.isNotBlank(platformAlias.getTitle())) {
                commitTemplate.setPlatform(platformAlias.getTitle());
            }
        }
        if (StringUtils.isNotBlank(taskId)) {
            commitTemplate.setTaskId(taskId);
        }
        if (StringUtils.isNotBlank(business)) {
            commitTemplate.setBusiness(business);
        }
        if (StringUtils.isNotBlank(longDescription)) {
            commitTemplate.setBody(longDescription);
        }
        String template = settings.getDateSettings().getTemplate().replaceAll("\\n", "");
        return VelocityUtils.convert(template, commitTemplate);
    }

    @Override
    public String toString() {
        return content;
    }
}