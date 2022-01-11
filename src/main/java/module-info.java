
import jplugman.api.Plugin;
import perobobbot.plugin.local.chat.LocalChatPlugin;

module perobobbot.plugin.localchat {
    requires static lombok;

    requires jplugman.api;
    requires com.google.common;

    requires perobobbot.lang;
    requires perobobbot.chat.core;
    requires java.desktop;
    requires perobobbot.data.service;
    requires perobobbot.plugin;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires perobobbot.http;
    requires perobobbot.messaging;
    requires perobobbot.command;
    requires perobobbot.eventsub;
    requires perobobbot.twitch.client.api;

    provides Plugin with LocalChatPlugin;
}
