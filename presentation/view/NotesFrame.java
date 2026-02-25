package presentation.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import application.ports.in.dto.NoteDto;
import presentation.viewmodel.NotesViewModel;

public class NotesFrame extends JFrame {
    private static final String CARD_LIST = "LIST";
    private static final String CARD_DETAIL = "DETAIL";

    private final NotesViewModel viewModel;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final JLabel messageLabel = new JLabel(" ");

    // LIST card
    private final DefaultListModel<NoteDto> listModel = new DefaultListModel<>();
    private final JList<NoteDto> notesList = new JList<>(listModel);
    private final JButton refreshButton = new JButton("Refresh");
    private final JButton newButton = new JButton("New");
    private final JButton openButton = new JButton("Open");
    private final JButton deleteFromListButton = new JButton("Delete");

    // DETAIL card
    private final JButton backButton = new JButton("Back");
    private final JButton saveButton = new JButton("Save");
    private final JButton deleteFromDetailButton = new JButton("Delete");
    private final JLabel detailHeaderLabel = new JLabel("Note");
    private final JTextField titleField = new JTextField();
    private final JTextField tagsField = new JTextField();
    private final JTextArea contentArea = new JTextArea();

    private boolean updatingView;

    public NotesFrame(NotesViewModel viewModel) {
        this.viewModel = viewModel;

        setTitle("Notes (MVP)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));
        setSize(1000, 700);
        setLocationRelativeTo(null);

        buildUi();
        wireListeners();

        this.viewModel.loadNotes();
        refreshFromViewModel();
    }

    private void buildUi() {
        setLayout(new BorderLayout(8, 8));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        cardPanel.add(buildListCard(), CARD_LIST);
        cardPanel.add(buildDetailCard(), CARD_DETAIL);
        add(cardPanel, BorderLayout.CENTER);

        messageLabel.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        add(messageLabel, BorderLayout.SOUTH);
    }

    private JPanel buildListCard() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topBar.add(refreshButton);
        topBar.add(newButton);
        topBar.add(openButton);
        topBar.add(deleteFromListButton);

        JLabel title = new JLabel("Notes");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        JPanel north = new JPanel(new BorderLayout(0, 6));
        north.add(title, BorderLayout.NORTH);
        north.add(topBar, BorderLayout.CENTER);

        notesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel c = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof NoteDto note) {
                    String titleText = note.title() == null || note.title().isBlank() ? "(untitled)" : note.title();
                    String preview = note.content() == null ? "" : note.content().replace('\n', ' ');
                    if (preview.length() > 40) {
                        preview = preview.substring(0, 40) + "...";
                    }
                    c.setText("#" + note.noteId() + "  " + titleText + (preview.isBlank() ? "" : " | " + preview));
                }
                return c;
            }
        });

        panel.add(north, BorderLayout.NORTH);
        panel.add(new JScrollPane(notesList), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDetailCard() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel topBar = new JPanel(new BorderLayout(8, 0));
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftButtons.add(backButton);

        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightButtons.add(saveButton);
        rightButtons.add(deleteFromDetailButton);

        topBar.add(leftButtons, BorderLayout.WEST);
        topBar.add(detailHeaderLabel, BorderLayout.CENTER);
        topBar.add(rightButtons, BorderLayout.EAST);

        JPanel form = new JPanel(new BorderLayout(8, 8));
        JPanel fields = new JPanel(new BorderLayout(0, 6));

        titleField.setBorder(BorderFactory.createTitledBorder("Title"));
        tagsField.setBorder(BorderFactory.createTitledBorder("Tags (comma separated)"));

        fields.add(titleField, BorderLayout.NORTH);
        fields.add(tagsField, BorderLayout.CENTER);

        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setBorder(BorderFactory.createTitledBorder("Content"));

        form.add(fields, BorderLayout.NORTH);
        form.add(contentScroll, BorderLayout.CENTER);

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private void wireListeners() {
        refreshButton.addActionListener(e -> {
            viewModel.loadNotes();
            refreshFromViewModel();
        });

        newButton.addActionListener(e -> {
            viewModel.startCreateNote();
            refreshFromViewModel();
        });

        openButton.addActionListener(e -> openSelectedNote());

        deleteFromListButton.addActionListener(e -> {
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
                refreshButtonsOnly();
            }
        });

        backButton.addActionListener(e -> {
            syncDraftToViewModel();
            viewModel.back();
            refreshFromViewModel();
        });

        saveButton.addActionListener(e -> {
            syncDraftToViewModel();
            viewModel.save();
            refreshFromViewModel();
        });

        deleteFromDetailButton.addActionListener(e -> {
            Integer id = viewModel.getSelectedNoteId();
            if (id == null) {
                return;
            }
            viewModel.deleteNote(id);
            refreshFromViewModel();
        });

        attachDraftFieldListeners();
    }

    private void attachDraftFieldListeners() {
        DocumentListener listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onDraftFieldChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onDraftFieldChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onDraftFieldChanged();
            }
        };

        titleField.getDocument().addDocumentListener(listener);
        tagsField.getDocument().addDocumentListener(listener);
        contentArea.getDocument().addDocumentListener(listener);
    }

    private void onDraftFieldChanged() {
        if (updatingView) {
            return;
        }

        syncDraftToViewModel();
        refreshButtonsOnly();
        refreshDetailHeaderOnly();
    }

    private void openSelectedNote() {
        NoteDto selected = notesList.getSelectedValue();
        if (selected == null) {
            return;
        }
        viewModel.openNote(selected.noteId());
        refreshFromViewModel();
    }

    private void syncDraftToViewModel() {
        if (viewModel.getMode() != NotesViewModel.Mode.DETAIL) {
            return;
        }
        viewModel.changeTitle(titleField.getText());
        viewModel.changeContent(contentArea.getText());
        viewModel.changeTags(parseTags(tagsField.getText()));
    }

    private void refreshFromViewModel() {
        updatingView = true;
        try {
            refreshListData();

            if (viewModel.getMode() == NotesViewModel.Mode.DETAIL) {
                cardLayout.show(cardPanel, CARD_DETAIL);
                titleField.setText(viewModel.getDraftTitle());
                contentArea.setText(viewModel.getDraftContent());
                tagsField.setText(formatTags(viewModel.getDraftTags()));
                contentArea.setCaretPosition(0);
            } else {
                cardLayout.show(cardPanel, CARD_LIST);
            }

            refreshDetailHeaderOnly();
            refreshMessageBar();
            refreshButtonsOnly();
        } finally {
            updatingView = false;
        }
    }

    private void refreshListData() {
        List<NoteDto> notes = viewModel.getNotesList();
        Integer selectedId = viewModel.getSelectedNoteId();
        NoteDto currentlySelectedInList = notesList.getSelectedValue();
        Integer listSelectedId = currentlySelectedInList == null ? null : currentlySelectedInList.noteId();

        listModel.clear();
        for (NoteDto note : notes) {
            listModel.addElement(note);
        }

        Integer targetId = selectedId != null ? selectedId : listSelectedId;
        if (targetId != null) {
            for (int i = 0; i < listModel.size(); i++) {
                if (listModel.get(i).noteId() == targetId) {
                    notesList.setSelectedIndex(i);
                    notesList.ensureIndexIsVisible(i);
                    break;
                }
            }
        }
    }

    private void refreshButtonsOnly() {
        boolean inDetail = viewModel.getMode() == NotesViewModel.Mode.DETAIL;
        boolean hasSelectedInList = notesList.getSelectedValue() != null;

        openButton.setEnabled(!inDetail && hasSelectedInList);
        deleteFromListButton.setEnabled(!inDetail && hasSelectedInList);

        backButton.setEnabled(inDetail);
        saveButton.setEnabled(inDetail);
        deleteFromDetailButton.setEnabled(inDetail && viewModel.getSelectedNoteId() != null);
    }

    private void refreshDetailHeaderOnly() {
        if (viewModel.getMode() != NotesViewModel.Mode.DETAIL) {
            detailHeaderLabel.setText("Note");
            return;
        }

        String idText = viewModel.getSelectedNoteId() == null ? "new" : String.valueOf(viewModel.getSelectedNoteId());
        String dirtyMark = viewModel.isDirty() ? " *" : "";
        detailHeaderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        detailHeaderLabel.setText("Note #" + idText + dirtyMark);
    }

    private void refreshMessageBar() {
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

    private List<String> parseTags(String text) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return Arrays.stream(text.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private String formatTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        return String.join(", ", tags);
    }
}
