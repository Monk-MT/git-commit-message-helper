package com.fulinlin.ui.commit;

import com.fulinlin.model.CommitTemplate;
import com.fulinlin.model.PlatformAlias;
import com.fulinlin.model.TypeAlias;
import com.fulinlin.storage.GitCommitMessageHelperSettings;
import com.fulinlin.utils.VelocityUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * @author fulin
 */
public class CommitMessage {

    private final String content;

    public CommitMessage(GitCommitMessageHelperSettings settings,
                         TypeAlias typeAlias,
                         PlatformAlias platformAlias,
                         String changeId,
                         String business,
                         String longDescription
//                         String closedIssues,
//                         String breakingChanges,
//                         String skipCi
    ) {
        this.content = buildContent(
                settings,
                typeAlias,
                platformAlias,
                changeId,
                business,
                longDescription
//                breakingChanges,
//                closedIssues,
//                skipCi
        );
    }

    private String buildContent(GitCommitMessageHelperSettings settings,
                                TypeAlias typeAlias,
                                PlatformAlias platformAlias,
                                String changeId,
                                String business,
                                String longDescription
//                                String breakingChanges,
//                                String closedIssues,
//                                String skipCi
    ) {

        CommitTemplate commitTemplate = new CommitTemplate();
        if (typeAlias != null) {
            if (StringUtils.isNotBlank(typeAlias.getTitle())) {
                commitTemplate.setType(typeAlias.getTitle());
            }
        }
        if (platformAlias!= null) {
            if (StringUtils.isNotBlank(platformAlias.getTitle())) {
                commitTemplate.setPlatform(platformAlias.getTitle());
            }
        }
        if (StringUtils.isNotBlank(changeId)) {
            commitTemplate.setChangeId(changeId);
        }
        if (StringUtils.isNotBlank(business)) {
            commitTemplate.setBusiness(business);
        }
        if (StringUtils.isNotBlank(longDescription)) {
            commitTemplate.setBody(longDescription);
        }
//        if (StringUtils.isNotBlank(breakingChanges)) {
//            commitTemplate.setChanges(breakingChanges);
//        }
//        if (StringUtils.isNotBlank(closedIssues)) {
//            commitTemplate.setCloses(closedIssues);
//        }
//        if (StringUtils.isNotBlank(skipCi)) {
//            commitTemplate.setSkipCi(skipCi);
//        }
        String template = settings.getDateSettings().getTemplate().replaceAll("\\n", "");
        return VelocityUtils.convert(template, commitTemplate);
    }

    @Override
    public String toString() {
        return content;
    }
}