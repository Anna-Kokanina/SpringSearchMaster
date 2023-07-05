# SpringSearchMaster
SpringSearchMaster - это проект поискового движка, основанный на Spring Framework и взаимодействующий с локальной базой данных MySQL. Проект использует следующие технологии: JPA, JSOUP, Morphology Library Lucene, SQL и Spring Framework.

SpringSearchMaster предоставляет пользователю специализированный API с основными функциями, включающими:
- Предварительное индексирование сайтов;
- Предоставление основной информации о сайтах;
- Поиск ключевых слов в проиндексированных сайтах и предоставление результатов пользователю.

## Доступ к веб-странице

Веб-страница доступна по адресу localhost:8080.

Данные для входа: 
- Логин: root
- Пароль: Papol

Для изменения пароля обратитесь к файлу SecurityConfig.

Перед первой компиляцией программы следует выполнить следующие шаги:

1. Установите СУБД (если они ещё не установлены).
2. Создайте схему базы данных под названием `search_engine` в вашей СУБД. Если вы выберете другое имя для схемы, убедитесь, что оно отражено в файле `application.yaml`.
3. Создайте пользователя `root` с паролем `Rjptk594535` в схеме. Если вы выберете другое имя пользователя или пароль, убедитесь, что они соответствуют параметрам `spring.datasource.username` и `spring.datasource.password` в файле `application.yaml`.

## Создание базы данных

Выполните следующие команды для создания базы данных:

```sql
CREATE DATABASE IF NOT EXISTS search_engine;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'Rjptk594535';
GRANT ALL PRIVILEGES ON search_engine.* TO 'root'@'localhost';
FLUSH PRIVILEGES;

Запуск Spring Boot Server

Для запуска Spring Boot Server используйте следующую команду в командной строке:
./mvnw spring-boot:run


