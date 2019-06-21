import util.FUSELink;
import util.FUSEOperations;
import util.FunctionMap;

public class EduFUSE {

    /**
     * There are parameters which must be passed to this application (in the future these should be created via options).
     * Firstly, you must pass the location of the libFUSELink.so file, followed by option flags and finally the directory
     * you wish to mount.
     * For example: /home/lukethompson/dev/edufuse/java/libFUSELink.so -d -s -f /tmp/example
     *
     * In addition to this, you must make the JVM aware of the libFUSELink.so file, so if you are running from an IDE,
     * then you must specify '-Djava.library.path=.' as VM arguments. If you are running from command line, simply
     * pass this as an argument.
     */
    public static void main(String[] args) {
        FUSELink link = new FUSELink();
        FunctionMap operations = new FunctionMap();
        operations.put(FUSEOperations.getattr, () -> System.out.println("it worked!"));
        link.register(operations, args);
    }
}
