package hoosk.api.roles;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Optional;
import java.util.function.Consumer;

public class SupportRole {

    /**
     * Creates the "Support Staff" role if it doesn't exist, otherwise returns existing role.
     * @param guild The server where the command was issued
     */
    public static void createSupportRole(@NotNull Guild guild, Consumer<Role> callback) {
        Optional<Role> supportRoleOpt = guild.getRoles().stream()
                .filter(role -> role.getName().equalsIgnoreCase("Support Staff"))
                .findFirst();

        if (supportRoleOpt.isPresent()) {
            callback.accept(supportRoleOpt.get());
        } else {
            guild.createRole()
                    .setName("Support Staff")
                    .setColor(Color.PINK)
                    .setPermissions(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND)
                    .queue(callback);
        }
    }
}
