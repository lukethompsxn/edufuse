package example;

import data.Stat;
import filesystem.AbstractFS;

public class ExampleFS extends AbstractFS {

    @Override
    public int getattr(String path, Stat stat) {
        System.out.println("java getattr got called!!!!");
        return super.getattr(path, stat);
    }
}
