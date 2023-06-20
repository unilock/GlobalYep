package cc.unilock.globalyep.util;

import com.velocitypowered.api.plugin.PluginManager;
import net.byteflux.libby.Library;
import net.byteflux.libby.VelocityLibraryManager;
import net.byteflux.libby.relocation.Relocation;
import org.slf4j.Logger;

import java.nio.file.Path;

public final class Libraries {
    public static void load(cc.unilock.globalyep.GlobalYep plugin, Logger logger, Path path, PluginManager pluginManager) {
        final VelocityLibraryManager<cc.unilock.globalyep.GlobalYep> libraryManager =
                new VelocityLibraryManager<>(logger, path, pluginManager, plugin, "libs");
        final Relocation configurateRelocation =
                new Relocation("org{}spongepowered", "cc.unilock.globalyep.libs.spongepowered");
        final Relocation geantyrefRelocation =
                new Relocation("io{}leangen{}geantyref", "cc.unilock.globalyep.libs.geantyref");
        final Library hocon = Library.builder()
                .groupId("org{}spongepowered")
                .artifactId("configurate-hocon")
                .version(Constants.CONFIGURATE)
                .id("configurate-hocon")
                .relocate(configurateRelocation)
                .relocate(geantyrefRelocation)
                .build();
        final Library confCore = Library.builder()
                .groupId("org{}spongepowered")
                .artifactId("configurate-core")
                .version(Constants.CONFIGURATE)
                .id("configurate-core")
                .relocate(configurateRelocation)
                .relocate(geantyrefRelocation)
                .build();
        final Library geantyref = Library.builder()
                .groupId("io{}leangen{}geantyref")
                .artifactId("geantyref")
                .version(Constants.GEANTYREF)
                .id("geantyref")
                .relocate(geantyrefRelocation)
                .build();

        libraryManager.addMavenCentral();
        libraryManager.loadLibrary(geantyref);
        libraryManager.loadLibrary(confCore);
        libraryManager.loadLibrary(hocon);
    }
}
