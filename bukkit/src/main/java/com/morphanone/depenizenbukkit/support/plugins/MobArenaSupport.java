package com.morphanone.depenizenbukkit.support.plugins;

import com.garbagemule.MobArena.MobArena;
import com.garbagemule.MobArena.framework.Arena;
import com.morphanone.depenizenbukkit.commands.mobarena.MobArenaCommand;
import com.morphanone.depenizenbukkit.events.mobarena.MobArenaEndsScriptEvent;
import com.morphanone.depenizenbukkit.extensions.mobarena.MobArenaPlayerExtension;
import com.morphanone.depenizenbukkit.objects.mobarena.MobArenaArena;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.tags.Attribute;
import com.morphanone.depenizenbukkit.events.mobarena.MobArenaStartsScriptEvent;
import com.morphanone.depenizenbukkit.events.mobarena.MobArenaWaveChangesScriptEvent;
import com.morphanone.depenizenbukkit.support.Support;

public class MobArenaSupport extends Support {

    MobArena plugin;

    public MobArenaSupport() {
        registerObjects(MobArenaArena.class);
        registerAdditionalTags("mobarena");
        new MobArenaCommand().activate().as("mobarena").withOptions("See Documentation.", 1);
        registerScriptEvents(new MobArenaStartsScriptEvent());
        registerScriptEvents(new MobArenaEndsScriptEvent());
        registerScriptEvents(new MobArenaWaveChangesScriptEvent());
        registerProperty(MobArenaPlayerExtension.class, dPlayer.class);
        plugin = Support.getPlugin(MobArenaSupport.class);
    }

    @Override
    public String additionalTags(Attribute attribute, TagContext tagContext) {
        if (attribute == null) {
            return null;
        }

        if (attribute.startsWith("mobarena")) {
            // <mobarena[<arena name>]>
            if (attribute.hasContext(1)) {
                MobArenaArena arena = MobArenaArena.valueOf(attribute.getContext(1));
                if (arena == null) {
                    return null;
                }
                return arena.getAttribute(attribute.fulfill(1));
            }

            attribute = attribute.fulfill(1);

            // <--[tag]
            // @attribute <mobarena.list_arenas>
            // @returns dList(MobArena)
            // @description
            // Returns a list of all MobArenas.
            // @plugin Depenizen, MobArena
            // -->
            if (attribute.startsWith("list_arenas")) {
                dList arenas = new dList();
                for (Arena a : plugin.getArenaMaster().getArenas()) {
                    if (plugin.getArenaMaster().getArenaWithName(a.configName()) == null) {
                        continue;
                    }
                    arenas.add(new MobArenaArena(a).identify());
                }
                return arenas.getAttribute(attribute.fulfill(1));
            }
        }

        return null;
    }
}