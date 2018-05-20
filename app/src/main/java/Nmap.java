import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.util.HashSet;
import java.nio.file.attribute.PosixFilePermission;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Run Nmap
@WebServlet(name="Nmap", value="/nmap")
public class Nmap extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    try {
      response.setContentType("text/plain");

      File to = new File("/tmp/nmap");
      if(!to.exists()) {
        File from = new File(System.getProperty("user.dir") + "/WEB-INF/nmap");
        Files.copy(from.toPath(), to.toPath());
        HashSet<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        Files.setPosixFilePermissions(to.toPath(), perms);
      }

      ProcessBuilder pb = new ProcessBuilder(
        "/tmp/nmap",
        "--datadir",
        System.getProperty("user.dir") + "/WEB-INF",
        "-p-",
        "169.254.169.253");
      pb.redirectErrorStream(true);
      Process proc = pb.start();
      proc.waitFor();

      BufferedReader reader = new BufferedReader(
        new InputStreamReader(proc.getInputStream()));
      StringBuffer sb = new StringBuffer();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
      String output = "Scanning all ports on 169.254.169.253\n\n";
      output += sb.toString();
      output += "\n\nNmap exit code: " + proc.exitValue();
      response.getWriter().println(output);
    } catch(Throwable t) {
      t.printStackTrace(response.getWriter());
    }
  }
}
