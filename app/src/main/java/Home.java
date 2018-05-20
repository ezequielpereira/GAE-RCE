import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Homepage
@WebServlet(name="Home", value="/*")
public class Home extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.getWriter().println("<html><body>");
    response.getWriter().println("<a href=\"/args\">Command line arguments</a><br>");
    response.getWriter().println("<a href=\"/nmap\">Nmap</a><br>");
    response.getWriter().println("<a href=\"/grpc\">gRPC client</a>");
    response.getWriter().println("</body></html>");
  }
}
