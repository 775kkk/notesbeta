package application.ports.in.dto;

import java.util.List;

public record CreateNoteDto(String title, String content, List<String> tags) {//мб не лист строк у тегов а лист дто тегов
    
}
