package presentation.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import application.ports.in.dto.NoteDto;
import presentation.viewmodel.NotesViewModel;

public class NotesFrame extends JFrame {
    private final NotesViewModel viewModel;

    private final DefaultListModel<NoteDto> listModel = new DefaultListModel<>();
    private final JList<NoteDto> notesList = new JList<>(listModel);

    private final JButton refreshButton = new JButton("Refresh");
    private final JButton newButton = new JButton("New");
    private final JButton openButton = new JButton("Open");
    private final JButton deleteButton = new JButton("Delete");

    private final JTextField titleFilterField = new JTextField();
    private final JComboBox<String> categoryCombo = new JComboBox<>();
    private final JLabel messageLabel = new JLabel(" ");

    private boolean updatingView;

    public NotesFrame(NotesViewModel viewModel) {
        this.viewModel = viewModel;

        setTitle("Notes - List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(700, 500));
        setSize(900, 650);
        setLocationRelativeTo(null);

        buildUi();
        wireListeners();

        this.viewModel.loadNotes();
        refreshFromViewModel();
    }

    private void buildUi() {
        setLayout(new BorderLayout(8, 8));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel top = new JPanel(new BorderLayout(0, 8));

        JLabel title = new JLabel("Notes");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        top.add(title, BorderLayout.NORTH);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.add(refreshButton);
        actions.add(newButton);
        actions.add(openButton);
        actions.add(deleteButton);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleFilterField.setColumns(20);
        titleFilterField.setBorder(BorderFactory.createTitledBorder("Title filter"));

        categoryCombo.setPreferredSize(new Dimension(180, 48));
        categoryCombo.setBorder(BorderFactory.createTitledBorder("Category"));

        filters.add(titleFilterField);
        filters.add(categoryCombo);

        JPanel controls = new JPanel(new BorderLayout(0, 6));
        controls.add(actions, BorderLayout.NORTH);
        controls.add(filters, BorderLayout.CENTER);
        top.add(controls, BorderLayout.CENTER);

        notesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel c = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof NoteDto note) {
                    String titleText = note.title() == null || note.title().isBlank() ? "(untitled)" : note.title();
                    String preview = note.content() == null ? "" : note.content().replace('\n', ' ');
                    if (preview.length() > 60) {
                        preview = preview.substring(0, 60) + "...";
                    }
                    String tags = (note.tags() == null || note.tags().isEmpty()) ? "" : "  [" + String.join(", ", note.tags()) + "]";
                    c.setText("#" + note.noteId() + "  " + titleText + (preview.isBlank() ? "" : " | " + preview) + tags);
                }
                return c;
            }
        });

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(notesList), BorderLayout.CENTER);

        messageLabel.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        add(messageLabel, BorderLayout.SOUTH);
    }

    private void wireListeners() {
        refreshButton.addActionListener(e -> {
            viewModel.loadNotes();
            refreshFromViewModel();
        });

        newButton.addActionListener(e -> {
            viewModel.startCreateNote();
            refreshFromViewModel();
            openEditorDialog();
        });

        openButton.addActionListener(e -> openSelectedNote());

        deleteButton.addActionListener(e -> {
            NoteDto selected = notesList.getSelectedValue();
            if (selected == null) {
                return;
            }
            viewModel.deleteNote(selected.noteId());
            refreshFromViewModel();
        });

        notesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openSelectedNote();
                }
            }
        });

        notesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                refreshButtonsAndMessages();
            }
        });

        titleFilterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onFilterChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onFilterChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onFilterChanged();
            }
        });

        categoryCombo.addActionListener(e -> {
            if (updatingView) {
                return;
            }
            Object selected = categoryCombo.getSelectedItem();
            viewModel.changeCategoryFilter(selected == null ? null : selected.toString());
            refreshFromViewModel();
        });
    }

    private void onFilterChanged() {
        if (updatingView) {
            return;
        }
        viewModel.changeTitleFilter(titleFilterField.getText());
        updatingView = true;
        try {
            refreshList();
            refreshButtonsAndMessages();
        } finally {
            updatingView = false;
        }
    }

    private void openSelectedNote() {
        NoteDto selected = notesList.getSelectedValue();
        if (selected == null) {
            return;
        }
        viewModel.openNote(selected.noteId());
        refreshFromViewModel();

        if (viewModel.getErrorMessage() == null) {
            openEditorDialog();
        }
    }

    private void openEditorDialog() {
        NoteEditorDialog dialog = new NoteEditorDialog(this, viewModel);
        dialog.setVisible(true);
        refreshFromViewModel();
    }

    private void refreshFromViewModel() {
        updatingView = true;
        try {
            refreshFilters();
            refreshList();
            refreshButtonsAndMessages();
        } finally {
            updatingView = false;
        }
    }

    private void refreshFilters() {
        String vmTitleFilter = viewModel.getTitleFilter();
        if (!titleFilterField.getText().equals(vmTitleFilter)) {
            titleFilterField.setText(vmTitleFilter);
        }

        List<String> categories = viewModel.getAvailableCategories();
        String selectedCategory = viewModel.getCategoryFilter();
        categoryCombo.setModel(new DefaultComboBoxModel<>(categories.toArray(String[]::new)));
        categoryCombo.setSelectedItem(selectedCategory);
    }

    private void refreshList() {
        Integer currentSelection = notesList.getSelectedValue() == null ? null : notesList.getSelectedValue().noteId();
        List<NoteDto> visibleNotes = viewModel.getFilteredNotesList();

        listModel.clear();
        for (NoteDto note : visibleNotes) {
            listModel.addElement(note);
        }

        if (currentSelection != null) {
            for (int i = 0; i < listModel.size(); i++) {
                if (listModel.get(i).noteId() == currentSelection) {
                    notesList.setSelectedIndex(i);
                    notesList.ensureIndexIsVisible(i);
                    break;
                }
            }
        }
    }

    private void refreshButtonsAndMessages() {
        boolean hasSelection = notesList.getSelectedValue() != null;
        openButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);

        if (viewModel.getErrorMessage() != null && !viewModel.getErrorMessage().isBlank()) {
            messageLabel.setForeground(new Color(180, 30, 30));
            messageLabel.setText(viewModel.getErrorMessage());
            return;
        }

        if (viewModel.getStatusMessage() != null && !viewModel.getStatusMessage().isBlank()) {
            messageLabel.setForeground(new Color(20, 110, 40));
            messageLabel.setText(viewModel.getStatusMessage());
            return;
        }

        messageLabel.setForeground(Color.DARK_GRAY);
        messageLabel.setText(" ");
    }
}
