import util.FUSELink;
import util.FUSEOperations;
import util.FunctionMap;

public class EduFUSE {

    public static void main(String[] args) {
        FUSELink link = new FUSELink();
        FunctionMap operations = new FunctionMap();
        operations.put(FUSEOperations.getattr, () -> System.out.println("it worked!"));
        link.register(operations);
    }
}
