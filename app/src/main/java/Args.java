public class Args {
  static {
    System.load(System.getProperty("user.dir") + "/WEB-INF/libhack.so");
  }

  public static native String[] getArgs();
}
