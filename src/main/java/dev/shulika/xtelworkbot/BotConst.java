package dev.shulika.xtelworkbot;

public class BotConst {
    /*
 :::::  List Commands:::::
start - Начало работы
reg - Регистрация сотрудника
help - Помощь
/send - Отправить рассылку (*только Админ)
*/

    // MESSAGES ------------------------------------
    public static final String HELLO_MSG = "_Привет, _";
    public static final String PROCESSED_MSG = "_Обрабатывается \\.\\.\\._";
    public static final String UNSUPPORTED_MSG = "_Неподдерживаемый тип сообщения\\!_";
    public static final String COMMAND_NOT_FOUND = "️❗️️️️️*_Команда отсутствует_*❗️️\\\nвведите или нажмите на /help _для получения списка доступных команд_";
    public static final String HELP_MSG = """
            📋 *_Список доступных команд:_* 📋\n
            /start \\- __Начало работы__, _нажмите или введите эту команду если хотите запустить бот или начать сначала_\n
            /reg \\- __Регистрация сотрудника__, _нажмите или введите эту команду, затем следуя инструкциям укажите пароль и выберите отдел/магазин_\n
            /help \\- __Помощь и доступные команды__\n
            ||_Дополнительно:_|| \n
            """;
    public static final String REG_MSG_TITLE = "📝 *_Регистрация сотрудника в системе оповещений_* 📝\n";
    public static final String MSG_CANCEL = "❌ *_Операция отменена_* ❌\n";
    public static final String REG_MSG_COMMON_PASS = "🔑 *Введите пароль1 общий* 🔑";
    public static final String REG_MSG_COMMON_PASS_FAIL = """
                    🔑❌ *_Неправильный пароль1 общий_* ❌🔑\n
                    *__Повторите ввод__* _или нажмите на кнопку_ *__Отмена__*\n
                    """;
    public static final String REG_MSG_INPUT_FULL_NAME = """
                    ✏️ *_Введите Фамилию Имя_* ✏️\n
                    _Например:_ *Пупкин Вова*\n
                    """;
    public static final String REG_MSG_SELECT_DEPARTMENT = "\uD83D\uDC47 *Выберите отдел/магазин* \uD83D\uDC47";

    public static final String REG_MSG_DEPARTMENT_PASS = "🔑 *Введите пароль2 отдела/магазина* 🔑";
    public static final String REG_MSG_DEPARTMENT_PASS_FAIL = """
                    🔑❌ *_Неправильный пароль2_* ❌🔑\n
                    *__Повторите ввод__* _или нажмите на кнопку_ *__Отмена__*\n
                    """;
    public static final String REG_MSG_REG_COMPLETE = "✅ *Регистрация прошла успешно* ✅";

    // COMMANDS ---------------------------------------
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_HELP = "/help";
    public static final String COMMAND_REGISTRATION = "/reg";
//    public static final String COMMAND_ = "";


    // BUTTONS AND CALLBACK -----------------------------------
    public static final String BTN_CANCEL = "Отмена";
    public static final String BTN_CANCEL_CALLBACK = "CANCEL:null";


    public static final String BTN_START_REG = "Начать";
    public static final String BTN_START_REG_CALLBACK = "START_REG:null";

}
