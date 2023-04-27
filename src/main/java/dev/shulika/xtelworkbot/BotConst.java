package dev.shulika.xtelworkbot;

public class BotConst {
    /*
 :::::  List Commands:::::
start - Начало работы
cancel - Отмена операции
reg - Регистрация сотрудника
profile - Аккаунт
help - Помощь

/send - Отправить рассылку (*только Админ)
*/

    // MESSAGES ------------------------------------
    public static final String HELLO_MSG = "_Привет_";
    public static final String PROCESSED_MSG = "_Обрабатывается \\.\\.\\._";
    public static final String UNSUPPORTED_MSG = "_Неподдерживаемый тип сообщения\\!_";
    public static final String COMMAND_NOT_FOUND = "️❗️️️️️*_Команда отсутствует_*❗️️\\\nвведите или нажмите на /help _для получения списка доступных команд_";
    public static final String HELP_MSG = """
            📋 *_Список доступных команд:_* 📋\n
            /start \\- __Начало работы__, _запустить бот или начать сначала_\n
            /cancel \\- __Отмена операции__, _завершит выполнение любой операции_\n
            /reg \\- __Регистрация сотрудника__, _для получения уведомлений необходимо пройти регистрацию, следуя инструкциям укажите пароль и выберите отдел/магазин_\n
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
    public static final String REG_MSG_REG_COMPLETE = "✅ *Регистрация прошла успешно* ✅\n";
    public static final String PROFILE_MSG = "ℹ️ *Профиль пользователя* ℹ️\n\n";
    public static final String PROFILE_NOT_FOUND = "❌ *Профиль не найден* ❌\n\n/reg \\- __Регистрация сотрудника__";


    // COMMANDS ---------------------------------------
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_CANCEL = "/cancel";
    public static final String COMMAND_REGISTRATION = "/reg";
    public static final String COMMAND_PROFILE = "/profile";
    public static final String COMMAND_HELP = "/help";


    // BUTTONS AND CALLBACK -----------------------------------
    public static final String BTN_CANCEL = "Отмена";
    public static final String BTN_CANCEL_CALLBACK = "CANCEL:null";
    public static final String BTN_START_REG = "Начать";
    public static final String BTN_START_REG_CALLBACK = "START_REG:null";

}
