# View ↔ ViewModel (Notes Screen) — события и поведение

Ниже зафиксирован контракт взаимодействия `View` и `NotesViewModel` для экрана заметок.

Базовые договорённости:
- Режимы экрана: `LIST`, `DETAIL`
- В `DETAIL` заметка редактируется сразу (без отдельного edit-mode)
- `dirty = true` означает несохранённые изменения в черновике
- Ошибки сервиса не пробрасываются в `View`: VM ловит `AppException` и заполняет `errorMessage`
- Для заметок VM работает через `NotesServiceInterface`; логика тегов скрыта внутри `NotesServiceImpl`

## Состояние, которое хранит `NotesViewModel`
- `mode` (`LIST | DETAIL`)
- `notesList` (список заметок для левой панели/списка)
- `selectedNoteId` (или `null`)
- `draftNote` (черновик открытой заметки: `title`, `content`, `tags`)
- `isNewDraft` (черновик новой заметки, ещё не сохранён)
- `dirty`
- `statusMessage`
- `errorMessage`

## Таблица событий `View -> NotesViewModel`

| Событие | Что делает ViewModel (VM) | Какой сервис вызывает | Успех | Ошибка |
| --- | --- | --- | --- | --- |
| `loadNotes()` | 1) Очищает `errorMessage/statusMessage` 2) Загружает список заметок 3) Обновляет `notesList` 4) Если открытая заметка уже удалена/недоступна, может сбросить `selectedNoteId/draftNote` | `NotesService.listNotes()` | `notesList` актуален, UI может отрисовать список | Остаётся в текущем режиме, `errorMessage` заполнен, UI не падает |
| `openNote(noteId)` | 1) Очищает сообщения 2) Загружает заметку 3) Заполняет `draftNote` 4) `mode=DETAIL`, `selectedNoteId=noteId`, `isNewDraft=false`, `dirty=false` | `NotesService.getNoteById(noteId)` | Открыта заметка в `DETAIL`, черновик синхронизирован с сохранённым состоянием | Обычно остаётся в `LIST`, `errorMessage` заполнен |
| `startCreateNote()` | 1) Очищает сообщения 2) Создаёт пустой `draftNote` (или с дефолтами) 3) `mode=DETAIL`, `selectedNoteId=null`, `isNewDraft=true`, `dirty=false` | Нет | Переход в экран новой заметки | Ошибок сервиса нет (если нет локальной валидации) |
| `changeTitle(newTitle)` | 1) Проверяет, что есть `draftNote` 2) Обновляет поле `title` в черновике 3) Ставит `dirty=true` | Нет | Черновик изменён, UI видит новое значение | При отсутствии `draftNote` — либо no-op, либо `errorMessage` (на выбор, но единообразно) |
| `changeContent(newContent)` | Аналогично `changeTitle`: обновляет `content` и ставит `dirty=true` | Нет | Черновик изменён | Аналогично `changeTitle` |
| `changeTags(newTags)` | 1) Обновляет теги в `draftNote` (UI-форма тегов) 2) `dirty=true` 3) Техническая нормализация допустима (например trim пустых строк) | Нет | Черновик изменён | Аналогично `changeTitle` |
| `save()` | 1) Очищает сообщения 2) Если `dirty=false`, можно сделать no-op или статус `"Нет изменений"` 3) Собирает DTO из `draftNote` 4) Делает техническую нормализацию ввода 5) Ветвление: `isNewDraft` -> create, иначе update 6) После успеха синхронизирует `draftNote`, `selectedNoteId`, `isNewDraft=false`, `dirty=false` 7) Обновляет `notesList` (локально или через `loadNotes`) | `NotesService.createNote(...)` **или** `NotesService.updateNote(...)` | Изменения сохранены, `dirty=false`, `statusMessage="Saved"` | Остаётся в `DETAIL`, `dirty` сохраняется, `errorMessage` заполнен |
| `back()` | **Ветвление:** 1) если `mode=LIST` -> no-op 2) если `mode=DETAIL` и `dirty=false` -> перейти в `LIST`, очистить `selectedNoteId/draftNote/isNewDraft` 3) если `mode=DETAIL` и `dirty=true` -> выполнить автосейв (логика как в `save`) и только после успеха перейти в `LIST` | Если `dirty=false` — нет. Если `dirty=true` — `createNote(...)` или `updateNote(...)` по правилу `isNewDraft` | Возврат в `LIST`; при автосейве данные не теряются | Если автосейв упал: остаётся `DETAIL`, `dirty=true`, `errorMessage` заполнен |
| `deleteNote(noteId)` | 1) Очищает сообщения 2) Вызывает удаление 3) Убирает заметку из `notesList` (или вызывает `loadNotes`) 4) Если удалена открытая заметка: `mode=LIST`, очистка `selectedNoteId/draftNote/isNewDraft`, `dirty=false` | `NotesService.deleteNote(noteId)` | Заметка удалена, список актуален; если была открыта — переход в `LIST` | Остаётся в текущем режиме, состояние не ломается, `errorMessage` заполнен |
| `renameNoteFromList(noteId, newTitle)` *(если оставляешь отдельный пункт в меню списка)* | 1) Очищает сообщения 2) Загружает заметку (если в списке нет всех данных) или берёт из локального состояния 3) Собирает `NoteDto` с новым `title` 4) Вызывает update 5) Обновляет `notesList` | `NotesService.getNoteById(noteId)` (опц.) + `NotesService.updateNote(...)` | Заголовок в списке обновлён, `statusMessage` заполнен | Остаётся в `LIST`, `errorMessage` заполнен |

## Дополнительные правила (важно для реализации)

### 1. Кто отвечает за теги
- `NotesViewModel` не вызывает `TagRepository` напрямую
- `NotesViewModel` обычно не обязан вызывать `TagsService` для сохранения заметки
- VM передаёт теги как часть DTO заметки, а `NotesServiceImpl` внутри делает `resolveTags(...)`

### 2. Политика очистки сообщений
Рекомендуемое правило:
- в начале каждого пользовательского события очищать `errorMessage` и `statusMessage`
- после успеха заполнять только `statusMessage`
- после ошибки заполнять только `errorMessage`

### 3. Политика `save` без изменений
Нужно выбрать один вариант и зафиксировать:
- `no-op` без сообщений
- `statusMessage = "Нет изменений"`
- кнопка `Save` неактивна при `dirty=false`

Для MVP практичнее:
- `Save` доступна всегда, при `dirty=false` -> no-op или короткий статус

### 4. Политика локальной (UI) валидации
`ViewModel` может делать только простую валидацию/подготовку:
- `trim`
- парсинг
- пустые поля на уровне UX

Бизнес-валидация (duplicate/not found/правила домена) остаётся в `Service`.

### 5. Что можно отложить до после MVP
- отдельный `TagsViewModel` (если нет отдельного экрана тегов)
- подтверждение удаления
- сложное редактирование тегов-плашек
- оптимистичные обновления списка без `loadNotes()`

## Минимальный набор методов VM для старта (без реализации)
- `loadNotes()`
- `openNote(int noteId)`
- `startCreateNote()`
- `changeTitle(String title)`
- `changeContent(String content)`
- `changeTags(List<String> tags)`
- `save()`
- `back()`
- `deleteNote(int noteId)`
