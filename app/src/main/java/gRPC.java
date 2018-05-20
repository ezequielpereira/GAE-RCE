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

import com.google.apphosting.api.ApiProxy;
import java.lang.reflect.Method;

// Run the C++ gRPC client
@WebServlet(name="gRPC", value="/grpc")
public class gRPC extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    try {
      response.setContentType("text/plain");

      File to = new File("/tmp/gRPC_client");
      if(!to.exists()) {
        File from = new File(
          System.getProperty("user.dir") + "/WEB-INF/gRPC_client");
        Files.copy(from.toPath(), to.toPath());
        HashSet<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        Files.setPosixFilePermissions(to.toPath(), perms);
      }

      String api_package = "stubby";
      String[] parameters = request.getParameterValues("api");
      if(parameters != null) api_package = parameters[0];

      String call = "Send";
      parameters = request.getParameterValues("method");
      if(parameters != null) call = parameters[0];

      String pb = "{}";
      parameters = request.getParameterValues("req");
      if(parameters != null) pb = parameters[0];

      Method getSecurityTicket = ApiProxy.getCurrentEnvironment().getClass()
        .getDeclaredMethod("getSecurityTicket");
      getSecurityTicket.setAccessible(true);
      String security_ticket = (String) getSecurityTicket
        .invoke(ApiProxy.getCurrentEnvironment());

      String set_pb = "1";
      parameters = request.getParameterValues("setPb");
      if(parameters != null) set_pb = parameters[0];
      if (set_pb.equals("false") || set_pb.equals("f") || set_pb.equals("0")) {
        set_pb = "0";
      } else {
        set_pb = "1";
      }

      ProcessBuilder process_builder = new ProcessBuilder(
        "/tmp/gRPC_client",
        api_package,
        call,
        pb,
        security_ticket,
        set_pb);
      process_builder.redirectErrorStream(true);
      Process proc = process_builder.start();
      proc.waitFor();

      BufferedReader reader = new BufferedReader(
        new InputStreamReader(proc.getInputStream()));
      StringBuffer sb = new StringBuffer();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
      String output = "gRPC client - GET parameters:\n";
      output += "api = API to call (i.e. app_identity_service)\n";
      output += "method = Method to invoke (i.e. GetAccessToken)\n";
      output +=
        "req = JSON representing the request (i.e. {\"scope\":[\"email\"]})\n";
      output += "setPb = 1 (Set the PB field), 0 (Do not set the PB field)\n";
      output += sb.toString();
      output += "gRPC client exit code: " + proc.exitValue();
      response.getWriter().println(output);
    } catch(Throwable t) {
      t.printStackTrace(response.getWriter());
    }
  }
}
