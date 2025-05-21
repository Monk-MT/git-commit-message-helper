package com.fulinlin.ui.setting;

import com.fulinlin.constant.GitCommitConstants;
import com.fulinlin.localization.PluginBundle;
import com.fulinlin.model.CommitTemplate;
import com.fulinlin.storage.GitCommitMessageHelperSettings;
import com.fulinlin.ui.setting.description.DescriptionRead;
import com.fulinlin.utils.VelocityUtils;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.*;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Optional;


public class TemplateEditPanel {
    private final TypeAliasTable typeAliasTable;
    private final PlatformAliasTable platformAliasTable;
    private final Editor templateEditor;
    private final EditorTextField previewEditor;
    private final JEditorPane myDescriptionComponent;
    protected GitCommitMessageHelperSettings settings;
    private JPanel mainPanel;
    private JPanel templatePanel;
    private JPanel typeEditPanel;
    private JPanel platformEditPanel;
    private JTabbedPane tabbedPane;
    private JLabel description;
    private JLabel descriptionLabel;
    private JLabel templateLabel;
    private JPanel descriptionPanel;
    private JLabel previewLabel;
    private JPanel previewPanel;
    private JCheckBox typeCheckBox;
    private JCheckBox platformCheckBox;
    private JCheckBox taskIdCheckBox;
    private JCheckBox businessCheckBox;
    private JCheckBox bodyCheckBox;
    private JButton restoreDefaultsButton;


