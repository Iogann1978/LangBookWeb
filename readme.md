###Генерация хранилища ключей и сертификата
```shell
keytool -genkey -keyalg RSA -alias server -keystore server.jks -storepass secret -validity 360 -keysize 2048
keytool -export -alias server -file server.crt -keystore server.jks
```
Сертификат server.crt нужно передать клиенту

###Доступ к приложению
[Словарь](https://localhost:8443/dictionary) \
[Добавить слово](https://localhost:8443/word/add) \
[Добавить перевод](https://localhost:8443/translation/add?wordId=1) \
[Добавить пример](https://localhost:8443/example/add?translationId=1) \
[Статьи](https://localhost:8443/article/list) \
[Просмотреть слово](https://localhost:8443/word?wordId=1) \
[Карусель](https://localhost:8443/roundrobin)
[Фразы](https://localhost:8443/phrase/list)
[Тексты](https://localhost:8443/text/list)

###Полезные ссылки
[Bootstrap 4.0 docs](https://getbootstrap.com/docs/4.0/getting-started/introduction/) \
[Bootstrap 5.0 docs](https://getbootstrap.com/docs/5.0/getting-started/introduction/) \
[Bootstrap 4 Tutorial](https://www.w3schools.com/bootstrap4) \
[Bootstrap icons](https://www.bootstrapicons.com)

###Поведение клиентов при различных перенаправлениях описано в таблице:
| Статус ответа | Кэширование | Если метод не GET или HEAD |
|-----------------------|------------------------|----------------------------|
|`301 Moved Permanently`|Можно как обычно|Спросить у пользователя подтверждения и запросить другой ресурс исходным методом|
|`307 Temporary Redirect`|Можно только если указан заголовок Cache-Control или Expires|Спросить у пользователя подтверждения и запросить другой ресурс исходным методом|
|`302 Found (HTTP/1.1)`|Можно только если указан заголовок Cache-Control или Expires|Спросить у пользователя подтверждения и запросить другой ресурс исходным методом|
|`302 Moved Temporarily (HTTP/1.0)`|Можно только если указан заголовок Cache-Control или Expires|Спросить у пользователя подтверждения и запросить другой ресурс исходным методом|
|`303 See Other`|Нельзя|Перейти автоматически, но уже методом GET|

###ToDo
Сделать кнопку очистки фильтра на странице словаря \
Добавить выбор словаря (?) \
При добавление слова сделать проверку, чтобы нельзя было добавлять такое же слово с таким же типом и таким же пользователем \
Сделать возможность создавать несколько каруселей для одного пользователя \
Использовать в репозиториях методы, которые возращают Page, для удобной работы с навигацией