NotesViewModel {
    // загрузить список
    // открыть заметку по id
    // создать новую заметку
    // изменить title
    // изменить content
    // изменить tags
    // сохранить
    // удалить
    // назад

    // помощ друга
    // зависимости:
    // NotesServiceInterface (обязательная)
    // возможно позже mapper/helper для UI, но не repo

    // состояние экрана:
    // режим LIST / DETAIL
    // список заметок для отображения
    // выбранная заметка id
    // draft (черновик открытой/новой заметки)
    // флаг isNewDraft
    // флаг dirty (есть несохраненные изменения)
    // statusMessage
    // errorMessage

    // загрузить список заметок (первичная загрузка / refresh)
    // открыть заметку по id (перейти в DETAIL, заполнить draft)
    // начать создание новой заметки (пустой draft, DETAIL)
    // изменить title (меняем draft, dirty=true)
    // изменить content (меняем draft, dirty=true)
    // изменить tags (меняем draft, dirty=true)
    // сохранить (если isNewDraft -> createNote, иначе updateNote)
    // удалить заметку по id
    // назад (если dirty=true -> автосейв, потом LIST)

    // служебные приватные методы:
    // очистить сообщения (error/status)
    // собрать DTO из draft
    // сбросить состояние DETAIL -> LIST
}