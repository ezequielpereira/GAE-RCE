import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Get command line arguments
@WebServlet(name="GetArgs", value="/args")
public class GetArgs extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("text/plain");

    response.getWriter().println("Command line arguments:\n");
    for (String arg : Args.getArgs()) {
      response.getWriter().println(arg);
    }
  }
}
