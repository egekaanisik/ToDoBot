package dev.mrpanda.ToDoBot;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.Console;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;

public class Bot extends ListenerAdapter {
	
	public static String DISCORD_TOKEN = "<insert_discord_bot_token>"; // Discord bot token
	public static String OWNER_ID = "<insert_your_discord_id>"; // ID of the bot owner
	
	public static void main(String[] args) throws LoginException, InterruptedException, IOException {
		// if there is no console, create a new one
		Console console = System.console();
		if (console == null && !GraphicsEnvironment.isHeadless()) {
			String filename = Bot.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
			Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/c","java -jar \"" + filename + "\""});
			System.exit(0); // comment this line while working on the project
		}

		System.out.println("Starting bot...");
		
		// shard manager configuration
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(DISCORD_TOKEN)
		.addEventListeners(new Bot())
		.setActivity(Activity.listening("/todo"))
		.setAutoReconnect(true);
		
		builder.build(); // build shard manager
	}
	
	// when the bot is ready and set, notify and add commands to the bot
	public void onReady(@Nonnull ReadyEvent event) {
		// add commands
		ArrayList<CommandData> commands = new ArrayList<CommandData>();
		commands.add(Commands.slash("todo", "Creates a to-do item.").addOption(OptionType.STRING, "content", "Content of the task.", true)); // todo command
		commands.add(Commands.slash("shutdown", "[RESTRICTED] Shuts the bot down.")); // shutdown command
		event.getJDA().updateCommands().addCommands(commands).complete();
		
		System.out.println("Bot started!");
	}

	private Button CLEAR = Button.primary("clear", "Clear"); // clear button
	private Button MARK_DONE = Button.primary("done", Emoji.fromFormatted("U+2714")); // check mark button
	private Button REMOVE = Button.secondary("remove", "Remove"); // remove button

	private String NOT_COMPLETED = ":white_large_square:"; // not completed task emote
	private String DONE = ":white_check_mark:"; // done task emote
	
	// when a slash command is used
	public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
		String name = event.getName(); // get the name of the command
		
		// if the command is "todo", create a to-do item
		if (name.equals("todo")) {
			String text = event.getOption("content").getAsString().strip(); // get the content
			
			// check if the length of the content is valid
			if (text.length() > 234) {
				event.reply("More than 234 characters are not allowed.").setEphemeral(true).queue();
				return;
			}
			
			event.deferReply().complete(); // acknowledge Discord
			
			// embed builder with corresponding fields to represent a not completed task (content as title, white color, task author, and timestamp)
			EmbedBuilder eb = new EmbedBuilder()
					.setTitle(NOT_COMPLETED + "  " + text)
					.setColor(Color.WHITE)
					.setFooter("Added by " + event.getUser().getAsTag())
					.setTimestamp(Instant.now());
			event.getHook().sendMessageEmbeds(eb.build()).addActionRow(MARK_DONE, REMOVE).queue(); // build the embed, add buttons, and send
		
		// if the command is "shutdown", shut the bot down
		} else if (name.equals("shutdown")) {
			// if the command user is MrPandaDev#8749, notify shutdown status and execute
			if (event.getUser().getId().equals(OWNER_ID)) {
				event.reply("Goodbye!").setEphemeral(true).complete();
				System.out.println("Shutting down...");
				event.getJDA().getShardManager().shutdown(); // close all connections and destroy all shards
			// if not, notify user
			} else {
				event.reply("Only my owner can shut me down.").setEphemeral(true).queue();
			}
		}
	}
	
	// when a button is pressed
	public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
		String id = event.getComponentId(); // get button ID

		event.deferEdit().complete(); // acknowledge Discord
		
		// if "Remove" button is pressed, delete the message
		if (id.equals("remove")) {
			event.getHook().deleteOriginal().queue();
			return;
		}		
		
		// new embed config
		String oldEmote;
		String newEmote;
		Color color;
		Button button;
		
		// set new embed config via the button pressed
		// if the check mark button is pressed
		if (id.equals("done")) {
			// set the new title emote as green check mark emote
			oldEmote = NOT_COMPLETED;
			newEmote = DONE;
			color = Color.GREEN; // set embed color as green
			button = CLEAR; // set action button as "Clear" button
		// if the "Clear" button is pressed
		} else {
			// set the new title emote as white box emote
			oldEmote = DONE;
			newEmote = NOT_COMPLETED;
			color = Color.WHITE; // set embed color as white
			button = MARK_DONE; // set action button as check mark button
		}

		event.editButton(button).queue(); // edit button
		MessageEmbed prev = event.getMessage().getEmbeds().get(0); // get the message embed
		// create a new embed builder with the new configuration
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle(prev.getTitle().replace(oldEmote, newEmote))
				.setColor(color)
				.setFooter(prev.getFooter().getText())
				.setTimestamp(prev.getTimestamp());
		event.getHook().editOriginalEmbeds(eb.build()).queue(); // edit the embed
	}
}
