package application.ports.in.dto;

import java.util.List;

public record NoteDto(int noteId, String title, String content, List<String> tags) {
    
}
