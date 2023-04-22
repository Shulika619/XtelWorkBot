package dev.shulika.xtelworkbot;

public class BotConst {
    /*
 :::::  List Commands:::::
start - Начало работы
reg - Регистрация сотрудника
help - Помощь
/send - Отправить рассылку (*только Админ)
*/

    // MESSAGES
    public static final String HELLO_MSG = "_Привет, _";
    public static final String PROCESSED_MSG = "_Обрабатывается \\.\\.\\._";
    public static final String UNSUPPORTED_MSG = "_Неподдерживаемый тип сообщения\\!_";
    public static final String COMMAND_NOT_FOUND = "️❗️️️️️*_Команда отсутствует_*❗️️\\\nвведите или нажмите на /help _для получения списка доступных команд_";
    public static final String HELP_MSG = """
                📋*_Список доступных команд:_*📋\n
                /start \\- __Начало работы__, _нажмите или введите эту команду если хотите начать сначала, запустить бот или запутались в меню_\n
                /reg \\- __Регистрация сотрудника__, _нажмите или введите эту команду, затем следуя инструкциям выберите отдел/магазин, а затем введите пароль_\n
                /help \\- __Помощь и доступные команды__\n
                ||_Дополнительно:_|| \n
                """;

    // COMMANDS
    public static final String COMMAND_START = "/start";
    public static final String COMMAND_HELP = "/help";
    public static final String COMMAND_REGISTRATION = "/reg";
//    public static final String COMMAND_ = "";




}
