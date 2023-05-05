package dev.shulika.xtelworkbot;

public class BotConst {
    /*
 :::::  List Commands:::::
start - Начало работы
cancel - Отмена операции
send - Отправить уведомление
list - Список заданий
reg - Регистрация сотрудника
profile - Аккаунт
help - Помощь

*/

    // MESSAGES ------------------------------------
//    public static final String PROCESSED_MSG = "_Обрабатывается \\.\\.\\._";
    public static final String HELLO_MSG = "_Привет_";
    public static final String FIRST_START_MSG = "/reg \\- __Регистрация сотрудника__\n/help \\- __Помощь и доступные команды__";
    public static final String UNSUPPORTED_MSG = "_Неподдерживаемый тип сообщения\\!_";
    public static final String COMMAND_NOT_FOUND = "️❗️️️️️*_Команда отсутствует_*❗️️\\\nвведите или нажмите на /help _для получения списка доступных команд_";
    public static final String HELP_MSG = """
            📋 *_Список доступных команд:_* 📋\n
            /start \\- __Начало работы__, _запустить бот или начать сначала_\n
            /cancel \\- __Отмена операции__, _завершит выполнение любой операции_\n
            /send \\- __Отправить уведомление__, _для выбранного отдела_\n
            /list \\- __Список заданий__, _для выбранного отдела_\n
            /reg \\- __Регистрация сотрудника__, _для получения уведомлений необходимо пройти регистрацию, следуя инструкциям укажите пароль1, выберите отдел/магазин и введите пароль2_\n
            /profile \\- __Аккаунт__, _информация об учетной записи_\n
            /help \\- __Помощь и доступные команды__, список доступных команд и описание\n
            ||_Дополнительно:_|| \n
            """;
    public static final String REG_MSG_TITLE = "📝 *_Регистрация сотрудника в системе оповещений_* 📝\n";
    public static final String MSG_CANCEL = "❌ *_Операция отменена_* ❌\n";
    public static final String REG_MSG_COMMON_PASS = "🔑 *Введите пароль1 общий* 🔑";
    public static final String REG_MSG_COMMON_PASS_FAIL = """
            🔑❌ *_Неправильный пароль1_* ❌🔑\n
            *__Повторите ввод__* _или используйте_ /cancel \\- *__Отмена__*\n
            """;
    public static final String REG_MSG_INPUT_FULL_NAME = """
            ✏️ *_Введите Фамилию Имя_* ✏️\n
            _Например:_ *Пупкин Вова*\n
            """;
    public static final String REG_MSG_SELECT_DEPARTMENT = "\uD83D\uDC47 *Выберите отдел/магазин* \uD83D\uDC47";

    public static final String REG_MSG_DEPARTMENT_PASS = "🔑 *Введите пароль2 отдела/магазина* 🔑";
    public static final String REG_MSG_DEPARTMENT_PASS_FAIL = """
            🔑❌ *_Неправильный пароль2_* ❌🔑\n
            *__Повторите ввод__* _или используйте_ /cancel \\- *__Отмена__*\n
                    """;
    public static final String REG_MSG_REG_COMPLETE = "✅ *Регистрация прошла успешно* ✅\n\nℹ️ /profile \\- _Посмотреть информация об учетной записи_";
    public static final String PROFILE_MSG = "ℹ️ *Профиль пользователя* ℹ️\n\n";
    public static final String PROFILE_NOT_FOUND = "❌ *Вы не зарегистрированы* ❌\n\n/reg \\- __Регистрация сотрудника__";
    public static final String SEND_MSG = """
            ✉️ *_Отправить уведомление_* ✉️\n\n
            ➡️ *__ТЕКСТ__* _просто введите текст и нажмите отправить_\n
            ➡️ *__Фото\\+Текст__* _прикрепите фото и введите подпись_\n
            ➡️ *__Файл\\+Текст__* _прикрепите файл и введите подпись_\n
                    """;
    public static final String SEND_MSG_COMPLETE = "✅ *Отправка прошла успешно* ✅\n";
    public static final String SEND_MSG_EMPTY_DEPARTMENT = "️️️️️️️️️️❗️️️️️ *На данный момент в отделе нет сотрудников* ❗️️️️️\n";
    public static final String SEND_MSG_CHANGED_EXECUTOR = "✅ *Вы приняли задание* ✅\n";
    public static final String SEND_MSG_CHANGED_EXECUTOR_FAIL = "❗️ *Задачу принял другой сотрудник* ❗️\n";
    public static final String SEND_MSG_TODAY_EMPTY_TASKS = "️️️️️️️️️️❗️️️️️ *Сегодня список заданий для выбранного отдела пуст* ❗️️️️️\n";
    public static final String SEND_MSG_TODAY_TASKS = "📝 *_Список заданий СЕГОДНЯ_* 📝\n";


    // COMMANDS ---------------------------------------
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_CANCEL = "/cancel";
    public static final String COMMAND_SEND = "/send";
    public static final String COMMAND_LIST = "/list";
    public static final String COMMAND_REGISTRATION = "/reg";
    public static final String COMMAND_PROFILE = "/profile";
    public static final String COMMAND_HELP = "/help";


    // BUTTONS AND CALLBACK -----------------------------------
    public static final String BTN_CANCEL = "❌ Отмена";
    public static final String BTN_CANCEL_CALLBACK = "CANCEL";
    public static final String BTN_START_REG = "▶️ Начать";
    public static final String BTN_START_REG_CALLBACK = "START_REG";
    public static final String BTN_DEPARTMENT_REG_CALLBACK = "REG_DEPARTMENT";
    public static final String BTN_DEPARTMENT_SEND_CALLBACK = "SEND_DEPARTMENT";
//    public static final String BTN_CANCEL_TASK = "❌ Отмена";
//    public static final String BTN_CANCEL_TASK_CALLBACK = "CANCEL_TASK";
    public static final String BTN_ACCEPT_TASK = "\uD83D\uDCCC Взять задание";
    public static final String BTN_ACCEPT_TXT_TASK_CALLBACK = "ACCEPT_TXT_TASK";
    public static final String BTN_ACCEPT_PHOTO_TASK_CALLBACK = "ACCEPT_PHOTO_TASK";
    public static final String BTN_ACCEPT_DOC_TASK_CALLBACK = "ACCEPT_DOC_TASK";
    public static final String BTN_DEPARTMENT_TASK_LIST_CALLBACK = "TASK_LIST_DEPARTMENT";
}
