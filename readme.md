###Генерация хранилища ключей и сертификата
```shell
keytool -genkey -keyalg RSA -alias server -keystore server.jks -storepass secret -validity 360 -keysize 2048
keytool -export -alias server -file server.crt -keystore server.jks
```
Сертификат server.crt нужно передать клиенту

###Доступ к приложению
[Словарь](https://localhost:8443/dictionary)
[Добавить слово](https://localhost:8443/word/add)

###Полезные ссылки
[Bootstrap 4.0 docs](https://getbootstrap.com/docs/4.0.getting-started/introduction/)
[Bootstrap 5.0 docs](https://getbootstrap.com/docs/5.0/getting-started/introduction/)
[Bootstrap 4 Tutorial](https://www.w3schools.com/bootstrap4)