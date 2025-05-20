package com.fulinlin.ui.commit;

import com.fulinlin.localization.PluginBundle;
import com.fulinlin.model.CentralSettings;
import com.fulinlin.model.CommitTemplate;
import com.fulinlin.model.PlatformAlias;
import com.fulinlin.model.TypeAlias;
import com.fulinlin.model.enums.PlatformDisplayStyleEnum;
import com.fulinlin.model.enums.TypeDisplayStyleEnum;
import com.fulinlin.storage.GitCommitMessageHelperSettings;
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
    private JComboBox<TypeAlias> changeType;
    private JComboBox<PlatformAlias> platforms;
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
    private ButtonGroup typeButtonGroup;
    private ButtonGroup platformButtonGroup;


    public CommitPanel(Project project, GitCommitMessageHelperSettings settings, CommitTemplate commitMessageTemplate) {
        this.settings = settings;
        typeDescriptionLabel.setText(PluginBundle.get("commit.panel.type.field"));
        platformDescriptionLabel.setText(PluginBundle.get("commit.panel.platform.field"));
        taskIdDescriptionLabel.setText(PluginBundle.get("commit.panel.id.field"));
        businessDescriptionLabel.setText(PluginBundle.get("commit.panel.business.field"));
        bodyDescriptionLabel.setText(PluginBundle.get("commit.panel.body.field"));

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
        List<TypeAlias> typeAliases = settings.getDateSettings().getTypeAliases();
        if (centralSettings.getHidden().getType()) {
            typeDescriptionLabel.setVisible(false);
            typePanel.setVisible(false);
        } else {
            if (centralSettings.getTypeDisplayStyle() == TypeDisplayStyleEnum.CHECKBOX) {
                changeType = new ComboBox<>();
                for (TypeAlias type : typeAliases) {
                    changeType.addItem(type);
                }
                if (commitMessageTemplate != null) {
                    typeAliases.stream()
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
                    typeDisplayNumber = typeAliases.size();
                }
                if (typeDisplayNumber > typeAliases.size()) {
                    typeDisplayNumber = typeAliases.size();
                }
                for (int i = 0; i < typeDisplayNumber; i++) {
                    TypeAlias type = typeAliases.get(i);
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
                    typeDisplayNumber = typeAliases.size();
                }
                if (typeDisplayNumber > typeAliases.size()) {
                    typeDisplayNumber = typeAliases.size();
                }
                for (int i = 0; i < typeDisplayNumber; i++) {
                    TypeAlias type = typeAliases.get(i);
                    JRadioButton radioButton = new JRadioButton(type.getTitle() + "-" + type.getDescription());
                    radioButton.setActionCommand(type.getTitle());
                    radioButton.addChangeListener(e -> {
                        if (radioButton.isSelected()) {
                            typeAliases.stream()
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
                        String typeTitle = ((TypeAlias) Objects.requireNonNull(changeType.getSelectedItem())).getTitle();
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
                for (TypeAlias type : typeAliases) {
                    changeType.addItem(type);
                }
                if (commitMessageTemplate != null) {
                    typeAliases.stream()
                            .filter(typeAlias -> typeAlias.getTitle().equals(commitMessageTemplate.getType()))
                            .findFirst()
                            .ifPresent(typeAlias ->
                                    changeType.setSelectedItem(typeAlias)
                            );
                }
                typePanel.add(changeType);
            }
        }
        
        List<PlatformAlias> platformAliases = settings.getDateSettings().getPlatformAliases();
        if (centralSettings.getHidden().getPlatform()) {
            platformDescriptionLabel.setVisible(false);
            platformPanel.setVisible(false);
        } else {
            if (centralSettings.getPlatformDisplayStyle() == PlatformDisplayStyleEnum.CHECKBOX) {
                platforms = new ComboBox<>();
                for (PlatformAlias platform : platformAliases) {
                    platforms.addItem(platform);
                }
                if (commitMessageTemplate != null) {
                    platformAliases.stream()
                            .filter(platformAlias -> platformAlias.getTitle().equals(commitMessageTemplate.getPlatform()))
                            .findFirst()
                            .ifPresent(platformAlias ->
                                    platforms.setSelectedItem(platformAlias)
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
                    PlatformAlias platform = platformAliases.get(i);
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
                    PlatformAlias platform = platformAliases.get(i);
                    JRadioButton radioButton = new JRadioButton(platform.getTitle() + "-" + platform.getDescription());
                    radioButton.setActionCommand(platform.getTitle());
                    radioButton.addChangeListener(e -> {
                        if (radioButton.isSelected()) {
                            platformAliases.stream()
                                    .filter(platformAlias -> platformAlias.getTitle().equals(radioButton.getActionCommand()))
                                    .findFirst()
                                    .ifPresent(platformAlias ->
                                            platforms.setSelectedItem(platformAlias)
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
                        String platformTitle = ((PlatformAlias) Objects.requireNonNull(platforms.getSelectedItem())).getTitle();
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
                for (PlatformAlias platform : platformAliases) {
                    platforms.addItem(platform);
                }
                if (commitMessageTemplate != null) {
                    platformAliases.stream()
                            .filter(platformAlias -> platformAlias.getTitle().equals(commitMessageTemplate.getPlatform()))
                            .findFirst()
                            .ifPresent(platformAlias ->
                                    platforms.setSelectedItem(platformAlias)
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

        }
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
        TypeAlias type = new TypeAlias();
        if (settings.getCentralSettings().getTypeDisplayStyle() == TypeDisplayStyleEnum.CHECKBOX) {
            if (changeType != null) {
                if (changeType.getSelectedItem() != null) {
                    type = ((TypeAlias) Objects.requireNonNull(changeType.getSelectedItem()));
                }
            }
        } else if (settings.getCentralSettings().getTypeDisplayStyle() == TypeDisplayStyleEnum.RADIO) {
            if (typeButtonGroup != null) {
                if (typeButtonGroup.getSelection() != null) {
                    if (typeButtonGroup.getSelection().getActionCommand() != null) {
                        type = new TypeAlias(typeButtonGroup.getSelection().getActionCommand(), "");
                    }
                }
            }
        } else if (settings.getCentralSettings().getTypeDisplayStyle() == TypeDisplayStyleEnum.MIXING) {
            if (changeType != null) {
                if (changeType.getSelectedItem() != null) {
                    type = ((TypeAlias) Objects.requireNonNull(changeType.getSelectedItem()));
                }
            }
        }

        PlatformAlias platform = new PlatformAlias();
        if (settings.getCentralSettings().getPlatformDisplayStyle() == PlatformDisplayStyleEnum.CHECKBOX) {
            if (platforms != null) {
                if (platforms.getSelectedItem() != null) {
                    platform = ((PlatformAlias) Objects.requireNonNull(platforms.getSelectedItem()));
                }
            }
        } else if (settings.getCentralSettings().getPlatformDisplayStyle() == PlatformDisplayStyleEnum.RADIO) {
            if (platformButtonGroup != null) {
                if (platformButtonGroup.getSelection() != null) {
                    if (platformButtonGroup.getSelection().getActionCommand() != null) {
                        platform = new PlatformAlias(platformButtonGroup.getSelection().getActionCommand(), "");
                    }
                }
            }
        } else if (settings.getCentralSettings().getPlatformDisplayStyle() == PlatformDisplayStyleEnum.MIXING) {
            if (changeType != null) {
                if (changeType.getSelectedItem() != null) {
                    platform = ((PlatformAlias) Objects.requireNonNull(platforms.getSelectedItem()));
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
                    commitTemplate.setType(((TypeAlias) Objects.requireNonNull(changeType.getSelectedItem())).getTitle());
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
                    commitTemplate.setType(((TypeAlias) Objects.requireNonNull(changeType.getSelectedItem())).getTitle());
                }
            }
        }

        if (settings.getCentralSettings().getPlatformDisplayStyle() == PlatformDisplayStyleEnum.CHECKBOX) {
            if (platforms != null) {
                if (platforms.getSelectedItem() != null) {
                    commitTemplate.setPlatform(((PlatformAlias) Objects.requireNonNull(platforms.getSelectedItem())).getTitle());
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
                    commitTemplate.setPlatform(((PlatformAlias) Objects.requireNonNull(platforms.getSelectedItem())).getTitle());
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
