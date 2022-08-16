# To-Do Bot
A simple Discord bot for creating to-do items. Supports slash commands and buttons.

## Dependencies
* Java 1.8+
* Gradle 7+
* JDA 5+

## Installation
To use this bot, a new Discord bot has to be created via [Discord Developer Portal](https://discord.com/developers/applications). The bot has to have the scopes `bot` and `applications.commands` while creating OAuth2 link.

In Bot section of the developer portal, a token has to be generated to be set as the `DISCORD_TOKEN` in the [Bot.java](https://github.com/egekaanisik/ToDoBot/blob/master/ToDoBot/src/main/java/dev/mrpanda/ToDoBot/Bot.java).

Another field that needs to be set is `OWNER_ID`. Enter the bot owner ID to this field to restrict the usage of owner-only commands.

> Note: The user ID can be obtained by turning on developer mode. After turning on, right-click the user profile and select "Copy ID".

## Creating a JAR
Run the following command in the project directory to create a JAR file. It will be outputted to `build/libs`.
```
gradlew shadowjar
```

## Usage
There are 2 commands:
* todo
* shutdown

### todo
Usage: `/todo <content>`

Description: `Creates a to-do item.`

After using the slash command, the bot creates an embed with the given content and 2 buttons. The check mark button marks the task as done, and the "Remove" button deletes the task. Marking the task done changes the embed color and emote. It also replaces the check mark button with the "Clear" button, which acts the opposite way.

![image](https://user-images.githubusercontent.com/66966617/184836027-fa80de83-f7dd-48c1-9789-aaa1992e6483.png)

![image](https://user-images.githubusercontent.com/66966617/184836186-52e623f3-3958-4af9-9f24-93a994308224.png)

![image](https://user-images.githubusercontent.com/66966617/184838277-cd205c53-5d05-45fc-b153-231c2be70a4c.png)

![image](https://user-images.githubusercontent.com/66966617/184838390-0752b556-687d-4ab0-81ff-ecd0ca4d928e.png)

### shutdown
Usage: `/shutdown`

Description: `[RESTRICTED] Shuts the bot down.`

This command shuts the bot down if the command user is the bot owner.

![image](https://user-images.githubusercontent.com/66966617/184840015-7efba2ad-1bf2-454f-aca8-8befa0ca008b.png)

![image](https://user-images.githubusercontent.com/66966617/184840142-718b7080-08f8-4aa1-b0e4-a054d1854766.png)

![image](https://user-images.githubusercontent.com/66966617/184843105-192aaf88-3ddf-4699-90c8-b6b3c652cbc4.png)








