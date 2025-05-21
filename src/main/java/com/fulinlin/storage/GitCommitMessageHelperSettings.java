package com.fulinlin.storage;

import com.fulinlin.constant.GitCommitConstants;
import com.fulinlin.localization.PluginBundle;
import com.fulinlin.model.CentralSettings;
import com.fulinlin.model.DataSettings;
import com.fulinlin.model.Alias;
import com.fulinlin.model.enums.PlatformDisplayStyleEnum;
import com.fulinlin.model.enums.TypeDisplayStyleEnum;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.rits.cloning.Cloner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * @program: git-commit-message-helper
 * @author: fulin
 * @create: 2019-12-05 21:13
 **/
@State(name = "GitCommitMessageHelperSettings",
        storages = {@Storage(value = GitCommitConstants.ACTION_PREFIX + "-settings.xml")})
public class GitCommitMessageHelperSettings implements PersistentStateComponent<GitCommitMessageHelperSettings> {
    private static final Logger log = Logger.getInstance(GitCommitMessageHelperSettings.class);
    private DataSettings dataSettings;

    private CentralSettings centralSettings;

    public GitCommitMessageHelperSettings() {
    }

    @Nullable
    @Override
    public GitCommitMessageHelperSettings getState() {
        if (this.dataSettings == null) {
            loadDefaultDataSettings();
        } else {
            checkDefaultDataSettings(dataSettings);
        }
        if (centralSettings == null) {
            loadDefaultCentralSettings();
        }
        return this;
    }

    @Override
    public void loadState(@NotNull GitCommitMessageHelperSettings gitCommitMessageHelperSettings) {
        XmlSerializerUtil.copyBean(gitCommitMessageHelperSettings, this);
    }


    public CentralSettings getCentralSettings() {
        if (centralSettings == null) {
            loadDefaultCentralSettings();
        }
        return centralSettings;
    }

    /**
     * Spelling error here, in order to maintain the current status of existing user data
     */
    public DataSettings getDateSettings() {
        if (dataSettings == null) {
            loadDefaultDataSettings();
        } else {
            checkDefaultDataSettings(dataSettings);
        }
        return dataSettings;
    }


    private void loadDefaultCentralSettings() {
        centralSettings = new CentralSettings();
        try {
            centralSettings.setTypeDisplayStyle(TypeDisplayStyleEnum.CHECKBOX);
            centralSettings.setTypeDisplayNumber(-1);
            centralSettings.setPlatformDisplayStyle(PlatformDisplayStyleEnum.CHECKBOX);
            centralSettings.setPlatformDisplayNumber(-1);
            centralSettings.setDefaultBusiness("");
            CentralSettings.Hidden hidden = new CentralSettings.Hidden();
            centralSettings.setHidden(hidden);
            centralSettings.getHidden().setType(Boolean.FALSE);
            centralSettings.getHidden().setPlatform(Boolean.FALSE);
            centralSettings.getHidden().setTaskId(Boolean.FALSE);
            centralSettings.getHidden().setBusiness(Boolean.FALSE);
            centralSettings.getHidden().setBody(Boolean.FALSE);
        } catch (Exception e) {
            log.error("loadDefaultCentralSettings failed", e);
        }
    }


    private void loadDefaultDataSettings() {
        dataSettings = new DataSettings();
        try {
            dataSettings.setTemplate(GitCommitConstants.DEFAULT_TEMPLATE);
            List<Alias> aliases = getTypeAliases();
            dataSettings.setTypeAliases(aliases);
            List<Alias> platformAliases = getPlatformAliases();
            dataSettings.setPlatformAliases(platformAliases);
        } catch (Exception e) {
            log.error("loadDefaultDataSettings failed", e);
        }
    }

    private void checkDefaultDataSettings(DataSettings dataSettings) {
        if (dataSettings.getTemplate() == null) {
            dataSettings.setTemplate(GitCommitConstants.DEFAULT_TEMPLATE);
        }
        if (dataSettings.getTypeAliases() == null) {
            List<Alias> aliases = getTypeAliases();
            dataSettings.setTypeAliases(aliases);
        }
        if (dataSettings.getPlatformAliases() == null) {
            List<Alias> aliases = getPlatformAliases();
            dataSettings.setPlatformAliases(aliases);
        }
    }


    @NotNull
    private static List<Alias> getTypeAliases() {
        List<Alias> aliases = new LinkedList<>();
        // default init i18n
        aliases.add(new Alias("feature", PluginBundle.get("feat.description")));
        aliases.add(new Alias("bugfix", PluginBundle.get("fix.description")));
        aliases.add(new Alias("optimize", PluginBundle.get("opt.description")));
        aliases.add(new Alias("ui", PluginBundle.get("ui.description")));
        aliases.add(new Alias("tea", PluginBundle.get("tea.description")));
        aliases.add(new Alias("other", PluginBundle.get("other.description")));
        return aliases;
    }

    @NotNull
    private static List<Alias> getPlatformAliases() {
        List<Alias> aliases = new LinkedList<>();

        // default init i18n
        aliases.add(new Alias("", PluginBundle.get("blank.description")));
        aliases.add(new Alias("meegoid", PluginBundle.get("meegoid.description")));
        aliases.add(new Alias("slardarid", PluginBundle.get("slardarid.description")));
        aliases.add(new Alias("feedbackid", PluginBundle.get("feedbackid.description")));

        return aliases;
    }


    public void updateTemplate(String template) {
        dataSettings.setTemplate(template);
    }

    public void updateTypeMap(List<Alias> aliases) {
        dataSettings.setTypeAliases(aliases);
    }

    /**
     * Spelling error here, in order to maintain the current status of existing user data
     */
    public void setDateSettings(DataSettings dateSettings) {
        this.dataSettings = dateSettings;
    }

    public void setCentralSettings(CentralSettings centralSettings) {
        this.centralSettings = centralSettings;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public GitCommitMessageHelperSettings clone() {
        Cloner cloner = new Cloner();
        cloner.nullInsteadOfClone();
        return cloner.deepClone(this);
    }

}
