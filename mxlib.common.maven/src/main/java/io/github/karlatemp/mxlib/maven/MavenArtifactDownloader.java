/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common-maven.main/MavenArtifactDownloader.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.maven;

import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.logger.MLoggerFactory;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.spi.log.LoggerFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MavenArtifactDownloader {

    private final RepositorySystem repositorySystem;
    private final RepositorySystemSession session;
    private final List<RemoteRepository> repositories;

    @Unmodifiable
    public static final List<RemoteRepository> DEFAULT_REPOSITORIES = Collections.unmodifiableList(
            defaultRepositories()
    );
    public static final File LOCAL_REPO = new File(MxLib.getDataStorage(), "mvn-repo");

    private final File loc;

    public MavenArtifactDownloader(
            @NotNull List<RemoteRepository> repositories,
            File localRepository,
            MLoggerFactory loggerFactory
    ) {
        repositorySystem = newRepositorySystem(loggerFactory);
        System.out.println("sys = " + repositorySystem);
        this.loc = sloveRepo(localRepository);
        session = newSession(repositorySystem, loc);
        this.repositories = repositories;
    }

    public File getLocalRepository() {
        return loc;
    }

    public MavenArtifactDownloader(
            @NotNull List<RemoteRepository> repositories,
            File localRepository
    ) {
        this(repositories, localRepository, null);
    }

    public List<RemoteRepository> getRepositories() {
        return repositories;
    }

    public RepositorySystem getRepositorySystem() {
        return repositorySystem;
    }

    public RepositorySystemSession getSession() {
        return session;
    }

    public File resolve(String groupId, String artifactId,
                        String version, String classifier,
                        String packaging) throws IOException {

        Artifact artifact = new DefaultArtifact(groupId, artifactId, classifier, packaging, version);
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(artifact);

        artifactRequest.setRepositories(repositories);

        File result;

        try {
            ArtifactResult artifactResult = repositorySystem.resolveArtifact(session, artifactRequest);
            artifact = artifactResult.getArtifact();
            if (artifact != null) {
                result = artifact.getFile();
            } else {
                result = null;
            }
        } catch (ArtifactResolutionException e) {
            throw new IOException("Artefakt " + groupId + ":" + artifactId + ":"
                    + version + " konnte nicht heruntergeladen werden.", e);
        }

        return result;

    }

    private static RepositorySystem newRepositorySystem(MLoggerFactory loggerFactory) {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.setErrorHandler(new DefaultServiceLocator.ErrorHandler() {
            @Override
            public void serviceCreationFailed(Class<?> type, Class<?> impl, Throwable exception) {
                System.err.println(type + " <== " + impl);
                if (exception != null) exception.printStackTrace();
            }
        });
        if (loggerFactory != null) {
            locator.setServices(LoggerFactory.class, new MavenLoggerFactory(loggerFactory));
            System.out.println("Logger setted.");
        }
        locator.addService(RepositoryConnectorFactory.class,
                BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        return locator.getService(RepositorySystem.class);

    }

    private static File sloveRepo(File repo) {
        if (repo != null) return repo;
        File dir = new File(System.getProperty("user.home"), ".m2/repository");
        if (dir.isDirectory()) return dir;
        return LOCAL_REPO;
    }

    private static RepositorySystemSession newSession(RepositorySystem system,
                                                      File localRepository) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = new LocalRepository(localRepository.toString());
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
        return session;
    }

    private static List<RemoteRepository> defaultRepositories() {
        List<RemoteRepository> remoteRepositories = new ArrayList<>();
        if (Locale.getDefault() == Locale.SIMPLIFIED_CHINESE) {
            remoteRepositories.add(new RemoteRepository.Builder(
                    "aliyun-mirror", "default", "https://maven.aliyun.com/repository/public"
            ).build());
        }

        remoteRepositories.add(new RemoteRepository.Builder(
                "central", "default", "https://repo1.maven.org/maven2/"
        ).build());
        remoteRepositories.add(new RemoteRepository.Builder(
                "jcenter", "default", "https://jcenter.bintray.com/"
        ).build());
        remoteRepositories.add(new RemoteRepository.Builder(
                "kotlin-dev", "default", "https://dl.bintray.com/kotlin/kotlin-dev/"
        ).build());

        return remoteRepositories;
    }
}
