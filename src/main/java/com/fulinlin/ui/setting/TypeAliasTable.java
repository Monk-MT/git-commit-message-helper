package com.fulinlin.ui.setting;

import com.fulinlin.model.Alias;
import com.fulinlin.storage.GitCommitMessageHelperSettings;

import java.util.LinkedList;
import java.util.List;

public class TypeAliasTable extends AliasTable{

    @Override
    public void commit(GitCommitMessageHelperSettings settings) {
        settings.getDateSettings().setTypeAliases(new LinkedList<>(getAliases()));
    }

    @Override
    public List<Alias> getRecordAliases(GitCommitMessageHelperSettings settings) {
        return settings.getDateSettings().getTypeAliases();
    }

}
