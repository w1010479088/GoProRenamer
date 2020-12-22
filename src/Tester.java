import presenter.GoProRenamer;
import utils.BasePage;

public class Tester {

    public static void main(String[] args) {
        new BasePage() {
            @Override
            protected void action(String path) {
                new GoProRenamer(path, this::log);
            }

            @Override
            protected String title() {
                return "GoPro重命名!";
            }
        }.showUI();
    }
}
