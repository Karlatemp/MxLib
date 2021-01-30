/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-nbt.test/NbtExample.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package nbt;

import io.github.karlatemp.mxlib.nbt.NBTCompressedStreamTools;
import io.github.karlatemp.mxlib.nbt.NBTReadLimiter;
import io.github.karlatemp.mxlib.nbt.NBTTagCompound;
import io.github.karlatemp.mxlib.nbt.RegionFile;
import io.github.karlatemp.mxlib.nbt.visitor.NBTPrinter;
import io.github.karlatemp.mxlib.utils.LineWriter;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

public class NbtExample {
    public static void main(String[] args) throws Throwable {
        // Run this code with -Dnbt.server=....
        String server = System.getProperty("nbt.server");
        if (server == null) return;
        File playerdata = new File(server, "world/playerdata");
        File firstPlayer = playerdata.listFiles()[0];
        NBTTagCompound tagCompound;

        try (DataInputStream input = new DataInputStream(
                new GZIPInputStream(
                        new BufferedInputStream(new FileInputStream(firstPlayer))
                )
        )) {
            tagCompound = NBTCompressedStreamTools.loadCompound(
                    input, NBTReadLimiter.UN_LIMITED
            );
        }
        tagCompound.accept(null, new NBTPrinter(LineWriter.from(System.out)));

        File region = new File(server, "world/region");
        try (RegionFile regionFile = new RegionFile(region.listFiles()[0])) {
            for (int x = 0; x < 32; x++) {
                for (int z = 0; z < 32; z++) {
                    DataInputStream chunkDataInputStream = regionFile.getChunkDataInputStream(x, z);
                    if (chunkDataInputStream != null) {
                        System.out.println("Chunk " + x + ", " + z);
                        NBTCompressedStreamTools.loadCompound(
                                chunkDataInputStream, NBTReadLimiter.UN_LIMITED
                        ).accept(null, new NBTPrinter(LineWriter.from(System.out)));
                        break;
                    }
                }
            }
        }
    }
}
