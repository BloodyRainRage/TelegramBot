package ru.baddragon.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.baddragon.db.*;
import ru.baddragon.weather.*;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class YolBot extends TelegramLongPollingBot {

    private Properties properties = new Properties();

    //sending reply messsage to user message with text
    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        System.out.println("Username: " + message.getChat().getUserName() + "\n"
                + "First name: " + message.getChat().getFirstName() + "\n"
                + "Last name: " + message.getChat().getLastName() + "\n"
                + "Chat ID " + message.getChat().getId());
        if (randomEvent(5)) {
            sendMessage.setText("sasai");
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    //Constructor
    //Loading .properties file
    public YolBot() {
        super();
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream("bot.properties");
            properties.load(stream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("FNF");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOE");
        }
    }

    private void welcomeUser(String chatID) {
        String info = "";
        Scanner scanner = null;
        try {
            File file = new File("src/main/resources/info.txt");

            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                info += scanner.nextLine();
                info += "\n";
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("FNF");
        } finally {
            scanner.close();
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);
        sendMessage.setText(info);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    //Used to send message to specific user. This method is changed and does not work now
    //May be used later
    private void sendMsgToUser(Message message, String secondPart) {
        SendMessage sendMessage = new SendMessage();
        Chat chat = new Chat();

        //sendMessage.setChatId(id);
        sendMessage.setText("Сасай кудасай :Р");
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    //simulates random even with <value>%
    private boolean randomEvent(Integer value) {

        return (Math.random() * 100 + 1) < value;

    }

    //splitting command with parameters like /command@parameter
    //or responding on command w/o parameter
    private void splitCommand(Message message) {
        Botdb db = new Botdb(properties);
        String[] command = new String[2];
        String result;
        if (message.getText().contains("@")) {
            command = message.getText().split("@");
        } else {
            command[0] = message.getText();
            command[1] = "";
        }
        WeatherStruc weatherStruc = new WeatherStruc();
        User user;
        switch (command[0]) {
            case "/ping":
                if (command[1].equals("YolBot"))
                    sendMsg(message, "Дада я ");
                break;
            case "/help":
                sendMsg(message, "This command is not ready yet");
                break;
            case "/start":
                welcomeUser(message.getChatId().toString());
                break;
            case "/regme":
                user = message.getFrom();
                //SendMsg(message, user.getUserName() + " " + user.getFirstName() + " " + user.getLastName());
                result = db.registerUser(user);
                if (result.equals("ok")) {
                    sendMsg(message, "Success");
                } else {
                    sendMsg(message, result);
                }
                //Chat chat = message.getChat();
                break;
            case "/deleteme":
                user = message.getFrom();
                result = db.deleteUser(user);
                if (result.equals("ok")) {
                    sendMsg(message, "Success");
                } else {
                    sendMsg(message, result);
                }
                break;
            case "/me":
                user = message.getFrom();
                Chat chat = message.getChat();

                sendMsg(message, user.getId() + " " + message.getChatId() + " " + user.getUserName() + " "
                        + user.getFirstName() + " " + user.getLastName());
                break;
            case "/weather":
                try {
                    String city = db.getWeather(message.getFrom());
                    System.out.println(city);
                    if (city.equals("NF")) {
                        sendMsg(message, "You did not register yourself.\nPlease, type /regme to use /weather command");
                        return;
                    } else if (city.equals("null")) {
                        sendMsg(message, "You did not register your city.\nPlease, type /regcity@<cityname> to use /weather command");
                    } else if (city.equals("SQLE")) {
                        sendMsg(message, "SQL exception has occured");
                        return;
                    } else {
                        sendMsg(message, Weather.getWeather(city, weatherStruc));
                    }
                } catch (Exception e) {
                    sendMsg(message, "Город не найден");
                }
                break;
            case "/regcity":
                result = db.addCity(command[1].trim(), message.getFrom());
                if (result == "ok") {
                    sendMsg(message, "Success");
                } else {
                    sendMsg(message, result);
                }
                break;
            //case "/send":
            //    sendMsgToUser(message, command[1]);
            //    break;
            case "/checkme":
                user = message.getFrom();
                System.out.println(db.isUserExist(user));
                break;

            default:

        }

    }


    //hanging income message
    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().toString());
        Message message = update.getMessage();
        if (message.getText().startsWith("/")) {
            splitCommand(message);
        } else if (message.getText().toLowerCase().startsWith("ааа") || message.getText().toLowerCase().startsWith("aaa")) {
            double n = Math.random() * 50;
            String str = "A";
            for (int i = 0; i < n; i++) {
                str += "А";
            }
            sendMsg(message, str);
        }

    }

    @Override
    public String getBotUsername() {
        return properties.getProperty("tgbot.name");
    }

    @Override
    public String getBotToken() {
        return properties.getProperty("tgbot.token");
    }
}
