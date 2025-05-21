package com.fulinlin.ui.central;

import com.fulinlin.localization.PluginBundle;
import com.fulinlin.model.enums.PlatformDisplayStyleEnum;
import com.fulinlin.model.enums.TypeDisplayStyleEnum;
import com.fulinlin.storage.GitCommitMessageHelperSettings;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.JBIntSpinner;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class CentralSettingPanel {
    protected GitCommitMessageHelperSettings settings;
    private JPanel mainPanel;
    private JPanel typePanel;
    private JPanel platformPanel;
    private JPanel businessPanel;
    private JPanel hiddenPanel;

    private JRadioButton typeComboboxRadioButton;
    private JRadioButton typeRadioRadioButton;
    private JRadioButton typeMixingRadioButton;
    private JBIntSpinner typeDisplayNumberSpinner;

    private JRadioButton platformComboboxRadioButton;
    private JRadioButton platformRadioRadioButton;
    private JRadioButton platformMixingRadioButton;
    private JBIntSpinner platformDisplayNumberSpinner;

    private JTextField defaultBusiness;

    //********************* hidden *********************//
    private JCheckBox typeCheckBox;
    private JCheckBox platformCheckBox;
    private JCheckBox taskIdCheckBox;
    private JCheckBox businessCheckBox;
    private JCheckBox bodyCheckBox;

    private JLabel typeDiskPlayStyleLabel;
    private JLabel typeDisplayNumberLabel;
    private JLabel platformDisplayNumberLabel;
    private JLabel platformDiskPlayStyleLabel;


    public CentralSettingPanel(GitCommitMessageHelperSettings settings) {
        //Get setting
        this.settings = settings.clone();
        // Init  description
        typePanel.setBorder(IdeBorderFactory.createTitledBorder(PluginBundle.get("setting.central.type.panel.title"), true));
        platformPanel.setBorder(IdeBorderFactory.createTitledBorder(PluginBundle.get("setting.central.platform.panel.title"), true));
        businessPanel.setBorder(IdeBorderFactory.createTitledBorder(PluginBundle.get("setting.central.business.panel.title"), true));
        hiddenPanel.setBorder(IdeBorderFactory.createTitledBorder(PluginBundle.get("setting.central.hidden.panel.title"), true));

        typeDiskPlayStyleLabel.setText(PluginBundle.get("setting.central.type.style"));
        typeDisplayNumberLabel.setVisible(false);
        typeDisplayNumberLabel.setText(PluginBundle.get("setting.central.type.number"));
        typeDisplayNumberSpinner.setVisible(false);
        typeDisplayNumberSpinner.setToolTipText(PluginBundle.get("setting.central.type.number.tooltip"));
        typeComboboxRadioButton.setText(PluginBundle.get("setting.central.type.combobox.button"));
        typeRadioRadioButton.setText(PluginBundle.get("setting.central.type.radio.button"));
        typeMixingRadioButton.setText(PluginBundle.get("setting.central.type.mixing.button"));

        platformDiskPlayStyleLabel.setText(PluginBundle.get("setting.central.platform.style"));
        platformDisplayNumberLabel.setVisible(false);
        platformDisplayNumberLabel.setText(PluginBundle.get("setting.central.platform.number"));
        platformDisplayNumberSpinner.setVisible(false);
        platformDisplayNumberSpinner.setToolTipText(PluginBundle.get("setting.central.platform.number.tooltip"));
        platformComboboxRadioButton.setText(PluginBundle.get("setting.central.platform.combobox.button"));
        platformRadioRadioButton.setText(PluginBundle.get("setting.central.platform.radio.button"));
        platformMixingRadioButton.setText(PluginBundle.get("setting.central.platform.mixing.button"));

        typeCheckBox.setText(PluginBundle.get("commit.panel.type.field").replaceAll(":", ""));
        platformCheckBox.setText(PluginBundle.get("commit.panel.platform.field").replaceAll(":", ""));
        taskIdCheckBox.setText(PluginBundle.get("commit.panel.taskid.field").replaceAll(":", ""));
        businessCheckBox.setText(PluginBundle.get("commit.panel.business.field").replaceAll(":", ""));
        bodyCheckBox.setText(PluginBundle.get("commit.panel.body.field").replaceAll(":", ""));

        // Init
        typeComboboxRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                typeDisplayNumberSpinner.setEnabled(false);
                typeRadioRadioButton.setSelected(false);
                typeMixingRadioButton.setSelected(false);
            }
        });
        typeRadioRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                typeDisplayNumberSpinner.setEnabled(true);
                typeComboboxRadioButton.setSelected(false);
                typeMixingRadioButton.setSelected(false);
            }
        });
        typeMixingRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                typeDisplayNumberSpinner.setEnabled(true);
                typeComboboxRadioButton.setSelected(false);
                typeRadioRadioButton.setSelected(false);
            }
        });
        platformComboboxRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                platformDisplayNumberSpinner.setEnabled(false);
                platformRadioRadioButton.setSelected(false);
                platformMixingRadioButton.setSelected(false);
            }
        });
        platformRadioRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                platformDisplayNumberSpinner.setEnabled(true);
                platformComboboxRadioButton.setSelected(false);
                platformMixingRadioButton.setSelected(false);
            }
        });
        platformMixingRadioButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                platformDisplayNumberSpinner.setEnabled(true);
                platformComboboxRadioButton.setSelected(false);
                platformRadioRadioButton.setSelected(false);
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
        if (platformComboboxRadioButton.isSelected()) {
            settings.getCentralSettings().setPlatformDisplayStyle(PlatformDisplayStyleEnum.CHECKBOX);
        } else if (platformRadioRadioButton.isSelected()) {
            settings.getCentralSettings().setPlatformDisplayStyle(PlatformDisplayStyleEnum.RADIO);
        }  else if (platformMixingRadioButton.isSelected()) {
            settings.getCentralSettings().setPlatformDisplayStyle(PlatformDisplayStyleEnum.MIXING);
        }
        settings.getCentralSettings().setPlatformDisplayNumber(platformNumber);

        // Default Business Option
        settings.getCentralSettings().setDefaultBusiness(defaultBusiness.getText());

        // Hidden Option
        settings.getCentralSettings().getHidden().setType(typeCheckBox.isSelected());
        settings.getCentralSettings().getHidden().setPlatform(platformCheckBox.isSelected());
        settings.getCentralSettings().getHidden().setTaskId(taskIdCheckBox.isSelected());
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
//        platformDisplayNumberSpinner.setNumber(settings.getCentralSettings().getPlatformDisplayNumber());

        // Default Business Option
        defaultBusiness.setText(settings.getCentralSettings().getDefaultBusiness());

        // Hidden Option
        typeCheckBox.setSelected(settings.getCentralSettings().getHidden().getType());
        platformCheckBox.setSelected(settings.getCentralSettings().getHidden().getPlatform());
        taskIdCheckBox.setSelected(settings.getCentralSettings().getHidden().getTaskId());
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

        // Default Business Option
        else if (!StringUtil.equals(defaultBusiness.getText(), data.getCentralSettings().getDefaultBusiness())) {
            isModified = true;
        }

        // Hidden Option
        else if (typeCheckBox.isSelected() != data.getCentralSettings().getHidden().getType()) {
            isModified = true;
        } else if (platformCheckBox.isSelected()!= data.getCentralSettings().getHidden().getPlatform()) {
            isModified = true;
        } else if (taskIdCheckBox.isSelected() != data.getCentralSettings().getHidden().getTaskId()) {
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
