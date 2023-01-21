/*
 * Copyright (C) 2016-2020 the original author or authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package space.npstr.wolfia.commands;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import space.npstr.wolfia.utils.discord.RestActions;

/**
 * Convenience container for values associated with an issued command, also does the parsing
 * <p>
 * Don't save these anywhere as they hold references to JDA objects, just pass them down through (short-lived) command execution
 */
public class CommandContext extends MessageContext {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CommandContext.class);

    //@formatter:off
    @NonNull public final String trigger;                        // the command trigger, e.g. "play", or "p", or "pLaY", whatever the user typed
    @NonNull public final String[] args ;                        // the arguments split by whitespace, excluding prefix and trigger
    @NonNull public final String rawArgs;                        // raw arguments excluding prefix and trigger, trimmed
    @NonNull public final BaseCommand command;
//    @NonNull private final Histogram.Timer received;             // time when we received this command
    //@formatter:on

    CommandContext(@NonNull final MessageReceivedEvent event, @NonNull final String trigger,
                   @NonNull final String[] args, @NonNull final String rawArgs, @NonNull final BaseCommand command) {

        super(event);
        this.trigger = trigger;
        this.args = args;
        this.rawArgs = rawArgs;
        this.command = command;
    }


    public boolean hasArguments() {
        return this.args.length > 0 && !this.rawArgs.isEmpty();
    }

    /**
     * Deletes the users message that triggered this command, if we have the permissions to do so
     */
    public void deleteMessage() {
        if (this.msg.isFromType(ChannelType.TEXT)) {
            final TextChannel tc = this.msg.getTextChannel();
            if (tc.getGuild().getSelfMember().hasPermission(tc, Permission.MESSAGE_MANAGE)) {
                RestActions.deleteMessage(this.msg);
            }
        }
    }

    //reply with the help
    public void help() {
        reply(this.command.formatHelp(this.invoker));
    }

    /**
     * Transforms this context into a guild context, telling the invoker to run the command in a guild if requested
     *
     * @param answerUser set to false to not tell the invoker about running the command in a guild
     *                   <p>
     * @return a GuildCommandContext if this command was issued in a guild, null otherwise
     */
    @Nullable
    public GuildCommandContext requireGuild(@NonNull final boolean... answerUser) {
        if (this.channel.getType() == ChannelType.TEXT) {
            final TextChannel tc = (TextChannel) this.channel;
            final Guild g = tc.getGuild();
            final Member m = this.event.getMember();
            if (m != null) {
                return new GuildCommandContext(this, g, m, tc);
            } else {
                log.warn("Uh oh member is unexpectedly null when transforming CommandContext to GuildCommandContext");
            }
        }

        if (answerUser.length == 0 || answerUser[0]) {
            replyWithMention("please run this command in a guild channel.");
        }
        return null;
    }
}
