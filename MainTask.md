NotesServiceImpl.java - направление в сторону унификаций исключений и ответов от репозитория - доделать

сделать TagsServiceImpl.java , TagsServiceInterface.java - и там точно также унифицировать исключения

выбрать стиль ошибок и провести его через репо

(кастомные ошибки типа NoteNotFoundException, DuplicateNoteException) лучше делать их наследниками RuntimeException и не писать throws в интерфейсе.

по слоям:
- `NotesServiceImpl` / `TagsServiceImpl` — это не ViewModel, это application/service слой.
- ViewModel (или контроллер UI) вызывает сервис.
- Сервис кидает исключение (`NoteNotFoundException` и т.п.).
- ViewModel ловит и превращает в реакцию UI (сообщение, состояние, диалог, подсветка).

Схема:
1. `View` (JFrame)
2. `ViewModel / Controller`
3. `NotesServiceInterface` / `TagsServiceInterface`
4. `Repository`

Почему `throws` не нужен в интерфейсах:
- потому что это `RuntimeException` (unchecked)
- они и так “пролетают” вверх
- интерфейс не обязан их объявлять

Но важно:
- исключения должны быть осмысленные и стабильные (`NoteNotFoundException`, `DuplicateNoteException`)
- ViewModel должен ловить их в одном месте, а не размазывать try/catch по UI-кнопкам бесконтрольно

`NotesServiceImpl` - не ViewModel, это model.