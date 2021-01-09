package io.github.karlatemp.mxlib.selenium;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ChromeKit {
    static String getDriverVersion(String chromever) throws IOException {
        String cver = chromever;
        File verfile = new File(MxSelenium.data, "chromedriver-" + chromever + ".mapping");
        if (verfile.isFile()) {
            try (BufferedReader bis = new BufferedReader(new FileReader(verfile))) {
                return bis.readLine();
            }
        }
        List<Throwable> throwables = new ArrayList<>(5);
        while (true) {
            try {
                String uri;
                if (chromever.isEmpty()) {
                    uri = "https://chromedriver.storage.googleapis.com/LATEST_RELEASE";
                } else {
                    uri = "https://chromedriver.storage.googleapis.com/LATEST_RELEASE_" + chromever;
                }
                System.out.println("Fetching chrome driver version from " + uri);
                HttpResponse response = MxSelenium.client.execute(new HttpGet(uri));
                if (response.getStatusLine().getStatusCode() != 200) {
                    response.getEntity().getContent().close();
                    throw new IOException("Response code not 200, " + response.getStatusLine().getStatusCode());
                }
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
                     Writer writer = new FileWriter(verfile)
                ) {
                    String l = reader.readLine();
                    System.out.println("Ver is " + l);
                    writer.write(l);
                    return l;
                }
            } catch (IOException ioException) {
                throwables.add(ioException);
            }
            if (chromever.isEmpty()) {
                IOException ioException = new IOException("Cannot get driver version of " + cver);
                for (Throwable t : throwables) ioException.addSuppressed(t);
                throw ioException;
            } else {
                int ln = chromever.lastIndexOf('.');
                if (ln == -1) chromever = "";
                else chromever = chromever.substring(0, ln);
            }
        }
    }

    static List<String[]> windowsVerCommands = Stream.concat(
            Stream.of(
                    //:: Look for machine-wide Chrome installs (stable, Beta, and Dev).
                    //:: Get the name, running version (if an update is pending relaunch), and
                    //:: installed version of each.
                    "{8A69D345-D564-463c-AFF1-A69D9E530F96}",
                    "{8237E44A-0054-442C-B6B6-EA0509993955}",
                    "{401C381F-E0DE-4B85-8BD8-3F3F14FBDA57}"
            ).flatMap(entry -> {
                //  reg query "HKLM\Software\Google\Update\Clients\%%A" /v name "/reg:32" 2>NUL
                //  reg query "HKLM\Software\Google\Update\Clients\%%A" /v opv "/reg:32" 2>NUL
                //  reg query "HKLM\Software\Google\Update\Clients\%%A" /v pv "/reg:32" 2>NUL
                return Stream.of(
                        new String[]{"reg", "query", "HKLM\\Software\\Google\\Update\\Clients\\" + entry, "/v", "name", "\"/reg:32\""},
                        new String[]{"reg", "query", "HKLM\\Software\\Google\\Update\\Clients\\" + entry, "/v", "opv", "\"/reg:32\""},
                        new String[]{"reg", "query", "HKLM\\Software\\Google\\Update\\Clients\\" + entry, "/v", "pv", "\"/reg:32\""}
                );
            }),
            Stream.of(
                    //:: Look for Chrome installs in the current user's %LOCALAPPDATA% directory
                    //:: (stable, Beta, Dev, and canary).
                    //:: Get the name, running version (if an update is pending relaunch), and
                    //:: installed version of each.
                    "{8A69D345-D564-463c-AFF1-A69D9E530F96}",
                    "{8237E44A-0054-442C-B6B6-EA0509993955}",
                    "{401C381F-E0DE-4B85-8BD8-3F3F14FBDA57}",
                    "{4ea16ac7-fd5a-47c3-875b-dbf4a2008c20}"
            ).flatMap(entry -> {
                // reg query "HKCU\Software\Google\Update\Clients\%%A" /v name "/reg:32" 2>NUL
                // reg query "HKCU\Software\Google\Update\Clients\%%A" /v opv "/reg:32" 2>NUL
                // reg query "HKCU\Software\Google\Update\Clients\%%A" /v pv "/reg:32" 2>NUL
                return Stream.of(
                        new String[]{"reg", "query", "HKCU\\Software\\Google\\Update\\Clients\\" + entry, "/v", "name", "\"/reg:32\""},
                        new String[]{"reg", "query", "HKCU\\Software\\Google\\Update\\Clients\\" + entry, "/v", "opv", "\"/reg:32\""},
                        new String[]{"reg", "query", "HKCU\\Software\\Google\\Update\\Clients\\" + entry, "/v", "pv", "\"/reg:32\""}
                );
            })
    ).collect(Collectors.toList());
}