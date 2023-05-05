package net.ramuemo;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        new Main("TOKEN HERE");
    }

    private final JDA jda;
    private final long roleId = Long.valueOf("ROLE ID HERE");
    private final long serverId = Long.valueOf("ROLE ID HERE");

    public Main(String token) {
        final JDABuilder builder = JDABuilder.createDefault(token);
        for (GatewayIntent intent : GatewayIntent.values()) {
            builder.enableIntents(intent);
        }
        builder.setActivity(Activity.listening("Server Working!"));

        this.jda = builder.build();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Getting guild...");
        final Guild guild = jda.getGuildById(serverId);
        System.out.println("Gotcha: " + guild);

        System.out.println("Getting role...");
        final Role role = guild.getRoleById(roleId);
        System.out.println("Gotcha: " + role + " members");

        System.out.println("Getting members...");
        final List<Member> members = guild.loadMembers().get()
                .stream()
                .filter(member -> !member.getRoles().contains(role))
                .collect(Collectors.toList());
        System.out.println("Gotcha: " + members.size() + " members");

        int i = 0;
        for (Member member : members) {
            i++;
            guild.addRoleToMember(member, role).complete();
            System.out.println("Added role " + member.getIdLong() + " count: " + i + " left: " + (members.size() - i));
        }
    }

    public void shutdown() {
        jda.shutdownNow();
    }

    public JDA getJda() {
        return jda;
    }
}