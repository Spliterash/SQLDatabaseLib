# SQL Database

Небольшая библиотека призванная упростить работу с базами данных.

С помощью этой библиотеки, вы сможете делать запросы к базе данных максимально просто.

Например:

* Запросы с параметрами по номеру

    ```java
    database.update("INSERT INTO test_table (id, column1, column2, column3) values (?,?,?,?)","id","value1","value2","value3");
    ```

* Запросы с named параметрами

    ```java
    Map<String,Object> paramMap = new HashMap<>(4);
    
    paramMap.put("userId", userId);
    paramMap.put("age", age);
    
    database.query("SELECT * FROM table where id=:userId and age=:age")
    ```
* Запросы с named параметрами из объекта

    ```java
    @Getter
    @Setter
    public class UserDto {
        private String id;
        private String name;
        private int age;
    }
    ```
  
  ```java
  UserDto dto = new UserDto();
  
  dto.setName("userName");
  dto.setAge(16);
  
  database.queryDto("SELECT * FROM table where id=:id and age=:age", dto)
  ```

* Сессия для запросов, позволяет совершить несколько запросов, используя 1 Connection.

  Очень сильно оптимизирует процесс, когда нужно сделать несколько запросов, например (я знаю что это можно сделать 1 запросом, тут как пример)
    ```java
    try (DatabaseSession session = database.createSession()) {
        QueryResult result = session.query("SELECT id, age, name from users where age >= ?", 18);

        List<String> userIds = result
                .stream()
                .map(row -> row.getString("id"))
                .collect(Collectors.toList());

        session.update("UPDATE users set canDrink=true where id in (?)", userIds);
    }
   ```
* и даже без проблем делать запросы с массивом параметров, это работает как в named запросах, так и обычных, например:

  ```java
  List<String> userIds = Arrays.asList("id1","id2","id3")
  
  database.query("SELECT * FROM users where id in (?)", userIds);
    ```
  Данный запрос превратится сначала в
  ```sql
  SELECT * FROM users where id in (?,?,?)
    ```
  а затем через средства JDBC установит необходимые поля

Чтобы начать пользоваться этой "замечательной" либой, нужно сделать всего 2 вещи

1) Подключить мой nexus
    * maven
    ```xml
    <repository>
        <id>spliterash-group</id>
        <url>http://nexus.spliterash.ru/repository/group/</url>
    </repository>
    ```
    * gradle
   ```groovy
    maven {
        name = "spliterash-group"
        url = uri("https://nexus.spliterash.ru/repository/group/")
    }
   ```
2) Добавить нужную зависимость
   * SQLite стандартный
    ```xml
    <dependency>
      <groupId>ru.spliterash.utils.sql-database</groupId>
      <artifactId>sqlite-simple</artifactId>
      <version>1.0.0-SNAPSHOT</version>
    </dependency>
    ```
    ```java
    Database base = new SimpleSQLiteDatabase(new File("base.sqlite"));
    ```
    * SQLite через HikariCP
    ```xml
    <dependency>
      <groupId>ru.spliterash.utils.sql-database</groupId>
      <artifactId>sqlite-hikaricp</artifactId>
      <version>1.0.0-SNAPSHOT</version>
    </dependency>
    ```
    ```java
    Database base = new HikariCPSQLiteDatabase(new File("base.sqlite"));
    ```
    
    <hr>

   * MySQL стандартный
    ```xml
    <dependency>
      <groupId>ru.spliterash.utils.sql-database</groupId>
      <artifactId>mysql-simple</artifactId>
      <version>1.0.0-SNAPSHOT</version>
    </dependency>
    ```
    ```java
    Database base = new SimpleMySQLDatabase("localhost", 3306, "login", "password");
    ```
    * MySQL через HikariCP
    ```xml
    <dependency>
      <groupId>ru.spliterash.utils.sql-database</groupId>
      <artifactId>mysql-hikaricp</artifactId>
      <version>1.0.0-SNAPSHOT</version>
    </dependency>
    ```
    ```java
    Database base = new HikariMySQLDatabase("localhost", 3306, "login", "password");
    ```

3) Инициализировать нужный вам тип базы данных
    * SimpleSQLiteDatabase(File file) - SQLite база данных, подключение к которой реализованно стандартными средствами
      java
    * HikariCPSQLiteDatabase(File file) - SQLite база данных, подключение к которой реализованно через HikariCP
4) Пользоваться базой данных, желательно привести её к Database типу, чтобы не привязываться к реализации
5) Обязательно вызывать метод destroy() при завершении работы приложения