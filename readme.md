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
[Добавить пример](https://localhost:8443/example/add?translationId=1)

###Полезные ссылки
[Bootstrap 4.0 docs](https://getbootstrap.com/docs/4.0/getting-started/introduction/) \
[Bootstrap 5.0 docs](https://getbootstrap.com/docs/5.0/getting-started/introduction/) \
[Bootstrap 4 Tutorial](https://www.w3schools.com/bootstrap4) \
[Bootstrap icons](https://www.bootstrapicons.com)

###ToDo
Сделать кнопку очистки фильтра на странице словаря \
Добавить выбор словаря (?) \
Сделать возможность вводить фразы для перевода \
При добавление слова сделать проверку, чтобы нельзя было добавлять такое же слово с таким же типом