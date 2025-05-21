package com.fulinlin.ui.setting;

import com.fulinlin.localization.PluginBundle;
import com.fulinlin.model.Alias;
import com.fulinlin.storage.GitCommitMessageHelperSettings;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.JBColor;
import com.intellij.ui.table.JBTable;
import org.apache.tools.ant.taskdefs.Get;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: git-commit-message-helper
 * @author: fulin
 * @create: 2019-12-06 21:21
 **/
public class AliasTable extends JBTable {

    private static final Logger log = Logger.getInstance(AliasTable.class);
    private static final int NAME_COLUMN = 0;
    private static final int VALUE_COLUMN = 1;
    private final MyTableModel myTableModel = new MyTableModel();


    private final List<Alias> aliases = new LinkedList<>();

    public List<Alias> getAliases() {
        return aliases;
    }

    /**
     * instantiation AliasTable
     */
    public AliasTable() {
        setModel(myTableModel);
        TableColumn column = getColumnModel().getColumn(NAME_COLUMN);
        TableColumn valueColumn = getColumnModel().getColumn(VALUE_COLUMN);
        column.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                final String macroValue = getAliasValueAt(row);
                component.setForeground(macroValue.isEmpty()
                        ? JBColor.RED
                        : isSelected ? table.getSelectionForeground() : table.getForeground());
                return component;
            }
        });
        setColumnSize(column, 150, 250, 150);
        setColumnSize(valueColumn, 550, 750, 550);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }


    /**
     * Set  Something  ColumnSize
     */
    public static void setColumnSize(TableColumn column, int preferredWidth, int maxWidth, int minWidth) {
        column.setPreferredWidth(preferredWidth);
        column.setMaxWidth(maxWidth);
        column.setMinWidth(minWidth);
    }


    public String getAliasValueAt(int row) {
        return (String) getValueAt(row, VALUE_COLUMN);
    }


    public void addAlias() {
        final AliasEditor macroEditor = new AliasEditor(PluginBundle.get("setting.alias.add.title"), "", "");
        if (macroEditor.showAndGet()) {
            final String name = macroEditor.getTitle();
            aliases.add(new Alias(macroEditor.getTitle(), macroEditor.getDescription()));
            final int index = indexOfAliasWithName(name);
            log.assertTrue(index >= 0);
            myTableModel.fireTableDataChanged();
            setRowSelectionInterval(index, index);
        }
    }

    private boolean isValidRow(int selectedRow) {
        return selectedRow >= 0 && selectedRow < aliases.size();
    }

    public void moveUp() {
        int selectedRow = getSelectedRow();
        int index1 = selectedRow - 1;
        if (selectedRow != -1) {
            Collections.swap(aliases, selectedRow, index1);
        }
        setRowSelectionInterval(index1, index1);
    }


    public void moveDown() {
        int selectedRow = getSelectedRow();
        int index1 = selectedRow + 1;
        if (selectedRow != -1) {
            Collections.swap(aliases, selectedRow, index1);
        }
        setRowSelectionInterval(index1, index1);
    }


    public void removeSelectedAliases() {
        final int[] selectedRows = getSelectedRows();
        if (selectedRows.length == 0) return;
        Arrays.sort(selectedRows);
        final int originalRow = selectedRows[0];
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            final int selectedRow = selectedRows[i];
            if (isValidRow(selectedRow)) {
                aliases.remove(selectedRow);
            }
        }
        myTableModel.fireTableDataChanged();
        if (originalRow < getRowCount()) {
            setRowSelectionInterval(originalRow, originalRow);
        } else if (getRowCount() > 0) {
            final int index = getRowCount() - 1;
            setRowSelectionInterval(index, index);
        }
    }


    public void commit(GitCommitMessageHelperSettings settings) {

    }

    public void resetDefaultAliases() {
        myTableModel.fireTableDataChanged();
    }

    public void reset(GitCommitMessageHelperSettings settings) {
        obtainAliases(aliases, settings);
        myTableModel.fireTableDataChanged();
    }


    private int indexOfAliasWithName(String name) {
        for (int i = 0; i < aliases.size(); i++) {
            final Alias alias = aliases.get(i);
            if (name.equals(alias.getTitle())) {
                return i;
            }
        }
        return -1;
    }

    private void obtainAliases(@NotNull List<Alias> aliases, GitCommitMessageHelperSettings settings) {
        aliases.clear();
        aliases.addAll(getRecordAliases(settings));
    }

    public List<Alias> getRecordAliases(GitCommitMessageHelperSettings settings) {
        return null;
    }

    public boolean editAlias() {
        if (getSelectedRowCount() != 1) {
            return false;
        }
        final int selectedRow = getSelectedRow();
        final Alias alias = aliases.get(selectedRow);
        final AliasEditor editor = new AliasEditor(PluginBundle.get("setting.alias.edit.title"), alias.getTitle(), alias.getDescription());
        if (editor.showAndGet()) {
            alias.setTitle(editor.getTitle());
            alias.setDescription(editor.getDescription());
            myTableModel.fireTableDataChanged();
        }
        return true;
    }

    public boolean isModified(GitCommitMessageHelperSettings settings) {
        final List<Alias> aliases = new LinkedList<>();
        obtainAliases(aliases, settings);
        return !aliases.equals(this.aliases);
    }

    //==========================================================================//

    /**
     * MyTableModel
     */
    private class MyTableModel extends AbstractTableModel {
        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public int getRowCount() {
            return aliases.size();
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            final Alias pair = aliases.get(rowIndex);
            switch (columnIndex) {
                case NAME_COLUMN:
                    return pair.getTitle();
                case VALUE_COLUMN:
                    return pair.getDescription();
            }
            log.error("Wrong indices");
            return null;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex) {
                case NAME_COLUMN:
                    return PluginBundle.get("setting.alias.field.title");
                case VALUE_COLUMN:
                    return PluginBundle.get("setting.alias.field.description");
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
}