    public TemplateEditPanel(GitCommitMessageHelperSettings settings) {
        //Get setting
        this.settings = settings.clone();

        // Init  description 
        description.setText(PluginBundle.get("setting.description"));
        descriptionLabel.setText(PluginBundle.get("setting.template.description"));
        templateLabel.setText(PluginBundle.get("setting.template.edit"));
        previewLabel.setText(PluginBundle.get("setting.template.preview"));
        tabbedPane.setTitleAt(0, PluginBundle.get("setting.tabbed.panel.template"));
        tabbedPane.setTitleAt(1, PluginBundle.get("setting.tabbed.panel.type"));
        tabbedPane.setTitleAt(2, PluginBundle.get("setting.tabbed.panel.platform"));
        restoreDefaultsButton.setText(PluginBundle.get("setting.template.restore.defaults"));

        // Init descriptionPanel
        myDescriptionComponent = new JEditorPane();
        myDescriptionComponent.setEditorKit(UIUtil.getHTMLEditorKit());
        myDescriptionComponent.setEditable(false);
        myDescriptionComponent.addHyperlinkListener(new BrowserHyperlinkListener());
        myDescriptionComponent.setCaretPosition(0);
        JBScrollPane descriptionScrollPanel = new JBScrollPane(myDescriptionComponent);
        descriptionScrollPanel.setMaximumSize(new Dimension(150, 50));
        descriptionPanel.add(descriptionScrollPanel);

        // Init  templatePanel
        String template = Optional.of(settings.getDateSettings().getTemplate()).orElse("");
        templateEditor = EditorFactory.getInstance().createEditor(
                EditorFactory.getInstance().createDocument(""),
                null,
                FileTypeManager.getInstance().getFileTypeByExtension("vm"),
                false);
        EditorSettings templateEditorSettings = templateEditor.getSettings();
        templateEditorSettings.setAdditionalLinesCount(0);
        templateEditorSettings.setAdditionalColumnsCount(0);
        templateEditorSettings.setLineMarkerAreaShown(false);
        templateEditorSettings.setVirtualSpace(false);
        JBScrollPane templateScrollPane = new JBScrollPane(templateEditor.getComponent());
        templateScrollPane.setMaximumSize(new Dimension(150, 50));
        templatePanel.add(templateScrollPane);

        // Init previewPanel
        previewEditor = new EditorTextField();
        previewEditor.setViewer(true);
        previewEditor.setOneLineMode(false);
        previewEditor.ensureWillComputePreferredSize();
        previewEditor.addSettingsProvider(uEditor -> {
            uEditor.setVerticalScrollbarVisible(true);
            uEditor.setHorizontalScrollbarVisible(true);
            uEditor.setBorder(null);
        });
        JBScrollPane previewScrollPane = new JBScrollPane(previewEditor.getComponent());
        previewScrollPane.setMaximumSize(new Dimension(150, 50));
        previewPanel.add(previewScrollPane);
        templateEditor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                showPreview();
            }
        });
        typeCheckBox.addChangeListener(e -> showPreview());
        platformCheckBox.addChangeListener(e -> showPreview());
        taskIdCheckBox.addChangeListener(e -> showPreview());
        businessCheckBox.addChangeListener(e -> showPreview());
        bodyCheckBox.addChangeListener(e -> showPreview());
        // Init  typeEditPanel
        typeAliasTable = new TypeAliasTable();
        typeEditPanel.add(
                ToolbarDecorator.createDecorator(typeAliasTable)
                        .setAddAction(button -> typeAliasTable.addAlias())
                        .setRemoveAction(button -> typeAliasTable.removeSelectedAliases())
                        .setEditAction(button -> typeAliasTable.editAlias())
                        .setMoveUpAction(anActionButton -> typeAliasTable.moveUp())
                        .setMoveDownAction(anActionButton -> typeAliasTable.moveDown())
                        .addExtraAction
                                (new AnActionButton("Reset Default Aliases", AllIcons.Actions.Rollback) {
                                    @Override
                                    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                                        typeAliasTable.resetDefaultAliases();
                                    }
                                }).createPanel(), BorderLayout.CENTER);

        // Init  platformEditPanel
        platformAliasTable = new PlatformAliasTable();
        platformEditPanel.add(
                ToolbarDecorator.createDecorator(platformAliasTable)
                        .setAddAction(button -> platformAliasTable.addAlias())
                        .setRemoveAction(button -> platformAliasTable.removeSelectedAliases())
                        .setEditAction(button -> platformAliasTable.editAlias())
                        .setMoveUpAction(anActionButton -> platformAliasTable.moveUp())
                        .setMoveDownAction(anActionButton -> platformAliasTable.moveDown())
                        .addExtraAction
                                (new AnActionButton("Reset Default Aliases", AllIcons.Actions.Rollback) {
                                    @Override
                                    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                                        platformAliasTable.resetDefaultAliases();
                                    }
                                }).createPanel(), BorderLayout.CENTER);

        // Init data
        ApplicationManager.getApplication().runWriteAction(() -> {
            templateEditor.getDocument().setText(template);
            myDescriptionComponent.setText(DescriptionRead.readHtmlFile());
        });
        restoreDefaultsButton.addActionListener(e -> {
            ApplicationManager.getApplication().runWriteAction(() -> {
                templateEditor.getDocument().setText(GitCommitConstants.DEFAULT_TEMPLATE);
            });
        });
        // Add  DoubleClickListener
        new DoubleClickListener() {
            @Override
            protected boolean onDoubleClick(@NotNull MouseEvent e) {
                return typeAliasTable.editAlias();
            }
        }.installOn(typeAliasTable);
        new DoubleClickListener() {
            @Override
            protected boolean onDoubleClick(@NotNull MouseEvent e) {
                return platformAliasTable.editAlias();
            }
        }.installOn(platformEditPanel);

    }

    private void showPreview() {
        CommitTemplate commitTemplate = new CommitTemplate();
        if (typeCheckBox.isSelected()) {
            commitTemplate.setType("<type>");
        }
        if (platformCheckBox.isSelected()) {
            commitTemplate.setPlatform("<platform>");
        }
        if (taskIdCheckBox.isSelected()) {
            commitTemplate.setTaskId("<taskId>");
        }
        if (businessCheckBox.isSelected()) {
            commitTemplate.setBusiness("<business>");
        }
        if (bodyCheckBox.isSelected()) {
            commitTemplate.setBody("<body>");
        }
        ApplicationManager.getApplication().runWriteAction(() -> {
            String previewTemplate = templateEditor.getDocument().getText().replaceAll("\\n", "");
            previewEditor.getDocument().setText(VelocityUtils.convert(previewTemplate, commitTemplate));
        });
    }


    public GitCommitMessageHelperSettings getSettings() {
        typeAliasTable.commit(settings);
        platformAliasTable.commit(settings);
        settings.getDateSettings().setTemplate(templateEditor.getDocument().getText());
        return settings;
    }

    public void reset(GitCommitMessageHelperSettings settings) {
        this.settings = settings.clone();
        typeAliasTable.reset(settings);
        platformAliasTable.reset(settings);
        ApplicationManager.getApplication().runWriteAction(() ->
                templateEditor.getDocument().setText(settings.getDateSettings().getTemplate())
        );
        myDescriptionComponent.setText(DescriptionRead.readHtmlFile());
    }

    public boolean isSettingsModified(GitCommitMessageHelperSettings settings) {
        if (typeAliasTable.isModified(settings)) return true;
        if (platformAliasTable.isModified(settings)) return true;
        return isModified(settings);
    }

    public boolean isModified(GitCommitMessageHelperSettings data) {
        if (!StringUtil.equals(settings.getDateSettings().getTemplate(), templateEditor.getDocument().getText())) {
            return true;
        }
        return settings.getDateSettings().getTypeAliases() == data.getDateSettings().getTypeAliases();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

}
