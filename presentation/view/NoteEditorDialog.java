package presentation.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import presentation.viewmodel.NotesViewModel;

public class NoteEditorDialog extends JDialog {
    private final NotesViewModel viewModel;

    private final JButton backButton = new JButton("Back");
    private final JButton saveButton = new JButton("Save");
    private final JButton deleteButton = new JButton("Delete");
    private final JLabel headerLabel = new JLabel("Note");
    private final JLabel messageLabel = new JLabel(" ");

    private final JTextField titleField = new JTextField();
    private final JTextField tagsField = new JTextField();
    private final JTextArea contentArea = new JTextArea();

    private boolean updatingView;

    public NoteEditorDialog(Frame owner, NotesViewModel viewModel) {
        super(owner, "Note", true);
        this.viewModel = viewModel;

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(700, 500));
        setSize(850, 620);
        setLocationRelativeTo(owner);

        buildUi();
        wireListeners();
        refreshFromViewModel();
    }

    private void buildUi() {
        setLayout(new BorderLayout(8, 8));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel topBar = new JPanel(new BorderLayout(8, 0));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.add(backButton);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.add(saveButton);
        right.add(deleteButton);

        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topBar.add(left, BorderLayout.WEST);
        topBar.add(headerLabel, BorderLayout.CENTER);
        topBar.add(right, BorderLayout.EAST);

        titleField.setBorder(BorderFactory.createTitledBorder("Title"));
        tagsField.setBorder(BorderFactory.createTitledBorder("Tags (comma separated)"));

        JPanel topFields = new JPanel(new BorderLayout(0, 6));
        topFields.add(titleField, BorderLayout.NORTH);
        topFields.add(tagsField, BorderLayout.CENTER);

        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setBorder(BorderFactory.createTitledBorder("Content"));

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.add(topFields, BorderLayout.NORTH);
        center.add(contentScroll, BorderLayout.CENTER);

        messageLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        add(topBar, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);
    }

    private void wireListeners() {
        backButton.addActionListener(e -> tryCloseWithBack());

        saveButton.addActionListener(e -> {
            syncDraftToViewModel();
            viewModel.save();
            refreshFromViewModel();
        });

        deleteButton.addActionListener(e -> {
            Integer id = viewModel.getSelectedNoteId();
            if (id == null) {
                return;
            }
            viewModel.deleteNote(id);
            refreshFromViewModel();
            if (viewModel.getErrorMessage() == null && viewModel.getMode() == NotesViewModel.Mode.LIST) {
                dispose();
            }
        });

        DocumentListener draftListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onDraftChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onDraftChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onDraftChanged();
            }
        };

        titleField.getDocument().addDocumentListener(draftListener);
        tagsField.getDocument().addDocumentListener(draftListener);
        contentArea.getDocument().addDocumentListener(draftListener);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                tryCloseWithBack();
            }
        });
    }

    private void onDraftChanged() {
        if (updatingView) {
            return;
        }
        syncDraftToViewModel();
        refreshHeaderAndButtons();
    }

    private void tryCloseWithBack() {
        syncDraftToViewModel();
        viewModel.back();
        refreshFromViewModel();

        if (viewModel.getMode() == NotesViewModel.Mode.LIST) {
            dispose();
        }
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
            titleField.setText(viewModel.getDraftTitle());
            tagsField.setText(String.join(", ", viewModel.getDraftTags()));
            contentArea.setText(viewModel.getDraftContent());
            contentArea.setCaretPosition(0);

            refreshHeaderAndButtons();
            refreshMessage();
        } finally {
            updatingView = false;
        }
    }

    private void refreshHeaderAndButtons() {
        String idText = viewModel.getSelectedNoteId() == null ? "new" : String.valueOf(viewModel.getSelectedNoteId());
        String dirtyMark = viewModel.isDirty() ? " *" : "";
        headerLabel.setText("Note #" + idText + dirtyMark);

        deleteButton.setEnabled(viewModel.getSelectedNoteId() != null);
        saveButton.setEnabled(viewModel.getMode() == NotesViewModel.Mode.DETAIL);
    }

    private void refreshMessage() {
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
}
