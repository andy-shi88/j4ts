package java.net;

import def.dom.Blob;
import def.dom.XMLHttpRequest;
import def.js.ArrayBuffer;
import def.js.Function;
import def.js.Int8Array;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Objects;

import static def.dom.Globals.self;
import static def.dom.Globals.window;
import static jsweet.util.Lang.*;

public class URL implements Serializable {
    static private def.js.Object jsUrlClass() {
        return (def.js.Object) window.$get("URL");
    }

    private final def.js.Object jsUrl;

    public URL(String spec) {
        jsUrl = $new(jsUrlClass(), spec);
    }

    public URL(String protocol, String host, String file) {
        this(protocol + "//" + host + "/" + file);
    }

    public URL(String protocol, String host, int port, String file) {
        this(protocol + "//" + host + ":" + port + "/" + file);
    }

    public InputStream openStream() {
        XMLHttpRequest request = makeConnection();

        switch (request.responseType) {
            case "arraybuffer":
                return new ByteArrayInputStream(any(new Int8Array((ArrayBuffer) request.response)));
            case "blob":
                Function fileReaderSyncConstructor = self.$get("FileReaderSync");
                if (typeof(fileReaderSyncConstructor).equals("function")) {
                    return new ByteArrayInputStream(((Function) fileReaderSyncConstructor.$get("readAsArrayBuffer")).$apply((Blob) request.response));
                }
                // TODO find a way to handle BLOB at main thread synchronously
                return new ByteArrayInputStream(createObjectURL(request.response).getBytes());
            default:
                return new ByteArrayInputStream(request.response.toString().getBytes());
        }
    }

    private static String createObjectURL(Object object) {
        return $insert("window.URL.createObjectURL(object)");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof URL)) return false;
        URL url = (URL) o;
        return Objects.equals(jsUrl, url.jsUrl);
    }

    @Override
    public int hashCode() {
        return jsUrl.hashCode();
    }

    public String getAuthority() {
        String username = jsUrl.$get("username");
        String password = jsUrl.$get("password");

        if (username == null && password == null)
            return "";
        if (username != null && password != null)
            return username + ":" + password;
        return username != null ? username : password;
    }

    public Object getContent() {
        return makeConnection().response;
    }

    public int getDefaultPort() {
        switch (getProtocol().toLowerCase()) {
            case "http":
            case "ws":
                return 80;
            case "https":
            case "wss":
                return 443;
            case "ftp":
                return 21;
            case "sftp":
                return 22;
            case "gopher":
                return 70;
            case "file":
                return 0;
        }
        return -1;
    }

    public String getFile() {
        String wholePath = jsUrl.$get("pathname");
        return wholePath.substring(wholePath.lastIndexOf('/')+1);
    }

    public String getHost() {
        return jsUrl.$get("hostname");
    }

    public String getPath() {
        String wholePath = jsUrl.$get("pathname");
        return wholePath.substring(0, wholePath.lastIndexOf('/'));
    }

    public int getPort() {
        return jsUrl.$get("port");
    }

    public String getProtocol() {
        return jsUrl.$get("protocol");
    }

    public String getQuery() {
        return jsUrl.$get("search");
    }

    public String getRef() {
        return jsUrl.$get("hash");
    }

    public String getUserInfo() {
        return jsUrl.$get("username");
    }

    public boolean sameFile(URL other) {
        return Objects.equals(getProtocol(), other.getProtocol()) &&
                Objects.equals(getAuthority(), other.getAuthority()) &&
                Objects.equals(getHost(), other.getHost()) &&
                getPort() == other.getPort() &&
                Objects.equals(getPath(), other.getPath()) &&
                Objects.equals(getFile(), other.getFile()) &&
                Objects.equals(getQuery(), other.getQuery());
    }

    public String toExternalForm() {
        return jsUrl.toString();
    }

    private XMLHttpRequest makeConnection() {
        XMLHttpRequest request = new XMLHttpRequest();
        // possible not want to do in main thred
        request.open("GET", jsUrl.$get("href"), false);
        request.send();
        return request;
    }

    @Override
    public String toString() {
        return jsUrl.$get("href");
    }
}
