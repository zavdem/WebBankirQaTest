# Тестовое задание
## QA Automation Engineer
### Java / Kotlin

---

> **Расчетное время:** 45–60 минут.

> **Формат сдачи:** Публичный Git-репозиторий (GitHub / GitLab). Ссылка на репо + короткий README с инструкцией запуска.

> **Важно:** Если вы использовали ИИ-инструменты (Copilot, ChatGPT и т.д.), решение не будет засчитано. Для ИИ есть отдельный трек.

---

## Контекст задания

Вам предоставлен фрагмент существующего тест-фреймворка. В нем есть архитектурные решения, намеренно допущенные ошибки и подозрительные места. Ваша задача – не просто написать тест, а продемонстрировать мышление QA-инженера: что замечаете, что фиксите, что предлагаете улучшить.

### Исходный код (для анализа и использования)

**Файл: `UserApiClient.java`**

```java
public class UserApiClient {
    private static UserApiClient instance;
    private final String baseUrl;
    private int timeout = 5000;

    private UserApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static UserApiClient getInstance(String baseUrl) {
        if (instance == null) {
            instance = new UserApiClient(baseUrl);
        }
        return instance;
    }

    public Response createUser(Map<String, Object> payload) {
        return given()
            .baseUri(baseUrl)
            .contentType(ContentType.JSON)
            .body(payload)
            .when()
            .post("/api/v1/users");
    }

    public Response getUserById(String userId) {
        return given()
            .baseUri(baseUrl)
            .pathParam("id", userId)
            .when()
            .get("/api/v1/users/{id}");
    }

    public void deleteUser(String userId) {
        given()
            .baseUri(baseUrl)
            .when()
            .delete("/api/v1/users/" + userId);
    }
}
```

**Файл: `BaseTest.java`**

```java
@ExtendWith(SeleniumExtension.class)
public class BaseTest {

    protected WebDriver driver;
    protected UserApiClient apiClient;

    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        apiClient = UserApiClient.getInstance("http://localhost:8080");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
```

---

## Задания

### Задание 1 – Code Review

Проведите письменный code review двух приведенных классов. Оформите его как комментарии в отдельном файле `REVIEW.md` в репозитории.

**Что ожидается в review:**
- Список найденных проблем с объяснением: почему это проблема (не просто "плохо", а конкретно: что сломается, при каком условии, какой риск)
- Как минимум одна проблема, которую вы бы вынесли на обсуждение с командой, а не просто починили сами, и почему
- Предложения по рефакторингу

---

### Задание 2 - Написать тест

Напишите тестовый сценарий на Java или Kotlin для следующего сценария:

> **Сценарий:** Создать нового пользователя через API, затем убедиться, что он появился на странице `/admin/users` в браузере (UI-проверка). После - удалить пользователя через API и убедиться, что он исчез со страницы.

**Требования к решению:**
1. Используйте исходные классы (можете их исправлять - это часть задания)
2. Тест должен быть идемпотентным
3. Код должен компилироваться (реальные зависимости можете замокать через WireMock или аналоги)

**Что НЕ требуется:**
- Реальный запущенный сервер или браузер - достаточно, чтобы тест был логически корректным

---

### Задание 3 - Вопросы с открытым ответом

Ответьте письменно (в файле `ANSWERS.md`) на следующие вопросы (нет правильных ответов).

#### Вопрос 1

В `BaseTest` используется `implicitlyWait(10s)`. Ваш коллега добавил явный `WebDriverWait(driver, 15s)` в одном из тестов. При запуске тесты стали нестабильными - иногда падают, иногда нет, без видимой причины. Объясните возможную механику происходящего и как бы вы исправили проблему.

#### Вопрос 2

Тест-сьют из 200 UI тестов стабильно проходит локально, но в CI падает ~15% тестов с разными ошибками: `StaleElementReferenceException`, `NoSuchElementException`, и иногда - неожиданный `401` от API. Тесты перезапускаются и проходят. Какие 3 наиболее вероятные причины нестабильности вы бы проверили в первую очередь, и как бы вы их диагностировали?

#### Вопрос 3

Ниже приведен полный код теста (импорты опущены для краткости). Найдите в нем проблему и объясните её:

```java
public class TestDeleteUser {
	@Test
	void testDeleteUser() {
	    String userId = "user-123";
	    apiClient.deleteUser(userId);
	
	    Response response = apiClient.getUserById(userId);
	    assertThat(response.statusCode()).isEqualTo(404);
	}
}
```

---

### Задание 4

Выполните одно из следующих (на выбор):

#### Вариант A: CI/CD

Добавьте в репозиторий `.github/workflows/run-tests.yml` (или аналог для GitLab CI), который запускает тесты в headless-режиме. Сделайте так, чтобы при падении тестов в артефакты CI сохранялся скриншот и лог последнего запроса API.

#### Вариант B: Параметризация

Перепишите тест из задания 2 как `@ParameterizedTest` с тремя наборами данных: валидный пользователь, пользователь с невалидным email, пользователь с дублирующимся username. Опишите в `ANSWERS.md`, как бы вы получали тестовые данные в реальном проекте.

#### Вариант C: Анализ производительности

В репозитории есть класс с 50+ тестами, все они наследуют `BaseTest` и поднимают `ChromeDriver`. Среднее время прогона - 18 минут. Предложите конкретный план оптимизации (шаги с ожидаемым эффектом). Что бы вы сделали в первую очередь и почему?

---

## Чеклист для кандидата

Перед отправкой убедитесь, что:

1. Репозиторий публичный и доступен по ссылке
2. README содержит инструкцию по запуску и, возможно, дополнительные пояснения
3. Файл `REVIEW.md` существует и содержит аргументированный разбор кода
4. Файл `ANSWERS.md` содержит ответы на все три вопроса
5. Тест-класс из задания 2 присутствует и компилируется
6. Если сделано доп. задание из задания 4 (по желанию), оно отдельно выделено в README
