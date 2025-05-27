package top.mrxiaom.citizensmodels.func;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import top.mrxiaom.citizensmodels.CitizensModels;
import top.mrxiaom.citizensmodels.api.IModelEngine;
import top.mrxiaom.pluginbase.func.AutoRegister;

import static top.mrxiaom.citizensmodels.api.IModelEngine.MODEL_ID_KEY;

@AutoRegister
public class NPCListener extends AbstractModule implements Listener {
    private final IModelEngine api;
    public NPCListener(CitizensModels plugin) {
        super(plugin);
        api = plugin.getModelEngine();
        registerEvents();
        plugin.getScheduler().runTaskLater(() -> {
            for (NPC npc : CitizensAPI.getNPCRegistry().sorted()) {
                if (npc.isSpawned()) {
                    api.applyModel(npc);
                }
            }
        }, 5L);
    }

    @EventHandler
    public void onNPCSpawn(NPCSpawnEvent e) {
        api.applyModel(e.getNPC());
    }

    @EventHandler
    public void onNPCDeSpawn(NPCDespawnEvent e){
        api.resetModel(e.getNPC(), true);
    }

    @EventHandler
    public void onNPCHurt(NPCDamageEvent e) {
        api.markHurt(e.getNPC());
    }

    @EventHandler
    public void onNPCDead(NPCDeathEvent e) {
        api.markDeath(e.getNPC());
    }

    public void setNPCModel(NPC npc, String modelId) {
        if (modelId == null) {
            npc.data().remove(MODEL_ID_KEY);
            api.resetModel(npc, false);
        } else {
            npc.data().setPersistent(MODEL_ID_KEY, modelId);
            api.applyModel(npc);
        }
    }

    public static NPCListener inst() {
        return instanceOf(NPCListener.class);
    }
}
