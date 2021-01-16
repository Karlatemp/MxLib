/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-impl.main/TransLoader.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot.impl;

import com.google.gson.JsonParser;
import io.github.karlatemp.mxlib.common.utils.ToolkitCommon;
import io.github.karlatemp.mxlib.spigot.PluginClassLoaderAccess;
import io.github.karlatemp.mxlib.translate.AbstractTranslator;
import io.github.karlatemp.mxlib.utils.IteratorSupplier;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TransLoader {
    private static InputStream invoke(Callable<InputStream> code) throws IOException {
        try {
            return code.call();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    private static Reader reader(InputStream is) {
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
    }

    @SuppressWarnings("DuplicatedCode")
    public static AbstractTranslator loadTranslate(JavaPlugin plugin) {
        class KvXEntry implements Comparable<KvXEntry> {
            boolean isUserCustom;
            String extname;
            String path;
            String sname;
            Callable<InputStream> openStream;

            @Override
            public int compareTo(@NotNull KvXEntry o) {
                if (o.isUserCustom == this.isUserCustom) {
                    return this.sname.compareTo(o.sname);
                }
                return isUserCustom ? -1 : 1;
            }

            @Override
            public String toString() {
                return "KvXEntry{" +
                        "isUserCustom=" + isUserCustom +
                        ", extname='" + extname + '\'' +
                        ", path='" + path + '\'' +
                        ", sname='" + sname + '\'' +
                        ", openStream=" + openStream +
                        '}';
            }
        }
        Map<String, List<KvXEntry>> languages = new HashMap<>();
        try (ZipFile zip = new ZipFile(PluginClassLoaderAccess.GET_FILE.apply(plugin))) {
            for (ZipEntry entry : new IteratorSupplier<>(ToolkitCommon.asIterator(zip.entries()))) {
                if (entry.getName().startsWith("/translate/trans") && !entry.isDirectory()) {
                    String name = entry.getName().substring(16);
                    int index = name.lastIndexOf('.');
                    if (index == -1) continue;
                    String type = name.substring(index + 1);
                    String fname = name.substring(0, index);
                    List<KvXEntry> list = languages.computeIfAbsent(fname.toLowerCase(Locale.ROOT), $ -> new ArrayList<>());
                    KvXEntry kvXEntry = new KvXEntry();
                    kvXEntry.extname = type;
                    kvXEntry.sname = fname;
                    kvXEntry.isUserCustom = false;
                    kvXEntry.path = "plugin:" + plugin + ":/" + name;
                    kvXEntry.openStream = () -> zip.getInputStream(entry);
                    list.add(kvXEntry);
                }
            }
            File users = new File(plugin.getDataFolder(), "languages");
            if (users.isDirectory()) {
                for (Path file : new IteratorSupplier<>(Files.walk(users.toPath())
                        .filter(it -> !Files.isDirectory(it))
                        .iterator())) {
                    String name = file.getFileName().toString();
                    int index = name.lastIndexOf('.');
                    if (index == -1) continue;
                    String type = name.substring(index + 1);
                    String fname = name.substring(0, index);
                    List<KvXEntry> list = languages.computeIfAbsent(fname.toLowerCase(Locale.ROOT), $ -> new ArrayList<>());
                    KvXEntry kvXEntry = new KvXEntry();
                    kvXEntry.extname = type;
                    kvXEntry.sname = name;
                    kvXEntry.isUserCustom = true;
                    kvXEntry.path = file.toString();
                    kvXEntry.openStream = () -> Files.newInputStream(file);
                    list.add(kvXEntry);
                }
            }

            for (Map.Entry<String, List<KvXEntry>> entry : languages.entrySet()) {
                entry.getValue().sort(KvXEntry::compareTo);
            }

            for (Map.Entry<String, List<KvXEntry>> entry : languages.entrySet()) {
                System.out.println(entry.getKey());
                for (KvXEntry kvXEntry : entry.getValue()) {
                    System.out.println("\t - " + kvXEntry);
                }
            }
            List<Locale> locales = ResourceBundle.Control
                    .getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT)
                    .getCandidateLocales("", Locale.getDefault());
            List<AbstractTranslator> translators = new ArrayList<>(locales.size());
            for (Locale locale : locales) {
                String key = locale.toString();
                if (!key.isEmpty()) key = "_" + key;
                List<KvXEntry> translates = languages.get(key);
                if (translates == null || translates.isEmpty()) continue;
                for (KvXEntry trans : translates) {
                    switch (trans.extname) {
                        case "json": {
                            try (Reader reader = reader(invoke(trans.openStream))) {
                                translators.add(new TransByObj(new JsonParser().parse(reader).getAsJsonObject()));
                            } catch (Exception exception) {
                                plugin.getLogger().log(Level.SEVERE, "Exception in reading " + trans.path, exception);
                            }
                            break;
                        }
                        case "properties": {
                            Properties properties = new Properties();
                            try (Reader reader = reader(invoke(trans.openStream))) {
                                properties.load(reader);
                            } catch (Exception exception) {
                                plugin.getLogger().log(Level.SEVERE, "Exception in reading " + trans.path, exception);
                            }
                            translators.add(new TransByMap(properties));
                            break;
                        }
                        case "yaml": {
                            YamlConfiguration yf = new YamlConfiguration();
                            try (Reader reader = reader(invoke(trans.openStream))) {
                                yf.load(reader);
                            } catch (Exception exception) {
                                plugin.getLogger().log(Level.SEVERE, "Exception in reading " + trans.path, exception);
                            }
                            translators.add(new TransByMapping(yf::getString));
                            break;
                        }
                        default: {
                            plugin.getLogger().log(Level.WARNING, "[MxLib] Unknown how to parse " + trans.path);
                        }
                    }
                }
            }
            return new AbstractTranslator.Link(translators, MxLibSpigotPlugin.MXLIB_TRANSLATOR);
        } catch (IOException exception) {
            plugin.getLogger().log(Level.SEVERE, "Exception in loading translates.");
            return AbstractTranslator.Empty.INSTANCE;
        }
    }
}
