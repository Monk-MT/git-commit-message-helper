package com.fulinlin.ui.central;

import com.fulinlin.localization.PluginBundle;
import com.fulinlin.model.DataSettings;
import com.fulinlin.model.PlatformAlias;
import com.fulinlin.model.enums.PlatformDisplayStyleEnum;
import com.fulinlin.model.enums.TypeDisplayStyleEnum;
import com.fulinlin.storage.GitCommitMessageHelperSettings;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.JBIntSpinner;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class CentralSettingPanel {
    protected GitCommitMessageHelperSettings settings;
    private JPanel mainPanel;
    private JPanel hiddenPanel;
    private JPanel typePanel;
    private JRadioButton typeComboboxRadioButton;
    private JRadioButton typeRadioRadioButton;
    private JRadioButton typeMixingRadioButton;
    private JBIntSpinner typeDisplayNumberSpinner;
    private JPanel platformPanel;
    private JRadioButton platformComboboxRadioButton;
    private JRadioButton platformRadioRadioButton;
    private JRadioButton platformMixingRadioButton;
    private JBIntSpinner platformDisplayNumberSpinner;
//    private JCheckBox skipCiEnableCheckBox;
//    private JComboBox<String> skipCiComboBox;
//    private JCheckBox skipCiDefaultApproveCheckedBox;

    //********************* hidden *********************//
    private JCheckBox typeCheckBox;
    private JCheckBox platformCheckBox;
    private JCheckBox changeIdCheckBox;
    private JCheckBox businessCheckBox;
    private JCheckBox bodyCheckBox;
    //    private JCheckBox changesCheckBox;
    //    private JCheckBox closedCheckBox;
    //    private JCheckBox skipCiCheckBox;
    private JLabel typeDiskPlayStyleLabel;
    private JLabel typeDisplayNumberLabel;
    private JLabel platformDisplayNumberLabel;
    private JLabel platformDiskPlayStyleLabel;
//    private JLabel skipCiDefaultValueLabel;
//    private JLabel skipEnableComboboxLabel;


    public CentralSettingPanel(GitCommitMessageHelperSettings settings) {
        //Get setting
        this.settings = settings.clone();
        // Init  description
        typePanel.setBorder(IdeBorderFactory.createTitledBorder(PluginBundle.get("setting.central.type.panel.title"), true));
        platformPanel.setBorder(IdeBorderFactory.createTitledBorder(PluginBundle.get("setting.central.platform.panel.title"), true));
        hiddenPanel.setBorder(IdeBorderFactory.createTitledBorder(PluginBundle.get("setting.central.hidden.panel.title"), true));

        // todo is need or not
//        ButtonGroup buttonGroup = new ButtonGroup();
//        buttonGroup.add(typeComboboxRadioButton);
//        buttonGroup.add(typeRadioRadioButton);
//        buttonGroup.add(typeMixingRadioButton);
        typeDiskPlayStyleLabel.setText(PluginBundle.get("setting.central.type.style"));
        typeDisplayNumberLabel.setText(PluginBundle.get("setting.central.type.number"));
        typeDisplayNumberSpinner.setToolTipText(PluginBundle.get("setting.central.type.number.tooltip"));
        typeComboboxRadioButton.setText(PluginBundle.get("setting.central.type.combobox.button"));
        typeRadioRadioButton.setText(PluginBundle.get("setting.central.type.radio.button"));
        typeMixingRadioButton.setText(PluginBundle.get("setting.central.type.mixing.button"));
        platformDiskPlayStyleLabel.setText(PluginBundle.get("setting.central.platform.style"));
        platformDisplayNumberLabel.setText(PluginBundle.get("setting.central.platform.number"));
        platformDisplayNumberSpinner.setToolTipText(PluginBundle.get("setting.central.platform.number.tooltip"));
        platformComboboxRadioButton.setText(PluginBundle.get("setting.central.platform.combobox.button"));
        platformRadioRadioButton.setText(PluginBundle.get("setting.central.platform.radio.button"));
        platformMixingRadioButton.setText(PluginBundle.get("setting.central.platform.mixing.button"));
        // Init
        typeComboboxRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                typeDisplayNumberSpinner.setEnabled(false);
            }
        });
        typeRadioRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                typeDisplayNumberSpinner.setEnabled(true);
            }
        });
        typeMixingRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                typeDisplayNumberSpinner.setEnabled(true);
            }
        });
        platformComboboxRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                platformDisplayNumberSpinner.setEnabled(false);
            }
        });
        platformRadioRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                platformDisplayNumberSpinner.setEnabled(true);
            }
        });
        platformMixingRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                platformDisplayNumberSpinner.setEnabled(true);
            }
        });
        // Init  Component
        initComponent(settings);

    }

    public GitCommitMessageHelperSettings getSettings() {
        // Type Display Style Option
        int typeNumber = typeDisplayNumberSpinner.getNumber();
        if (typeComboboxRadioButton.isSelected()) {
            settings.getCentralSettings().setTypeDisplayStyle(TypeDisplayStyleEnum.CHECKBOX);
        } else if (typeRadioRadioButton.isSelected()) {
            settings.getCentralSettings().setTypeDisplayStyle(TypeDisplayStyleEnum.RADIO);
        } else if (typeMixingRadioButton.isSelected()) {
            settings.getCentralSettings().setTypeDisplayStyle(TypeDisplayStyleEnum.MIXING);
        }
        settings.getCentralSettings().setTypeDisplayNumber(typeNumber);

        // platform Display Style Option
        int platformNumber = typeDisplayNumberSpinner.getNumber();
        if (typeComboboxRadioButton.isSelected()) {
            settings.getCentralSettings().setPlatformDisplayStyle(PlatformDisplayStyleEnum.CHECKBOX);
        } else if (typeRadioRadioButton.isSelected()) {
            settings.getCentralSettings().setPlatformDisplayStyle(PlatformDisplayStyleEnum.RADIO);
        }  else if (typeMixingRadioButton.isSelected()) {
            settings.getCentralSettings().setPlatformDisplayStyle(PlatformDisplayStyleEnum.MIXING);
        }
        settings.getCentralSettings().setPlatformDisplayNumber(platformNumber);

        // Hidden Option
        // settings.getCentralSettings().getHidden().setSubject(subjectCheckBox.isSelected());
        settings.getCentralSettings().getHidden().setType(typeCheckBox.isSelected());
        settings.getCentralSettings().getHidden().setPlatform(platformCheckBox.isSelected());
        settings.getCentralSettings().getHidden().setChangeId(changeIdCheckBox.isSelected());
        settings.getCentralSettings().getHidden().setBusiness(businessCheckBox.isSelected());
        settings.getCentralSettings().getHidden().setBody(bodyCheckBox.isSelected());
        return settings;
    }


    public void reset(GitCommitMessageHelperSettings settings) {
        this.settings = settings.clone();
        initComponent(settings);
    }

    private void initComponent(GitCommitMessageHelperSettings settings) {
        // Type Display Style Option
        if (settings.getCentralSettings().getTypeDisplayStyle().equals(TypeDisplayStyleEnum.CHECKBOX)) {
            typeComboboxRadioButton.setSelected(true);
        } else if (settings.getCentralSettings().getTypeDisplayStyle().equals(TypeDisplayStyleEnum.RADIO)) {
            typeRadioRadioButton.setSelected(true);
        } else if (settings.getCentralSettings().getTypeDisplayStyle().equals(TypeDisplayStyleEnum.MIXING)) {
            typeMixingRadioButton.setSelected(true);
        } else {
            typeComboboxRadioButton.setSelected(true);
        }
        typeDisplayNumberSpinner.setNumber(settings.getCentralSettings().getTypeDisplayNumber());

        // platform Display Style Option
        if (settings.getCentralSettings().getPlatformDisplayStyle().equals(PlatformDisplayStyleEnum.CHECKBOX)) {
            platformComboboxRadioButton.setSelected(true);
        } else if (settings.getCentralSettings().getPlatformDisplayStyle().equals(PlatformDisplayStyleEnum.RADIO)) {
            platformRadioRadioButton.setSelected(true);
        } else if (settings.getCentralSettings().getPlatformDisplayStyle().equals(PlatformDisplayStyleEnum.MIXING)) {
            platformMixingRadioButton.setSelected(true);
        }
        platformDisplayNumberSpinner.setNumber(settings.getCentralSettings().getPlatformDisplayNumber());
        // Hidden Option
        typeCheckBox.setSelected(settings.getCentralSettings().getHidden().getType());
        platformCheckBox.setSelected(settings.getCentralSettings().getHidden().getPlatform());
        changeIdCheckBox.setSelected(settings.getCentralSettings().getHidden().getChangeId());
        businessCheckBox.setSelected(settings.getCentralSettings().getHidden().getBusiness());
        bodyCheckBox.setSelected(settings.getCentralSettings().getHidden().getBody());
    }


    public boolean isModified(GitCommitMessageHelperSettings data) {
        boolean isModified = false;
        // Type Display Style Option
        if (typeComboboxRadioButton.isSelected() != data.getCentralSettings().getTypeDisplayStyle()
                .equals(TypeDisplayStyleEnum.CHECKBOX)) {
            isModified = true;
        } else if (typeRadioRadioButton.isSelected() != data.getCentralSettings().getTypeDisplayStyle()
                .equals(TypeDisplayStyleEnum.RADIO)) {
            isModified = true;
        } else if (typeMixingRadioButton.isSelected() != data.getCentralSettings().getTypeDisplayStyle()
                .equals(TypeDisplayStyleEnum.MIXING)) {
            isModified = true;
        } else if (typeDisplayNumberSpinner.getNumber() != data.getCentralSettings().getTypeDisplayNumber()) {
            isModified = true;
        }

        // platform Display Style Option
        else if (platformComboboxRadioButton.isSelected()!= data.getCentralSettings().getPlatformDisplayStyle()
               .equals(PlatformDisplayStyleEnum.CHECKBOX)) {
            isModified = true;
        } else if (platformRadioRadioButton.isSelected()!= data.getCentralSettings().getPlatformDisplayStyle()
               .equals(PlatformDisplayStyleEnum.RADIO)) {
            isModified = true;
        } else if (platformMixingRadioButton.isSelected()!= data.getCentralSettings().getPlatformDisplayStyle()
              .equals(PlatformDisplayStyleEnum.MIXING)) {
            isModified = true;
        } else if (platformDisplayNumberSpinner.getNumber()!= data.getCentralSettings().getPlatformDisplayNumber()) {
            isModified = true;
        }

        // Hidden Option
        else if (typeCheckBox.isSelected() != data.getCentralSettings().getHidden().getType()) {
            isModified = true;
        } else if (platformCheckBox.isSelected()!= data.getCentralSettings().getHidden().getPlatform()) {
            isModified = true;
        } else if (changeIdCheckBox.isSelected() != data.getCentralSettings().getHidden().getChangeId()) {
            isModified = true;
        } else if (businessCheckBox.isSelected()!= data.getCentralSettings().getHidden().getBusiness()) {
            isModified = true;
        } else if (bodyCheckBox.isSelected() != data.getCentralSettings().getHidden().getBody()) {
            isModified = true;
        }
        return isModified;
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }


    private void createUIComponents() {
        typeDisplayNumberSpinner = new JBIntSpinner(1, -1, 999);
        platformDisplayNumberSpinner = new JBIntSpinner(1, -1, 999);
    }
}
