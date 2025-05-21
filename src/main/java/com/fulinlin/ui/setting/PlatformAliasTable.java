package com.fulinlin.ui.setting;

import com.fulinlin.model.Alias;
import com.fulinlin.storage.GitCommitMessageHelperSettings;

import java.util.LinkedList;
import java.util.List;

public class PlatformAliasTable extends AliasTable{

    @Override
    public void commit(GitCommitMessageHelperSettings settings) {
        settings.getDateSettings().setPlatformAliases(new LinkedList<>(getAliases()));
    }

    @Override
    public List<Alias> getRecordAliases(GitCommitMessageHelperSettings settings) {
        return settings.getDateSettings().getPlatformAliases();
    }

}
