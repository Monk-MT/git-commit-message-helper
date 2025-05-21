package com.dcar.ui.commit;

import com.dcar.localization.PluginBundle;
import com.dcar.model.CentralSettings;
import com.dcar.model.CommitTemplate;
import com.dcar.model.Alias;
import com.dcar.model.enums.PlatformDisplayStyleEnum;
import com.dcar.model.enums.TypeDisplayStyleEnum;
import com.dcar.storage.GitCommitMessageHelperSettings;
import com.intellij.ide.ui.laf.darcula.ui.DarculaEditorTextFieldBorder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.EditorTextField;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class CommitPanel {
    private final GitCommitMessageHelperSettings settings;
    private JPanel mainPanel;
    private JComboBox<Alias> changeType;
    private JComboBox<Alias> platforms;
    private JTextField taskId;
    private JTextField business;
    private EditorTextField longDescription;
    private JLabel typeDescriptionLabel;
    private JLabel platformDescriptionLabel;
    private JLabel taskIdDescriptionLabel;
    private JLabel businessDescriptionLabel;
    private JLabel bodyDescriptionLabel;
    private JPanel typePanel;
    private JPanel platformPanel;
    private JScrollPane longDescriptionScrollPane;
    private JButton businessResetButton;
    private ButtonGroup typeButtonGroup;
    private ButtonGroup platformButtonGroup;


    public CommitPanel(Project project, GitCommitMessageHelperSettings settings, CommitTemplate commitMessageTemplate) {
        this.settings = settings;
        typeDescriptionLabel.setText(PluginBundle.get("commit.panel.type.field"));
        platformDescriptionLabel.setText(PluginBundle.get("commit.panel.platform.field"));
        taskIdDescriptionLabel.setText(PluginBundle.get("commit.panel.taskid.field"));
        businessDescriptionLabel.setText(PluginBundle.get("commit.panel.business.field"));
        bodyDescriptionLabel.setText(PluginBundle.get("commit.panel.body.field"));
        businessResetButton.setText(PluginBundle.get("commit.panel.business.reset.button"));

        longDescriptionScrollPane.setBorder(BorderFactory.createEmptyBorder());
        longDescription.setBorder(new DarculaEditorTextFieldBorder());
        longDescription.setOneLineMode(false);
        longDescription.ensureWillComputePreferredSize();
        longDescription.addSettingsProvider(uEditor -> {
            uEditor.setVerticalScrollbarVisible(true);
            uEditor.setHorizontalScrollbarVisible(true);
            uEditor.setBorder(null);
        });

        settingHidden(commitMessageTemplate);
        computePanelHeight();
    }

    private void settingHidden(CommitTemplate commitMessageTemplate) {
        CentralSettings centralSettings = settings.getCentralSettings();
        List<Alias> aliases = settings.getDateSettings().getTypeAliases();
        if (centralSettings.getHidden().getType()) {
            typeDescriptionLabel.setVisible(false);
            typePanel.setVisible(false);
        } else {
            if (centralSettings.getTypeDisplayStyle() == TypeDisplayStyleEnum.CHECKBOX) {
                changeType = new ComboBox<>();
                for (Alias type : aliases) {
                    changeType.addItem(type);
                }
                if (commitMessageTemplate != null) {
                    aliases.stream()
                            .filter(typeAlias -> typeAlias.getTitle().equals(commitMessageTemplate.getType()))
                            .findFirst()
                            .ifPresent(typeAlias ->
                                    changeType.setSelectedItem(typeAlias)
                            );
                }
                typePanel.add(changeType);
            } 
            else if (centralSettings.getTypeDisplayStyle() == TypeDisplayStyleEnum.RADIO) {
                typeButtonGroup = new ButtonGroup();
                typePanel.setLayout(new GridLayout(0, 1));
                Integer typeDisplayNumber = centralSettings.getTypeDisplayNumber();
                if (typeDisplayNumber == -1) {
                    typeDisplayNumber = aliases.size();
                }
                if (typeDisplayNumber > aliases.size()) {
                    typeDisplayNumber = aliases.size();
                }
                for (int i = 0; i < typeDisplayNumber; i++) {
                    Alias type = aliases.get(i);
                    JRadioButton radioButton = new JRadioButton(type.getTitle() + "-" + type.getDescription());
                    radioButton.setActionCommand(type.getTitle());
                    typeButtonGroup.add(radioButton);
                    typePanel.add(radioButton);
                    if (commitMessageTemplate != null) {
                        if (type.getTitle().equals(commitMessageTemplate.getType())) {
                            radioButton.setSelected(true);
                        }
                    }
                }
            } else if (centralSettings.getTypeDisplayStyle() == TypeDisplayStyleEnum.MIXING) {
                typePanel.setLayout(new GridLayout(0, 1));
                changeType = new ComboBox<>();
                typeButtonGroup = new ButtonGroup();
                Integer typeDisplayNumber = centralSettings.getTypeDisplayNumber();
                if (typeDisplayNumber == -1) {
                    typeDisplayNumber = aliases.size();
                }
                if (typeDisplayNumber > aliases.size()) {
                    typeDisplayNumber = aliases.size();
                }
                for (int i = 0; i < typeDisplayNumber; i++) {
                    Alias type = aliases.get(i);
                    JRadioButton radioButton = new JRadioButton(type.getTitle() + "-" + type.getDescription());
                    radioButton.setActionCommand(type.getTitle());
                    radioButton.addChangeListener(e -> {
                        if (radioButton.isSelected()) {
                            aliases.stream()
                                    .filter(typeAlias -> typeAlias.getTitle().equals(radioButton.getActionCommand()))
                                    .findFirst()
                                    .ifPresent(typeAlias ->
                                            changeType.setSelectedItem(typeAlias)
                                    );
                        }
                    });
                    typeButtonGroup.add(radioButton);
                    typePanel.add(radioButton);
                    if (commitMessageTemplate != null) {
                        if (type.getTitle().equals(commitMessageTemplate.getType())) {
                            radioButton.setSelected(true);
                        }
                    }
                }
                changeType.addActionListener(e -> {
                    if (changeType.getSelectedItem() != null) {
                        String typeTitle = ((Alias) Objects.requireNonNull(changeType.getSelectedItem())).getTitle();
                        Iterator<AbstractButton> iterator = typeButtonGroup.getElements().asIterator();
                        boolean flag = false;
                        while (iterator.hasNext()) {
                            AbstractButton radioButton = iterator.next();
                            boolean equals = radioButton.getActionCommand().equals(typeTitle);
                            radioButton.setSelected(equals);
                            if (equals) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            typeButtonGroup.clearSelection();
                        }
                    }
                });
                for (Alias type : aliases) {
                    changeType.addItem(type);
                }
                if (commitMessageTemplate != null) {
                    aliases.stream()
                            .filter(typeAlias -> typeAlias.getTitle().equals(commitMessageTemplate.getType()))
                            .findFirst()
                            .ifPresent(typeAlias ->
                                    changeType.setSelectedItem(typeAlias)
                            );
                }
                typePanel.add(changeType);
            }
        }
        
        List<Alias> platformAliases = settings.getDateSettings().getPlatformAliases();
        if (centralSettings.getHidden().getPlatform()) {
            platformDescriptionLabel.setVisible(false);
            platformPanel.setVisible(false);
        } else {
            if (centralSettings.getPlatformDisplayStyle() == PlatformDisplayStyleEnum.CHECKBOX) {
                platforms = new ComboBox<>();
                for (Alias platform : platformAliases) {
                    platforms.addItem(platform);
                }
                if (commitMessageTemplate != null) {
                    platformAliases.stream()
                            .filter(alias -> alias.getTitle().equals(commitMessageTemplate.getPlatform()))
                            .findFirst()
                            .ifPresent(alias ->
                                    platforms.setSelectedItem(alias)
                            );
                }
                platformPanel.add(platforms);
            } 
            else if (centralSettings.getPlatformDisplayStyle() == PlatformDisplayStyleEnum.RADIO) {
                platformButtonGroup = new ButtonGroup();
                platformPanel.setLayout(new GridLayout(0, 1));
                Integer platformDisplayNumber = centralSettings.getPlatformDisplayNumber();
                if (platformDisplayNumber == -1) {
                    platformDisplayNumber = platformAliases.size();
                }
                if (platformDisplayNumber > platformAliases.size()) {
                    platformDisplayNumber = platformAliases.size();
                }
                for (int i = 0; i < platformDisplayNumber; i++) {
                    Alias platform = platformAliases.get(i);
                    JRadioButton radioButton = new JRadioButton(platform.getTitle() + "-" + platform.getDescription());
                    radioButton.setActionCommand(platform.getTitle());
                    platformButtonGroup.add(radioButton);
                    platformPanel.add(radioButton);
                    if (commitMessageTemplate != null) {
                        if (platform.getTitle().equals(commitMessageTemplate.getPlatform())) {
                            radioButton.setSelected(true);
                        }
                    }
                }
            }
            else if (centralSettings.getPlatformDisplayStyle() == PlatformDisplayStyleEnum.MIXING) {
                platformPanel.setLayout(new GridLayout(0, 1));
                platforms = new ComboBox<>();
                platformButtonGroup = new ButtonGroup();
                Integer platformDisplayNumber = centralSettings.getPlatformDisplayNumber();
                if (platformDisplayNumber == -1) {
                    platformDisplayNumber = platformAliases.size();
                }
                if (platformDisplayNumber > platformAliases.size()) {
                    platformDisplayNumber = platformAliases.size();
                }
                for (int i = 0; i < platformDisplayNumber; i++) {
                    Alias platform = platformAliases.get(i);
                    JRadioButton radioButton = new JRadioButton(platform.getTitle() + "-" + platform.getDescription());
                    radioButton.setActionCommand(platform.getTitle());
                    radioButton.addChangeListener(e -> {
                        if (radioButton.isSelected()) {
                            platformAliases.stream()
                                    .filter(alias -> alias.getTitle().equals(radioButton.getActionCommand()))
                                    .findFirst()
                                    .ifPresent(alias ->
                                            platforms.setSelectedItem(alias)
                                    );
                        }
                    });
                    platformButtonGroup.add(radioButton);
                    platformPanel.add(radioButton);
                    if (commitMessageTemplate != null) {
                        if (platform.getTitle().equals(commitMessageTemplate.getPlatform())) {
                            radioButton.setSelected(true);
                        }
                    }
                }
                platforms.addActionListener(e -> {
                    if (platforms.getSelectedItem() != null) {
                        String platformTitle = ((Alias) Objects.requireNonNull(platforms.getSelectedItem())).getTitle();
                        Iterator<AbstractButton> iterator = platformButtonGroup.getElements().asIterator();
                        boolean flag = false;
                        while (iterator.hasNext()) {
                            AbstractButton radioButton = iterator.next();
                            boolean equals = radioButton.getActionCommand().equals(platformTitle);
                            radioButton.setSelected(equals);
                            if (equals) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            platformButtonGroup.clearSelection();
                        }
                    }
                });
                for (Alias platform : platformAliases) {
                    platforms.addItem(platform);
                }
                if (commitMessageTemplate != null) {
                    platformAliases.stream()
                            .filter(alias -> alias.getTitle().equals(commitMessageTemplate.getPlatform()))
                            .findFirst()
                            .ifPresent(alias ->
                                    platforms.setSelectedItem(alias)
                            );
                }
                platformPanel.add(platforms);
            }
        }
        if (centralSettings.getHidden().getTaskId()) {
            taskIdDescriptionLabel.setVisible(false);
            taskId.setVisible(false);
        }
        if (centralSettings.getHidden().getBody()) {
            bodyDescriptionLabel.setVisible(false);
            longDescriptionScrollPane.setVisible(false);
            longDescription.setVisible(false);
        }

        if (commitMessageTemplate != null) {
            // with cache init
            taskId.setText(commitMessageTemplate.getTaskId());
            business.setText(commitMessageTemplate.getBusiness());
            longDescription.setText(commitMessageTemplate.getBody());
        } else {
            business.setText(centralSettings.getDefaultBusiness());
        }

        businessResetButton.addActionListener(e -> {
            business.setText(centralSettings.getDefaultBusiness());
        });
    }


    private void computePanelHeight() {
        int height = 0;
        if (changeType != null) {
            height += 33;
        }
        if (typeButtonGroup != null) {
            height += 33 * typeButtonGroup.getButtonCount();
        }
        if (platforms!= null) {
            height += 33;
        }
        if (platformButtonGroup!= null) {
            height += 33 * platformButtonGroup.getButtonCount();
        }
        if (!settings.getCentralSettings().getHidden().getTaskId()) {
            height += 33;
        }
        if (!settings.getCentralSettings().getHidden().getBusiness()) {
            height += 33;
        }
        if (!settings.getCentralSettings().getHidden().getBody()) {
            longDescriptionScrollPane.setPreferredSize(new Dimension(500, 130));
            height += 150;
        }

        mainPanel.setPreferredSize(new Dimension(500, height));
    }

    CommitMessage getCommitMessage(GitCommitMessageHelperSettings settings) {
        Alias type = new Alias();
        if (settings.getCentralSettings().getTypeDisplayStyle() == TypeDisplayStyleEnum.CHECKBOX) {
            if (changeType != null) {
                if (changeType.getSelectedItem() != null) {
                    type = ((Alias) Objects.requireNonNull(changeType.getSelectedItem()));
                }
            }
        } else if (settings.getCentralSettings().getTypeDisplayStyle() == TypeDisplayStyleEnum.RADIO) {
            if (typeButtonGroup != null) {
                if (typeButtonGroup.getSelection() != null) {
                    if (typeButtonGroup.getSelection().getActionCommand() != null) {
                        type = new Alias(typeButtonGroup.getSelection().getActionCommand(), "");
                    }
                }
            }
        } else if (settings.getCentralSettings().getTypeDisplayStyle() == TypeDisplayStyleEnum.MIXING) {
            if (changeType != null) {
                if (changeType.getSelectedItem() != null) {
                    type = ((Alias) Objects.requireNonNull(changeType.getSelectedItem()));
                }
            }
        }

        Alias platform = new Alias();
        if (settings.getCentralSettings().getPlatformDisplayStyle() == PlatformDisplayStyleEnum.CHECKBOX) {
            if (platforms != null) {
                if (platforms.getSelectedItem() != null) {
                    platform = ((Alias) Objects.requireNonNull(platforms.getSelectedItem()));
                }
            }
        } else if (settings.getCentralSettings().getPlatformDisplayStyle() == PlatformDisplayStyleEnum.RADIO) {
            if (platformButtonGroup != null) {
                if (platformButtonGroup.getSelection() != null) {
                    if (platformButtonGroup.getSelection().getActionCommand() != null) {
                        platform = new Alias(platformButtonGroup.getSelection().getActionCommand(), "");
                    }
                }
            }
        } else if (settings.getCentralSettings().getPlatformDisplayStyle() == PlatformDisplayStyleEnum.MIXING) {
            if (changeType != null) {
                if (changeType.getSelectedItem() != null) {
                    platform = ((Alias) Objects.requireNonNull(platforms.getSelectedItem()));
                }
            }
        }

        return new CommitMessage(
                settings,
                type,
                platform,
                taskId.getText().trim(),
                business.getText().trim(),
                longDescription.getText().trim()
        );
    }

    CommitTemplate getCommitMessageTemplate() {
        CommitTemplate commitTemplate = new CommitTemplate();
        if (settings.getCentralSettings().getTypeDisplayStyle() == TypeDisplayStyleEnum.CHECKBOX) {
            if (changeType != null) {
                if (changeType.getSelectedItem() != null) {
                    commitTemplate.setType(((Alias) Objects.requireNonNull(changeType.getSelectedItem())).getTitle());
                }
            }
        } else if (settings.getCentralSettings().getTypeDisplayStyle() == TypeDisplayStyleEnum.RADIO) {
            if (typeButtonGroup != null) {
                if (typeButtonGroup.getSelection() != null) {
                    if (typeButtonGroup.getSelection().getActionCommand() != null) {
                        commitTemplate.setType(typeButtonGroup.getSelection().getActionCommand());
                    }
                }
            }
        } else if (settings.getCentralSettings().getTypeDisplayStyle() == TypeDisplayStyleEnum.MIXING) {
            if (changeType != null) {
                if (changeType.getSelectedItem() != null) {
                    commitTemplate.setType(((Alias) Objects.requireNonNull(changeType.getSelectedItem())).getTitle());
                }
            }
        }

        if (settings.getCentralSettings().getPlatformDisplayStyle() == PlatformDisplayStyleEnum.CHECKBOX) {
            if (platforms != null) {
                if (platforms.getSelectedItem() != null) {
                    commitTemplate.setPlatform(((Alias) Objects.requireNonNull(platforms.getSelectedItem())).getTitle());
                }
            }
        } else if (settings.getCentralSettings().getPlatformDisplayStyle() == PlatformDisplayStyleEnum.RADIO) {
            if (platformButtonGroup != null) {
                if (platformButtonGroup.getSelection() != null) {
                    if (platformButtonGroup.getSelection().getActionCommand() != null) {
                        commitTemplate.setPlatform(platformButtonGroup.getSelection().getActionCommand());
                    }
                }
            }
        } else if (settings.getCentralSettings().getPlatformDisplayStyle() == PlatformDisplayStyleEnum.MIXING) {
            if (platforms != null) {
                if (platforms.getSelectedItem() != null) {
                    commitTemplate.setPlatform(((Alias) Objects.requireNonNull(platforms.getSelectedItem())).getTitle());
                }
            }
        }

        commitTemplate.setTaskId(taskId.getText().trim());
        commitTemplate.setBusiness(business.getText().trim());
        commitTemplate.setBody(longDescription.getText().trim());

        return commitTemplate;
    }

    JPanel getMainPanel() {
        return mainPanel;
    }


}
