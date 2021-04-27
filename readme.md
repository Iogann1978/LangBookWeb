### Генерация хранилища ключей и сертификата
```shell
keytool -genkey -keyalg RSA -alias server -keystore server.jks -storepass secret -validity 360 -keysize 2048
keytool -export -alias server -file server.crt -keystore server.jks
```
Сертификат server.crt нужно передать клиенту

### Доступ к приложению
[Словарь](https://localhost:8443/dictionary) \
[Добавить слово](https://localhost:8443/word/add) \
[Добавить перевод](https://localhost:8443/translation/add?wordId=1) \
[Добавить пример](https://localhost:8443/example/add?translationId=1) \
[Статьи](https://localhost:8443/articles) \
[Просмотреть слово](https://localhost:8443/word?wordId=1) \
[Карусель](https://localhost:8443/roundrobin)
[Фразы](https://localhost:8443/phrases)
[Тексты](https://localhost:8443/text)

### Полезные ссылки
[Bootstrap 4.0 docs](https://getbootstrap.com/docs/4.0/getting-started/introduction/) \
[Bootstrap 5.0 docs](https://getbootstrap.com/docs/5.0/getting-started/introduction/) \
[Bootstrap 4 Tutorial](https://www.w3schools.com/bootstrap4) \
[Bootstrap icons](https://www.bootstrapicons.com) \
[PDF to Text](https://www.baeldung.com/pdf-conversions-java) \
[XDXF format](https://github.com/soshial/xdxf_makedict/blob/master/format_standard/xdxf_description.md)

### Поведение клиентов при различных перенаправлениях описано в таблице:
| Статус ответа | Кэширование | Если метод не GET или HEAD |
|-----------------------|------------------------|----------------------------|
|`301 Moved Permanently`|Можно как обычно|Спросить у пользователя подтверждения и запросить другой ресурс исходным методом|
|`307 Temporary Redirect`|Можно только если указан заголовок Cache-Control или Expires|Спросить у пользователя подтверждения и запросить другой ресурс исходным методом|
|`302 Found (HTTP/1.1)`|Можно только если указан заголовок Cache-Control или Expires|Спросить у пользователя подтверждения и запросить другой ресурс исходным методом|
|`302 Moved Temporarily (HTTP/1.0)`|Можно только если указан заголовок Cache-Control или Expires|Спросить у пользователя подтверждения и запросить другой ресурс исходным методом|
|`303 See Other`|Нельзя|Перейти автоматически, но уже методом GET|

### ToDo
Сделать кнопку очистки фильтра на странице словаря \
При добавлении слова сделать проверку, чтобы нельзя было добавлять такое же слово с таким же типом и таким же пользователем \
Сделать возможность создавать несколько каруселей для одного пользователя \
Валидация в thymeleaf \
Настроить webjars-locator \
Добавить проверку при загрузки статьи, чтобы размер файла не превышал 100 000 байт

### Таблица тестирования
| Функционал | Результат тестирования |
|------------|------------------------|
| [Словарь](https://localhost:8443/dictionary) | **3(5)** |
| Просмотреть слово | да |
| Добавить слово в карусель | да |
| Повторно добавить слово в карусель | да |
| Поиск | да |
| Паджинация | |
| [Добавить слово](https://localhost:8443/word/add) | **7(7)** |
| Простое слово | да |
| Существительное | да |
| Глагол | да |
| Прилагательное | да |
| Наречие | да |
| Причастие | да |
| Фраза | да |
| [Карусель](https://localhost:8443/roundrobin) | **4(4)** |
| Отображение слова без перевода | да |
| Отображение слова с переводом | да |
| Перейти к слову | да |
| Удалить слово из карусели | да |
| [Фразы](https://localhost:8443/phrases) | **1(2)** |
| Просмотреть | да |
| Паджинация | |
| [Статьи](https://localhost:8443/articles) | **3(6)** |
| Добавить статью | да |
| Добавить статью с пустым названием |  |
| Просмотреть | да |
| Изменить |  |
| Удалить | да |
| Паджинация | |
| [Тексты](https://localhost:8443/text) | **3(4)** |
| Загрузить текст txt | да |
| Загрузить текст pdf | |
| Добавить в словарь | да |
| Паджинация | да |
| [Просмотреть слово](https://localhost:8443/word?wordId=1) | **3(3)** |
| Изменить слово | да |
| Удалить слово | да |
| Редактировать переводы | да |
| [Добавить перевод](https://localhost:8443/translation/add?wordId=1) | **4(4)** |
| Добавить перевод | да |
| Примеры | да |
| Изменить | да |
| Удалить | да |
| [Добавить пример](https://localhost:8443/example/add?translationId=1) | **3(3)** |
| Добавить пример | да |
| Изменить пример | да |
| Удалить пример | да |
