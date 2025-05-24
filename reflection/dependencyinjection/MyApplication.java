
@Singleton
public class MyApplication {
    
    @Inject 
    private MyDatabase dbService;

    public boolean isDbServiceSet() {
        return dbService != null;
    }
}
